package com.capitalone.dashboard.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AzureDevOpsApiExceptionTest {

	@Test
	public void testAzureDevOpsApiExceptionStringThrowable() throws Exception {
		// Arrange
		String expected = "fail";
		Throwable cause = new Throwable();
		// Act
		AzureDevOpsApiException exception = new AzureDevOpsApiException(expected, cause);
		// Assert
		assertEquals(expected, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}

	@Test
	public void testAzureDevOpsApiExceptionString() throws Exception {
		// Arrange
		String expected = "fail";
		// Act
		AzureDevOpsApiException exception = new AzureDevOpsApiException(expected);
		// Assert
		assertEquals(expected, exception.getMessage());
	}

	@Test
	public void testAzureDevOpsApiExceptionThrowable() throws Exception {
		// Arrange
		Throwable expected = new Throwable();
		// Act
		AzureDevOpsApiException exception = new AzureDevOpsApiException(expected);
		// Assert
		assertEquals(expected, exception.getCause());
	}

}
