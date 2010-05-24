package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

@SuppressWarnings("serial")
/**
 * The beginning of the problem submission process. This page
 * creates an HTML form that the player uses to submit their problem.
 * From here, the player's code is sent to another page, which uses an
 * HTTP POST to send the data to a CGI program and determine whether it's
 * solved correctly. Then it produces an error message if appropriate or
 * tells the user they succeeded and updates the database.
 */
public class SubmissionFormServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - Submit a Solution");
		hw.writeHeader();
		resp.getWriter().println("<h1>Submit a Solution</h1>");
		
		//Make sure the player is logged in, is in the database and has a team.
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(req.getParameter("pid") == null)
		{
			resp.getWriter().println("Error: No problem ID was supplied. If you try to submit this form, the " +
					"submission will not be processed. This error most likely occurred because you logged out " +
					"and logged in again. If you were at this page before, you can safely return to it with your " +
					"browser's back button.");
			hw.writeEpilog();
			return;
		}
		//Display a message to the user
		resp.getWriter().println("Copy and paste your source code into the area below.<br/>\n");
		//Submission form
		resp.getWriter().println("<form name=\"input\" action=\"DBprocess_submission\" method=\"post\" >\n" +
				"<textarea name=\"inputText\" rows=\"25\" cols=\"120\">Paste code here.</textarea>\n" +
				"<input type=\"hidden\" name=\"pid\" value=\""+req.getParameter("pid")+"\"/><br/>\n" +
				"<input type=\"submit\" value=\"Submit\"/>\n" +
				"</form>");
		hw.writeEpilog();
	}
}
