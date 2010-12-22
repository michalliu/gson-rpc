package com.google.code.gson.rpc;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;

import com.google.gson.Gson;

/**
 * 
 * @author wangzijian
 * 
 */
public abstract class ExporterTestSupport {

	private Server server;
	
	protected abstract Handler newWebAppContext();
	
	protected String get(String urlString) throws Exception {
		InputStream openStream = new URL(urlString).openStream();
		return Streams.read(openStream);
	}
	
	protected Error toError(String json) {
		return new Gson().fromJson(json, Error.class);
	}
	
	protected String post(String urlString, String request) throws Exception {
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

}
