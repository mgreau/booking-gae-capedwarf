package com.mgreau.book.wildfly.booking.taskqueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.common.io.ByteStreams;
import com.mgreau.book.wildfly.booking.endpoint.DashboardEndpoint;
import com.mgreau.book.wildfly.booking.endpoint.HotelEndpoint;
import com.mgreau.book.wildfly.booking.entities.Hotel;
import com.mgreau.book.wildfly.booking.search.IndexSearchHotel;

@SuppressWarnings("serial")
public class LoadHotelServlet extends HttpServlet {

	/** Logger */
	private static final Logger LOG = Logger.getLogger(LoadHotelServlet.class
			.getCanonicalName());

	/** Prefix to see step in sysout */
	private static final String PREFIX = "[BOOKING-GAE-CAPEDWARF]";
	
	private static final String BUCKET_NAME = "hotels_datas";

	/**
	 * This is where backoff parameters are configured. Here it is aggressively
	 * retrying with backoff, up to 10 times but taking no more that 15 seconds
	 * total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory
			.createGcsService(new RetryParams.Builder()
					.initialRetryDelayMillis(10).retryMaxAttempts(10)
					.totalRetryPeriodMillis(15000).build());

	/**
	 * Used below to determine the size of chucks to read in. Should be > 1kb
	 * and < 10MB
	 */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		loadHotelsFromGSC(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		loadHotelsFromGSC(req, resp);
	}

	private void addToGSC(String filename, String size) {
		GcsOutputChannel writeChannel = null;
		try {
			 writeChannel = gcsService.createOrReplace(
					new GcsFilename(BUCKET_NAME, filename),
					GcsFileOptions.getDefaultInstance());

			String fileToStore;

			switch (size) {
			case "XXS":
				fileToStore = "hotels_100.csv";
				break;
			case "XS":
				fileToStore = "hotels_500.csv";
				break;
			case "S":
				fileToStore = "hotels_1000.csv";
				break;
			case "M":
				fileToStore = "hotels_5000.csv";
				break;
			case "XL":
				fileToStore = "hotels_10000.csv";
				break;
			case "XXL":
				fileToStore = "hotels_30000.csv";
				break;
			case "XXXL":
				fileToStore = "hotels_all.csv";
				break;
			default:
				fileToStore = "hotels_10.csv";
				break;
			}
			if ("all".equals(size))
				fileToStore = "hotels_all.csv";
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(fileToStore);
			
			writeChannel.waitForOutstandingWrites();
			writeChannel.write(ByteBuffer.wrap(ByteStreams.toByteArray(in)));
			writeChannel.close();
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Exception when try to write a CSV file into Google Cloud Storage", e);
		} finally{
			
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 */
	private void loadHotelsFromGSC(HttpServletRequest req,
			HttpServletResponse resp) {

		final String fileName = req.getParameter("fileKey") + System.currentTimeMillis();
		final String size = req.getParameter("size");
		LOG.info(PREFIX + "Insert file into GSC.");
		addToGSC(fileName, size);

		LOG.info(PREFIX + "Try to load datastore from file : " + fileName);
		GcsFilename gcsFileName = new GcsFilename(BUCKET_NAME, fileName);
		InputStream in = getInputStreamWithGCSAPI(gcsFileName);

		if (in == null) {
			LOG.log(Level.SEVERE, PREFIX + "File : " + fileName
					+ " => Unable to read from GCS!");
		} else {
			insertToDatastore(in);
		}
	}

	/**
	 * Add datas (hotels, bookings), if datastore is empty.
	 */
	private void insertToDatastore(InputStream in) {
		HotelEndpoint he = new HotelEndpoint();
		DashboardEndpoint dash = new DashboardEndpoint();

		LOG.info(PREFIX + "...Check datas......");
		int datas = dash.getDashboard().getNbHotels();
		LOG.info(PREFIX + "...Hotels already added (" + datas
				+ " hotels)......");

		// removeIndex();
		// create hotels
		LOG.info(PREFIX + "...Adding hotels......");
		Scanner lineScan = new Scanner(in);
		int countAdded = 0;
		while (lineScan.hasNextLine()) {
			Scanner s = new Scanner(lineScan.nextLine());
			s.useDelimiter(",");
			while (s.hasNext()) {
				Hotel h1 = new Hotel();
				// "CountryCode","Country","RegionCode","Region","City","Location","Address","Venue","ZipCode"
				s.next();
				h1.setCountry(s.next());
				s.next();
				s.next();
				h1.setCity(s.next());
				s.next();
				h1.setAddress(s.next());
				h1.setHotelName(s.next());
				h1.setZip(s.next());

				int stars = new Random().nextInt(5);
				h1.setNumberOfStars(stars);
				if (stars > 0)
					h1.setPrice(new Long(new Random().nextInt((40 * stars))
							+ ""));
				else
					h1.setPrice(new Long(new Random().nextInt(35) + ""));
				h1.setImage(new Text("" + "hotel" + new Random().nextInt(29)
						+ ".jpg"));
				he.insertHotel(h1);
				countAdded++;
				IndexSearchHotel.addHotelToSearchIndex(h1);
			}
			s.close();
		}
		lineScan.close();

		datas = dash.getDashboard().getNbHotels();
		LOG.info(PREFIX + countAdded + "...hotels have been created......");
		LOG.info(PREFIX + "...Hotels new count (" + datas + " hotels)......");
		LOG.info(PREFIX + "...City list (" + dash.getDashboard().getCityList()
				+ " )......");
		LOG.info(PREFIX + "...Country list ("
				+ dash.getDashboard().getCountryList() + " )......");
	}

	
	private InputStream getInputStreamWithBlobstoreAPI(GcsFilename gcsFileName,
			HttpServletResponse resp) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/"
				+ gcsFileName.getBucketName() + "/"
				+ gcsFileName.getObjectName());
		try {
			blobstoreService.serve(blobKey, resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private InputStream getInputStreamWithGCSAPI(GcsFilename gcsFileName) {
		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(
				gcsFileName, 0, BUFFER_SIZE);
		return Channels.newInputStream(readChannel);
	}

	/**
	 * Transfer the data from the inputStream to the outputStream. Then close
	 * both streams.
	 */
	private void copyFromInputStreamToGCS(InputStream input, OutputStream output)
			throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			LOG.info(PREFIX+"bytesRead:" + bytesRead);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			output.close();
		}
	}
}
