package html;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.*;

import redirects.Redirector;
/**
 * 
 * @author Randy Bohrer
 * HTMLWriter contains functions for writing common
 * pieces of HTML.
 */
public class HTMLWriter {
	private HttpServletResponse resp;
	private HttpServletRequest req;
	
	/**
	 * @param Resp The HttpServletResponse which the HTMLWriter will write to
	 */
	public HTMLWriter(HttpServletRequest Req, HttpServletResponse Resp)
	{
		req = Req;
		resp = Resp;
	}
	
	/**
	 * @param titleText The page's title
	 * @throws IOException
	 * This creates the beginning of the XHTML page. It writes out the doctype and html tags, and a 
	 * header (which includes the specified title and a meta tag denoting the character encoding)
	 */
	public void writeProlog(String titleText) throws IOException
	{
		resp.getWriter().println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
				"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
				"<head>\n" +
				"<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />" +
				"<title>" + titleText + "</title>\n" +
				"</head>\n" +
				"<body>");
	}
	/**
	 * @throws IOException
	 * Writes the end of an XHTML page (Closing tags for "body" and "html")
	 */
	public void writeEpilog() throws IOException
	{
		resp.getWriter().println("</body>\n" +
								 "</html>");
	}
	
	/**
	 * @param address The address that the link leads to.
	 * @param text The text of the link
	 * @throws IOException
	 * Inserts a hyperlink.
	 */
	public void writeLink(String address, String text) throws IOException
	{
		resp.getWriter().println("<a href=\"" + address + "\">" + text + "</a>");
	}
	
	/**
	 * @throws IOException 
	 * Inserts a horizontal rule on the page.
	 */
	public void writeRule() throws IOException
	{
		resp.getWriter().println("<hr/>");
	}
	/**
	 * @throws IOException
	 * This writes out the XHTML for a header bar, containing links to the major
	 * pages of the site. Use this function instead of writing out the header 
	 * "by hand" so that when the links change it remains consistent.
	 */
	public void writeHeader() throws IOException
	{
		//Unimplemented pages are commented out.
		UserService us = UserServiceFactory.getUserService();
		Redirector redir = new Redirector(req,resp);
		writeLink("home", "Home");
		writeLink("problems", "Unsolved Problems");
		//writeLink("solved", "Solved Problems");
		//writeLink("tools", "Tools");
		//writeLink("highscores", "Team Rankings");
		writeLink("addteamform", "Create New Team");
		writeLink("viewteams", "Join a Different Team");
		writeLink("addproblemform","Create New Problem");
		//Use Google Apps API to generate login/logout links.
		if(redir.isLoggedIn())
		{
			writeLink(us.createLogoutURL(req.getRequestURI()), "Log Out");
		}
		else
		{
			writeLink(us.createLoginURL(req.getRequestURI()), "Log In");
		}
		writeRule();
	}
}
