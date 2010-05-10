package bugkillr;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;

@SuppressWarnings("serial")
public class ViewProblemServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - View Problem");
		redir.loginRedirect();
		long pid = 0;
		Problem curProblem = null;
		User curUser = null;
		hw.writeHeader();
		try {
			redir.userRedirect();
			pid = Integer.parseInt(req.getParameter("pid"));
			curUser = redir.getUserFromDatastore();
			Query q = pm.newQuery("select from " + Problem.class.getName() + " where key == pid parameters long pid");
			List<Problem> probs = (List<Problem>) q.execute(pid);
			if(probs.size() > 1)throw new Exception("Multiple problems found for this ID");
			if(probs.size() == 0)throw new Exception("Problem not found");
			curProblem = probs.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(curProblem == null)
		{
			resp.getWriter().println("This problem is not in the database. If you reached this page from the problem " +
					"menu, this is a bug. If you reached this page by entering the URL in the address bar, you most likely " +
					"forgot or mis-typed the problem ID.");
			hw.writeEpilog();
			return;
			
		}
		if(curProblem.getMinscore() > curUser.getScore())
		{
			resp.getWriter().println("Error: You do not have the score required to view this problem.");
		}
		else
		{
			resp.getWriter().println("<h1>View Problem</h1>");
			resp.getWriter().println("<h2>Problem Name: "+ curProblem.getName()+"</h2>");
			resp.getWriter().println("<h3>Current Score: " + curUser.getScore() + "</h3>");
			resp.getWriter().println("<h3>Description</h3>");
	
			URL url = new URL(curProblem.getDescriptionURL());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			String line = null;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				while((line = reader.readLine()) != null)
					resp.getWriter().println(line);
			}
			else
			{
					resp.getWriter().println("Error: Could not open description file.");
			}
			hw.writeLink( curProblem.getHelpURL() ,"View hints for this problem.");
			hw.writeLink("submitform?pid=" + pid, "Submit your solution for this problem.");
			hw.writeEpilog();
		}	
	}
}
