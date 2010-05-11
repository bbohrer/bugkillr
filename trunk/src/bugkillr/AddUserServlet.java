package bugkillr;
import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;

@SuppressWarnings("serial")
public class AddUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - Add User");
		hw.writeHeader();
		resp.getWriter().println("<h1>Add User</h1>");
		try {
			if(redir.isLoggedIn())
			{
				//Add the user to the datastore (the main purpose of this page)
				if(redir.getUserFromDatastore() == null)
				{
					//When dealing with the PersistenceManager, use a try/finally block so
					//the PersistenceManager is cleaned up even if something goes wrong
					try
					{
							//Create a new user, with no team, and the user's email address as its address.
							User user = new User(null, req.getUserPrincipal().getName());
							//Add the user to the database.
							pm.makePersistent(user);
							//Inform the user of success.
							resp.getWriter().println("<p>You have successfully been added to the database." +
									"the next step is to ");
							hw.writeLink("viewteams","join a team.");
							resp.getWriter().println("</p>");
					
					}
					finally{
						pm.close();
					}
				}
				else
				{
					resp.getWriter().println("It appears you're already in the database.");
				}
			}
			else
			{
				resp.getWriter().println("Please log in to your Google Apps Account");
			}
		} catch (Exception e) {
			//TODO Use a more specific exception
			resp.getWriter().println("<p>This page has encountered an error. Most likely, it found multiple accounts for your" +
					"email address in the database. This is a bug, and you should yell at the developers.</p>");
		}
		/** TODO Add a "Getting Started" guide*/
		hw.writeEpilog();
	}
}
