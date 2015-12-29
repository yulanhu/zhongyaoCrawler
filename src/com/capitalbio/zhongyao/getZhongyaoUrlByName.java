package com.capitalbio.zhongyao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * @data:2015-12-29
 * @yulanhu
 * */
public class getZhongyaoUrlByName {
	static String init = "http://www.tcm100.com/ShuJuKu/ZhongYao/";
	static String url = "http://www.tcm100.com/ShuJuKu/ZhongYao/ZhongYao.aspx";

	public static void main(String[] args) throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
		webClient.getOptions().setCssEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);// 开启cookie管理
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(false);
		webClient.getOptions().setTimeout(60000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		List<String> list = readerFile("rs/onlyno.txt");//onlyno.txt 为数据信息

		for (String name : list) {
			// 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可
			HtmlPage page = webClient.getPage(url);
			HtmlInput input = (HtmlInput) page.getHtmlElementById("Txt_1");
			input.setValueAttribute(name);
			HtmlInput btn = (HtmlInput) page.getHtmlElementById("Button1");
			HtmlPage page2 = btn.click(); // 输出新页面的文本
			parser(webClient, page2, name);

			Document doc = Jsoup.parse(page2.asXml());
			Elements nume = doc.getElementsByAttributeValue("class",
					"PageNumber");
			int num = Integer.parseInt(nume.last().text());

			int flag = 0;
			do {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				ScriptResult sr = page2
						.executeJavaScript("javascript:__doPostBack('DataGrid_SearchResult$ctl14$ctl02','')");
				HtmlPage newPage = (HtmlPage) sr.getNewPage();
				// System.out.println(newPage.asText());
				parser(webClient, newPage, name);
				page2 = newPage;
				flag++;

			} while (flag < num);
		}

	}

	public static List<String> readerFile(String filename) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void parser(WebClient webClient, HtmlPage page, String name)
			throws Exception {
		Document doc = Jsoup.parse(page.asXml());
		Elements value = doc.getElementsByAttributeValue("id",
				"SearchResultPanelShuJuKu");
		Elements tr = value.select("tr");
		StringBuffer sb = null;
		if (tr.size() > 3) {
			for (int i = 2; i < tr.size() - 1; i++) {
				sb = new StringBuffer();
				Elements hrefText = tr.get(i).select("a[href]");
				String rs = hrefText.text().replace(" ", "");
				String urlnew = init + hrefText.attr("href");
				sb.append(rs + "\t" + urlnew + "\r\n");
				writeFile1(sb.toString(), rs);
			}
		}
	}

	public static void writeFile1(String content, String rs) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("E:\\1229\\" + "aa_onlyno.txt", true)));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
