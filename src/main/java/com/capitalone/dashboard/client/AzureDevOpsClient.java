package com.capitalone.dashboard.client;

import java.util.List;

import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildWorkItems;
import com.capitalone.dashboard.model.VSTSWorkItem;

public interface AzureDevOpsClient {

	List<VSTSBuildDefinition> getBuildDefinitions() throws AzureDevOpsApiException;

	List<VSTSBuild> getBuilds(String buildDefinitionId) throws AzureDevOpsApiException;

	VSTSBuild getBuild(String buildId) throws AzureDevOpsApiException;

	List<VSTSBuildChanges> getBuildChanges(String buildId) throws AzureDevOpsApiException;

	List<VSTSBuildWorkItems> getBuildWorkItemRef(String repositoryId, String scmRevisionNumber)
			throws AzureDevOpsApiException;

	List<VSTSWorkItem> getBuildWorkItem(List<String> workItemsIds) throws AzureDevOpsApiException;
}
