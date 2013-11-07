<jsp:include page="../header.jsp"></jsp:include>
<h2>Task Queue</h2>
<jsp:include page="../msg.jsp"></jsp:include>

<div id="content">
	<h2>Use a task Queue (push) to insert hotels into datastore :</h2>
	
	<p>Choose a number of hotels to load into datastore :</p>
	<form action="/add-to-queue"
		method="get" >
		<input type="radio" name="size" value="XXS"> <label>100 hotels</label>
		<br/>
		<input type="radio" name="size" value="XS"> <label>500 hotels</label>
		<br/>
		<input type="radio" name="size" value="S"> <label>1 000 hotels</label>
		<br/>
		<input type="radio" name="size" value="M"> <label>5 000 hotels</label>
		<input type="hidden" value="hotel_datas"
			name="filename"> <input type="submit" value="Submit">
	</form>
</div>

<jsp:include page="../sidebar.jsp"></jsp:include>
<jsp:include page="../footer.jsp"></jsp:include>