package com.google.code.gson.rpc;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author wangzijian
 * 
 */
public class MockJsonServiceExporter extends JsonServiceExporter {

	private static final long serialVersionUID = 7625460552552795282L;

	private MockStudentService mockStudentService = new MockStudentService();
	@Override
	protected Object getService(HttpServletRequest request) {
		return mockStudentService;
	}

}
