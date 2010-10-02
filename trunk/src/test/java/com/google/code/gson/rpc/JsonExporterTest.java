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
		InputStream openStream = new URL(service + "mockService.toString").openStream();
		String response = Streams.read(openStream);
		assertThat(response, is(new JsonPrimitive("Mock").toString()));
	}

	@Test
	public void testGet() throws Exception {
		URLConnection connection = new URL(service + "mockService.get").openConnection();
		connection.setDoOutput(true);

		Streams.write("987", connection.getOutputStream());
		String response = Streams.read(connection.getInputStream());
		assertThat(response, is(Student.JSON));
	}

	@Test
	public void testDelete() throws Exception {
		URLConnection connection = new URL(service + "mockService.delete").openConnection();
		connection.setDoOutput(true);

		Streams.write(Student.JSON, connection.getOutputStream());
		String response = Streams.read(connection.getInputStream());
		assertTrue(Check.isBlank(response));
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
		server.setStopAtShutdown(true);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	protected WebAppContext newWebAppContext() {
		WebAppContext context = new WebAppContext(".", "/test");
		context.addServlet(MockJsonExporter.class, "/json/services/*");
		return context;
	}

}
