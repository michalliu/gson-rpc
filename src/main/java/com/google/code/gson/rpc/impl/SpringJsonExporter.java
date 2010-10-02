package com.google.code.gson.rpc.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.gson.rpc.JsonConverter;
import com.google.code.gson.rpc.JsonExporter;

/**
 * 
 * @author wangzijian
 * 
 */
public class SpringJsonExporter extends JsonExporter {

	private static final long serialVersionUID = 1822290971511957851L;

	private String converterName = "jsonConverter";
	private ApplicationContext applicationContext;

	@Override
	protected Object named(String name) {
		return applicationContext.getBean(name);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		converter = applicationContext.getBean(converterName, JsonConverter.class);
	}

	public void setConverterName(String converterName) {
		this.converterName = converterName;
	}

}
