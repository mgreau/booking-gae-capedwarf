package com.mgreau.book.wildfly.booking.endpoint;

import static com.mgreau.book.wildfly.booking.datastore.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.cmd.Query;
import com.mgreau.book.wildfly.booking.Ids;
import com.mgreau.book.wildfly.booking.entities.Hotel;
import com.mgreau.book.wildfly.booking.search.IndexSearchHotel;

@Api(name = "bookingendpoint", clientIds = { Ids.CLIENT_ID }, audiences = { Ids.AUDIENCE })
public class HotelEndpoint {
	
	private static Logger LOG = Logger.getLogger("BookingServlet");

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method.
	 * 
	 * @return List of all entities persisted.
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	@ApiMethod(name = "hotels.list")
	public CollectionResponse<Hotel> listHotel(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {
		List<Hotel> result = new ArrayList<>();
		Cursor cursor = null;
		
		if (limit == null)
			limit = 10;
		
		Query<Hotel> query = ofy().load().type(Hotel.class).limit(limit);
		
		// To check if its the latest cursor
		String backupCursor = cursorString;
		if (cursorString != null && cursorString != "") {
			cursor = Cursor.fromWebSafeString(cursorString);
			query = query.startAt(cursor);
		}
		
		QueryResultIterator<Hotel> iterator = query.iterable().iterator();
		while (iterator.hasNext()) {
	        result.add(iterator.next());
	    }
		
		cursor = iterator.getCursor();
		LOG.info("cursor: " + cursor);
		
		if (cursor != null && result != null && limit != null
				&& result.size() == limit)
			cursorString = cursor.toWebSafeString();
		else
			cursorString = "";

		if (cursorString != null && backupCursor != null
				&& backupCursor.equals(cursorString)) {
			cursorString = "";
		}

		return CollectionResponse.<Hotel> builder().setItems(result)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "hotels.get")
	public Hotel getHotel(@Named("id") Long id) {
		LoadResult<Hotel> result = ofy().load()
				.key(Key.create(Hotel.class, id));
		return result.now();
	}

	/**
	 * This inserts the entity into App Engine datastore. It uses HTTP POST
	 * method.
	 * 
	 * @param hotel
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "hotels.insert")
	public Hotel insertHotel(Hotel hotel) {
		ofy().save().entity(hotel).now();
		IndexSearchHotel.addHotelToSearchIndex(hotel);
		return hotel;
	}

	/**
	 * This method is used for updating a entity. It uses HTTP PUT method.
	 * 
	 * @param hotel
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "hotels.update")
	public Hotel updateHotel(Hotel hotel) {
		ofy().save().entity(hotel).now();
		IndexSearchHotel.addHotelToSearchIndex(hotel);
		return hotel;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "hotels.delete")
	public Hotel removeHotel(@Named("id") Long id) {
		Hotel hotel = ofy().load().key(Key.create(Hotel.class, id)).now();
		ofy().delete().entity(hotel).now();
		return hotel;
	}

	@ApiMethod(httpMethod = "GET", name = "hotels.search")
	public List<Hotel> searchHotel(@Named("term") String queryString) {
		return IndexSearchHotel.searchHotel(queryString);
	}

}
