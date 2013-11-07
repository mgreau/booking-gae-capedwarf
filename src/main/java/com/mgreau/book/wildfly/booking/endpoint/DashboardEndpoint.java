package com.mgreau.book.wildfly.booking.endpoint;

import static com.mgreau.book.wildfly.booking.datastore.OfyService.ofy;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.googlecode.objectify.cmd.Query;
import com.mgreau.book.wildfly.booking.Dashboard;
import com.mgreau.book.wildfly.booking.Ids;
import com.mgreau.book.wildfly.booking.entities.Hotel;

@Api(name = "bookingendpoint", clientIds = { Ids.CLIENT_ID }, audiences = { Ids.AUDIENCE })
public class DashboardEndpoint {

	@ApiMethod(path = "dashboard", name = "dashboard", httpMethod = "GET")
	public Dashboard getDashboard() {
		Dashboard dashboard = new Dashboard();
		dashboard.setNbHotels(0);
		dashboard.setCityList(new ArrayList<String>());
		dashboard.setCountryList(new ArrayList<String>());

		// count hotels
		dashboard.setNbHotels(ofy().load().type(Hotel.class).count());
		// city
		Query<Hotel> query = ofy().load().type(Hotel.class).limit(200);
		HashSet<String> cities = new HashSet<>();
		HashSet<String> countries = new HashSet<>();
		for (Hotel h : query) {
			cities.add(h.getCity());
			countries.add(h.getCountry());
		}
		dashboard
				.setCityList(new ArrayList<String>(new HashSet<String>(cities)));
		// country
		dashboard.setCountryList(new ArrayList<String>(new HashSet<String>(
				countries)));

		return dashboard;
	}

}
