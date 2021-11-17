package com.capitalone.dashboard.model;

import java.util.List;
import java.util.Map;

public class VSTSBuildWorkItemsResponse {
	
	private Map<String, List<VSTSBuildWorkItems>> artifactUrisQueryResult;

	public Map<String, List<VSTSBuildWorkItems>> getArtifactUrisQueryResult() {
		return artifactUrisQueryResult;
	}

	public void setArtifactUrisQueryResult(Map<String, List<VSTSBuildWorkItems>> artifactUrisQueryResult) {
		this.artifactUrisQueryResult = artifactUrisQueryResult;
	}

}
