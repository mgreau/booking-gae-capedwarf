package com.mgreau.book.wildfly.booking.servlet;

import static com.mgreau.book.wildfly.booking.datastore.OfyService.ofy;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.cmd.LoadType;
import com.mgreau.book.wildfly.booking.entities.Hotel;

@SuppressWarnings("serial")
public class BookingServlet extends HttpServlet {

	private static Logger LOG = Logger.getLogger("BookingServlet");

	private static final String APP_NAME = "Cloud Booking";

	private static final String BASE_URL = "https://cloud-booking.appspot.com/img/";


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		int nb =  ofy().load().type(Hotel.class).list().size();
		
		req.setAttribute("nbHotel", nb);
		LOG.info("home - nbHotel : " + nb);
		req.getRequestDispatcher("/overview.jsp").forward(req, resp);

	}
}
