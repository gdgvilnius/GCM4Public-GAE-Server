package lt.andro.gcm4public;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class SendGCM2Clients extends HttpServlet {
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String senderId = req.getParameter("senderId");
		String apiKey = req.getParameter("apiKey");
		String title = req.getParameter("title");
		String message = req.getParameter("message");
		String url = req.getParameter("url");
		if (senderId != null && !"".equalsIgnoreCase(senderId)) {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Query query = new Query("Client");
			query = query.addFilter("senderId", FilterOperator.EQUAL, senderId);
			PreparedQuery preparedQuery = datastore.prepare(query);
			List<Entity> entities = preparedQuery.asList(FetchOptions.Builder
					.withLimit(1000));
			resp.setContentType("text/plain");
			List<String> devices = new ArrayList<String>();
			for (Entity entity : entities) {
				devices.add((String) entity.getProperty("registrationId"));
			}
			MulticastResult result = sendMessageToDevice(apiKey, devices,
					title, message, url);

			resp.getWriter().println("GCM Message fields:");
			resp.getWriter().println("title: " + title);
			resp.getWriter().println("message:" + message);
			resp.getWriter().println("url: " + url);
			resp.getWriter().println(
					"Message sent to these devices: " + devices);
			resp.getWriter().println("This API key was used: " + apiKey);
			resp.getWriter().println("The result is: " + result);
		} else {
			resp.getWriter()
					.println(
							"Please provide your senderId in the URL parameter. Something like this: http://gcm4public.appspot.com/registeredgcmclients?senderId=716163315987");
		}
	}

	private MulticastResult sendMessageToDevice(String apiKey,
			List<String> devices, String title, String message, String url) {
		Sender sender = new Sender(apiKey);
		Message gcmMessage = new Message.Builder().addData("title", title)
				.addData("message", message).addData("url", url).build();
		MulticastResult result = null;
		try {
			result = sender.send(gcmMessage, devices, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
