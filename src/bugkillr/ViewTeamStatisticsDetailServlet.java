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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
        Team curTeam = null;
        String curId = request.getParameter("teamId");
        try{
        	Query q = pm.newQuery("select from " + Team.class.getName() + " where key == curId");
        	q.declareParameters("String curId");
        	List<Team> teams = (List<Team>) q.execute(curId);
        	if(teams.size() > 1)
        		response.getWriter().println("Error: Database error. Please contact administrator");
        	else if (teams.size() == 0){
        		response.getWriter().println("Error: Team " + curId + " not found");
        		response.getWriter().println("select from " + Team.class.getName() + " where key == " + curId);
        	}
        	else
        		curTeam = teams.get(0);
        	//Key teamId = KeyFactory.createKey(Team.class.getSimpleName(), curId);
        	//response.getWriter().println(teamId);
        	//response.getWriter().println(teamId.toString());
        	//curTeam = pm.getObjectById(Team.class, curId);
        	//if (curTeam == null) {
        	//	response.getWriter().println("Team " + curId.toString() + "not found");
        	//} else response.getWriter().println("Team " + curTeam.toString() + " found");
		} catch (Exception e) {
			response.getWriter().println(e);
			e.printStackTrace();
		}
        
		try {
        //Show the members of the team
        response.getWriter().println("Scores By User: <br/>" + "select from " + User.class.getName() + " where teamId == " + curTeam.getKey() + "order by score descending");
		} catch (Exception e) {
			response.getWriter().println(e);
			e.printStackTrace();
		}
        //Make a query to show all the team member
        Query getUsers = pm.newQuery("select from " + User.class.getName() + " where teamId == curId order by score descending");
        getUsers.declareParameters("String curId");
        try {
        	List<User> results = (List<User>)getUsers.execute(curTeam.getKey());
            //Write out the list of teams
            int rank = 1;
            response.getWriter().println("<table>" +
            		"<tr><td>Name</td><td>Score</td><td>Rank</td></tr>");
            for( User u : results){
            	response.getWriter().println("<tr><td>"+u.getAccountId() + "</a></td><td>"+ u.getScore()+"</td><td>"+ rank++ + "</td><td>"+ u.getTeamId() +"</td></tr>");
            }
            response.getWriter().println("</table>");
        } catch (Exception e)
        {
        	e.printStackTrace();
        }
        hw.writeEpilog();
    }
}