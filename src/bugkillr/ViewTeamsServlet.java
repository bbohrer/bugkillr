package bugkillr;
import java.io.IOException;
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

public class ViewTeamsServlet extends HttpServlet {
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
			e.printStackTrace(response.getWriter());
		}
        
        hw.writeProlog("Bugkiller - View Teams");
        hw.writeHeader();
        response.getWriter().println("<h1>View Teams</h1>");
        try{
        //If the user has a current team, tell them what it is
			Team curTeam = redir.getTeamFromDatastore();
			if(curTeam == null){
				response.getWriter().println("<p> You are currently not on a team.</p>");
			}
			else{
				response.getWriter().println("<p> Your current team is \"" + curTeam.getName() + "\".</p>");
			}
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		}
        
        //Show the available teams
        response.getWriter().println("The following teams are available for joining: <br/>");
        //Make a query to show all the teams

        Query query = pm.newQuery("select from " + Team.class.getName());
        List<Team> results = (List<Team>)query.execute();
        //Write out the list of teams
        response.getWriter().println("<ul>");
        for(Team team : results){
        	response.getWriter().println("<li>"+ team.getName() + "</li>");
        }
        response.getWriter().println("</ul>");
        //Create a form to take the name of a team as an input
        response.getWriter().println("<form name=\"input\" action=\"changeteam\" method=\"post\">" +
        		"Enter the name of the team you wish to join<br/>" +
        		"<input type=\"text\" name=\"teamname\">" +
        		"</input>" +
        		"<input type=\"submit\" value=\"Submit\"></input>" +
        "</form>");
        
        hw.writeEpilog();
    }
}