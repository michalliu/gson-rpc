package com.google.code.gson.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.gson.rpc.Check;
import com.google.code.gson.rpc.Default;
import com.google.code.gson.rpc.JsonConverter;
import com.google.code.gson.rpc.JsonInvoker;
import com.google.code.gson.rpc.Streams;

/**
 * Simple implementation by URLConnection
 * 
 * @author wangzijian
 * 
 */
public class SimpleJsonInvoker implements JsonInvoker {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJsonInvoker.class);

	private final JsonConverter converter;

	public SimpleJsonInvoker() {
		this(Default.CONVERTER);
	}

	public SimpleJsonInvoker(JsonConverter jonConverter) {
		this.converter = Check.notNull(jonConverter, "jonConverter");
	}

	@Override
	public Object invoke(URL url, Type returnType) throws IOException {
		Check.notNull(url, "url");
		Check.notNull(returnType, "returnType");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Invoke url -> " + url);
		}
		return readAsJson(url.openStream(), returnType);
	}

	@Override
	public Object invoke(URL url, Object object, Type returnType) throws IOException {
		Check.notNull(url, "url");
		Check.notNull(object, "object");
		Check.notNull(returnType, "returnType");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Invoke url -> " + url);
		}

		String request = converter.toJson(object);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Request -> " + request);
		}

		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		Streams.write(request, connection.getOutputStream());

		return readAsJson(connection.getInputStream(), returnType);
	}

	private Object readAsJson(InputStream inputStream, Type returnType) throws IOException {
		String response = Streams.read(inputStream);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Response -> " + response);
		}
		return converter.fromJson(response, returnType);
	}
}
