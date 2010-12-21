package com.google.code.gson.rpc.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.gson.rpc.Check;
import com.google.code.gson.rpc.JsonConverter;
import com.google.code.gson.rpc.JsonExporter;

/**
 * 
 * @author wangzijian
 * 
 */
public class SpringJsonExporter extends JsonExporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringJsonExporter.class);
	
	private static final long serialVersionUID = 7155345174761467268L;
	private String converterName;
	private ApplicationContext applicationContext;

	@Override
	protected Object named(String name) {
		return applicationContext.getBean(name);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		applicationContext = applicationContextFrom(config);
		if (Check.isNotBlank(converterName)) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Converter Name: " + converterName);
			}
			setConverter(applicationContext.getBean(converterName, JsonConverter.class));
		}
	}

	private ApplicationContext applicationContextFrom(ServletConfig config) {
		return (ApplicationContext) config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	public void setConverterName(String converterName) {
		this.converterName = converterName;
	}

}
