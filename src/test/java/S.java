import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月28日 下午12:50:21
 */
public class S {
	private static Socket  socket = null;
	public static void main(String[] args) throws UnknownHostException, IOException {
	      String host = "211.140.14.6";  //要连接的服务端IP地址  
	      int port = 7071;   //要连接的服务端对应的监听端口  
	      //与服务端建立连接  
	      socket = new Socket(host, port);  
	      //建立连接后就可以往服务端写数据了  
//	      Writer writer = new OutputStreamWriter(socket.getOutputStream());  
//	      writer.write("2001000217<?xml version=\"1.0\" encoding=\"utf-8\"?><mall><request><channelId>0001</channelId><checkId>20140528111420</checkId><checkTime>20140528111420</checkTime><ext1></ext1><ext2></ext2><ext3></ext3></request></mall>");  
//	      writer.flush();//写完后要记得flush  
//	      writer.close();  
//	      socket.close();
	      
	      String str = null;
	      while((str=read())!=null){
	    	  System.out.println(str);
	      }
	}
	
	public static String read() throws IOException {
		String str;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			int b ;
			if((b = bufferedReader.read())!= -1 ){
				sb.append((char)b);
			}else{
				return null;
			}
		}

		int length = Integer.parseInt(sb.toString().substring(4, 10))-10;
		System.out.println(length);
		
		
		StringBuffer sbContent = new StringBuffer();
		for (int i = 0; i < length; i++) {
				//System.out.println(i);
			int b ;
			if((b = bufferedReader.read())!= -1 ){
				sbContent.append((char)b);
			}else{
				return null;
			}
			
		}
		str = sbContent.toString();
		return str;
	}
}
