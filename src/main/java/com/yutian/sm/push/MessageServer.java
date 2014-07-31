/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午3:05:58
 */
public class MessageServer {
	private static final Logger logger = Logger.getLogger(MessageServer.class);
	public static void main(String[] args) {
		try {
			logger.info("MessageServer is started.");
			new MessagePushServer().run();
			new MessageReceiveServer().run();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
