package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;

/**
 * @author Randy Bohrer
 * */
@SuppressWarnings("serial")
public class FeedbackServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		hw.writeProlog("Bugkiller - Feedback");
		hw.writeHeader();
		resp.getWriter().println("<h1>Feedback</h1>Bug Killer is a work in progress. Most likely there are bugs and" +
				" other things that should be improved.<br/> There are a few different ways you can send " +
				" feedback. <br/>For bugs, you can submit a bug report to the ");
		hw.writeLink("http://code.google.com/p/bugkillr/issues/entry", "bug tracker.");
		resp.getWriter().println("<br/> For general comments, you can ");
		hw.writeLink("mailto:bjb322@drexel.edu?subject=Bug%20Killer%20Feedback", "send us an email.");
		resp.getWriter().println("<p>Also, please fill out ");
		hw.writeLink("http://spreadsheets.google.com/viewform?hl=en&formkey=dGJQVnRuUDJBRlc1Q1dKdEw0czM2ZlE6MQ", "this survey.");
		resp.getWriter().println("</p>");
		hw.writeEpilog();
	}
}
