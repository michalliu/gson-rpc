package com.google.code.gson.rpc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

/**
 * 
 * @author wangzijian
 * 
 */
public interface JsonInvoker {

	Object invoke(URL url, Type returnType) throws IOException;

	Object invoke(URL url, Object object, Type returnType) throws IOException;

}
