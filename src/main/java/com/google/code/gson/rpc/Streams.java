package com.google.code.gson.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wangzijian
 * 
 */
class Streams {

	private static final Logger LOGGER = LoggerFactory.getLogger(Streams.class);
	
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
	static String read(InputStream inputStream) throws IOException {
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
	static void write(Object content, OutputStream outputStream) throws IOException {
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
		close(from);
		flush(to);
		close(to);
		return total;
	}

	private static void flush(Flushable flushable) {
		try {
			flushable.flush();
		} catch (IOException e) {
			LOGGER.error("Flush error", e);
		}
	}

	private static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			LOGGER.error("Close error", e);
		}
	}
}
