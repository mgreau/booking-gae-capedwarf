package com.mgreau.book.wildfly.booking.storage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

/**
 * THis servlet uses the BlobStore API to get the uploaded file and to store the
 * blob in Google Cloud Storage.
 * 
 * @author mgreau
 * 
 */
@SuppressWarnings("serial")
public class UploadToStorageServlet extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	public void init(ServletConfig config) throws ServletException {

	}

	/**
	 * This is where backoff parameters are configured. Here it is aggressively
	 * retrying with backoff, up to 10 times but taking no more that 15 seconds
	 * total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory
			.createGcsService(new RetryParams.Builder()
					.initialRetryDelayMillis(10).retryMaxAttempts(10)
					.totalRetryPeriodMillis(15000).build());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Map<String, List<FileInfo>> map = blobstoreService.getFileInfos(req);
		FileInfo fi = map.get("csvFile").get(0);

		String gcsFileName = fi.getFilename();
		System.out.println("-------------------------");
		System.out.println("gcs-filename : " + gcsFileName);
		System.out.println("gcs size : " + fi.getSize());
		System.out.println("gcs object name : " + fi.getGsObjectName());

		if (gcsFileName == null) {
			resp.sendRedirect("/home?error=1");
		} else {
			String msg = "File upload (" + gcsFileName + ")";
			resp.sendRedirect("/home?msg=" + msg);
		}

	}

}
