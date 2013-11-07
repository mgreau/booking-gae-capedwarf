package com.mgreau.book.wildfly.booking.search;

import static com.mgreau.book.wildfly.booking.datastore.OfyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.mgreau.book.wildfly.booking.entities.Hotel;
import com.mgreau.book.wildfly.booking.taskqueue.LoadHotelServlet;

public class IndexSearchHotel {
	
	/** Logger */
	private static final Logger LOG = Logger.getLogger(LoadHotelServlet.class
			.getCanonicalName());
	
	public static final Index INDEX = getIndex();

	private static Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("hotelindex")
				.build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}
	
	public static List<Hotel> searchHotel(String queryString){
		List<Hotel> hotelList = new ArrayList<Hotel>();
		Results<ScoredDocument> results = IndexSearchHotel.INDEX.search(queryString);

		for (ScoredDocument scoredDoc : results) {
			Field f = scoredDoc.getOnlyField("id");
			if (f == null || f.getText() == null)
				continue;

			long hotelId = Long.parseLong(f.getText());
			if (hotelId != -1) {
				LoadResult<Hotel> result = ofy().load().key(Key.create(Hotel.class, hotelId));
				hotelList.add(result.now());
			}
		}
		return hotelList;
	}
	
	public static void addHotelToSearchIndex(Hotel h) {
		Document.Builder docBuilder = Document
				.newBuilder()
				.addField(
						Field.newBuilder()
								.setName("name")
								.setText(
										h.getHotelName() != null ? h
												.getHotelName() : ""))
				.addField(
						Field.newBuilder().setName("id")
								.setText(Long.toString(h.getId())))
				.addField(
						Field.newBuilder()
								.setName("country")
								.setText(
										h.getCountry() != null ? h.getCountry()
												: ""))
				.addField(
						Field.newBuilder()
								.setName("price")
								.setNumber(
										h.getPrice() != null ? h.getPrice() : 0))
				.addField(
						Field.newBuilder()
								.setName("city")
								.setText(h.getCity() != null ? h.getCity() : ""))
				.addField(
						Field.newBuilder()
								.setName("numberOfStars")
								.setNumber(
										h.getNumberOfStars() != null ? h
												.getNumberOfStars() : 0))
				.addField(
						Field.newBuilder().setName("published")
								.setDate((new Date())));
		docBuilder.setId(Long.toString(h.getId()));
		Document doc = docBuilder.build();
		INDEX.put(doc);
	}

	/**
	 * Clean Index before insert tests datas
	 */
	public static void removeIndex() {

		LOG.info("Remove Search Index");

		try {
			while (true) {
				List<String> docIds = new ArrayList<String>();
				 // Return a set of doc_ids.
		        GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
		        GetResponse<Document> response = getIndex().getRange(request);
		        if (response.getResults().isEmpty()) {
		            break;
		        }
				for (Document doc : response) {
					docIds.add(doc.getId());
				}
				INDEX.deleteAsync(docIds);
			}
		} catch (RuntimeException e) {
			LOG.log(Level.SEVERE, "Failed to remove documents", e);
		}
	}


}
