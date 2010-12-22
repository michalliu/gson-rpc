package com.google.code.gson.rpc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static com.google.code.gson.rpc.MalformedRequestPatternException.messageOf;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.webapp.WebAppContext;

import com.google.gson.JsonPrimitive;

/**
 * 
 * @author wangzijian
 * 
 */
public class MultiJsonServiceExporterTest extends ExporterTestSupport {

	private String service = "http://localhost:8090/test/json/services/";

	@Test
	public void testToString() throws Exception {
		String response = get(service + "mockService.toString");
		assertThat(response, is(new JsonPrimitive("Mock").toString()));
	}

	@Test
	public void testGet() throws Exception {
		String response = post(service + "mockService.get", "987");
		assertThat(response, is(Student.JSON));
	}

	@Test
	public void testDelete() throws Exception {
		String response = post(service + "mockService.delete", Student.JSON);
		assertTrue(Check.isBlank(response));
	}

	@Test
	public void testUnqiueArguments() throws Exception {
		String request = "[456,789]";
		String response = post(service + "mockService.addBook", request);
		assertThat(toError(response), is(new Error(
				"java.lang.IllegalArgumentException", 
				"Cannot determine the unqiue argument from " +
				"public void com.google.code.gson.rpc.MockStudentService." +
				"addBook(java.lang.String,java.lang.String)")));
	}
	
	@Test
	public void testServiceNotFound() throws Exception {
		String response = get(service + "inexistentService.toString");
		assertThat(toError(response), is(new Error("java.lang.NullPointerException",
				"Service not found by 'inexistentService'")));
	}
	@Test
	public void testMalformedRequestPattern1() throws Exception {
		String response = get(service + "mockService");
		assertThat(toError(response), is(new Error(
				"com.google.code.gson.rpc.MalformedRequestPatternException",
				messageOf("/test/json/services/mockService"))));
	}
	
	@Test
	public void testMalformedRequestPattern2() throws Exception {
		String response = get(service + ".update");
		assertThat(toError(response), is(new Error(
				"com.google.code.gson.rpc.MalformedRequestPatternException",
				messageOf("/test/json/services/.update"))));
	}
	
	@Override
	protected Handler newWebAppContext() {
		WebAppContext context = new WebAppContext(".", "/test");
		context.addServlet(NameIndexedMultiJsonServiceExporter.class, "/json/services/*");
		return context;
	}

}
