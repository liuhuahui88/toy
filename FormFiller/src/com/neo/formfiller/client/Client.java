package com.neo.formfiller.client;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Client {
	
	private static final String DEFAULT_CHARSET = "GB2312"; 
	
	private String host;
	private String charset;
	
	public Client(String host) {
		this(host, DEFAULT_CHARSET);
	}
	
	public Client(String host, String charset) {
		this.host = host;
		this.charset = charset;
	}
	
	public String submit(String path, List<NameValuePair> parameters) {
		String location = null;
		try
        {
			URL url = new URL(ClientUtility.buildURL(host, path));
			HttpURLConnection connection =
					(HttpURLConnection) url.openConnection();            
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String content = EntityUtils.toString(
            		new UrlEncodedFormEntity(parameters, charset));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            		connection.getOutputStream()));
            writer.write(content);
            writer.flush();
            writer.close();
            location = connection.getHeaderField(HttpHeaders.LOCATION);
            connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return location;
	}
	
	public String getAndFetchLocation(String path) {
		return get(path, ClientUtility.LOCATION_HANDLER);
	}
	
	public String getAndFetchContent(String path) {
		return get(path, ClientUtility.CONTENT_HANDLER);
	}
	
	public Image getAndFetchImage(String path) {
		return get(path, ClientUtility.IMAGE_HANDLER);
	}
	
	public String postAndFetchLocation(String path, String content) {
		return post(path, content, ClientUtility.LOCATION_HANDLER);
	}
	
	public String postAndFetchContent(String path, String content) {
		return post(path, content, ClientUtility.CONTENT_HANDLER);
	}
	
	public Image postAndFetchImage(String path, String content) {
		return post(path, content, ClientUtility.IMAGE_HANDLER);
	}
	
	public <T> T get(String path, ResponseHandler<T> handler) {
		HttpGet request = new HttpGet(ClientUtility.buildURL(host, path));
		return execute(request, handler);
	}
	
	public <T> T post(String path, String content, ResponseHandler<T> handler) {
		HttpPost request = new HttpPost(ClientUtility.buildURL(host, path));
		try {
			request.setEntity(new StringEntity(content));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return execute(request, handler);
	}
	
	private <T> T execute(HttpUriRequest request, ResponseHandler<T> handler) {
		T result = null;
		CloseableHttpClient client = openClient();
		try {
			result = client.execute(request, handler);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeClient(client);
		}
		return result;
	}
	
	private CloseableHttpClient openClient() {
		return HttpClients.custom().disableRedirectHandling().build();
	}
	
	private void closeClient(CloseableHttpClient client) {
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Client c = new Client("202.197.120.56");
		System.out.println(c.getAndFetchLocation(""));
	}
}
