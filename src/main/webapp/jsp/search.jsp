<%@page import="com.mgreau.book.wildfly.booking.entities.Hotel"%>
<%@page import="java.util.*"%>

<jsp:include page="../header.jsp"></jsp:include>
<h2>Search API :</h2>
<jsp:include page="../msg.jsp"></jsp:include>

<p>Insert a query to test the search API :</p>
<form action="/search" method="get">
	<input type="text" name="query" value="" placeholder="Write a query">
	<input type="submit" value="Submit">
</form>

<%
	if (request.getAttribute("msg") != null) {
		List<Hotel> list = (List<Hotel>) request.getAttribute("hotels");
		if (list != null && list.size() > 0) {
			for (Hotel h : list){
%>
<div>
	<ul>
		<li>ID : <a href="/_ah/api/bookingendpoint/v1/hotel/<%=h.getId()%>"><%=h.getId()%> - (Endpoint URL)</a></li>
		<li>Name : <%=h.getHotelName()%></li>
		<li>Address : <%=h.getAddress()%> </li>
		<li>City : <%=h.getCity()%> </li>
	</ul>
</div>
<%			}
	} else {
%>
<div>No result.</div>
<%
	}
%>
<%
	}
%>

<jsp:include page="../sidebar.jsp"></jsp:include>
<jsp:include page="../footer.jsp"></jsp:include>
