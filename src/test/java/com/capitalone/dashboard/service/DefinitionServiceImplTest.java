package com.capitalone.dashboard.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.adapter.BuildAdapter;
import com.capitalone.dashboard.client.AzureDevOpsClient;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildJob;

@RunWith(MockitoJUnitRunner.class)
public class DefinitionServiceImplTest {

	@Mock
	private AzureDevOpsClient azureDevOpsClient;
	@Mock
	private BuildAdapter buildAdapter;

	@InjectMocks
	private DefinitionServiceImpl definitionServiceImpl;

	@Test
	public void testGetBuildDefinitions() throws Exception {
		// Arrange
		List<VSTSBuildDefinition> definitions = new ArrayList<>();
		VSTSBuildDefinition definition = new VSTSBuildDefinition();
		definitions.add(definition);
		VSTSBuildJob job = new VSTSBuildJob();
		when(buildAdapter.convertVSTSBuildDefinitionToVSTSBuildJob(definition)).thenReturn(job);
		when(azureDevOpsClient.getBuildDefinitions()).thenReturn(definitions);
		// Act
		List<VSTSBuildJob> actual = definitionServiceImpl.getBuildDefinitions();
		// Assert
		assertTrue(actual.contains(job));
		verify(buildAdapter).convertVSTSBuildDefinitionToVSTSBuildJob(definition);
		verify(azureDevOpsClient).getBuildDefinitions();
	}

	@Test(expected = RuntimeException.class)
	public void testGetBuildDefinitionsException() throws Exception {
		// Arrange
		doThrow(new AzureDevOpsApiException("Fail")).when(azureDevOpsClient).getBuildDefinitions();
		// Act
		definitionServiceImpl.getBuildDefinitions();
	}

}
