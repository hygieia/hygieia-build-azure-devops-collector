package com.capitalone.dashboard.model;

import java.util.Arrays;
import java.util.List;

public class ArtifactUri {
	
	List<String> artifactUris;

	public ArtifactUri(String commitUrl) {
		this.artifactUris = Arrays.asList(commitUrl);
	}

	public List<String> getArtifactUris() {
		return artifactUris;
	}

	public void setArtifactUris(List<String> artifactUris) {
		this.artifactUris = artifactUris;
	}
}
