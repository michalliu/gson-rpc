package com.google.code.gson.rpc;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.ByteStreams.copy;
import static com.google.common.io.ByteStreams.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author wangzijian
 * 
 */
public class Streams {

	private Streams() {

	}

	/**
	 * Read inputStream as String type
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream inputStream) throws IOException {
		return new String(toByteArray(inputStream), Default.CHARSET);
	}

	/**
	 * Flush String type to outputStream
	 * 
	 * @param content
	 * @param outputStream
	 * @throws IOException
	 */
	public static void write(Object content, OutputStream outputStream) throws IOException {
		checkNotNull(content, "content");
		copy(new ByteArrayInputStream(content.toString().getBytes(Default.CHARSET)), outputStream);
	}
}
