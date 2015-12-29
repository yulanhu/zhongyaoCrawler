package URLConnectionTest;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * @author huyulan
 * @date 2015-12-28
 */
public class CopyOfzongyaodatabase {
	static Map<String, String> map;
	static String init = "http://www.tcm100.com/ShuJuKu/ZhongYao/";
	static String url = "http://www.tcm100.com/ShuJuKu/ZhongYao/ZhongYao.aspx";
	public static void getContentByURL(String name,WebClient webClient){
		map = new LinkedHashMap<String, String>();
        HtmlPage page = null;
		try {
			
			page = (HtmlPage)webClient.getPage(url);
			// 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可 
			HtmlInput input = (HtmlInput)page.getHtmlElementById("Txt_1");
	        input.setValueAttribute(name);
	       //获取搜索按钮并点击
	        HtmlInput btn = (HtmlInput)page.getHtmlElementById("Button1");
	        page = btn.click(); 
	        Document doc = Jsoup.parse(page.asXml());
	        Elements nume = doc.getElementsByAttributeValue("class", "PageNumber");
			int num = Integer.parseInt(nume.last().text());
			System.out.println(num);
			int flag = 0;
			do{
				parser(webClient,page,name);	
				if(page.asXml().contains("javascript:__doPostBack('DataGrid_SearchResult$ctl08$ctl01','')")){
					 ScriptResult sr = page.executeJavaScript("javascript:__doPostBack('DataGrid_SearchResult$ctl08$ctl01','')");
				        page = (HtmlPage) sr.getNewPage();  
				        flag ++;
				}
				
			}while(flag < 4);
//	        System.out.println(flag);
			
		} catch (Exception e) {
			System.out.println("err: " + url);
			e.printStackTrace();
		}
		
	}
	
	public static void parser(WebClient webClient, HtmlPage page ,String name) throws Exception{
//		System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		Elements value = doc.getElementsByAttributeValue("id", "SearchResultPanelShuJuKu");
		Elements tr = value.select("tr");
		StringBuffer sb = null;
		if(tr.size() > 3){
			for (int i = 2; i < tr.size() - 1; i++) {
				sb= new StringBuffer();
				Elements hrefText = tr.get(i).select("a[href]");
				String rs = hrefText.text().replace(" ", "");
				
				String urlnew = init + hrefText.attr("href");
				HtmlPage page1 = (HtmlPage)webClient.getPage(urlnew);
				doc = Jsoup.parse(page1.asXml());
				Elements select = doc.getElementsByAttributeValue("class",
						"SearchResultDetailPanelShuJuKu").select("tr");
				for (Element element : select) {
					sb.append(element.text()+ "\r\n");
				}
				writeFile(sb.toString(),rs);
			}
		}
	}
	
	public static void writeFile(String content,String rs){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("E:\\test\\" + rs + ".txt", true)));
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
				list.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void main(String[] args) {
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
	    webClient.getOptions().setCssEnabled(false);  
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(false);
//        webClient.getOptions().setTimeout(6000000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        List<String> list = readerFile("rs/a.txt");
//        
//        for (String name : list) {
//        	getContentByURL(name,webClient);	
//		}
        getContentByURL("薄荷",webClient);	
		CookieManager CM = webClient.getCookieManager(); //WC= Your WebClient's name
	    Set<Cookie> cookies_ret = CM.getCookies();//返回的Cookie在这里，下次请求的时候可能可以用上啦。
		webClient.closeAllWindows();	
	}
}
