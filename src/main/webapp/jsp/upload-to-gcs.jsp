<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page
	import="com.google.appengine.api.blobstore.UploadOptions.Builder"%>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions"%>

<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	//Add bucket name to store in Google Cloud Storage instead of Blobstore
	UploadOptions upOpts = Builder
			.withGoogleStorageBucketName("hotels_datas");
	upOpts.maxUploadSizeBytes(10000000);
%>

<jsp:include page="../header.jsp"></jsp:include>
<h2>Google Cloud Storage</h2>
<jsp:include page="../msg.jsp"></jsp:include>

<h2>Upload a file to Google Cloud Storage</h2>
<form
	action="<%=blobstoreService
					.createUploadUrl("/upload-to-gcs", upOpts)%>"
	method="post" enctype="multipart/form-data">
	<input type="text" name="foo"> <input type="file"
		name="csvFile"> <input type="submit" value="Submit">
</form>

<jsp:include page="../sidebar.jsp"></jsp:include>
<jsp:include page="../footer.jsp"></jsp:include>
