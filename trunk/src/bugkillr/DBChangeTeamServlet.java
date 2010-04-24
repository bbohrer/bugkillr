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
import java.util.List;


@SuppressWarnings("serial")
public class DBChangeTeamServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		String teamName = req.getParameter("teamname");
		Redirector redir = new Redirector(req,resp);
		//Redirect if the user is logged in or not in the database.
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(teamName == null)
		{
			resp.getWriter().println("Error: You did not enter a team name. Please go back and enter a team name.");
		}
		else
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				//:TODO: Make this safe, if it isn't
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
					//TODO Fix this. Currently the user's team does not change.
					User theUser = redir.getUserFromDatastore();
					theUser.setTeam(results.get(0).getName());
					if(redir.getTeamFromDatastore() == null)resp.getWriter().println("Error: New team not changed in database");
					else
						resp.getWriter().println("Team successfully updated to " + results.get(0).getName() + ".");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{	
				pm.close();
			}
		}
	}
}
