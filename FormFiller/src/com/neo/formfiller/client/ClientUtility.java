package com.neo.formfiller.client;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public final class ClientUtility {
	
	public static final String HTTP = "http://";
	
	public static final ResponseHandler<String> LOCATION_HANDLER =
			new ResponseHandler<String>() {

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			Header headers[] = response.getHeaders(HttpHeaders.LOCATION);
			return headers[0].getValue();
		}
	};
	
	public static final ResponseHandler<String> CONTENT_HANDLER =
			new ResponseHandler<String>() {

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			String content = null;
			try {
				content = EntityUtils.toString(response.getEntity());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return content;
		}
	};
	
	public static final ResponseHandler<Image> IMAGE_HANDLER =
			new ResponseHandler<Image>() {

		@Override
		public Image handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			Image image = null;
			try {
				InputStream inputStream = response.getEntity().getContent();
				image = ImageIO.read(inputStream);
				inputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return image;
		}
	};
	
	public static String buildURL(String host, String path) {
		return HTTP + host + path;
	}
}
