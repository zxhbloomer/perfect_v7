package com.perfect.excel.upload;

/**
 * 异常类
 * @author zxh
 */
public class PerfectExcelException extends RuntimeException {

	private static final long serialVersionUID = 1830974553436749465L;

	public PerfectExcelException() {

	}

	public PerfectExcelException(String message) {
		super(message);
	}

	public PerfectExcelException(Throwable cause) {
		super(cause);
	}

	public PerfectExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public PerfectExcelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
