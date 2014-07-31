/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月19日 下午6:22:52
 */
public class SessionMap {
	private static final ConcurrentHashMap<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
	
	public static void freshSession(String key,IoSession session){
		sessionMap.put(key, session);
	}
	
	public static IoSession getSession(String key){
		return sessionMap.get(key);
	}
	
}
