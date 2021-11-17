package com.capitalone.dashboard.exception;

public class BuildException extends Exception {

	private static final long serialVersionUID = 6056858738790614791L;

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}
}
