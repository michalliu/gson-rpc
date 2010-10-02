package com.google.code.gson.rpc;

import java.nio.charset.Charset;

import com.google.code.gson.rpc.impl.GsonConverter;
import com.google.common.base.Charsets;

/**
 * 
 * @author wangzijian
 * 
 */
public class Default {

	public static final Charset CHARSET = Charsets.UTF_8;
	
	public static final JsonConverter CONVERTER = new GsonConverter();

	private Default() {

	}
}
