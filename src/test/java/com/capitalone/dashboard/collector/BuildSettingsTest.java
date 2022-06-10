package com.capitalone.dashboard.collector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuildSettingsTest {

	@InjectMocks
	private BuildSettings buildSettings;

	@Test
	public void testSetCron() throws Exception {
		// Arrange
		String expected = "cron";
		// Act
		buildSettings.setCron(expected);
		String actual = buildSettings.getCron();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testSetProtocol() throws Exception {
		// Arrange
		String expected = "cron";
		// Act
		buildSettings.setProtocol(expected);
		String actual = buildSettings.getProtocol();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testSetHost() throws Exception {
		// Arrange
		String expected = "host";
		// Act
		buildSettings.setHost(expected);
		String actual = buildSettings.getHost();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testSetAccount() throws Exception {
		// Arrange
		String expected = "Account";
		// Act
		buildSettings.setAccount(expected);
		String actual = buildSettings.getAccount();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testSetProjectId() throws Exception {
		// Arrange
		String expected = "Project";
		// Act
		buildSettings.setProjectId(expected);
		String actual = buildSettings.getProjectId();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testSetPort() throws Exception {
		// Arrange
		String expected = "Port";
		// Act
		buildSettings.setPort(expected);
		String actual = buildSettings.getPort();
		// Assert
		assertEquals(expected, actual);
	}

}
