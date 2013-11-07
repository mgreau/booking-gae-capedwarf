<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions.Builder"%>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions"%>

<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	UploadOptions upOpts = Builder.withDefaults();
	upOpts.maxUploadSizeBytes(10000000);
%>

<jsp:include page="../header.jsp"></jsp:include>
<h2>Blobstore</h2>
<jsp:include page="../msg.jsp"></jsp:include>

	<form action="<%=blobstoreService.createUploadUrl("/upload-to-blobstore", upOpts)%>"
		method="post" enctype="multipart/form-data">
		<input type="text" name="foo"> <input type="file"
			name="csvFile"> <input type="submit" value="Submit">
	</form>
	
<jsp:include page="../sidebar.jsp"></jsp:include>
<jsp:include page="../footer.jsp"></jsp:include>