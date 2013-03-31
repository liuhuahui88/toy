package com.lhh.cggf.se.impl;

import java.net.URLEncoder;

import com.lhh.cggf.nlp.Preprocessor;
import com.lhh.cggf.se.Item;
import com.lhh.cggf.se.Result;
import com.lhh.cggf.se.SearchEngine;
import com.lhh.cggf.web.Crawler;

public class Sogou implements SearchEngine {
	private Crawler crawler = null;

	public Sogou() {
		crawler = new Crawler("gb2312");
	}

	private String encodeURL(String keyword, int num) {
		String url = null;
		try {
			String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
			url = "http://www.sogou.com/web?num=" + num + "&query="
					+ encodedKeyword;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	private Result extractInfo(String content, int num) {
		// Collapse Content To One Line String
		content = content.replaceAll("\n", "");

		int start, end;

		// Extract Total Count
		String countStart = "<!--resultbarnum:";
		String countEnd = "-->";
		start = content.indexOf(countStart) + countStart.length();
		end = content.indexOf(countEnd, start);
		String temp = content.substring(start, end);
		temp = temp.replace(",", "");
		long count = Long.parseLong(temp);

		// Detect Splitting Line
		end = content.indexOf("<!--a-->") + "<!--a-->".length();

		Item[] items = new Item[num];
		for (int i = 0; i < num; i++) {

			// Two Types Of Item Exist In Sogou
			boolean mark;
			int index1 = content.indexOf("<div class=\"rb\">", end);
			int index2 = content.indexOf("<div class=\"vrwrap\">", end);

			// Detect Item Type
			if (index1 < 0 && index2 < 0) {
				for (; i < num; i++)
					items[i] = new Item("**Unknown**", "**Unknown**",
							"**Unknown**");
				break;
			} else if (index1 < 0 || index2 < 0) {
				end = index2 < 0 ? index1 : index2;
				mark = index2 < 0;
			} else {
				end = index1 < index2 ? index1 : index2;
				mark = index1 < index2;
			}

			// Extract URL
			String url;
			start = content.indexOf("href=\"", end) + "href=\"".length();
			end = content.indexOf("\"", start);
			url = content.substring(start, end);

			// Extract Title
			String title;
			start = content.indexOf(">", end) + ">".length();
			end = content.indexOf("</a>", start);
			title = content.substring(start, end);
			title = title.replaceAll("<.*?>", "");
			title = Preprocessor.transform(title);

			// Extract Snippet
			String snippet;
			if (mark) {
				start = end + "</a>".length();
				end = content.indexOf("</div>", start);
				snippet = content.substring(start, end);
				snippet = snippet.replaceAll("<.*?>", "");
			} else
				snippet = "**Unknown**";
			snippet = Preprocessor.transform(snippet);

			// Add Item
			items[i] = new Item(title, snippet, url);
		}

		return new Result(count, items);
	}

	public Result search(String keyword, int num) {
		String url = encodeURL(keyword, num);

		String content = crawler.fetch(url);
		if (content == null)
			return null;

		Result result = extractInfo(content, num);

		return result;
	}

	public static void main(String[] args) {
		SearchEngine se = new Sogou();
		Result result = se.search("山东省长", 20);
		System.out.println(result);
	}
}
