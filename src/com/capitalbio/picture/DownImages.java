package com.capitalbio.picture;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownImages {
    private static int COUNT = 0;
    private static int DOWN_COUNT = 0;
     
    public static void jsoupHTML(String urlPath) throws Exception{
        Document doc = Jsoup.connect(urlPath).timeout(1000000).get();
        //:当前页中的图片
        Elements srcLinks = doc.select("img[src$=.jpg]");
        for (Element link : srcLinks) {
            //:剔除标签，只剩链接路径
            String imagesPath = link.attr("src");
            System.out.println("当前访问路径:"+imagesPath);
            getImages(imagesPath, "d://images//0000"+ ++COUNT +".jpg");
        }
         
        //:提取网站中所有的href连接
        Elements linehrefs = doc.select("a[href]");
         
        for (Element linehref : linehrefs) {
            String lihr = linehref.attr("href");
            if(lihr.length()>4){
                String ht = lihr.substring(0, 4);
                String htt = lihr.substring(0, 1);
                if(!ht.equals("http") && htt.equals("/")){
                    lihr = urlPath + lihr;
                }
                if(lihr.substring(0, 4).equals("http")){
                    Document docs = Jsoup.connect(lihr).timeout(1000000).get();
                    Elements links = docs.select("img[src$=.jpg]");
                    for (Element link : links) {
                        //:剔除标签，只剩链接路径
                        String imagesPath = link.attr("src");
                        System.out.println("当前访问路径:"+imagesPath);
                        getImages(imagesPath, "d://images//0000"+ COUNT++ +".jpg");
                    }
                }
            }
        }
    }
     
     
    /**
     * @param urlPath 图片路径
     * @throws Exception 
     */
    public static void getImages(String urlPath,String fileName) throws Exception{
        URL url = new URL(urlPath);//：获取的路径
        //:http协议连接对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(6 * 10000);
        if (conn.getResponseCode() <10000){
            InputStream inputStream = conn.getInputStream();
            byte[] data = readStream(inputStream);
            if(data.length>(1024*10)){
                FileOutputStream outputStream = new FileOutputStream(fileName);
                outputStream.write(data);
                System.err.println("第"+ ++DOWN_COUNT +"图片下载成功");
                outputStream.close();
            }
        }
         
    }
     
    /**
     * 读取url中数据，并以字节的形式返回
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer)) !=-1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
     
    public static void main(String[] args) {
        try {
            String urlPath = "http://www.22mm.cc/";
            jsoupHTML(urlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println("共访问"+COUNT+"张图片，其中下载"+DOWN_COUNT+"张图片");
        }
    }
}
