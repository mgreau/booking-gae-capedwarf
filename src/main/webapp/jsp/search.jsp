<%@page import="com.mgreau.book.wildfly.booking.entities.Hotel"%>

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
		Hotel h = (Hotel) request.getAttribute("hotel");
		if (h != null) {
%>
<div>
	<p>Here is a sample hotel found with the query :</p>
	<ul>
		<li><%=h.getHotelName()%></li>
	</ul>
</div>
<%
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
