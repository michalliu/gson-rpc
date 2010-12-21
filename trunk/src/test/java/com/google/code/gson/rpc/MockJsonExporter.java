package com.google.code.gson.rpc;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * 
 * @author wangzijian
 * 
 */
public class MockJsonExporter extends JsonExporter {

	private static final long serialVersionUID = -7246291847524439612L;

	@Override
	protected Object named(String name) {
		assertThat(name, is("mockService"));
		return new MockStudentService();
	}

}
