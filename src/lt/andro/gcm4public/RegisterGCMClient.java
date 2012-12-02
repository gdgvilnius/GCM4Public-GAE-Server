package lt.andro.gcm4public;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class RegisterGCMClient extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String registrationId = req.getParameter("registrationId");
		String senderId = req.getParameter("senderId");

		resp.setContentType("text/plain");
		resp.getWriter().println(
				"Registered this registrationId: " + registrationId);
		resp.getWriter().println("senderId: " + senderId);

		Entity entity = new Entity("Client");
		entity.setProperty("registrationId", registrationId);
		entity.setProperty("senderId", senderId);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(entity);
	}
}
