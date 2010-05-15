package bugkillr;
import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

import bugkillr.PMF;
import bugkillr.Team;
/**
 * @author Randy Bohrer
 * This servlet takes a request to create a new team and adds the team to the database.
 * It also adds the user to that team if they request it on the form.
 * */
@SuppressWarnings("serial")
public class DBAddTeamServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		hw.writeProlog("Bugkiller - Add Team to Database");
		hw.writeHeader();
		resp.getWriter().println("<h1>Add Team to Database</h1>");
		hw.writeUnsupportedGet();
		hw.writeEpilog();
	}
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		//Make sure the user is logged in. Otherwise it would be easy for the attacker to add teams
		//TODO Make it so that only administrators can add teams. Otherwise an attacker could still add them!
		redir.loginRedirect();
		hw.writeProlog("Bugkiller - Add Team to Database");

		//Check if a team exists with this name.
		Query getExistingTeams = pm.newQuery("select from " + Team.class.getName() + " where name == theName");
		getExistingTeams.declareParameters("String theName");
		String teamName = req.getParameter("teamName");
		List<Team> existingTeams = (List<Team>) getExistingTeams.execute(teamName);

		//If it does not exist, then create it
		if(existingTeams.isEmpty()){
			try {
				
				Team newTeam = new Team(req.getParameter("teamName"), redir.getUserFromDatastore().getKey());
				pm.makePersistent(newTeam);
				
				if(req.getParameter("joinTeam") != null)
				{
					User curUser = redir.getUserFromDatastorePM(pm);
					curUser.setTeam( teamName);
					pm.makePersistent(curUser);
				}
					 
			} catch (Exception e) {
				resp.getWriter().println(e);
			}
		}

		hw.writeHeader();
		resp.getWriter().println("<h1>Add Team to Database</h1>");
		if(!existingTeams.isEmpty()){
			resp.getWriter().println("Error: There is already a team with the name you chose. Please go back and pick a different name.");
		}
		else{
			resp.getWriter().println(
					"Added Team with the following parameters:<br/>\n" +
					"Team Name: " + req.getParameter("teamName") +"<br/>\n" +
					"Join Team: " + req.getParameter("joinTeam"));
		}
		hw.writeEpilog(); 
		pm.close();
	}
}
