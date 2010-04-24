package bugkillr;
import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;

/**
 * @author Randy Bohrer
 * This servlet takes a request to build a new problem and adds the problem to the database.
 * */
@SuppressWarnings("serial")
public class DBAddProblemServlet extends HttpServlet {
	/**					"<p>Name of the problem as it will appear in the problem list:<br/>\n" +
					"<input type=\"text\" name=\"problemName\"/></p>\n" +
					"<p>Complete URL for the problem's help material (including http://)<br/>\n" +
					"<input type=\"text\" name=\"helpURL\"/></p>\n" +
					"<p>Complete URL for the problem's description (including http://)<br/>\n" +
					"<input type=\"text\" name=\"descriptionURL\"/></p>\n" +
					"<p>Complete URL for the problem-solving program (including http://)<br/>\n" +
					"<input type=\"text\" name=\"solverURL\"></textarea></p>\n" +
					"<input type=\"submit\" value=\"Submit\"/>"+*/
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		//Make sure the user is logged in. Otherwise it would be easy for the attacker to add problems
		//TODO Make it so that only administrators can add problems. Otherwise an attacker could still add them!
		redir.loginRedirect();
		hw.writeProlog("Bugkiller - Add Problem to Database");
		//(String Name, String DescriptionURL, String HelpURL, String SolverURL)
		Problem problem = new Problem(req.getParameter("problemName"),req.getParameter("descriptionURL"),
				req.getParameter("helpURL"), req.getParameter("solverURL"));
		pm.makePersistent(problem);
		pm.close();
		hw.writeHeader();
		resp.getWriter().println("<h1>Add Problem to Database</h1>" +
				"Added problem with the following parameters:<br/>\n" +
				"Problem Name: " + req.getParameter("problemName") +"<br/>\n" +
				"Description URL: " + req.getParameter("descriptionURL") + "<br/>\n" +
				"Help URL: " + req.getParameter("helpURL") + "<br/>\n" +
				"Solver URL: " + req.getParameter("solverURL"));
		hw.writeEpilog(); 
	}
}
