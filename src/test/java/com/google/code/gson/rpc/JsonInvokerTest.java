package com.google.code.gson.rpc;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import com.google.code.gson.rpc.impl.SimpleJsonInvoker;

/**
 * 
 * @author wangzijian
 * 
 */
public class JsonInvokerTest extends TestCase {

	private String service = "http://localhost:8090/test/json/service";

	private Server server;
	private JsonInvoker invoker;

	@Test
	public void test() throws Exception {
		Object actual = invoker.invoke(new URL(service), Student.class);
		assertEquals(Student.EXAMPLE, actual);
	}

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
		invoker = new SimpleJsonInvoker();
		startServer();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	private void startServer() throws Exception {
		server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8090);
		server.addConnector(connector);
		server.setHandler(newWebAppContext());
		server.setStopAtShutdown(true);
		server.start();
	}

}
