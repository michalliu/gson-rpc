package com.google.code.gson.rpc;

/**
 * 
 * @author wangzijian
 * 
 */
class Check {

	private Check() {

	}

	static <T> T notNull(T reference, Object errorMessage) {
		if (reference == null) {
			throw new NullPointerException(String.valueOf(errorMessage));
		}
		return reference;
	}

	static boolean isBlank(String string) {
		int strLen;
		if (string == null || (strLen = string.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(string.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	static boolean isNotBlank(String string) {
		return !isBlank(string);
	}

}
