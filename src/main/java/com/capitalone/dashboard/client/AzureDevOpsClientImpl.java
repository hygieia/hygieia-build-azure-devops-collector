package com.capitalone.dashboard.client;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.ArtifactUri;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildChangesResponse;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildDefinitionResponse;
import com.capitalone.dashboard.model.VSTSBuildResponse;
import com.capitalone.dashboard.model.VSTSBuildWorkItems;
import com.capitalone.dashboard.model.VSTSBuildWorkItemsResponse;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.VSTSWorkItemsResponse;
import com.capitalone.dashboard.util.BuildUtil;


/**
 * Azure DevOps Client implementation
 */
@Component
public class AzureDevOpsClientImpl extends GenericClient implements AzureDevOpsClient {

	private static final String INITIAL_VALUE = "0";

	private final UrlUtility urlUtility;
	private final BuildUtil buildUtil;

	@Autowired
	public AzureDevOpsClientImpl(RestOperationsSupplier restOperationsSupplier, BuildSettings settings,
			UrlUtility urlUtility, BuildUtil buildUtil) {
		super(settings, restOperationsSupplier);
		this.urlUtility = urlUtility;
		this.buildUtil = buildUtil;
	}

	@Override
	public List<VSTSBuildDefinition> getBuildDefinitions() throws AzureDevOpsApiException {
		List<VSTSBuildDefinition> buildDefinitions = new ArrayList<>();
		URI apiUrl;
		String continuationToken = Instant.EPOCH.toString();

		try {
			while (StringUtils.isNotEmpty(continuationToken)) {

				apiUrl = urlUtility.getApiBuildDefinitions(continuationToken);
				ResponseEntity<VSTSBuildDefinitionResponse> response = makeGetRestCall(apiUrl,
						VSTSBuildDefinitionResponse.class);

				continuationToken = buildUtil.extractedContinuationToken(response);

				buildDefinitions.addAll(response.getBody().getValue());
			}
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}

		return buildDefinitions;
	}

	@Override
	public List<VSTSBuild> getBuilds(String buildDefinitionId) throws AzureDevOpsApiException {
		List<VSTSBuild> builds = new ArrayList<>();
		URI apiUrl;
		String continuationToken = INITIAL_VALUE;

		try {
			while (StringUtils.isNotEmpty(continuationToken)) {

				apiUrl = urlUtility.getApiBuildsbyBuildDefinition(buildDefinitionId, continuationToken);
				ResponseEntity<VSTSBuildResponse> response = makeGetRestCall(apiUrl, VSTSBuildResponse.class);

				continuationToken = buildUtil.extractedContinuationToken(response);

				builds.addAll(response.getBody().getValue());
			}
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}

		return builds;
	}

	@Override
	public VSTSBuild getBuild(String buildId) throws AzureDevOpsApiException {

		try {
			URI apiUrl = urlUtility.getApiBuild(buildId);
			ResponseEntity<VSTSBuild> response = makeGetRestCall(apiUrl, VSTSBuild.class);
			VSTSBuild build = response.getBody();
			return build;
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}

	}

	@Override
	public List<VSTSBuildChanges> getBuildChanges(String buildId) throws AzureDevOpsApiException {
		List<VSTSBuildChanges> buildChanges = new ArrayList<>();
		URI apiUrl;
		String continuationToken = INITIAL_VALUE;

		try {
			while (StringUtils.isNotEmpty(continuationToken)) {

				apiUrl = urlUtility.getApiBuildChangesbyBuildId(buildId, continuationToken);
				ResponseEntity<VSTSBuildChangesResponse> response = makeGetRestCall(apiUrl,
						VSTSBuildChangesResponse.class);

				continuationToken = buildUtil.extractedContinuationToken(response);

				buildChanges.addAll(response.getBody().getValue());
			}
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}

		return buildChanges;
	}
	
	@Override
	public List<VSTSBuildWorkItems> getBuildWorkItemRef(String repositoryId, String scmRevisionNumber)
			throws AzureDevOpsApiException {
		List<VSTSBuildWorkItems> buildWorkItems = new ArrayList<>();
		try {
			String commitUrl = urlUtility.getCommitUrl(repositoryId, scmRevisionNumber);
			ArtifactUri artifactUri = new ArtifactUri(commitUrl);
			URI apiUrl = urlUtility.getArtifactUriQuery();
			ResponseEntity<VSTSBuildWorkItemsResponse> response = makePostRestCall(apiUrl, artifactUri,
					VSTSBuildWorkItemsResponse.class);
			Map<String, List<VSTSBuildWorkItems>> map = response.getBody().getArtifactUrisQueryResult();
			buildWorkItems.addAll(map.get(commitUrl));
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}
		return buildWorkItems;
	}
	
	@Override
	public List<VSTSWorkItem> getBuildWorkItem(List<String> workItemsIds) throws AzureDevOpsApiException {
		List<VSTSWorkItem> workItems = new ArrayList<>();
		URI apiUrl;
		try {
			apiUrl = urlUtility.getApiWorkItems(workItemsIds);
			ResponseEntity<VSTSWorkItemsResponse> response = makeGetRestCall(apiUrl,
					VSTSWorkItemsResponse.class);
			workItems.addAll(response.getBody().getValue());
		} catch (Exception e) {
			throw new AzureDevOpsApiException(e);
		}
		return workItems;
	}
}
