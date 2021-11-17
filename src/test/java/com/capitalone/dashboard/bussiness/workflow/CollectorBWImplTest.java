package com.capitalone.dashboard.bussiness.workflow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.bussiness.logic.CollectorLogic;
import com.capitalone.dashboard.exception.BuildException;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BuildCollectionRepository;
import com.capitalone.dashboard.repository.BuildComponentRepository;
import com.capitalone.dashboard.repository.BuildJobRepository;
import com.capitalone.dashboard.service.DefinitionService;

@RunWith(MockitoJUnitRunner.class)
public class CollectorBWImplTest {

	@Mock
	private DefinitionService definitionService;
	@Mock
	private CollectorLogic collectorLogic;
	@Mock
	private BuildJobRepository buildJobRepository;
	@Mock
	private BuildComponentRepository buildComponentRepository;
	@Mock
	private BuildCollectionRepository buildCollectionRepository;
	@Mock
	private BuildBW buildBW;

	@InjectMocks
	private CollectorBWImpl collectorBWImpl;

	@Test
	public void testGetAzureCollectorItem() throws Exception {
		// Arrange
		List<VSTSBuildJob> expected = new ArrayList<>();
		VSTSBuildJob job = new VSTSBuildJob();
		expected.add(job);
		when(definitionService.getBuildDefinitions()).thenReturn(expected);
		// Act
		List<VSTSBuildJob> actual = collectorBWImpl.getAzureCollectorItem();
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = BuildException.class)
	public void testGetAzureCollectorItemException() throws Exception {
		// Arrange
		List<VSTSBuildJob> expected = new ArrayList<>();
		when(definitionService.getBuildDefinitions()).thenReturn(expected);
		// Act
		collectorBWImpl.getAzureCollectorItem();
	}

	@Test
	public void testCleanAndUpdateCollectorItems() throws Exception {
		// Arrange
		List<VSTSBuildJob> expected = new ArrayList<>();
		ObjectId collectorId = ObjectId.get();
		when(buildJobRepository.findCollectorItemsByCollectorId(collectorId)).thenReturn(expected);
		List<Component> components = new ArrayList<>();
		when(buildComponentRepository.findByCollectorId(collectorId)).thenReturn(components);
		when(collectorLogic.disableUnconfiguredCollectorItems(collectorId, components, expected)).thenReturn(expected);
		List<Build> builds = new ArrayList<Build>();
		when(buildCollectionRepository.findBuildWithDifferentCollectorItemsId(any())).thenReturn(builds);
		// Act
		collectorBWImpl.cleanAndUpdateCollectorItems(collectorId, expected);
		// Assert
		verify(buildJobRepository, atLeast(2)).findCollectorItemsByCollectorId(collectorId);
		verify(buildJobRepository).delete(expected);
		verify(collectorLogic).disableUnconfiguredCollectorItems(collectorId, components, expected);
		verify(buildJobRepository).save(expected);
		verify(buildCollectionRepository).findBuildWithDifferentCollectorItemsId(any());
		verify(buildCollectionRepository).delete(builds);
	}

	@Test
	public void testCollectInfoFromEnableCollectorItems() throws Exception {
		// Arrange
		ObjectId collectorId = ObjectId.get();
		List<VSTSBuildJob> expected = new ArrayList<>();
		VSTSBuildJob job = new VSTSBuildJob();
		expected.add(job);
		when(buildJobRepository.findEnabledCollectorItems(collectorId)).thenReturn(expected);
		// Act
		collectorBWImpl.collectInfoFromEnableCollectorItems(collectorId);
		// Assert
		verify(buildBW).workflow(job);
		verify(buildJobRepository).findEnabledCollectorItems(collectorId);
		verify(collectorLogic).progressing(any(), anyInt());
		verify(buildJobRepository).save(expected);
	}

	@Test(expected = BuildException.class)
	public void testCollectInfoFromEnableCollectorItemsExceptionWorkFlow() throws Exception {
		// Arrange

		ObjectId collectorId = ObjectId.get();
		List<VSTSBuildJob> expected = new ArrayList<>();
		VSTSBuildJob job = new VSTSBuildJob();
		expected.add(job);
		when(buildJobRepository.findEnabledCollectorItems(collectorId)).thenReturn(expected);
		doThrow(new RuntimeException("Test")).when(buildBW).workflow(job);
		// Act
		collectorBWImpl.collectInfoFromEnableCollectorItems(collectorId);
	}

	@Test(expected = BuildException.class)
	public void testCollectInfoFromEnableCollectorItemsException() throws Exception {
		// Arrange
		ObjectId collectorId = ObjectId.get();
		doThrow(new NullPointerException()).when(buildJobRepository).findEnabledCollectorItems(collectorId);
		// Act
		collectorBWImpl.collectInfoFromEnableCollectorItems(collectorId);
	}

}
