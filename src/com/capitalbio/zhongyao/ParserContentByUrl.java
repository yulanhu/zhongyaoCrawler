package com.capitalbio.zhongyao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ParserContentByUrl {

	public static void main(String[] args) {
		// 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getCookieManager().setCookiesEnabled(true);// 开启cookie管理
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(false);
		webClient.getOptions().setTimeout(60000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		Map<String, String> map = readerFile("E:\\1229\\new.txt");
		System.out.println(map.size());
		for (Entry<String, String> entry : map.entrySet()) {
			tets(webClient, entry.getValue(), entry.getKey());
		}
	}

	public static void tets(WebClient webClient, String urlnew, String name) {
		HtmlPage page;
		try {
			page = (HtmlPage) webClient.getPage(urlnew);
			Document doc = Jsoup.parse(page.asXml());
			Elements select = doc.getElementsByAttributeValue("class",
					"SearchResultDetailPanelShuJuKu").select("tr");
			StringBuffer sb = new StringBuffer();
			for (Element element : select) {
				sb.append(element.text() + "\r\n");
			}
			writeFile(sb.toString(), name);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeFile(String content, String rs) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("E:\\bohe\\" + rs + ".txt", true)));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> readerFile(String filename) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split("\t", 2);
				map.put(split[0], split[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

}
