package com.meetmeup.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;
//This is a helper class which is used in making connection with server.
public class HttpRequest {
	
	  static InputStream is = null;
	  static String response = "";
	 
	
	  public static String post(String url) throws UnknownHostException {
	        try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	       //     httpPost.setHeader("Content-type", "text/plain");
			//	httpPost.setEntity( new StringEntity(strParam));
	    //        httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	            int statusCode = httpResponse.getStatusLine().getStatusCode();
	            Log.e("Connecting to <><><<><><>", "= " + url);
	            Log.e("url return Status Code <><><<><><>", "cod = " + statusCode);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            response = sb.toString();
	     //       WebServices.isStatus = true;
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	     //       WebServices.isStatus = true;
	        }
	        return response;
	    }
	  
	  public static String post(String url,MultipartEntity multipart) {
	        try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	            
				httpPost.setEntity(multipart);
				
				
				/*HttpParams httpParameters = httpPost.getParams();
		        // Set the timeout in milliseconds until a connection is
		        // established.
		        int timeoutConnection = 100000;
		        HttpConnectionParams.setConnectionTimeout(httpParameters,
		                timeoutConnection);
		        // Set the default socket timeout (SO_TIMEOUT)
		        // in milliseconds which is the timeout for waiting for data.
		        int timeoutSocket = 100000;
		        HttpConnectionParams
		                .setSoTimeout(httpParameters, timeoutSocket);*/
				
				
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	            int statusCode = httpResponse.getStatusLine().getStatusCode();
	            Log.e("Status Code <><><<><><>", "cod = " + statusCode);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            response = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	            response = null;
	        }
	        return response;
	    }
	  
	  public static String postMultipartEntity(String url,MultipartEntity multipartEntity){
			InputStream is=null;
			try {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = 15000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(url);
				
				httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				httppost.setEntity(multipartEntity);
				HttpResponse response1 = httpClient.execute(httppost);
			//	responseCode = response1.getStatusLine().getStatusCode();
		//		message = response1.getStatusLine().getReasonPhrase();
				HttpEntity httpEntity1 = response1.getEntity();
				is = httpEntity1.getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
		//		HostUrl.checkNull=false;
			//	 response = null;
			} catch (IOException e) {
				e.printStackTrace();
		//		HostUrl.checkNull=false;
			//	 response = null;
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim() + "\n");
				}
				is.close();
				response = sb.toString();
		//		HostUrl.checkNull=true;
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
		//		HostUrl.checkNull=false;
				 response = null;
			} 
			return response;
		}
}