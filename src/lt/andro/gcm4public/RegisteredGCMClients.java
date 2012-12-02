package lt.andro.gcm4public;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class RegisteredGCMClients extends HttpServlet {
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String senderId = req.getParameter("senderId");
		if (senderId != null && !"".equalsIgnoreCase(senderId)) {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Query query = new Query("Client");
			query = query.addFilter("senderId", FilterOperator.EQUAL, senderId);
			PreparedQuery preparedQuery = datastore.prepare(query);
			List<Entity> entities = preparedQuery.asList(FetchOptions.Builder
					.withLimit(1000));
			for (Entity entity : entities) {

				String entityRegistrationId = (String) entity
						.getProperty("registrationId");
				String entitySenderId = (String) entity.getProperty("senderId");

				resp.getWriter().println(
						"senderId: " + entitySenderId + ". registrationId: "
								+ entityRegistrationId);
			}
		} else {
			resp.getWriter()
					.println(
							"Please provide your senderId in the URL parameter. Something like this: http://gcm4public.appspot.com/registeredgcmclients?senderId=716163315987");
		}
	}
}
