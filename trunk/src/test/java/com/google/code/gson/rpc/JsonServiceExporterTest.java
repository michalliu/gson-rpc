package com.google.code.gson.rpc;

import static com.google.code.gson.rpc.MalformedRequestPatternException.messageOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.webapp.WebAppContext;

import com.google.gson.JsonPrimitive;

/**
 * 
 * @author wangzijian
 * 
 */
public class JsonServiceExporterTest extends HttpTestSupport {
	
	private String service = "http://localhost:8090/test/json/services/";
	
	@Test
	public void testToString() throws Exception {
		String response = get(service + "toString");
		assertThat(response, is(new JsonPrimitive("Mock").toString()));
	}
	
	@Test
	public void testMalformedRequestPattern1() throws Exception {
		String response = get(service);
		assertThat(toError(response), is(new Error(
				"com.google.code.gson.rpc.MalformedRequestPatternException",
				messageOf("/test/json/services/"))));
	}
	
	@Override
	protected Handler newWebAppContext() {
		WebAppContext context = new WebAppContext(".", "/test");
		context.addServlet(MockJsonServiceExporter.class, "/json/services/*");
		return context;
	}

	public static class MockJsonServiceExporter extends JsonServiceExporter {
		private static final long serialVersionUID = 7625460552552795282L;

		private MockStudentService mockStudentService = new MockStudentService();
		@Override
		protected Object getService(HttpServletRequest request) {
			return mockStudentService;
		}
	}
}
