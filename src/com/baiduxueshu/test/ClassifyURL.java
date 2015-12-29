package com.baiduxueshu.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ClassifyURL {
static String urlrespath = "E:\\分类后url\\wanfangdata_1211.txt";
	public static void main(String[] args)

	throws Exception {
		List<String> list = readerFile("E:\\分类后url\\1207.txt");
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			String[] split = string.split("\t");
			if(split.length == 2){
				URL aURL = new URL(split[1]);
				String host = aURL.getHost();
				if (host.contains(".wanfangdata.")) {
					sb.append(split[0] + "\t" + split[1] + "\r\n");
				}
			}
		}
		writeResult(sb.toString());
	}

	public static List<String> readerFile(String filename) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"GBK"));  
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
//				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Boolean writeResult(String str) {
		try {
			OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(urlrespath, true));
			bw.write(str);
			bw.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return true;
	}

}