package bugkillr;
import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;
import bugkillr.PMF;

/**
 * @author Randy Bohrer
 * This is a template to base your servlets off of. It contains two functions, doGet and doPost.
 * The difference between GET and POST is that when a GET page has a parameter, its value is
 * passed through the URL, and when a POST page has a parameter, it's passed separately in
 * the HTTP request. POST can handle more data, so for pages with more than a few parameters,
 * use POST. Also, use POST for pages that may have sensitive data in the parameters, because
 * using GET makes this information very easy to change. For static pages or pages with a few
 * parameters, use GET. If you really want, it is also possible to use both.
 */
@SuppressWarnings("serial")
public class ServletTemplate extends HttpServlet {
	/** When this page is accessed with the GET method, this function is called.
	 * 	"req" contains information sent from the user, such as the account
	 *  they're logged into and the parameters to the web page. "resp" is used
	 *  to create the web page, and some other things, like redirecting to another page.*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*The PersistenceManager is used to access the database. The class "PMF" is a wrapper
		around PersistenceManager that makes it easier to reuse the same object without
		spending time creating multiple instances.*/
		PersistenceManager pm = PMF.get().getPersistenceManager();
		/*HTMLWriter is a custom class used to write common pieces of HTML. Its most useful
		 * function is to create the boilerplate HTML at the start of a page, including
		 * doctypes and the header. The other functions are not as powerful but may still
		 * be useful.*/
		HTMLWriter hw = new HTMLWriter(req, resp);
		/* Redirector contains functions that will redirect to another page under a certain condition,
		 * for example if the user is not logged in or is not in the database. It also contains some other
		 * functions such as checking whether the user is logged in because it is poorly organized.
		 */
		Redirector redir = new Redirector(req,resp);
		/*Write out the boilerplate code at the beginning of the web page.
		 * This functions takes the page title as a parameter.*/
		hw.writeProlog("Bugkiller - CHANGE THIS TEXT");
		/*This inserts the program's menu bar. Please use this function instead of
		 * making the menu yourself so if/when the menu changes, it will automatically update everywhere.*/
		hw.writeHeader();
		/*Write out the end of the HTML file. At the moment this is just a pair of closing tags for <body>
		 * and <html>, but it looks nicer than manually writing out the ending tags.*/
		hw.writeEpilog();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*The PersistenceManager is used to access the database. The class "PMF" is a wrapper
		around PersistenceManager that makes it easier to reuse the same object without
		spending time creating multiple instances.*/
		PersistenceManager pm = PMF.get().getPersistenceManager();
		/*HTMLWriter is a custom class used to write common pieces of HTML. Its most useful
		 * function is to create the boilerplate HTML at the start of a page, including
		 * doctypes and the header. The other functions are not as powerful but may still
		 * be useful.*/
		HTMLWriter hw = new HTMLWriter(req, resp);
		/* Redirector contains functions that will redirect to another page under a certain condition,
		 * for example if the user is not logged in or is not in the database. It also contains some other
		 * functions such as checking whether the user is logged in because it is poorly organized.
		 */
		Redirector redir = new Redirector(req,resp);
		/*Write out the boilerplate code at the beginning of the web page.
		 * This functions takes the page title as a parameter.*/
		hw.writeProlog("Bugkiller - CHANGE THIS TEXT");
		/*This inserts the program's menu bar. Please use this function instead of
		 * making the menu yourself so if/when the menu changes, it will automatically update everywhere.*/
		hw.writeHeader();
		/*Write out the end of the HTML file. At the moment this is just a pair of closing tags for <body>
		 * and <html>, but it looks nicer than manually writing out the ending tags.*/
		hw.writeEpilog();

	}
}
