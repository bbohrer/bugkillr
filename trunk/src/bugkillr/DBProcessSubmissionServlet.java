package bugkillr;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * @author Randy Bohrer
 * Processes a programming problem submission. Looks up the URL for the problem
 * solver, sends the code to the solver, reads the result, and updates the database
 * as necessary.
 */

@SuppressWarnings("serial")
public class DBProcessSubmissionServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		hw.writeProlog("Bugkiller - Submit Problem Solution");
		hw.writeHeader();
		hw.writeUnsupportedGet();
		hw.writeEpilog();
	}
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		redir.loginRedirect();
		try {
			redir.userRedirect();
			redir.teamRedirect();
		} catch (Exception e) {
			resp.getWriter().println(e);
		}


		hw.writeProlog("Bugkiller - Submit Problem Solution");
		hw.writeHeader();

		//Look up the problem.
		Query getProblem = pm.newQuery("select from " + Problem.class.getName() + " where key == pid");
		getProblem.declareParameters("Long pid");
		List<Problem> result= (List<Problem>) getProblem.execute(new Long(req.getParameter("pid")));
		//Error: No problem found for this ID
		if(result.isEmpty())
		{
			resp.getWriter().println("Error: This problem was not found in the database.");
		}
		//Error: Multiple problems found for this ID
		else if(result.size() > 1)
		{
			resp.getWriter().println("Error: Multiple copies(" + result.size() + ")" +
					" of this problem were found in the database. PID was " + new Long(req.getParameter("pid")) +"<br/>");
			for(Problem p : result)
			{
				resp.getWriter().println(p.getName() + " " + p.getKey() + "<br/>");
			}
		}
		//Found exactly one problem. Find the URL to submit to, and submit it.
		else
		{
			//Check whether the problem has already been solved.
			Query getSolvedProblem = pm.newQuery("select from " + UserProblemJunction.class.getName() + " where userId == curUserId" +
			"&& problemId == curProblemID");
			getSolvedProblem.declareParameters("String curUserId, String curProblemID");
			User curUser = null;
			try {
				curUser = redir.getUserFromDatastorePM(pm);
			} catch (Exception e1) {
				resp.getWriter().println(e1);
			}
			List<UserProblemJunction> existingSolvedProblem = (List<UserProblemJunction>) getSolvedProblem.execute(curUser.getKey(), result.get(0).getKey());
			String solverURL = result.get(0).getSolverURL();

			//Problem has already been solved
			if(!existingSolvedProblem.isEmpty())
			{
				resp.getWriter().println("You've already solved this problem!<br/>");
			}
			else{
				//Build a request to send to the solver. Based on http://code.google.com/appengine/docs/java/urlfetch/usingjavanet.html
				try {
					URL url = new URL(solverURL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					
					//Add the source code to the request
					OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		            writer.write("inputText=" + URLEncoder.encode(req.getParameter("inputText"), "UTF-8"));
		            writer.close();
		            
					if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						//User's code passed the test
						String firstLine;
						if((firstLine = reader.readLine()).startsWith("PASS"))
						{
							resp.getWriter().println("Congratulations. Your code passed.");
							UserProblemJunction prob = new UserProblemJunction(curUser.getKey(), new Long(req.getParameter("pid")));
							curUser.setScore(curUser.getScore()+1);
							Team curTeam = redir.getTeamFromDatastorePM(pm);
							curTeam.setScore(curTeam.getScore()+1);
							pm.makePersistent(prob);
							resp.getWriter().println("<br/> Updating score to " + (curUser.getScore()));
						}
						//User's code failed
						else
						{
							//Print out an error message.
							resp.getWriter().println("Your submission failed with the folllowing error:");
							String line;
							while((line = reader.readLine()) != null)
							{
								resp.getWriter().println(line);
							}
							resp.getWriter().println("Debugging information: <br/>");
							resp.getWriter().println("First line = " + firstLine);
							resp.getWriter().println("Solver URL = " + solverURL);
						}
					} else {
						resp.getWriter().println("An error occurred in the problem solver, HTTP Response code = " + connection.getResponseCode());
					}
				} 
				catch (MalformedURLException e) {
					resp.getWriter().println("Error: "+e);
				} catch (IOException e) {
					resp.getWriter().println("Error: "+e);
				} catch (Exception e) {
					resp.getWriter().println("Error: "+e);
				}
				finally
				{
					if(!pm.isClosed())
						pm.close();	
				}
			}
		}
		hw.writeEpilog();
	}
}
