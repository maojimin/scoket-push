/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.yutian.sm.push.PushProtocalCodecFactory;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午1:59:40
 */
public class MessagePushClient {
	public void run(){
		 //Create TCP/IP connection  
        NioSocketConnector connector = new NioSocketConnector();  
          
        //创建接受数据的过滤器  
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();  
          
        //设定这个过滤器将一行一行(/r/n)的读取数据  
        chain.addLast( "logger", new LoggingFilter() );
//        chain.addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
        chain.addLast( "codec", new ProtocolCodecFilter( new PushProtocalCodecFactory( Charset.forName( "UTF-8" ))));
        //服务器的消息处理器：一个SamplMinaServerHander对象  
        connector.setHandler(new MessagePushClientHander());  
          
        //set connect timeout  
        connector.setConnectTimeoutMillis(30);  
          
        //连接到服务器：  
        ConnectFuture cf = connector.connect(new InetSocketAddress("211.140.14.6",7071));  
//      ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",7071));
        
        //Wait for the connection attempt to be finished.  
        cf.awaitUninterruptibly();  
        
        IoSession session=cf.getSession();
//		Charset ch =Charset.forName("utf-8");

		for(;;){
			Scanner sc = new Scanner(System.in);
			String str = sc.nextLine();
			session.write(str);
		}
          
//        cf.getSession().getCloseFuture().awaitUninterruptibly();  
//          
//        connector.dispose(); 
	}
	
	public static void main(String[] args) {
		new MessagePushClient().run();
	}
}
