package org.lybf.custom.lang;

public class CustomException extends Exception {
	private String message;
	private int code;
    public CustomException() {}

    public CustomException(String message) {
		this(message, null);
		this.message = message;
	}

    public CustomException(String message, Throwable cause) {
		super(message, cause);
		this.message  = message;
	}

    public CustomException(Throwable cause) {
		super(cause);
	}

    protected CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}



}
