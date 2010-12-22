package com.google.code.gson.rpc;

import java.nio.charset.Charset;

/**
 * 
 * @author wangzijian
 * 
 */
class Default {

	static final Charset CHARSET = Charset.forName("UTF-8");
	
	static final JsonConverter CONVERTER = new GsonConverter();

	private Default() {

	}
}
