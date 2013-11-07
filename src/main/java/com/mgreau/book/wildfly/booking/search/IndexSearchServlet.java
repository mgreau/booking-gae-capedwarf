package com.mgreau.book.wildfly.booking.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mgreau.book.wildfly.booking.entities.Hotel;

@SuppressWarnings("serial")
public class IndexSearchServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String query = req.getParameter("query");

		List<Hotel> hotels = IndexSearchHotel.searchHotel(query);

		req.setAttribute("msg", "Nb result :  " + hotels.size() +" for search query : " + query);
		
		if (hotels.size() > 0){
			req.setAttribute("hotel", hotels.get(0));
		}
		req.getRequestDispatcher("/jsp/search.jsp").forward(req, resp);
	}

}
