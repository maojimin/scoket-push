/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午5:32:52
 */
public class PushProtocalDecoder implements ProtocolDecoder {  
    private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");  
    private final Charset charset;  
    private int maxPackLength = 2000;  
    private static final Logger logger = Logger.getLogger(PushProtocalDecoder.class);
  
    public PushProtocalDecoder() {  
        this(Charset.defaultCharset());  
    }  
  
    public PushProtocalDecoder(Charset charset) {  
        this.charset = charset;  
    }  
  
    public int getMaxLineLength() {  
        return maxPackLength;  
    }  
  
    public void setMaxLineLength(int maxLineLength) {  
        if (maxLineLength <= 0) {  
            throw new IllegalArgumentException("maxLineLength: " + maxLineLength);  
        }  
        this.maxPackLength = maxLineLength;  
    }  
  
    private Context getContext(IoSession session) {  
        Context ctx;  
        ctx = (Context) session.getAttribute(CONTEXT);  
        if (ctx == null) {  
            ctx = new Context();  
            session.setAttribute(CONTEXT, ctx);  
        }  
        return ctx;  
    }  

    /**
     * 200200015aaaa
     * 报头10位后面是报文体
     */
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {  
        final int packHeadLength = 10;  
        // 先获取上次的处理上下文，其中可能有未处理完的数据  
        Context ctx = getContext(session);  
        ctx.append(in);  
        IoBuffer buf = ctx.getBuffer();
        buf.flip();
        int limit = buf.limit();
        byte[] lastContent = ctx.getBody();//取出上次次没处理完的
        byte[] byteContent = null;
        if(lastContent!=null){
        	int len = 0;
        	len = limit + lastContent.length;
        	byteContent = new byte[len];
        	System.arraycopy(lastContent, 0, byteContent, 0, lastContent.length);
        	byte[] temp = new byte[limit];
        	buf.get(temp);
        	System.arraycopy(temp, 0, byteContent, lastContent.length, limit);
        }else{
        	byteContent = new byte[limit];
        	buf.get(byteContent);
        }
        String content = new String(byteContent,charset);
        logger.info("recieve message:" + content + ", from session:"+session+",length:"+content.length());
        int bodyLen = ctx.getBodyLen();
        // parse head
        if(!ctx.isHeadDecodeOver()){
        	if(content!=null && content.length() >= packHeadLength){
        		//匹配报头前缀
        		if(!content.startsWith("2001")){
        			buf.clear();
        			ctx.reset();
        			return;
        		}
        		//获取报文长度
        		String len = content.substring(4,10);
        		try{
        			bodyLen = Integer.parseInt(len);
        			ctx.setBodyLen(bodyLen);
        		}catch(Exception e){
        			buf.clear();
        			ctx.reset();
        			return;
        		}
        		ctx.setHeadDecodeOver(true);
        	}else{// 报头不足，下次处理
        		ctx.setBody(content.getBytes());
        		buf.clear();
        		return;
        	}
        }
        
        // parse body
        if(content.length() >= bodyLen){
			String text = content.substring(0,bodyLen);
			// 多余的放到buf,下次处理
			if(content.length() > bodyLen){
				String temp = content.substring(bodyLen);
				buf.flip();
				buf.clear();
				buf.put(temp.getBytes());
			}else{
				buf.clear();
			}
			ctx.reset();
			out.write(text);
		}else{
			//报文未全部送到，放到上下文，下次处理
			ctx.setBody(content.getBytes());
			buf.clear();
		}
    }  
  
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {  
    }  
  
    public void dispose(IoSession session) throws Exception {  
        Context ctx = (Context) session.getAttribute(CONTEXT);  
        if (ctx != null) {  
            session.removeAttribute(CONTEXT);  
        }  
    }  
  
    /**
     * 上下文持有
     * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
     * 2014年5月15日 下午9:21:08
     */
    private class Context {  
        private final CharsetDecoder decoder;  
        private IoBuffer buf;  
        private byte[] body;
        private boolean isHeadDecodeOver;
        private int bodyLen;
        private Context() {  
            decoder = charset.newDecoder();  
            buf = IoBuffer.allocate(128).setAutoExpand(true);  
        }  
        public IoBuffer getBuffer() {  
            return buf;  
        }  
  
        /**
		 * @return the isHeadDecodeOver
		 */
		public boolean isHeadDecodeOver() {
			return isHeadDecodeOver;
		}

		/**
		 * @param isHeadDecodeOver the isHeadDecodeOver to set
		 */
		public void setHeadDecodeOver(boolean isHeadDecodeOver) {
			this.isHeadDecodeOver = isHeadDecodeOver;
		}

  
        /**
		 * @return the bodyLen
		 */
		public int getBodyLen() {
			return bodyLen;
		}

		/**
		 * @param bodyLen the bodyLen to set
		 */
		public void setBodyLen(int bodyLen) {
			this.bodyLen = bodyLen;
		}

		public void reset() {  
            decoder.reset();
            body = null;
            isHeadDecodeOver = false;
        }  
  
        /**
		 * @return the body
		 */
		public byte[] getBody() {
			return body;
		}

		/**
		 * @param body the body to set
		 */
		public void setBody(byte[] body) {
			this.body = body;
		}

		public void append(IoBuffer in) {  
            getBuffer().put(in);  
  
        }  
  
    }  
    
}  
