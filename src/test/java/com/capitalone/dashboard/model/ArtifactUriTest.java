package com.capitalone.dashboard.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactUriTest {

	@Test
	public void testSetArtifactUris() {
		// Arrange
		ArtifactUri artifactUri = new ArtifactUri("");
		artifactUri.setArtifactUris(Arrays.asList("test"));
		// Act
		List<String> actual = artifactUri.getArtifactUris();
		// Assert
		assertTrue(actual.contains("test"));
	}

	@Test
	public void testGetArtifactUris() throws Exception {
		// Arrange
		ArtifactUri artifactUri = new ArtifactUri("test");
		// Act
		List<String> actual = artifactUri.getArtifactUris();
		// Assert
		assertTrue(actual.contains("test"));
	}
}
