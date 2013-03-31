package com.lhh.cggf.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Crawler {
	private Charset charset;
	private DefaultHttpClient httpClient;

	public Crawler(String encoding) {
		this.charset = Charset.forName(encoding);

		httpClient = new DefaultHttpClient();

		HttpParams hp = httpClient.getParams();
		HttpConnectionParams.setSoTimeout(hp, 10000);
		HttpConnectionParams.setConnectionTimeout(hp, 10000);
		HttpClientParams
				.setCookiePolicy(hp, CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.setParams(hp);
	}

	public String fetch(String url) {
		String content = null;

		HttpGet httpget = null;
		HttpResponse response = null;
		InputStream is = null;

		httpget = new HttpGet(url);
		try {
			response = httpClient.execute(httpget);
			is = response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				charset));
		StringBuffer sb = new StringBuffer();
		String temp = null;

		try {
			while ((temp = br.readLine()) != null)
				sb.append(temp + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		content = sb.toString();

		return content;
	}

	public static void main(String[] args) throws Exception {
		String str = new Crawler("gb2312").fetch("http://www.baidu.com");

		System.out.println(str);
	}

}
