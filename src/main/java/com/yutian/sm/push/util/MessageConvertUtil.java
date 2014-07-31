/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push.util;

import org.apache.commons.lang.StringUtils;

import com.yutian.entity.sms.SmsReception;
import com.yutian.sm.push.vo.MessageNotifyRequest;


/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午3:13:00
 */
public class MessageConvertUtil {
	public static String convert(String src){
		if(src == null){
			return null;
		}
		MessageNotifyRequest request = JsonUtil.jsonToObject(src, MessageNotifyRequest.class);
		return getSocketResponse(request.getContent());
	}
	
	public static String getSocketResponse(SmsReception sms)
	{
		StringBuilder sb = new StringBuilder("1001");
		Integer length = 0;
		StringBuilder body = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		body.append( "<mall>" ).append( "<request>" ).append( "<msgId>" ).append( sms.getId() ).append( "</msgId>" );
		body.append( "<mobileNo>" ).append( sms.getMobileNo() ).append( "</mobileNo>" );
		body.append( "<accessNo>" ).append( sms.getAccessNo() ).append( "</accessNo>" );
		body.append( "<sendTime>" ).append( DateUtil.getDateYMDHMSFormat().format(sms.getSendTime())).append( "</sendTime>" );
		body.append( "<content>" ).append(sms.getContent()).append("</content>");
		body.append("<ext1></ext1 >").append("<ext2></ext2 >").append("<ext3></ext3 >");
		body.append("</request>").append("</mall>");
		length = body.toString().length() + 10;
		sb.append(StringUtils.leftPad(length.toString(), 6, '0')).append(body);
		return  sb.toString();
	}
}
