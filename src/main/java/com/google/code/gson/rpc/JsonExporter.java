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
public abstract class JsonExporter extends HttpServlet {

	private static final long serialVersionUID = 5848275812045557458L;
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonExporter.class);

	protected JsonConverter converter = Default.CONVERTER;

	protected abstract Object named(String name);

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
		Object service = named(getServiceName(request));
		Method method = firstMethodOf(service, getMethodName(request));
		if (noArguments(method)) {
			return method.invoke(service);
		}
		String requestContent = Streams.read(request.getInputStream());
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Request body -> " + requestContent);
		}
		Type type = method.getGenericParameterTypes()[0];
		Object argument = converter.fromJson(requestContent, type);
		return method.invoke(service, argument);
	}

	private String toJsonError(Throwable throwable) {
		LOGGER.error("Proccess request error", throwable);
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

	private String getMethodName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.substring(requestURI.lastIndexOf(".") + 1);
	}

	private String getServiceName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		int lastIndexOfSlash = requestURI.lastIndexOf("/");
		int beginIndex = 0;
		if (lastIndexOfSlash != -1) {
			beginIndex = lastIndexOfSlash + 1;	
		}
		return requestURI.substring(beginIndex, requestURI.lastIndexOf("."));
	}

	private boolean noArguments(Method method) {
		return method.getGenericParameterTypes().length == 0;
	}

	private void respond(HttpServletResponse response, String body) throws IOException {
		if (Check.isBlank(body)) {
			return;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Response body -> " + body);
		}
		response.setContentType("text/plain; charset=" + Default.CHARSET.name());
		Streams.write(body, response.getOutputStream());
	}

	public void setConverter(JsonConverter converter) {
		this.converter = converter;
	}

}
