package com.google.code.gson.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wangzijian
 * 
 */
public class MockJsonServlet extends HttpServlet {

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
