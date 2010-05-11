package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

/**
 * @author Randy Bohrer
 * A simple servlet that asks the user to register in the database. This would be static
 * except the website header is subject to change.
 */
@SuppressWarnings("serial")
public class AskAddUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Redirector redir = new Redirector(req,resp);
			redir.loginRedirect();
			HTMLWriter hw = new HTMLWriter(req,resp);
			hw.writeProlog("Bugkiller - Please Register");
			hw.writeHeader();
			resp.getWriter().println("<h1>Please Register</h1>" +
					" You have been redirected to this page because you are" +
					" currently not in the Bug Killer database." +
					" To use most of the site's features, such as joining a team, solving problems," +
					" and recording your scores, you must first register with the database. This takes" +
					" only a single click, and we will record no personal information other than your" +
					" email address and the problems you've solved.");
			hw.writeLink("adduser", "Please click here to register in the database.");
			hw.writeEpilog();
	}
}
