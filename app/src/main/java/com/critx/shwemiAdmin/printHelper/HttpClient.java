package com.critx.shwemiAdmin.printHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HttpClient {

	static final String prfx_boundary = "-----------------------------";
	static final String POST_METHOD = "POST";
	static final String GET_METHOD = "GET";
	static final String CONNECTION = "Connection";
	static final String CHARSET = "Charset";
	static final String CONTENT_TYPE = "Content-Type";
	static final String KEEP_ALIVE = "Keep-Alive";
	static final String UTF8 = "UTF-8";
	static final String MULTIPART_BOUNDARY = "multipart/form-data; boundary=";

	static final String twohyphens = "--";
	static final String end = "\r\n";
	static final String dq = "\"";

	public static boolean checkNetworkIsEnabled(Context context) {
		System.setProperty("http.keepAlive", "false");
		ConnectivityManager cMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
		if (null == netInfo) {
			return false;
		}
		return netInfo.isAvailable();
	}

	public static boolean checkCanResolveDNS(String hostname) {
		try {
			InetAddress.getByName(hostname);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean checkCanReachByHTTP(String tryToConnectUrl) throws IOException {
		boolean retValue = false;
		URL syncUrl;
		syncUrl = new URL(tryToConnectUrl);
		HttpURLConnection urlconn = null;
		try{
			urlconn = (HttpURLConnection) syncUrl.openConnection();
			urlconn.connect();
			if (200 == urlconn.getResponseCode()) {
				retValue = true;
			}
		}finally{
			if(urlconn != null){
				urlconn.disconnect();
			}
		}
		return retValue;
	}

	public static Map<String, String> get(String url, Map<String, String> paramMap) throws HttpClientException {
		HttpURLConnection conn = null;
		StandardHandler handler = new StandardHandler();
		URL syncUrl;
		InputStream is = null;
		try {
			// form parameter
			String param = "";
			if (paramMap != null) {
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					if(param.length() > 0){
						param = param + "&";
					}else{
						param = "?";
					}
					param = param + URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue());
				}
			}

			// connection
			syncUrl = new URL(url + param);
			conn = (HttpURLConnection) syncUrl.openConnection();

			// header
			conn.setRequestMethod(GET_METHOD);

			conn.connect();


			// result code
			final int responceCode = conn.getResponseCode();

			if (responceCode != HttpURLConnection.HTTP_OK) {
				throw new HttpClientException("invalid responce code, " + responceCode);
			}

			// result parsing
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			SAXParser parser = spfactory.newSAXParser();
			is = conn.getInputStream();
			parser.parse(is, handler);
		} catch (MalformedURLException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		} catch (ParserConfigurationException e) {
			throw new HttpClientException(e);
		} catch (SAXException e) {
			throw new HttpClientException(e);
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		return handler.getItem();
	}

	public static byte[] getBinary(String url, Map<String, String> paramMap) throws HttpClientException {
		byte[] retBytes = null;
		HttpURLConnection conn = null;
		URL syncUrl;
		try {
			// form parameter
			String param = "";
			if (paramMap != null) {
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					if(param.length() > 0){
						param = param + "&";
					}else{
						param = "?";
					}
					param = param + URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue());
				}
			}

			// connection
			syncUrl = new URL(url + param);
			conn = (HttpURLConnection) syncUrl.openConnection();

			// header
			conn.setRequestMethod(GET_METHOD);

			conn.connect();


			// result code
			final int responceCode = conn.getResponseCode();

			if (responceCode != HttpConstants.HTTP_OK) {
				throw new HttpClientException("invalid responce code, " + responceCode);
			}

			// result parsing
			byte[] buffer = new byte[1024]; 
			int bytesRead; 
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			while ((bytesRead = conn.getInputStream().read(buffer)) != -1) 
			{ 
				output.write(buffer, 0, bytesRead); 
			}
			output.flush();
			output.close();
			if(output.size() > 0){
				retBytes = output.toByteArray();
			}
		} catch (MalformedURLException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		} finally {
			if(conn != null){
				conn.disconnect();
			}
		}
		return retBytes;
	}


	public static Map<String, String> post(String url, Map<String, String> paramMap) throws HttpClientException {
		HttpURLConnection conn = null;
		StandardHandler handler = new StandardHandler();
		InputStream is = null;
		String mBoundary = prfx_boundary + Long.toString(System.currentTimeMillis());
		URL syncUrl;
		try {
			// connection
			syncUrl = new URL(url);
			conn = (HttpURLConnection) syncUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// header
			conn.setRequestMethod(POST_METHOD);
			conn.setRequestProperty(CONNECTION, KEEP_ALIVE);
			conn.setRequestProperty(CHARSET, UTF8);
			conn.setRequestProperty(CONTENT_TYPE, MULTIPART_BOUNDARY + mBoundary);
			DataOutputStream os = new DataOutputStream(conn.getOutputStream());

			// form parameter
			if (paramMap != null) {
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					append_string(os, mBoundary, URLEncoder.encode(entry.getKey()), URLEncoder.encode(entry.getValue()));
				}
			}

			// send data
			append_final(os, mBoundary);
			os.flush();
			os.close();

			// result code
			final int responceCode = conn.getResponseCode();

			if (responceCode != HttpURLConnection.HTTP_OK) {
				throw new HttpClientException("invalid responce code, " + responceCode);
			}

			// result parsing
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			SAXParser parser = spfactory.newSAXParser();
			is = conn.getInputStream();
			parser.parse(is, handler);
		} catch (MalformedURLException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		} catch (ParserConfigurationException e) {
			throw new HttpClientException(e);
		} catch (SAXException e) {
			throw new HttpClientException(e);
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		return handler.getItem();
	}

	public static Map<String, String> post(String url, Map<String, String> paramMap, String paramName, String fileName, byte[] attachData) throws HttpClientException{
		HttpURLConnection conn = null;
		String mBoundary = prfx_boundary + Long.toString(System.currentTimeMillis());
		URL syncUrl;
		StandardHandler handler = new StandardHandler();
		InputStream is = null;
		try {
			// connection
			syncUrl = new URL(url);
			conn = (HttpURLConnection) syncUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			//conn.setConnectTimeout(30*1000);

			// header
			conn.setRequestMethod(POST_METHOD);
			conn.setRequestProperty(CONNECTION, KEEP_ALIVE);
			conn.setRequestProperty(CHARSET, UTF8);
			conn.setRequestProperty(CONTENT_TYPE, MULTIPART_BOUNDARY + mBoundary);
			DataOutputStream os = new DataOutputStream(conn.getOutputStream());

			// form parameter
			if (paramMap != null) {
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					append_string(os, mBoundary, entry.getKey(), entry.getValue());
				}
			}
			if (attachData != null) {
				append_file(os, mBoundary, paramName, fileName, attachData);
			}

			// send data
			append_final(os, mBoundary);
			os.flush();
			os.close();

			// result code
			final int responceCode = conn.getResponseCode();

			if (responceCode != HttpURLConnection.HTTP_OK) {
				throw new HttpClientException("invalid responce code, " + responceCode);
			}

			// result parsing
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			SAXParser parser = spfactory.newSAXParser();
			is = conn.getInputStream();
			parser.parse(is, handler);

		} catch (MalformedURLException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		} catch (ParserConfigurationException e) {
			throw new HttpClientException(e);
		} catch (SAXException e) {
			throw new HttpClientException(e);
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		return handler.getItem();
	}

	private static void append_string(DataOutputStream os, String mBoundary, String name, String value) throws IOException {
		
		String boundaryLine = twohyphens + mBoundary + end;
		os.write(boundaryLine.getBytes("UTF-8"));
		
		String contentDispositionLine = "Content-Disposition: form-data; name=" + dq + name + dq + end + end;
		os.write(contentDispositionLine.getBytes("UTF-8"));
		
		if (value != null) {
			if(value.length() > 0){
				os.write(value.getBytes("UTF-8"));
			}
		}
		
		os.write(end.getBytes("UTF-8"));
	}

	private static void append_file(DataOutputStream os, String mBoundary, String name, String filename, byte[] value) throws IOException {
		os.writeBytes(twohyphens + mBoundary + end);
		os.writeBytes("Content-Disposition: form-data; name=" + dq + name + dq + "; filename=" + dq + filename + dq + end);
		os.writeBytes("Content-Type: application/octet-stream" + end + end);
		os.write(value, 0, value.length);
		os.flush();
		os.writeBytes(end);
	}

	private static void append_final(DataOutputStream os, String mBoundary) throws IOException {
		os.writeBytes(twohyphens + mBoundary + twohyphens + end);
	}

	@SuppressWarnings("serial")
	public static class HttpClientException extends Throwable {
		/**
		 * 
		 */
		public HttpClientException() {
			super();
		}

		/**
		 * @param detailMessage
		 * @param throwable
		 */
		public HttpClientException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		/**
		 * @param detailMessage
		 */
		public HttpClientException(String detailMessage) {
			super(detailMessage);
		}

		/**
		 * @param throwable
		 */
		public HttpClientException(Throwable throwable) {
			super(throwable);
		}
	}

}
