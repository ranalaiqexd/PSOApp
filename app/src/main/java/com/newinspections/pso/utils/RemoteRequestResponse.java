package com.newinspections.pso.utils;

/**This class for used to request on remote server and getting response from server as well and also handle different error in encoding and requesting **/

/**
 *
 * @author Pintu Kumar Patil 9977368049
 * @author 11-May-2016
 *
 */
import android.os.Environment;
import android.util.Log;


import org.apache.http.NameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RemoteRequestResponse {
	private final int TIMEOUT_CONNECTION = 13000;
	private String response = "";
	int serverResponseCode;
	final int POST_TYPE = 1, GET_TYPE = 2, PUT_TYPE = 3;
	static public boolean isUnknownHostException = false;

	// http://hmkcode.com/android-send-json-data-to-server/
	public String encodingUTF(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
	public String doPostAndExecute(String serviceUrl,
			ArrayList<NameValuePair> parameters,
			ArrayList<NameValuePair> imageList,
			ArrayList<NameValuePair> headers, int requestMethod) {
		switch (requestMethod) {
		case POST_TYPE:
			return httpPost(serviceUrl, parameters, imageList, headers);
		case GET_TYPE:
			return httpGet(serviceUrl, parameters, headers);
		case PUT_TYPE:
			return "";
		}
		return "Please provide HTTP method type eg POST, GET";
	}



//	edited by exd
//	private static final int MAX_RETRIES = 5;
	int triesCount = 0;
	public String httpPost(String serviceUrl, ArrayList<NameValuePair> nameValuePairs, ArrayList<NameValuePair> imageList, ArrayList<NameValuePair> headers)
	{
		Log.i(getClass().getName(), "serviceUrl : " + serviceUrl);
		try {
			String crlf = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			// Setup the request: complete path of file eg:
			// /mnt/ext_card/bluetooth/hidden.jpg
			HttpURLConnection httpUrlConnection = null;
			URL url;

			// http://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection
			url = new URL(serviceUrl);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
//			final long filesize = httpUrlConnection.getContentLength();
//			Log.d("filesize", String.valueOf(filesize));

			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setConnectTimeout(20000);
			if (headers != null) {
				for (int i = 0; i < headers.size(); i++) {
					httpUrlConnection.setRequestProperty(headers.get(i)
							.getName(), headers.get(i).getValue());
				}
			}


			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("ENCTYPE",
					"multipart/form-data");
			httpUrlConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			if (imageList != null) {
				for (int i = 0; i < imageList.size(); i++) {
					httpUrlConnection.setRequestProperty(imageList.get(i)
							.getName(), imageList.get(i).getValue());
				}
			}
			// Start content wrapper:

			DataOutputStream request = new DataOutputStream(
					httpUrlConnection.getOutputStream());
			String paramStr = "";
			for (int i = 0; i < nameValuePairs.size(); i++) {
				paramStr = paramStr + "&" + nameValuePairs.get(i).getName()
						+ "=" + nameValuePairs.get(i).getValue();
				request.writeBytes(twoHyphens + boundary + crlf);
				request.writeBytes("Content-Disposition: form-data; name=\""
						+ nameValuePairs.get(i).getName() + "\"" + crlf);
				request.writeBytes(crlf);
				// assign value
				request.writeBytes(nameValuePairs.get(i).getValue());
				request.writeBytes(crlf);
				request.writeBytes(twoHyphens + boundary + crlf);
			}
			Log.i(getClass().getName(), "paramStr : " + paramStr);
			if (imageList != null) {
				for (int i = 0; i < imageList.size(); i++) {
					File sourceFile = new File(imageList.get(i).getValue());
					Log.i(getClass().getName(), imageList.get(i).getName()
							+ " =" + sourceFile.getPath() + " file size : "
							+ sourceFile.length());
					// http://stackoverflow.com/questions/25093134/android-java-net-urlconnection-send-image-and-data-at-once
					FileInputStream fileInputStream = new FileInputStream(
							sourceFile);

					// send image
					request.writeBytes(twoHyphens + boundary + crlf);
					request.writeBytes("Content-Disposition: form-data; name='"
							+ imageList.get(i).getName() + "';filename='"
							+ imageList.get(i).getValue() + "'" + crlf);

					request.writeBytes(crlf);

					// create a buffer of maximum size
					bytesAvailable = fileInputStream.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					// read file and write it into form...
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

					while (bytesRead > 0) {
						request.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					// send multipart form data necesssary after file data...
					request.writeBytes(crlf);
					request.writeBytes(twoHyphens + boundary + twoHyphens
							+ crlf);
					fileInputStream.close();
				}
			}

			// Responses from the server (code and message)
			serverResponseCode = httpUrlConnection.getResponseCode();
			String serverResponseMessage = httpUrlConnection.getResponseMessage();

			Log.i("HTTP Response is :", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			if (serverResponseCode == 404 ||serverResponseCode == 500 ) {
				return Constants.SERVER_ERROR+serverResponseMessage
						+ ": " + serverResponseCode;
			}
			InputStream responseStream = new BufferedInputStream(
					httpUrlConnection.getInputStream());

			BufferedReader responseStreamReader = new BufferedReader(
					new InputStreamReader(responseStream));
			String line = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			responseStreamReader.close();
			response = stringBuilder.toString();
			// Close response stream:
			responseStream.close();
			// close the streams //
			request.flush();
			request.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			return null;
		}
//		catch (EOFException e) {
//			if (triesCount < MAX_RETRIES)
//			{
//				httpPost(serviceUrl, nameValuePairs, imageList, headers);
//				triesCount++;
//			}
//			else
//				return null;
//		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Log.i(getClass().getName(), "response : " + response);
//		edited by exd; temporary
		writeToFile(response);
		Log.d("exd", "file written");

		return response;
	}

//	edited by exd; temporary
	public void writeToFile(String data)
	{
		// Get the directory for the user's public pictures directory.
		final File path =
				Environment.getExternalStoragePublicDirectory
						(
								Environment.DIRECTORY_DCIM
								//Environment.DIRECTORY_DCIM + "/YourFolder/"
						);

		// Make sure the path directory exists.
		if(!path.exists())
		{
			// Make it, if it doesn't exit
			path.mkdirs();
		}

		final File file = new File(path, "response.txt");

		// Save your stream, don't forget to flush() it before closing it.

		try
		{
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);

			myOutWriter.close();

			fOut.flush();
			fOut.close();
		}
		catch (IOException e)
		{
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	public String httpGet(String serviceUrl,
			ArrayList<NameValuePair> parameters,
			ArrayList<NameValuePair> headers) {

		String paramStr = "";
		try {
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					paramStr = paramStr
							+ "&"
							+ parameters.get(i).getName()
							+ "="
							+ URLEncoder.encode(parameters.get(i).getValue(),
									"UTF-8");
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(getClass().getName(), "httpGet serviceUrl : " + serviceUrl);
		Log.i(getClass().getName(), "httpGet paramStr : " + paramStr);

		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder;
		try {
			// create the HttpURLConnection
			url = new URL(serviceUrl + "?" + paramStr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestProperty("Content-Type", "application/json");

			if (headers != null) {
				for (int i = 0; i < headers.size(); i++) {
					connection.setRequestProperty(headers.get(i).getName(),
							headers.get(i).getValue());
					Log.i(getClass().getName(), "Header : Key "
							+ headers.get(i).getName() + " Value: "
							+ headers.get(i).getValue());
				}
			}
			// just want to do an HTTP GET here
			connection.setRequestMethod("GET");
			// uncomment this if you want to write output to this url
			// connection.setDoOutput(true);
			connection.setReadTimeout(TIMEOUT_CONNECTION);
			connection.connect();
			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			response = stringBuilder.toString();
			isUnknownHostException = false;
		} catch (UnknownHostException e) {
			isUnknownHostException = true;
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the reader; this can throw an exception too, so
			// wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		Log.i(getClass().getName(), "response : " + response);
		return response;
	}

	public String makeGetRequest(String serviceUrl) {

		Log.i(getClass().getName(), "httpGet serviceUrl : " + serviceUrl);
		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder;
		try {
			// create the HttpURLConnection
			url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestProperty("Content-Type", "application/json");

			// just want to do an HTTP GET here
			connection.setRequestMethod("GET");
			// uncomment this if you want to write output to this url
			// connection.setDoOutput(true);
			connection.setReadTimeout(TIMEOUT_CONNECTION);
			connection.connect();
			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			response = stringBuilder.toString();
			isUnknownHostException = false;
		} catch (UnknownHostException e) {
			isUnknownHostException = true;
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the reader; this can throw an exception too, so
			// wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		Log.i(getClass().getName(), "response : " + response);
		return response;
	}

	public String makeGetRequestXml(String serviceUrl) {

		Log.i(getClass().getName(), "httpGet serviceUrl : " + serviceUrl);
		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder;
		try {
			// create the HttpURLConnection
			url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestProperty("Content-Type", "text/html");

			// just want to do an HTTP GET here
			connection.setRequestMethod("GET");
			// uncomment this if you want to write output to this url
			// connection.setDoOutput(true);
			connection.setReadTimeout(TIMEOUT_CONNECTION);
			connection.connect();
			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			response = stringBuilder.toString();
			isUnknownHostException = false;
		} catch (UnknownHostException e) {
			isUnknownHostException = true;
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the reader; this can throw an exception too, so
			// wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		Log.i(getClass().getName(), "response : " + response);
		return response;
	}
}
