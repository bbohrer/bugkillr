package bugkillr;
import java.io.IOException;
import java.util.Enumeration;



import javax.servlet.http.*;

import html.HTMLWriter;
/**
 * @author Randy Bohrer
 * This servlet exists as a debugging tool for the problem submission system.
 * It prints the content of a submitted problem.
 */
@SuppressWarnings("serial")
public class PrintSubmissionServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		hw.writeProlog("Bugkiller - View Problem Submission");
		hw.writeHeader();
		
		resp.getWriter().println("<h1>View Problem Submission</h1>" +
				"Viewing submission from " + req.getUserPrincipal().getName()+" <br/>\n" +
				"Problem ID = " + req.getParameter("pid")+"<br/>\n" +
				"Source Code =" + req.getParameter("inputText") + "<br/>\n" );
	    Enumeration paramNames = req.getParameterNames();
	    while(paramNames.hasMoreElements())
	    {
	    	resp.getWriter().println("PARAM " + paramNames.nextElement() + "<br/>");
	    }
	    
		hw.writeEpilog();
		//req.getParameter("inputtext")
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		hw.writeProlog("Bugkiller - View Problem Submission");
		hw.writeHeader();
		hw.writeUnsupportedGet();
		hw.writeEpilog();
	}
}
