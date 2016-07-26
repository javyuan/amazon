package com.javyuan.amazon.model.common;

/**
 * SQLBuildery异常类
 * @author javyuan 
 * 2016年7月25日
 */
public class SQLBuilderException extends Exception {

	private static final long serialVersionUID = 1L;

	public SQLBuilderException() {
		super();
	}

	public SQLBuilderException(String string) {
		super(string);
	}

	public SQLBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLBuilderException(Throwable cause) {
		super(cause);
	}

	protected SQLBuilderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
