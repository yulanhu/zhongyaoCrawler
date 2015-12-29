package com.capitalbio.zhongyaoshuju;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 上海证券交易所数据抓取测试
 * @since 1.0
 * @author  Lanxiaowei@citic-finance.com
 * @date    2015-8-27下午6:16:14
 *
 */
public class others {
	public static void main(String[] args) throws Exception {
		downloadListPage();
	}
	
	public static void downloadListPage() throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
	    webClient.getOptions().setCssEnabled(false);  
        webClient.getOptions().setJavaScriptEnabled(true);  
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(false);
        webClient.getOptions().setTimeout(10000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        
        int totalPage = 22;
        boolean first = true;
        HtmlPage page = null;
        do {
        	if(first) {
        		page = (HtmlPage)webClient.getPage("http://www.sse.com.cn/assortment/stock/list/name/");
        		System.out.println(page.asXml());
//                FileUtils.writeFile(page.asXml(), "C:/shh/list/" + totalPage + ".html", "UTF-8", false);
        		first = false;
        	} else {
        		HtmlAnchor anchor = null;
        		if(totalPage == 22 -1) {
        			anchor = (HtmlAnchor) page.getHtmlElementById("xsgf_next");
        		} else {
        			anchor = (HtmlAnchor) page.getHtmlElementById("dateList_container_next");
        		}
                page = (HtmlPage) anchor.click();
//                FileUtils.writeFile(page.asXml(), "C:/shh/list/" + totalPage + ".html", "UTF-8", false);
                System.out.println(page.asXml());
        	}
        	
            totalPage--;
        } while(totalPage > 0);
	    
        //关闭模拟窗口  
        webClient.closeAllWindows();
	}
}
