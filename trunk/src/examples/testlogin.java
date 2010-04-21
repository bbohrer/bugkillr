package examples;
import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bugkillr.PMF;
import bugkillr.Team;

import com.google.appengine.api.users.*;

import html.HTMLWriter;

public class testlogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        HTMLWriter hw = new HTMLWriter(request, response);
        hw.writeProlog("Bugkillr - Log In");
        hw.writeHeader();
        String thisURL = request.getRequestURI();
        if (request.getUserPrincipal() != null) {
            response.getWriter().println("<p>Hello, " +
                                         request.getUserPrincipal().getName() +
                                         "!  You can <a href=\"" +
                                         userService.createLogoutURL(thisURL) +
                                         "\">sign out</a>.</p>");
            response.getWriter().println("The following teams are available for joining: <br/>");
			//Make a query to show all the teams
    		PersistenceManager pm = PMF.get().getPersistenceManager();
	
    		Query query = pm.newQuery("select from " + Team.class.getName());
			List<Team> results = (List<Team>)query.execute();
			//Write out the list of teams
			response.getWriter().println("<ul>");
			for(Team team : results){
				response.getWriter().println("<li>"+ team.getName() + "</li>");
			}
			response.getWriter().println("</ul>");
			//Create a form to take the name of a team as an input
			response.getWriter().println("<form name=\"input\" action=\"configAcct\" method=\"post\">" +
					"Enter the name of the team you wish to join<br/>" +
					"<input type=\"text\" name=\"teamname\">" +
					"</input>" +
					"<input type=\"submit\" value=\"Submit\"></input>" +
					"</form>");
        } else {
            response.getWriter().println("<p>Please <a href=\"" +
                                         userService.createLoginURL(thisURL) +
                                         "\">sign in</a>.</p>");
        }
        hw.writeEpilog();
    }
}