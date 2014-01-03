<div class="alert alert-info">
	<%
		if (request.getParameter("msg") != null) {
	%>
		<%=request.getParameter("msg")%>
	<%
		} else if (request.getAttribute("msg") != null){
	%>
		<%=request.getAttribute("msg").toString()%>
	<%
		}
	%>
	
</div>