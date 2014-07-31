package com.yutian.sm.push.vo;

import com.yutian.entity.sms.SmsReception;

/**
 * 信息提醒请求报文
 * @author leon
 * @Create 2014-4-30
 */
public class MessageNotifyRequest {

	/**
	 * ip
	 */
	private String ip;
	
	/**
	 * 发送内容
	 */
	private SmsReception content;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public SmsReception getContent() {
		return content;
	}

	public void setContent(SmsReception content) {
		this.content = content;
	}
}
