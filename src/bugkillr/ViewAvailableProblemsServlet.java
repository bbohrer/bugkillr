package bugkillr;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;
import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;
import bugkillr.Problem;

@SuppressWarnings("serial")
public class ViewAvailableProblemsServlet extends HttpServlet {
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
		hw.writeProlog("Bugkiller - Available Problems");
		hw.writeHeader();
		resp.getWriter().println("<h1>Available Problems</h1>");

		//Find the problems available to the current user, sorted by the minumim score
		Query getUnsolved = pm.newQuery("select from " + Problem.class.getName() +
				" where minscore <= curScore order by minscore ascending");
		getUnsolved.declareParameters("int curScore");
		
		//Find the problems solved by the current user
		PersistenceManager junction_pm = PMF.get().getPersistenceManager();
		Query getJunctions = junction_pm.newQuery("select problemId from " + UserProblemJunction.class.getName() +
					" where userId == theID");
		getJunctions.declareImports("import com.google.appengine.api.datastore.Key;");
		getJunctions.declareParameters("Key theID");
		try {
			User curUser = redir.getUserFromDatastore();
			List<Problem> unsolvedProblems = (List<Problem>) getUnsolved.execute(curUser.getScore());
			List<Long> junctions = (List<Long>) getJunctions.execute(curUser.getKey());
			Set<Long> pidSet = new HashSet<Long>(junctions);
			resp.getWriter().println("<table>" +
					"<th>Problem Name</th><th>Status</th>");
			int curRow = 1;
			for(Problem p : unsolvedProblems)
			{
				String trClass = (curRow++%2==0)?"even":"odd";
				resp.getWriter().println("<tr class =\"" +trClass+"\"><td>");
				hw.writeLink("viewproblem?pid=" + p.getKey(), p.getName());
				resp.getWriter().println("</td><td>");
				resp.getWriter().println(pidSet.contains(p.getKey())? "Solved":"Unsolved");
				resp.getWriter().println("</td></tr>");
			}
			resp.getWriter().println("</table>");
		} catch (Exception e) {
			resp.getWriter().println("<pre>");
			e.printStackTrace(resp.getWriter());
			resp.getWriter().println("</pre>");
		}
		hw.writeEpilog();
	}
}