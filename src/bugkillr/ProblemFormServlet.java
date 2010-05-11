package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

/**
 * @author Randy Bohrer
 * This page offers a form for submitting new problems.
 * This should probably be implemented as a static page.
 */
@SuppressWarnings("serial")
public class ProblemFormServlet extends HttpServlet {
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
		
		hw.writeProlog("Bugkiller - Submit New Problem");
		hw.writeHeader();
		resp.getWriter().println(
				"<h1>Submit New Problem</h1>" +
				"<form name=\"input\" action=\"addproblemDB\" method=\"post\" >\n" +
					"<p>Name of the problem as it will appear in the problem list:<br/>\n" +
					"<input type=\"text\" name=\"problemName\"/></p>\n" +
					"<p>Complete URL for the problem's help material (including http://)<br/>\n" +
					"<input type=\"text\" name=\"helpURL\"/></p>\n" +
					"<p>Complete URL for the problem's description (including http://)<br/>\n" +
					"<input type=\"text\" name=\"descriptionURL\"/></p>\n" +
					"<p>Complete URL for the problem-solving program (including http://)<br/>\n" +
					"<input type=\"text\" name=\"solverURL\"/></p>\n" +
					"<p>Minimum score required to play this problem<br/>\n"+
					"<input type=\"text\" name=\"minscore\"/></p>\n"+
					"<input type=\"submit\" value=\"Submit\"/>"+
				"</form>");
		hw.writeEpilog();
	}
}
