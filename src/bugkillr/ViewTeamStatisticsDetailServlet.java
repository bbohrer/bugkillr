package bugkillr;
import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

import bugkillr.PMF;
import bugkillr.Team;

import html.HTMLWriter;
import redirects.Redirector;


public class ViewTeamStatisticsDetailServlet extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		response.setContentType("text/html");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(request, response);
		Redirector redir = new Redirector(request, response);
		//If the user is not logged in or not in the database, redirect them to the appropriate page.
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e) {
			response.getWriter().println(e);
		}

		hw.writeProlog("Bugkiller - View Team Statistics");
		hw.writeHeader();
		response.getWriter().println("<h1>View Team Statistics</h1>");

		if(request.getParameter("team") == null)
		{
			response.getWriter().println("Error: No team was specified. This may occur if you log out and log in again," +
					" or if you entered the URL manually and misspelled it. If you reached this page from the team list," +
					" it is a bug. You can safely return to the page you were viewing with your browser's back button, or" +
			" navigate to another page using the menu bar.");
			hw.writeEpilog();
			return;
		}

		Team curTeam = null;
		try{
			Query q = pm.newQuery("select from "+Team.class.getName() + " where name == teamName parameters String teamName");
			List<Team> teams = (List<Team>) q.execute(URLDecoder.decode(request.getParameter("team"), "UTF-8"));
			if(teams.size() > 1)
				response.getWriter().println("Error: Multiple teams with the same name");
			else if (teams.size() == 0)
				response.getWriter().println("Error: Team not found");
			else
				curTeam = teams.get(0);
		} catch (Exception e) {
			response.getWriter().println(e);
		}

		//Show the available teams
		//Make a query to show all the teams
		Query getUsers = pm.newQuery("select from " + User.class.getName() + " where teamId == curId order by score descending");
		getUsers.declareParameters("String curId");
		List<User> results = (List<User>)getUsers.execute(curTeam.getName());
		//Write out the list of teams
		if(results.isEmpty())
		{
			response.getWriter().println("There are no players on this team.");
		}
		else
		{
			response.getWriter().println("Scores By User: <br/>");
			int rank = 1;
			response.getWriter().println("<table>" +
			"<tr><td>Name</td><td>Score</td><td>Rank</td></tr>");
			for( User u : results){
				response.getWriter().println("<tr><td><a href=\"mailto:"+u.getAccountId() + "\">"+u.getAccountId() +"</a></td><td>"+ u.getScore()+"</td><td>"+ rank++ + "</td></tr>");
			}
			response.getWriter().println("</table>");
		}
		hw.writeEpilog();
	}
}