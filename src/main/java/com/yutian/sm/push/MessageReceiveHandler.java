/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.yutian.sm.push.util.JsonUtil;
import com.yutian.sm.push.util.MessageConvertUtil;
import com.yutian.sm.push.util.MessageFileUtil;
import com.yutian.sm.push.util.SessionMap;
import com.yutian.sm.push.vo.MessageNotifyRequest;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 上午11:17:16
 */
public class MessageReceiveHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(MessageReceiveHandler.class);
	
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
    	}
    }
    
	/* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("session:"+session+" is created.");
	}
	

	/* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("session:"+session+" is closed.");
	}


	/**
     * If the message is 'quit', we exit by closing the session. Otherwise,
     * we return the current date.
     */
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
    	String dealMsg = null;
    	if(message!=null){
    		dealMsg = message.toString().trim();
    	}
    	logger.info("messageReceived. message:"+dealMsg+",session:"+session);
        broadcast(dealMsg);
    }

    /* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("Send message:"+message.toString()+", session:"+session);
	}

	/**
     * On idle, we just write a message on the console
     */
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
    	String heartBeat = "Heartbeat from server.";
    	session.write(heartBeat);
    	if(logger.isDebugEnabled()){
    		logger.debug("server sessionIdle called. message:"+heartBeat);
    	}
    }
    
    private void broadcast(String message){
    	/*String[] scoketClientIps = ScoketClientUtil.getAllScoketClinetIp();
    	for(String clientIp:scoketClientIps){
    		IoSession session = SessionMap.getSession(clientIp);
    		if(session!=null && session.isConnected()){
    			session.write(MessageConvertUtil.convert(message));
    		}else{
    			MessageFileUtil.save(clientIp, message);
    		}
    	}*/
    	MessageNotifyRequest request = JsonUtil.jsonToObject(message, MessageNotifyRequest.class);
    	if(request == null){
    		return;
    	}
    	String ip = request.getIp();
    	String content = MessageConvertUtil.getSocketResponse(request.getContent());
    	IoSession session = SessionMap.getSession(ip);
    	if(session!=null && session.isConnected()){
			session.write(content);
		}else{
			MessageFileUtil.save(ip, message);
		}
    }
}