package examples;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.*;

public class TestFetchServlet extends HttpServlet {
	private static final long serialVersionUID = -2975919054867013676L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		String message = "inputText=" + URLEncoder.encode("Hello, URLFetch!\n", "UTF-8");
		try {
			URL url = new URL("http://cs.drexel.edu/~bjb322/cgi-bin/cat.cgi");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setUseCaches(true);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			OutputStream w = connection.getOutputStream();
			w.write(message.getBytes());
			w.flush();
			w.close();
			resp.getWriter().println("According to Java, request method is " + connection.getRequestMethod());
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while((line = reader.readLine())!= null)
				{
					resp.getWriter().println(line);
				}
			}
		} 
		catch (MalformedURLException e) {
			resp.getWriter().println("Error: "+e);
		} catch (IOException e) {
			resp.getWriter().println("Error: "+e);
		}
	}
}
