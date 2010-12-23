package com.google.code.gson.rpc;

/**
 * 
 * @author wangzijian
 * 
 */
public class MalformedRequestPatternException extends RuntimeException {

	private static final long serialVersionUID = -5750989233953428760L;
	
	private final String requestURI;

	public MalformedRequestPatternException(String requestURI) {
		super(messageOf(requestURI));
		this.requestURI = requestURI;
	}
	
	public String getRequestURI() {
		return requestURI;
	}

	static String messageOf(String requestURI) {
		return "Cannot determine the invocation from request URI " + requestURI;
	}

}
