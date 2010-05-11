package bugkillr;
import java.io.IOException;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;
import bugkillr.Problem;

@SuppressWarnings("serial")
public class ViewUnsolvedProblemsServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		redir.loginRedirect();
		try {
			redir.userRedirect();
			redir.teamRedirect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		hw.writeProlog("Bugkiller - Unsolved Problems");
		hw.writeHeader();
		resp.getWriter().println("<h1>Unsolved Problems</h1>");

		Query getUnsolved = pm.newQuery("select from " + Problem.class.getName() +
				" where minscore <= curScore order by minscore ascending");
		getUnsolved.declareParameters("int curScore");
		try {
			List<Problem> unsolvedProblems = (List<Problem>) getUnsolved.execute(redir.getUserFromDatastore().getScore());
			resp.getWriter().println("<table>");
			for(Problem p : unsolvedProblems)
			{
				resp.getWriter().println("<tr><td>");
				hw.writeLink("viewproblem?pid=" + p.getKey(), p.getName());
				resp.getWriter().println("</td></tr>");
			}
			resp.getWriter().println("</table>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		hw.writeEpilog();
	}
}
