package com.google.code.gson.rpc;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * 
 * @author wangzijian
 * 
 */
public class JsonInvokerTest extends HttpTestSupport {

	private String service = "http://localhost:8090/test/json/service";

	private JsonInvoker invoker;

	@Test
	public void test() throws Exception {
		Object actual = invoker.invoke(new URL(service), Student.class);
		assertEquals(Student.EXAMPLE, actual);
	}

	@Test
	public void test2() throws IOException {
		Object actual = invoker.invoke(new URL(service), Student.EXAMPLE, boolean.class);
		assertEquals(true, actual);
	}

	protected WebAppContext newWebAppContext() {
		WebAppContext context = new WebAppContext(".", "/test");
		context.addServlet(MockJsonServlet.class, "/json/service");
		return context;
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		invoker = new SimpleJsonInvoker();
	}

	public static class MockJsonServlet extends HttpServlet {
		private static final long serialVersionUID = -4663923718196115190L;

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			Streams.write(Student.JSON, response.getOutputStream());
		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String json = Streams.read(request.getInputStream());
			Streams.write(Student.JSON.equals(json), response.getOutputStream());
		}

	}
}
