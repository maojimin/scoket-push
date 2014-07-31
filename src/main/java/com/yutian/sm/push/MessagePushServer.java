/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.yutian.sm.push.vo.PushConfig;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 上午11:16:56
 */
public class MessagePushServer {
	private final int PUSH_PORT = PushConfig.getConfig().getPushPort();
	public void run() throws IOException{
		IoAcceptor acceptor = new NioSocketAcceptor(); 
        
        // Add two filters : a logger and a codec
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new PushProtocalCodecFactory( Charset.forName( "UTF-8" ))));
   
        // Attach the business logic to the server
        acceptor.setHandler( new MessagePushHandler() );

        // Configurate the buffer size and the iddle time
        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        
//      acceptor.getSessionConfig().set( IdleStatus.BOTH_IDLE, 10 );
        
        // And bind !
        acceptor.bind( new InetSocketAddress(PUSH_PORT) );
        
	}
	
	public static void main(String[] args) {
		try {
			new MessagePushServer().run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
