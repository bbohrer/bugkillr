package bugkillr;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.*;

import html.HTMLWriter;

/**
 * @author Duc Anh Nguyen
 */
@SuppressWarnings("serial")
public class ToolServlet extends HttpServlet {
	/** When this page is accessed with the GET method, this function is called.
	 * 	"req" contains information sent from the user, such as the account
	 *  they're logged into and the parameters to the web page. "resp" is used
	 *  to create the web page, and some other things, like redirecting to another page.*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*HTMLWriter is a custom class used to write common pieces of HTML. Its most useful
		 * function is to create the boilerplate HTML at the start of a page, including
		 * doctypes and the header. The other functions are not as powerful but may still
		 * be useful.*/
		HTMLWriter hw = new HTMLWriter(req, resp);
		/*Write out the boilerplate code at the beginning of the web page.
		 * This functions takes the page title as a parameter.*/
		hw.writeProlog("Bugkiller - Tools for debugging master");
		/*This inserts the program's menu bar. Please use this function instead of
		 * making the menu yourself so if/when the menu changes, it will automatically update everywhere.*/
		hw.writeHeader();
		/*resp.getWriter().println("<iframe frameborder='0' src ='../static/toolpage/tools.html' width='100%' height='90%'>");
		resp.getWriter().println(" <p>Your browser does not support iframes.</p>");
		resp.getWriter().println(" </iframe>");*/
		try {
			URL url = new URL("http://bugkillr.appspot.com/static/toolpage/tools.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                resp.getWriter().println(line);
            }
            reader.close();
		} catch (MalformedURLException e) {
			resp.getWriter().println("Can't fetch the tool page due to malformed URL");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*Write out the end of the HTML file. At the moment this is just a pair of closing tags for <body>
		 * and <html>, but it looks nicer than manually writing out the ending tags.*/
		hw.writeEpilog();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*HTMLWriter is a custom class used to write common pieces of HTML. Its most useful
		 * function is to create the boilerplate HTML at the start of a page, including
		 * doctypes and the header. The other functions are not as powerful but may still
		 * be useful.*/
		HTMLWriter hw = new HTMLWriter(req, resp);
		/*Write out the boilerplate code at the beginning of the web page.
		 * This functions takes the page title as a parameter.*/
		hw.writeProlog("Bugkiller - Tools for debugging master");
		/*This inserts the program's menu bar. Please use this function instead of
		 * making the menu yourself so if/when the menu changes, it will automatically update everywhere.*/
		hw.writeHeader();
		/*Write out the end of the HTML file. At the moment this is just a pair of closing tags for <body>
		 * and <html>, but it looks nicer than manually writing out the ending tags.*/
		hw.writeEpilog();

	}
}
