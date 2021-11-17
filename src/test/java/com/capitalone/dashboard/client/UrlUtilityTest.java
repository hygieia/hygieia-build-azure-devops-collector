package com.capitalone.dashboard.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.util.BuildUtil;

@RunWith(MockitoJUnitRunner.class)
public class UrlUtilityTest {

	@Mock
	private BuildSettings collectorSettings;
	@Mock
	private BuildUtil buildUtil;

	@InjectMocks
	private UrlUtility urlUtility;

	@Test
	public void testGetBaseHost() throws Exception {
		// Arrange
		String expected = "host";
		when(collectorSettings.getHost()).thenReturn(expected);
		// Act
		String actual = urlUtility.getBaseHost();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testGetBaseHostDefault() throws Exception {
		// Act
		String actual = urlUtility.getBaseHost();
		// Assert
		assertEquals("dev.azure.com", actual);
	}

	@Test
	public void testGetAccountName() throws Exception {

		// Arrange
		String expected = "Test";
		when(collectorSettings.getAccount()).thenReturn(expected);
		// Act
		String actual = urlUtility.getAccountName();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testGetApiBuildDefinitions() throws Exception {
		// Act
		URI actual = urlUtility.getApiBuildDefinitions("token");
		// Assert
		assertTrue(actual.toString().matches(".*token.*"));
		assertTrue(actual.toString().matches(".*definitions.*"));
	}

	@Test
	public void testGetApiBuildsbyBuildDefinition() throws Exception {
		// Act
		URI actual = urlUtility.getApiBuildsbyBuildDefinition("123", "token");
		// Assert
		assertTrue(actual.toString().matches(".*123.*"));
		assertTrue(actual.toString().matches(".*token.*"));
	}

	@Test
	public void testGetApiBuildsbyBuildDefinitionWithPortAndProtocol() throws Exception {
		// Arrange
		when(collectorSettings.getPort()).thenReturn("80");
		when(collectorSettings.getProtocol()).thenReturn("https");
		// Act
		URI actual = urlUtility.getApiBuildsbyBuildDefinition("123", "token");
		// Assert
		assertTrue(actual.toString().matches(".*123.*"));
		assertTrue(actual.toString().matches(".*token.*"));
		assertTrue(actual.toString().matches(".*80.*"));
		assertTrue(actual.toString().matches(".*https.*"));
	}

	@Test
	public void testGetApiBuild() throws Exception {
		// Act
		URI actual = urlUtility.getApiBuild("123");
		// Assert
		assertTrue(actual.toString().matches(".*123.*"));
	}

	@Test
	public void testGetApiBuildChangesbyBuildId() throws Exception {
		// Act
		URI actual = urlUtility.getApiBuildChangesbyBuildId("123", "token");
		// Assert
		assertTrue(actual.toString().matches(".*123.*"));
		assertTrue(actual.toString().matches(".*token.*"));
	}

	@Test
	public void testGetApiBuildWorkItemsByBuildId() throws Exception {
		// Act
		URI actual = urlUtility.getArtifactUriQuery();
		// Assert
		assertTrue(actual.toString().matches(".*artifacturiquery.*"));
		assertTrue(actual.toString().matches(".*wit.*"));
	}

	@Test
	public void testGetCommitUrl() throws Exception {
		// Act
		String actual = urlUtility.getCommitUrl("repo", "hash");
		// Assert
		assertTrue(actual.matches(".*repo.*"));
		assertTrue(actual.matches(".*hash.*"));
	}

	@Test
	public void testGetApiWorkItems() throws Exception {
		// Act
		URI actual = urlUtility.getApiWorkItems(Arrays.asList("123", "567"));
		// Assert
		assertTrue(actual.toString().matches(".*123.*"));
		assertTrue(actual.toString().matches(".*567.*"));
	}

}
