package com.google.code.gson.rpc;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * @author wangzijian
 * 
 */
public abstract class MultiJsonServiceExporter extends JsonServiceExporter {

	private static final long serialVersionUID = 5287482239887918387L;

	protected abstract Object named(String name);
	
	@Override
	protected Object getService(HttpServletRequest request) {
		String serviceName = parseServiceName(request);
		if (logger.isInfoEnabled()) {
			logger.info("Service name " + serviceName);
		}
		Object service = named(serviceName);
		return Check.notNull(service, "Service not found by '" + serviceName + "'");
	}
	
	@Override
	protected String parseMethodName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.substring(lastIndexOfDot(requestURI) + 1);
	}

	protected String parseServiceName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		int begin = parseBeginIndex(requestURI);
		int end = parseEndIndex(requestURI);
		String serviceName = requestURI.substring(begin, end);
		if (Check.isBlank(serviceName)) {
			throw new MalformedRequestPatternException(requestURI);
		}
		return serviceName;
	}

	private int parseBeginIndex(String requestURI) {
		int lastIndexOfSlash = requestURI.lastIndexOf("/");
		int beginIndex = 0;
		if (lastIndexOfSlash != -1) {
			beginIndex = lastIndexOfSlash + 1;	
		}
		return beginIndex;
	}

	private int parseEndIndex(String requestURI) {
		return lastIndexOfDot(requestURI);
	}

	private int lastIndexOfDot(String requestURI) {
		int lastIndexOfDot = requestURI.lastIndexOf(".");
		if (lastIndexOfDot == -1) {
			throw new MalformedRequestPatternException(requestURI);
		}
		return lastIndexOfDot;
	}
}
