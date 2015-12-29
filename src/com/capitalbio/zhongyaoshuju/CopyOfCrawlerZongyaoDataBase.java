package com.capitalbio.zhongyaoshuju;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * @author huyulan
 * @date 2015-12-28
 */
public class CopyOfCrawlerZongyaoDataBase {
	static Map<String, String> map;
	static String init = "http://www.tcm100.com/ShuJuKu/ZhongYao/";
	static final String USER_AGENT = " Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)";
	static String url = "http://www.tcm100.com/ShuJuKu/ZhongYao/ZhongYao.aspx";
	static Set<String> set = new HashSet<String>();
	public static void getContentByURL(String name,WebClient webClient){
		map = new LinkedHashMap<String, String>();
        HtmlPage page = null;
        String html = "";
		try {
			
			page = (HtmlPage)webClient.getPage(url);
			// 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可 
			HtmlInput input = (HtmlInput)page.getHtmlElementById("Txt_1");
	        input.setValueAttribute(name);
	       //获取搜索按钮并点击
	        HtmlInput btn = (HtmlInput)page.getHtmlElementById("Button1");
	        HtmlPage page2 = btn.click(); 
	        DomElement dom = page2.getElementById("SearchResultPanelShuJuKu");
	       
	        html = dom.asXml();
	        parser(html,name);
	        
		} catch (Exception e) {
			System.out.println("err: " + url);
			e.printStackTrace();
		}
		
	}
	
	public static void parser(String html,String name) throws Exception{
		Document doc = null;
		doc = Jsoup.parse(html);
		
		Elements nume = doc.getElementsByAttributeValue("class", "PageNumber");
		String num = nume.last().text();
		if(!num.equals("1")){
			Elements tr = doc.select("tr");
			StringBuffer sb = null;
			if(tr.size() > 3){
				for (int i = 2; i < tr.size() - 1; i++) {
					sb= new StringBuffer();
					Elements hrefText = tr.get(i).select("a[href]");
					String rs = hrefText.text().replace(" ", "");
					
					String urlnew = init + hrefText.attr("href");
					doc = Jsoup.connect(urlnew).userAgent(USER_AGENT).timeout(6000)
							.get();
					Elements select = doc.getElementsByAttributeValue("class",
							"SearchResultDetailPanelShuJuKu").select("tr");
					for (Element element : select) {
						sb.append(element.text()+ "\r\n");
					}
					if(!set.contains(rs)){
						set.add(rs);
						writeFile(sb.toString(),rs);
					}	
				}
			}
		}
	}
	public static Set<String> rare_read(String path){
		Set<String> set = new HashSet<String>();
		File file = new File(path);
		File[] files = file.listFiles();
		for(File f : files) {
		String name = f.getName();
		set.add(name.substring(0, name.length() - 4));
		}
		return set;
	}
	
	public static void writeFile(String content,String name){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("E:\\test\\" + name + ".txt", true)));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
	public static void main(String[] args) {
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
	    webClient.getOptions().setCssEnabled(true);  
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(false);
        webClient.getOptions().setTimeout(60000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        List<String> list = readerFile("rs/a.txt");
        set.addAll(rare_read("E:\\path\\"));
        for (String name : list) {
        	getContentByURL(name,webClient);	
		}
		CookieManager CM = webClient.getCookieManager(); //WC= Your WebClient's name
//	    Set<Cookie> cookies_ret = CM.getCookies();//返回的Cookie在这里，下次请求的时候可能可以用上啦。
		webClient.closeAllWindows();	
	}
}
