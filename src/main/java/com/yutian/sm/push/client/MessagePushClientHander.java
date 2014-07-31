/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a> 2014年5月14日 下午2:09:00
 */
public class MessagePushClientHander extends IoHandlerAdapter {
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 当客户端接受到消息时
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// 我们已设定了服务器的消息规则是一行一行读取，这里就可以转为String:
		String s = (String) message;

		// Writer the received data back to remote peer
		System.out.println("服务器发来的收到消息: " + s);

	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 当一个客户端被关闭时
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("one client Disconnect");

	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client sessionIdle called.");
	}

	/**
	 * 当一个客户端连接进入时
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("incomming client:" + session.getRemoteAddress());
//		session.write("我来啦");
	}
}
