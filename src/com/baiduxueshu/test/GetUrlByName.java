package com.baiduxueshu.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetUrlByName {
	static final String USER_AGENT = " Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)";
	static String init = "http://xueshu.baidu.com";
	public static String filename = "rs/subsection.txt";
	public static String urlpath = "E:\\baiduxueshu\\res_1210.txt";
//	public static String urlpath = "subsection.txt";
	public static void main(String[] args) throws Exception {
		
		List<String> list = readerFile(filename);
//		urlpath = args[1];
    	for (String string : list) {
			dealString(string);
		}
	}
	
	/**
	 * @param name
	 * @throws Exception
	 */
	public static void dealString(String name) throws Exception {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(urlpath, true)));
		String url = init + "/s?wd=" + URLEncoder.encode(name, "utf-8")
				+ "&pn=0"
				+ "&tn=SE_baiduxueshu_c1gjeupa&ie=utf-8&sc_hit=1&rsv_page=1";
		Document doc;
		while (true) {
			doc = Jsoup.connect(url).userAgent(USER_AGENT)
					.timeout(6000).get();
			Elements elements = doc.getElementsByAttributeValue("class", "result sc_default_result xpath-log");
			StringBuffer sb = new StringBuffer();
			for (Element element : elements) {
				sb.append(name + "\t" +element.attr("mu") + "\r\n");
			}
//			System.out.println(sb.toString());
			bw.write(sb.toString());
			bw.flush();
			Elements page = doc.getElementsByAttributeValue("id", "page");
			if (page.text().length() == 0) {
				break;
			} else {
				Element lastLable = doc.getElementsByAttributeValue("class", "n").last();
				if (lastLable.text().contains("下一页")) {
					String nextPageUrl = lastLable.attr("href");
					if (nextPageUrl.length() > 0) {
						url = init + nextPageUrl;
						System.out.println(url);
					}
				} else if (lastLable.text().contains("上一页")) {
					break;
				}
			}
			
			Thread.sleep(1000);
		}

	}


	public static List<String> readerFile(String filename) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line.trim());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}
