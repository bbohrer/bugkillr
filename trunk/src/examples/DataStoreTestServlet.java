package examples;
import java.io.IOException;
import javax.servlet.http.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bugkillr.PMF;
import bugkillr.Team;

import java.util.List;

@SuppressWarnings("serial")
public class DataStoreTestServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Data store test");
		//Create some data, store it and read it.
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			for(int i = 0; i < 10; i++)
			{
				//Make a new team with a new team name, and no owner (no users exist yet, so noone could own the team!
				Team u = new Team("Team".concat(Integer.toString(i)), null);
				//Add the team to the database.
				pm.makePersistent(u);
			}
			//Make a query to show all the teams
			Query query = pm.newQuery("select from " + Team.class.getName());
			List<Team> results = (List<Team>)query.execute();
			for(Team team : results){
				resp.getWriter().println(team.getName());
			}
		}
		finally{
			pm.close();
		}
	}
}
