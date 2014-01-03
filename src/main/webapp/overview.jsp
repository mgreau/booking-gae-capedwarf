
<%
	Integer nbHotel = (Integer) request.getAttribute("nbHotel");
    if (nbHotel == null) nbHotel = 0;
%>

<jsp:include page="header.jsp"></jsp:include>
	<h1>Hello Booking - GAE - CapeDwarf!</h1>

	<h2>App Overview</h2>
	<p>The same code is deployed on <b>OpenShift / CapeDwarf-WildFly and on AppEngine GAE</b></p>
		<span class="label label-danger">Hotels in datastore : <%=nbHotel%></span>
		<%
			if (nbHotel == 0) {
		%>
		<div class="alert alert-warning"> Use the <a href="/jsp/taskqueue.jsp">TaskQueue to add hotel</a> to the datastore</div>
		<%
			}
		%>
	</ul>

<jsp:include page="msg.jsp"></jsp:include>

<jsp:include page="sidebar.jsp"></jsp:include>
<jsp:include page="footer.jsp"></jsp:include>

