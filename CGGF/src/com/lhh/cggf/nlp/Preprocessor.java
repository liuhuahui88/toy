package com.lhh.cggf.nlp;

public class Preprocessor {
	// Transform Escaped HTML Character
	public static String transform(String str) {
		str = str.replace("&amp;", "&");
		str = str.replace("&gt;", ">");
		str = str.replace("&lt;", "<");
		str = str.replace("&quot;", "\"");
		str = str.replace("&ldquo;", "“");
		str = str.replace("&rdquo;", "”");
		str = str.replace("&nbsp;", " ");
		str = str.replace("&middot;", "·");
		str = str.replace("　", " ");
		return str;
	}
	
	// Strip Non-Chinese Character
	public static String strip(String str) {
		String pattern = "[a-zA-Z0-9（）〔〕［］｛｝《》【】〖〗〈〉()\\[\\]{}<>:;,.?!'\"、…—\\-\\u4e00-\\u9fa5]";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
			if (str.substring(i, i + 1).matches(pattern))
				sb.append(str.substring(i, i + 1));
		str = sb.toString();

		return str;
	}
}
