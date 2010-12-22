package com.google.code.gson.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wangzijian
 * 
 */
public abstract class JsonServiceExporter extends HttpServlet {

	private static final long serialVersionUID = 9034106918119043502L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected JsonConverter converter = Default.CONVERTER;

	protected abstract Object getService(HttpServletRequest request);
	
	protected String parseMethodName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		int lastIndexOfSlash = requestURI.lastIndexOf("/");
		if (lastIndexOfSlash == -1) {
			return requestURI;
		}
		String methodName = requestURI.substring(lastIndexOfSlash + 1);
		if (Check.isBlank(methodName)) {
			throw new MalformedRequestPatternException(requestURI);
		}
		return methodName;
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletInvocationContext.set(request, response);
		try {
			String responseContent = handleRequest(request);
			respond(response, responseContent);
		} finally {
			ServletInvocationContext.clear();
		}
	}

	private String handleRequest(HttpServletRequest request) {
		try {
			Object invoke = processInvocation(request);
			if (invoke != null) {
				return converter.toJson(invoke);
			}
		} catch (InvocationTargetException exception) {
			return toJsonError(exception.getCause());
		} catch (Exception exception) {
			return toJsonError(exception);
		}
		return null;
	}

	private Object processInvocation(HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, IOException {
		Object service = getService(request);
		String methodName = parseMethodName(request);
		if (logger.isInfoEnabled()) {
			logger.info("Method -> " + methodName);
		}
		Method method = firstMethodOf(service, methodName);
		if (noArguments(method)) {
			return method.invoke(service);
		}
		Type type = uniqueArgumentTypeOf(method);
		String requestContent = Streams.read(request.getInputStream());
		if (logger.isInfoEnabled()) {
			logger.info("Request body -> " + requestContent);
		}
		Object argument = converter.fromJson(requestContent, type);
		return method.invoke(service, argument);
	}

	private Type uniqueArgumentTypeOf(Method method) {
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (genericParameterTypes.length != 1) {
			throw new IllegalArgumentException("Cannot determine the unqiue argument from " + method);
		}
		return genericParameterTypes[0];
	}

	private String toJsonError(Throwable throwable) {
		logger.error("Proccess request error", throwable);
		return converter.toJson(throwable);
	}

	private Method firstMethodOf(Object service, String methodName) {
		Method method = null;
		for (Method each : service.getClass().getMethods()) {
			if (each.getName().equals(methodName)) {
				method = each;
				break;
			}
		}
		return Check.notNull(method, 
				"Method '" + methodName + "' not found for " + service.getClass().getName());
	}

	private boolean noArguments(Method method) {
		return method.getGenericParameterTypes().length == 0;
	}

	private void respond(HttpServletResponse response, String body) throws IOException {
		if (Check.isBlank(body)) {
			return;
		}
		if (logger.isInfoEnabled()) {
			logger.info("Response body -> " + body);
		}
		response.setContentType("text/html; charset=" + Default.CHARSET.name());
		Streams.write(body, response.getOutputStream());
	}

	public void setConverter(JsonConverter converter) {
		this.converter = converter;
	}

}
