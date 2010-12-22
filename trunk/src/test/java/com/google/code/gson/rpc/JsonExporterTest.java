package com.google.code.gson.rpc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;

/**
 * 
 * @author wangzijian
 * 
 */
public class JsonExporterTest {

	private String service = "http://localhost:8090/test/json/services/";
	private Server server;

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
	public void testMalformedInvocationPattern1() throws Exception {
		String response = get(service + "mockService");
		assertThat(toError(response), is(new Error("java.lang.IllegalArgumentException",
				"Cannot determine the invocation from " +
				"request URI /test/json/services/mockService, " +
				"expected invocation pattern ../studentService.delete")));
	}
	
	@Test
	public void testMalformedInvocationPattern2() throws Exception {
		String response = get(service + "update");
		assertThat(toError(response), is(new Error("java.lang.IllegalArgumentException",
				"Cannot determine the invocation from " +
				"request URI /test/json/services/update, " +
				"expected invocation pattern ../studentService.delete")));
	}
	
	@Test
	public void testServiceNotFound() throws Exception {
		String response = get(service + "inexistentService.toString");
		assertThat(toError(response), is(new Error("java.lang.NullPointerException",
				"Service not found by 'inexistentService'")));
	}
	
	private Error toError(String json) {
		return new Gson().fromJson(json, Error.class);
	}
	
	private String get(String urlString) throws Exception {
		InputStream openStream = new URL(urlString).openStream();
		return Streams.read(openStream);
	}
	
	private String post(String urlString, String request) throws Exception {
		URLConnection connection = new URL(urlString).openConnection();
		connection.setDoOutput(true);
		Streams.write(request, connection.getOutputStream());
		return Streams.read(connection.getInputStream());
	}
	
	@Before
	public void setUp() throws Exception {
		startServer();
	}

	private void startServer() throws Exception {
		server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8090);
		server.addConnector(connector);
		server.setHandler(newWebAppContext());
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	protected WebAppContext newWebAppContext() {
		WebAppContext context = new WebAppContext(".", "/test");
		context.addServlet(FindByNameJsonExporter.class, "/json/services/*");
		return context;
	}

}
