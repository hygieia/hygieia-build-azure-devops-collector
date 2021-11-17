package com.capitalone.dashboard.exception;

public class AzureDevOpsApiException extends Exception {

	private static final long serialVersionUID = -7334635587580123963L;

	public AzureDevOpsApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public AzureDevOpsApiException(String message) {
		super(message);
	}

	public AzureDevOpsApiException(Throwable cause) {
		super(cause);
	}
}
