package com.google.code.gson.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wangzijian
 * 
 */
public class ServletInvocationContext {

	private static final ThreadLocal<HttpServletRequest> CURRENT_REQUEST = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> CURRENT_RESPONSE = new ThreadLocal<HttpServletResponse>();

	private ServletInvocationContext() {
	}

	public static HttpServletRequest currentRequest() {
		return CURRENT_REQUEST.get();
	}
	
	public static HttpServletResponse currentResponse() {
		return CURRENT_RESPONSE.get();
	}
	
	static void set(HttpServletRequest request, HttpServletResponse response) { 
		CURRENT_REQUEST.set(request);
		CURRENT_RESPONSE.set(response);
	}

	static void clear() {
		CURRENT_REQUEST.set(null);
		CURRENT_RESPONSE.set(null);
	}

}
