package com.yutian.sm.push.util;

import com.google.gson.Gson;

/**
 * json 格式转换工具类，通过gson包转换
 * @author leon
 * @Create 2014-2-28
 */
public final class JsonUtil
{
	/**
	 * 将Gson对象静态化，不需要重复实例
	 */
	private static Gson json = new Gson();
	
	private JsonUtil(){
		
	}
	
	/**
	 * 将对象转换为json对象
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj){
		return json.toJson( obj );
	}
	
	/**
	 * json字符串转换为对象
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> clazz){
		try{
			return json.fromJson( jsonStr, clazz );
		}catch(Exception e){
			return null;
		}
	}

}
