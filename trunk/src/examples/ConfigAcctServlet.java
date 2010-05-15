/*
 * This page takes form data from the account management page
 * and performs the relevant updates to the user's information
 * in the datastore.
 * :TODO: remove obvious SQL injection opportunities
 */
package examples;
import java.io.IOException;
import javax.servlet.http.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bugkillr.PMF;
import bugkillr.Team;
import bugkillr.User;

import java.util.List;


@SuppressWarnings("serial")
public class ConfigAcctServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String teamName = req.getParameter("teamname");
		if(teamName == null)
		{
			resp.getWriter().println("Error: You did not enter a team name.");
		}
		else
		{
			resp.getWriter().println("DEBUG: Team name was recieved.\n");
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
					resp.getWriter().println("DEBUG: There is exactly one team with the name you picked. This is good.");
					//The user is not logged in 
					if(req.getUserPrincipal() == null)
					{
						resp.getWriter().println("Please log in first");
					}
					//Look for the user in the datastore
					else{
						Team teamToJoin = results.get(0);
						Query getUser = pm.newQuery("select from " + User.class.getName() + " where emailAddr == email_addr");
						getUser.declareParameters("String email_addr");
						List<User> userResults = (List<User>)getUser.execute(req.getUserPrincipal().getName());
						//Put the user in the datastore
						if(userResults.isEmpty())
						{
							resp.getWriter().println("DEBUG: You are not in the datastore and are being added. This is okay.");
							User newUser = new User( teamToJoin.getName(), req.getUserPrincipal().getName());
							pm.makePersistent(newUser);
						}
						//Duplicate users exist. Complain.
						else if(userResults.size() > 1)
						{
							resp.getWriter().println("Error: Multiple users with your email address exist in the datastore." +
									"Please file a bug report.");
						}
						//One user exists. Update their team.
						else
						{
							userResults.get(0).setTeam( teamToJoin.getName());
							resp.getWriter().println("Team successfully updated.");
						}
					}
				}
			}
			finally{	
				pm.close();
			}
		}
	}
}
