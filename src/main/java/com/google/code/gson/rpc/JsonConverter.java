package com.google.code.gson.rpc;

import java.lang.reflect.Type;

/**
 * 
 * @author wangzijian
 * 
 */
public interface JsonConverter {

	Object fromJson(String json, Type type);

	String toJson(Object object);
	
	String toJson(Throwable throwable);
}
