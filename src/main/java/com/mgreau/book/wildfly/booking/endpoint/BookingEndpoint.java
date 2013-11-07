package com.mgreau.book.wildfly.booking.endpoint;

import static com.mgreau.book.wildfly.booking.datastore.OfyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;
import com.mgreau.book.wildfly.booking.Ids;
import com.mgreau.book.wildfly.booking.entities.Booking;

@Api(name = "bookingendpoint", clientIds = { Ids.CLIENT_ID }, audiences = { Ids.AUDIENCE })
public class BookingEndpoint {
	
	private static Logger LOG = Logger.getLogger("BookingServlet");

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method.
	 * 
	 * @return List of all entities persisted.
	 */
	@ApiMethod(name = "hotels.bookings.list", path = "hotels/{hotelId}/bookings")
	public List<Booking> listBooking(@Named("hotelId") Long hotelId) {
		Query<Booking> query = ofy().load().type(Booking.class).filter("hotelId", hotelId);
		return query.list();
	}

	/**
	 * This method lists all bookings for a user. It uses HTTP GET method.
	 * 
	 * @return List of all entities persisted.
	 */
	@ApiMethod(name = "hotels.bookings.listByUser", path = "hotels/bookings/user")
	public List<Booking> listBookingByUser(User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("missing user");
		}
		Query<Booking> query = ofy().load().type(Booking.class).filter("userLogin", user);
		return query.list();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "hotels.bookings.get", path = "hotels/bookings/{id}")
	public Booking getBooking(@Named("id") Long id, User user)
			throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("missing user");
		}
		return ofy().load().type(Booking.class).id(id).now();
	}

	/**
	 * This inserts the entity into App Engine datastore. It uses HTTP POST
	 * method.
	 * 
	 * @param booking
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "hotels.bookings.insert", path = "hotels/{hotelId}/bookings")
	public Booking insertBooking(@Named("hotelId") Long hotelId,
			Booking booking, User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("missing user");
		}
		booking.setUser(user);
		booking.setHotelId(hotelId);
		booking.setId(ofy().save().entity(booking).now().getId());
		return booking;
	}

	/**
	 * This method is used for updating a entity. It uses HTTP PUT method.
	 * 
	 * @param booking
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "hotels.bookings.update", path = "hotels/{hotelId}/bookings")
	public Booking updateBooking(@Named("hotelId") Long hotelId,
			Booking booking, User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("missing user");
		}
		booking.setUser(user);
		booking.setHotelId(hotelId);
		booking.setId(ofy().save().entity(booking).now().getId());
		return booking;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "hotels.bookings.delete", path = "hotels/{hotelId}/bookings/{bookingId}")
	public Booking removeBooking(@Named("hotelId") Long hotelId,
			@Named("bookingId") Long id, User user)
			throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("missing user.");
		}
		
		Booking booking = ofy().load().type(Booking.class).id(id).now();
		
		if (hotelId.longValue() != booking.getHotelId().longValue()){
			throw new UnauthorizedException("hotel id doesn't macth with this booking.");
		}
		
		ofy().delete().entity(booking);
		return booking;
	}

}
