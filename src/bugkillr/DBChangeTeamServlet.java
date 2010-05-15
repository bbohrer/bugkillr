/*
 * This page takes form data from the account management page
 * and performs the relevant updates to the user's information
 * in the datastore.
 * :TODO: remove obvious SQL injection opportunities
 */
package bugkillr;
import java.io.IOException;
import javax.servlet.http.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bugkillr.PMF;
import bugkillr.Team;
import redirects.Redirector;
import html.HTMLWriter;

import java.util.List;


@SuppressWarnings("serial")
public class DBChangeTeamServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		String teamName = req.getParameter("teamname");
		Redirector redir = new Redirector(req,resp);
		HTMLWriter hw = new HTMLWriter(req,resp);
		
		hw.writeProlog("Bugkiller - Change Teams");
		hw.writeHeader();
		resp.getWriter().println("<h1>Change Teams</h1>");
		
		//Redirect if the user is logged in or not in the database.
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Make sure the user entered a team name
		if(teamName == null){
			resp.getWriter().println("Error: You did not enter a team name. Please go back and enter a team name.");
		}
		
		//Add them to the specified team.
		else
		{
			PersistenceManager user_pm = PMF.get().getPersistenceManager();
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				//Look in the datastore for a team with the given name
				Query getTeam = pm.newQuery("select from "+Team.class.getName() + " where name == team_name");
				getTeam.declareParameters("String team_name");
				List<Team> results = (List<Team>)getTeam.execute(teamName);
				
				//If no results were returned, it's a non-existent team
				if(results.isEmpty())
				{
					resp.getWriter().println("Error: The team you selected does not exist.");
				}
				
				//Multiple results mean there were multiple teams with a given name. This is bad.
				else if(results.size() > 1)
				{
					resp.getWriter().println("Error: There are multiple teams with this name." +
							"Please file a bug report.");
				}
				
				//Exactly one team with the given name exists. Join it.
				else{
					//Look for the user in the datastore
					User theUser = redir.getUserFromDatastorePM(user_pm);
					Team newTeam = results.get(0);
					Team oldTeam = redir.getTeamFromDatastorePM(pm);
					
					//Update the scores for the previous and new teams.
					newTeam.setScore(newTeam.getScore() + theUser.getScore());
					if(oldTeam !=null){
					oldTeam.setScore(oldTeam.getScore() - theUser.getScore());
					}
					//Update the player's team
					theUser.setTeam( newTeam.getName());
					//I'm pretty sure this line should be removed
					user_pm.makePersistent(theUser);
				}
			}
			
			catch (Exception e) {
				e.printStackTrace(resp.getWriter());
			}
			//Always close the PersistenceManager, even in the event of an error.
			finally{	
				pm.close();
				user_pm.close();
			}
			resp.getWriter().println("Team has been updated.");
		}
		hw.writeEpilog();
	}
}
