<div class="alert alert-info">
	<%
		if (request.getAttribute("msg") != null) {
	%>
		<%=request.getAttribute("msg").toString()%>
	<%
		}
	%>
</div>