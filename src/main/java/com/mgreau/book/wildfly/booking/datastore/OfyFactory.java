/*
 */

package com.mgreau.book.wildfly.booking.datastore;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.googlecode.objectify.ObjectifyFactory;
import com.mgreau.book.wildfly.booking.entities.Booking;
import com.mgreau.book.wildfly.booking.entities.Hotel;

/**
 * Our version of ObjectifyFactory which integrates with Guice.  You could and convenience methods here too.
 *
 * @author Jeff Schnitzer
 */
@Singleton
public class OfyFactory extends ObjectifyFactory
{
	
	static Logger LOG = Logger.getLogger("OfyFactory");

	/** Register our entity types*/
	public OfyFactory() {
		long time = System.currentTimeMillis();

		this.register(Hotel.class);
		this.register(Booking.class);

		long millis = System.currentTimeMillis() - time;
		LOG.info("Registration took " + millis + " millis");
	}

	
	@Override
	public Ofy begin() {
		return new Ofy(this);
	}
}