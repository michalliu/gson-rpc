package com.google.code.gson.rpc;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author wangzijian
 * 
 */
public class FindByNameJsonExporter extends JsonExporter {

	private static final long serialVersionUID = -7246291847524439612L;

	private Map<String, Object> services = new HashMap<String, Object>();
	
	public FindByNameJsonExporter() {
		services.put("mockService", new MockStudentService());
	}
	
	@Override
	protected Object named(String name) {
		return services.get(name);
	}

}
