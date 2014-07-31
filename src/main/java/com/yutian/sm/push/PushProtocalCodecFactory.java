/*
 * yutian.com Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 */
package com.yutian.sm.push;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月14日 下午5:40:36
 */
public class PushProtocalCodecFactory implements ProtocolCodecFactory {  
    private final PushProtocalEncoder encoder;  
    private final PushProtocalDecoder decoder;  
  
    public PushProtocalCodecFactory(Charset charset) {  
        encoder = new PushProtocalEncoder(charset);  
        decoder = new PushProtocalDecoder(charset);  
    }  
  
    public ProtocolEncoder getEncoder(IoSession session) {  
        return encoder;  
    }  
  
    public ProtocolDecoder getDecoder(IoSession session) {  
        return decoder;  
    }  
  
}
