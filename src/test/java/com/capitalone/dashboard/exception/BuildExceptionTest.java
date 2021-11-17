package com.capitalone.dashboard.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuildExceptionTest {

	@Test
	public void testBuildExceptionString() throws Exception {
		// Arrange
		String expected = "fail";
		// Act
		BuildException exception = new BuildException(expected);
		// Assert
		assertEquals(expected, exception.getMessage());
	}

	@Test
	public void testBuildExceptionThrowable() throws Exception {
		// Arrange
		Throwable expected = new Throwable();
		// Act
		BuildException exception = new BuildException(expected);
		// Assert
		assertEquals(expected, exception.getCause());
	}

	@Test
	public void testBuildExceptionStringThrowable() throws Exception {
		// Arrange
		String expected = "fail";
		Throwable cause = new Throwable();
		// Act
		BuildException exception = new BuildException(expected, cause);
		// Assert
		assertEquals(expected, exception.getMessage());
		assertEquals(cause, exception.getCause());

	}

}
