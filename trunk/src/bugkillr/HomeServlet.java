package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;
import html.HTMLWriter;
import redirects.Redirector;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - Home");
		hw.writeHeader();
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
					resp.getWriter().println("Welcome back," + req.getUserPrincipal().getName());
				}
			}
			else
			{
				resp.getWriter().println("Please log in to your Google Apps Account");
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
