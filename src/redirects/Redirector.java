package redirects;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bugkillr.PMF;
import bugkillr.Team;
import bugkillr.User;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/**
 * @author Randy Bohrer
 * Handles redirecting to other pages, and detecting conditions that require a redirect
 */
public class Redirector {

	///The servlet request containing information about the user.
	private HttpServletRequest req;
	/// The servlet response for the page, which can be used to redirect.
	private HttpServletResponse res;
	
	public Redirector(HttpServletRequest Req, HttpServletResponse Res)
	{
		req = Req;
		res = Res;
	}
	/**
	 * @return True if the user is logged in, false otherwise.
	 * Tests whether the user is logged in to Google Apps
	 */
	public boolean isLoggedIn()
	{
		if(req.getUserPrincipal() != null) return true;
		return false;
	}
	
	/**
	 * Redirects to the Google Apps login page if the user is not logged in.
	 * @throws IOException 
	 */
	public void loginRedirect() throws IOException
	{
		if(!isLoggedIn())
		{
			UserService userService = UserServiceFactory.getUserService();
			res.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
	
	/**
	 * Redirects to the "Add user to datastore" page if the user is not in
	 * the datastore.
	 * @throws Exception If the user appears multiple times in the database.
	 */
	public void userRedirect() throws Exception
	{
		if(getUserFromDatastore() == null)res.sendRedirect("askadduser");
	}
	
	/**
	 * Redirects to the "Join a team" page if the user is not on a team.
	 * @throws Exception If the user is on multiple teams.
	 */
	public void teamRedirect() throws Exception
	{
		if(getTeamFromDatastore() == null)res.sendRedirect("askjointeam");
	}
	
	/**
	 * @return User's record in datastore if it exists, null otherwise
	 * @throws Exception If there are multiple User objects for the current user's email address
	 */
	@SuppressWarnings("unchecked")
	public User getUserFromDatastore() throws Exception
	{
		//TODO Find a better way to handle the user not being logged in!.
		if(req.getUserPrincipal() == null)return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query getUser = pm.newQuery("select from " + User.class.getName() + " where emailAddr == email_addr");
		getUser.declareParameters("String email_addr");
		List<User> userResults = (List<User>)getUser.execute(req.getUserPrincipal().getName());
		if(userResults.isEmpty()) return null;
		if(userResults.size() > 1) throw new Exception("Multiple users exist for email address: " + req.getUserPrincipal().getName());
		return userResults.get(0);
	}
	
	/**
	 * @return User's record in datastore if it exists, null otherwise
	 * @throws Exception If there are multiple User objects for the current user's email address
	 */
	@SuppressWarnings("unchecked")
	public User getUserFromDatastorePM(PersistenceManager pm) throws Exception
	{
		//TODO Find a better way to handle the user not being logged in!.
		if(req.getUserPrincipal() == null)return null;
		Query getUser = pm.newQuery("select from " + User.class.getName() + " where emailAddr == email_addr");
		getUser.declareParameters("String email_addr");
		List<User> userResults = (List<User>)getUser.execute(req.getUserPrincipal().getName());
		if(userResults.isEmpty()) return null;
		if(userResults.size() > 1) throw new Exception("Multiple users exist for email address: " + req.getUserPrincipal().getName());
		return userResults.get(0);
	}
	
	/**
	 * @return User's team if the user is on a team, null otherwise
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Team getTeamFromDatastore() throws Exception
	{

		User curUser = getUserFromDatastore();
		if(curUser == null) throw new Exception("No use");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query getTeam = pm.newQuery("select from "+Team.class.getName() + " where name == team_name");
		getTeam.declareParameters("String team_name");
		List<Team> results = (List<Team>)getTeam.execute(curUser.getTeamId());
		//Note: results.size() must be called while the datastore is open.
		int numResults = results.size();
		pm.close();
		//If no results were returned, it's a non-existent team
		if(numResults == 0)
			return null;
		//Multiple results mean there were multiple teams with a given name. This is bad.
		else if(numResults > 1)
		{
			throw new Exception("User has more than one team: " + curUser.getAccountId());
		}
		else return results.get(0);
	}
	
	/**
	 * @return User's team if the user is on a team, null otherwise
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Team getTeamFromDatastorePM(PersistenceManager pm) throws Exception
	{

		User curUser = getUserFromDatastore();
		if(curUser == null) throw new Exception("No use");
		Query getTeam = pm.newQuery("select from "+Team.class.getName() + " where name == team_name");
		getTeam.declareParameters("String team_name");
		List<Team> results = (List<Team>)getTeam.execute(curUser.getTeamId());
		//Note: results.size() must be called while the datastore is open.
		int numResults = results.size();
		//If no results were returned, it's a non-existent team
		if(numResults == 0)
			return null;
		//Multiple results mean there were multiple teams with a given name. This is bad.
		else if(numResults > 1)
		{
			throw new Exception("User has more than one team: " + curUser.getAccountId());
		}
		else return results.get(0);
	}
}
