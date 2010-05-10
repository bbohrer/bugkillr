package bugkillr;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bugkillr.PMF;
import bugkillr.Team;

import html.HTMLWriter;
import redirects.Redirector;

public class ViewTeamStatisticsServlet extends HttpServlet {
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
        try{
        //If the user has a current team, tell them what it is
			Team curTeam = redir.getTeamFromDatastore();
			if(curTeam == null){
				response.getWriter().println("<p> You are currently not on a team.</p>");
			}
			else{
				response.getWriter().println("<p> Your current team is \"" + curTeam.getName() + "\".");
			}
		} catch (Exception e) {
			response.getWriter().println(e);
		}
        
        //Show the available teams
        response.getWriter().println("Scores By Team: <br/>");
        //Make a query to show all the teams

        Query query = pm.newQuery("select from " + Team.class.getName() + " order by score descending");
        List<Team> results = (List<Team>)query.execute();
        //Write out the list of teams
        int rank = 1;
        response.getWriter().println("<table>" +
        		"<tr><td>Name</td><td>Score</td><td>Rank</td></tr>");
        for(Team team : results){
        	response.getWriter().println("<tr><td><a href = \"viewteamdetail?team="+ URLEncoder.encode(team.getName(),"UTF-8")+"\">"+ 
        			team.getName() + "</a></td><td>"+ team.getScore()+"</td><td>"+ rank++ + "</td></tr>");
        }
        response.getWriter().println("</table>");
        hw.writeEpilog();
    }
}