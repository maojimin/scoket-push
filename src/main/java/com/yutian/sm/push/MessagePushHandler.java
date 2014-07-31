/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.yutian.sm.push.util.MessageFileUtil;
import com.yutian.sm.push.util.SessionMap;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 上午11:17:16
 */
public class MessagePushHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(MessagePushHandler.class);
	
    /**
     * Trap exceptions.
     */
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
    	try{
	    	String clientIp = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
	        logger.error("session:" + clientIp + " cause exception.",cause);
    	}catch(Exception e){
    		logger.error("",e);	
    	}finally{
    		session.close(true);
    	}
    }
    
	/* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		String clientIp = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
		if(clientIp.equals("10.212.214.25") || clientIp.equals("10.212.214.26")){
			session.close(true);
			logger.info("session:"+session);
		}
		IoSession exisitSession = SessionMap.getSession(clientIp);
		if(exisitSession!=null){
			exisitSession.close(true);
			logger.info("exisitSession:"+exisitSession+" is closed.");
		}
		SessionMap.freshSession(clientIp, session);
		
		// 处理上次未处理的消息
		List<String> messageList = MessageFileUtil.readAll(clientIp);
		if(messageList!=null && messageList.size()>0){
			for(String message : messageList){
				session.write(message);
			}
		}
		logger.info("session:"+session+" is created.");
	}
	

	/* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("session:"+session+" is closed.");
		session.close(true);
	}


	/**
     * If the message is 'quit', we exit by closing the session. Otherwise,
     * we return the current date.
     */
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        String msg = message.toString();
        logger.info("messageReceived. message:"+message+",session:"+session);
        // 心跳检测
        if(msg!=null && msg.startsWith("2001") && msg.length()>=10){
        	try{
	        	String len = msg.substring(4,10);
	        	int bodyLen = Integer.parseInt(len);
	        	String content = msg.substring(10,bodyLen);
	        	Document document = DocumentHelper.parseText(content);
				Element root = document.getRootElement();
				Node channelIdNode = root.selectSingleNode("//channelId");
	//			Node checkIdNode = root.selectSingleNode("//checkId");
	//			Node checkTimeNode = root.selectSingleNode("//checkTime ");
				String checkId = channelIdNode.getText();
				String response = "<?xml version=\"1.0\" encoding=\"utf-8\"?><mall><response><state>0</state><checkId>"
						+ checkId + "</checkId></response></mall>";
				response = "2002"
						+ StringUtils.leftPad(response.length() + 10 + "", 6,
								"0") + response;
	        	session.write(response);
        	}catch(Exception e){
        		logger.error("", e);
        	}
        }
    }
    
    /* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("SERVER-->messageSent. message:"+message+",session:"+session);
	}

	/**
     * On idle, we just write a message on the console
     */
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
    }
}