package bugkillr;
import java.io.IOException;
import javax.servlet.http.*;
import html.HTMLWriter;
import redirects.Redirector;

@SuppressWarnings("serial")
public class AskJoinTeamServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Redirector redir = new Redirector(req,resp);
			redir.loginRedirect();
			try {
				redir.userRedirect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HTMLWriter hw = new HTMLWriter(req,resp);
			hw.writeProlog("Bugkiller - Please Join a Team");
			hw.writeHeader();
			resp.getWriter().println("<h1>Please Join a Team</h1>" +
					"You have been redirected to this page because" +
					" you are currently not on a team. Some features" +
					" of this site only work when you are on a team. To use these features," +
					" please ");
			hw.writeLink("viewteams", "join a team");
			resp.getWriter().println(" or ");
			hw.writeLink("addteamform","create a new one.");
			hw.writeEpilog();
	
	}
}
