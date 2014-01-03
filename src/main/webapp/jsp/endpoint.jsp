<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page
	import="com.google.appengine.api.blobstore.UploadOptions.Builder"%>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions"%>

<jsp:include page="../header.jsp"></jsp:include>

<h2>Endpoints</h2>

<jsp:include page="../msg.jsp"></jsp:include>

<p>In order to test endpoints, just use CURL or a browser :</p>

<ul>
	<li>Get hotel : <a
		href="/_ah/api/bookingendpoint/v1/hotel/1">/_ah/api/bookingendpoint/v1/hotel/1</a></li>
	<li>Add hotel : " curl -H 'Content-Type: application/json' -d
		'{"nameHotel": "Hôtel de France", "price":"120", "city":"Nantes",
		"country":"FRANCE" }'
		http://localhost:8888/_ah/api/bookingendpoint/v1/hotel"</li>
	<li>Search hotels : <a
		href="/_ah/api/bookingendpoint/v1/searchHotel/price=5">/_ah/api/bookingendpoint/v1/searchHotel/price=5</a></li>
</ul>

<jsp:include page="../sidebar.jsp"></jsp:include>
<jsp:include page="../footer.jsp"></jsp:include>
