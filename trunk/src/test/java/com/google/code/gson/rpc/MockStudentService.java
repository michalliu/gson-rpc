package com.google.code.gson.rpc;

import static com.google.code.gson.rpc.ServletInvocationContext.currentRequest;
import static com.google.code.gson.rpc.ServletInvocationContext.currentResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author wangzijian
 * 
 */
public class MockStudentService {

	private Student expectedStudent;
	private long expectedId;

	public MockStudentService() {
		expectedStudent = Student.EXAMPLE;
		expectedId = 987L;
	}

	@Override
	public String toString() {
		assertNotNull(currentRequest());
		assertNotNull(currentResponse());
		return "Mock";
	}

	public Student get(Long id) {
		assertThat(id, is(expectedId));
		return expectedStudent;
	}

	public void delete(Student student) {
		assertThat(student, is(expectedStudent));
	}

}
