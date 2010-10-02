package com.google.code.gson.rpc;

import java.nio.charset.Charset;

import com.google.code.gson.rpc.impl.GsonConverter;

/**
 * 
 * @author wangzijian
 * 
 */
public class Default {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	public static final JsonConverter CONVERTER = new GsonConverter();

	private Default() {

	}
}
