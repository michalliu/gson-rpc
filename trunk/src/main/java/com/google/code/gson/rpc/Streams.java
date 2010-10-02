package com.google.code.gson.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author wangzijian
 * 
 */
public class Streams {

	private static final int BUF_SIZE = 0x1000; // 4K

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
		Check.notNull(inputStream, "inputStream");
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
		Check.notNull(content, "content");
		Check.notNull(outputStream, "outputStream");
		copy(new ByteArrayInputStream(content.toString().getBytes(Default.CHARSET)), outputStream);
	}

	private static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	private static long copy(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}
}
