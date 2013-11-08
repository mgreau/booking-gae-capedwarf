<div class="alert alert-info">
	<%
		if (request.getParameter("msg") != null) {
	%>
		<%=request.getParameter("msg")%>
	<%
		}
	%>
</div>