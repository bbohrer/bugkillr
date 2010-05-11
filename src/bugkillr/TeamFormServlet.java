package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

/**
 * @author Randy Bohrer
 * This page offers a form for creating new teams.
 * This should probably be a static page.
 */
@SuppressWarnings("serial")
public class TeamFormServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hw.writeProlog("Bugkiller - Create New Team");
		hw.writeHeader();
		resp.getWriter().println(
				"<h1>Create New Team</h1>" +
				"<form name=\"input\" action=\"addteamDB\" method=\"post\" >\n" +
					"<p>Name of the team to create:<br/>\n" +
					"<input type=\"text\" name=\"teamName\"/></p>\n" +
					"<p>Join the new team " +
					"<input type=\"checkbox\" name=\"joinTeam\" value=\"true\"/></p>\n" +
					"<input type=\"submit\" value=\"Submit\"/>"+
				"</form>");
		hw.writeEpilog();
	}
}
