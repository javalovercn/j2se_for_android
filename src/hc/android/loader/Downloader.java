package hc.android.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
	int start, end = Integer.MAX_VALUE;
	File file = null;
	URL url = null;
	long startMS;
	int downloadBS;
	boolean isError = false;

	public Downloader(File storeFile, URL url) {
		this.file = storeFile;
		this.url = url;
	}

	public void run() throws IOException {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");

			startMS = System.currentTimeMillis();
			downloadBS = 0;

			HttpURLConnection conn = null;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
			if (conn.getResponseCode() == 206) {
				InputStream inStream = conn.getInputStream();
				byte[] b = new byte[1024 * 10];
				int len = 0;
				boolean hasIOException = false;

				do {
					raf.seek(start);
					hasIOException = false;
					try {
						while (((start + downloadBS) <= end) && (len = inStream.read(b)) != -1) {
							raf.write(b, 0, len);
							downloadBS += len;
						}
					} catch (IOException e) {
						hasIOException = true;
						start += downloadBS;
						downloadBS = 0;

						try {
							conn.disconnect();
						} catch (Exception ex) {
						}
						try {
							Thread.sleep(5000);
						} catch (Exception ex) {
						}

						conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						// conn.setReadTimeout(0);//无穷
						conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
						inStream = conn.getInputStream();
					}
				} while (hasIOException);
			} else {
				throw new IOException("No 206");
			}
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
