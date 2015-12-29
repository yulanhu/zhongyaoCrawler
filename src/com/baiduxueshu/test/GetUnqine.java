package com.baiduxueshu.test;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class GetUnqine {
	public static ArrayList<String> FileReaderUrl(String filename) {
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static Map<String, String> quchong(List<String> list) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String string : list) {
			String[] split = string.split("\t", 2);
			String value = split[0];
			String tag = split[1];
			if (map.get(tag) != null) {
				String flag = map.get(tag);
				if (value != null) {
					map.put(tag, flag + " | " + value);
				}
			} else {
				if (value != null) {
					map.put(tag, value);
				}
			}
		}
		return map;
	}

	public static List<String> directoryFile(String path ) {
		File file = new File(path);
		File[] tempList = file.listFiles();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				String temp = tempList[i].getName();
				list.add(temp.substring(0, temp.length() - 4));
			}
		}
		return list;
	}

	public static void main(String[] args) throws FileNotFoundException {
//		String path = "D:\\rss\\";
//		System.out.println(directoryFile(path));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("E:\\分类后url\\cqvip_1211_unq.txt", true)));
		String path = "E:\\分类后url\\cqvip_1211.txt";
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> entry: quchong(FileReaderUrl(path)).entrySet()) {
			sb.append(entry.getValue() + "\t"  + entry.getKey() + "\r\n");
		}
		try {
			bw.write(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
