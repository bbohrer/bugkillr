package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import html.HTMLWriter;
import redirects.Redirector;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - Home");
		hw.writeHeader();
		resp.getWriter().println("<h1>Home</h1>");
		try {
			if(redir.isLoggedIn())
			{
				if(redir.getUserFromDatastore() == null)
				{
					resp.getWriter().println("<p>It appears that you're new to the site. If you'd like to get started, ");
					hw.writeLink("adduser", "register");
					resp.getWriter().println("(It only takes a single click), and then ");
					hw.writeLink("viewteams", "join a team.</p>");
				}
				else
				{
					resp.getWriter().println("Welcome back, " + req.getUserPrincipal().getName() +"<br/>" +
							"<p>Note: We're constantly updating things, so if something's broken, please ");
					hw.writeLink("feedback", "tell us");
					resp.getWriter().println(". Even if nothing's broken, feel free to tell us what you think.</p>" +
							"<p> Don't know what to do? Read the ");
					hw.writeLink("/gettingstarted", "getting started");
					resp.getWriter().println(" page.</p>");
				}
			}
			else
			{
				UserService us = UserServiceFactory.getUserService();
				resp.getWriter().println("Please ");
				hw.writeLink(us.createLoginURL(req.getRequestURI()), "log in.");
				resp.getWriter().println(" Bug Killer uses Google Accounts to handle logins. " +
						"If you do not have a Google Account, please "); 
				hw.writeLink("https://www.google.com/accounts/NewAccount", "create one.");
			}
		} catch (Exception e) {
			//TODO Use a more specific exception
			e.printStackTrace();
			//resp.getWriter().println("<p>This page has encountered an error. Most likely, it found multiple accounts for your" +
			//		"email address in the database. This is a bug, and you should yell at the developers.</p>");
		}
		/** TODO Add a "Getting Started" guide*/
		hw.writeEpilog();
	}
}
