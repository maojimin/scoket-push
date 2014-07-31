/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午1:27:15
 */
public class MessageFileUtil {
	private static ConcurrentHashMap<String, Object> monitor = new ConcurrentHashMap<String, Object>();
	private static String rootPath;
	private static final String  CHARSET = "UTF-8";
	static{
		String userHome = System.getProperty("user.home");
		rootPath = userHome + File.separator + "pushmessage";
		File dir = new File(rootPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}
	public static void save(String ip,String message){
		if(ip==null || message==null){
			return;
		}
		BufferedWriter bw = null;
//		Object monitor1 = monitor.putIfAbsent(ip, new Object());
		synchronized (monitor) {
			try {
				//append模式
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(createFileNameByIp(ip),true),CHARSET));
				bw.write(message);
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static List<String> readAll(String ip){
		if(ip==null){
			return null;
		}
		BufferedReader br = null;
		List<String> result = new ArrayList<String>();
		File file = null;
//		Object monitor1 = monitor.putIfAbsent(ip, new Object());
		synchronized (monitor) {
			try {
				file = new File(createFileNameByIp(ip));
				if(!file.exists()){
					return null;
				}
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET));
				String line = null;
				while((line=br.readLine())!=null){
					if(!"".equals(line)){
						result.add(line);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(br!=null){
						br.close();
					}
					file.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	private static String createFileNameByIp(String ip){
		ip = ip.replace(".", "_");
		String filePath = rootPath + File.separator +  ip +".txt";
		System.out.println("filePaht:"+filePath);
		return filePath;
	}
}
