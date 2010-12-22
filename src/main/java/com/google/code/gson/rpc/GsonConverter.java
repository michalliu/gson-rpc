package com.google.code.gson.rpc;

import java.lang.reflect.Type;

import com.google.code.gson.rpc.Check;
import com.google.code.gson.rpc.JsonConverter;
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
		this.gson = Check.notNull(gson, "gson");
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
		Check.notNull(throwable, "throwable");
		JsonObject error = new JsonObject();
		error.addProperty("type", throwable.getClass().getName());
		String message = throwable.getMessage();
		if (Check.isNotBlank(message)) {
			error.addProperty("message", message);
		}
		return error.toString();
	}

}
