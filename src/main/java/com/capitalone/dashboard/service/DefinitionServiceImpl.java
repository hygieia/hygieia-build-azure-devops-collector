package com.capitalone.dashboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.adapter.BuildAdapter;
import com.capitalone.dashboard.client.AzureDevOpsClient;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildJob;

@Component
public class DefinitionServiceImpl implements DefinitionService {

	private final AzureDevOpsClient azureDevOpsClient;
	private final BuildAdapter buildAdapter;

	@Autowired
	public DefinitionServiceImpl(AzureDevOpsClient azureDevOpsClient, BuildAdapter buildAdapter) {
		this.azureDevOpsClient = azureDevOpsClient;
		this.buildAdapter = buildAdapter;
	}

	@Override
	public List<VSTSBuildJob> getBuildDefinitions() {

		try {
			List<VSTSBuildDefinition> definitions = azureDevOpsClient.getBuildDefinitions();
			Set<VSTSBuildJob> collectorItems = definitions.stream()
					.map(buildAdapter::convertVSTSBuildDefinitionToVSTSBuildJob).collect(Collectors.toSet());
			return new ArrayList<>(collectorItems);
		} catch (AzureDevOpsApiException e) {
			throw new RuntimeException(e);
		}
	}

}
