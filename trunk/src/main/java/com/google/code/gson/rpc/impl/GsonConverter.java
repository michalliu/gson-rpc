package com.google.code.gson.rpc.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Type;

import com.google.code.gson.rpc.JsonConverter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author wangzijian
 * 
 */
public class GsonConverter implements JsonConverter {

	private final Gson gson;

	public GsonConverter() {
		this(new Gson());
	}

	public GsonConverter(Gson gson) {
		this.gson = checkNotNull(gson, "gson");
	}
	
	@Override
	public Object fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	@Override
	public String toJson(Object object) {
		return gson.toJson(object);
	}
	
	@Override
	public String toJson(Throwable throwable) {
		checkNotNull(throwable, "throwable");
		JsonObject error = new JsonObject();
		error.addProperty("type", throwable.getClass().getName());
		String message = throwable.getMessage();
		if (Strings.isNullOrEmpty(message)) {
			error.addProperty("message", message);
		}
		return error.toString();
	}

}
