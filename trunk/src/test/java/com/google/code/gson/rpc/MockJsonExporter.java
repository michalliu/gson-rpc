package com.google.code.gson.rpc;


/**
 * 
 * @author wangzijian
 * 
 */
public class MockJsonExporter extends JsonExporter {

	private static final long serialVersionUID = -7246291847524439612L;

	@Override
	protected Object named(String name) {
		return new MockStudentService();
	}

}
