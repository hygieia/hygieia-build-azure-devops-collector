package com.capitalone.dashboard.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
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

@RunWith(MockitoJUnitRunner.class)
public class AzureDevOpsClientImplTest {

	@Mock
	private UrlUtility urlUtility;

	@Mock
	private BuildUtil buildUtil;

	@Mock
	private RestOperations restOperations;

	@Mock
	private BuildSettings collectorSettings;

	@Mock
	private RestOperationsSupplier restOperationsSupplier;

	private AzureDevOpsClientImpl azureDevOpsClientImpl;

	@Before
	public void initTest() {
		when(restOperationsSupplier.get()).thenReturn(restOperations);
		azureDevOpsClientImpl = new AzureDevOpsClientImpl(restOperationsSupplier, collectorSettings, urlUtility,
				buildUtil);
	}

	@Test
	public void testGetBuildDefinitions() throws Exception {

		// Arrange
		VSTSBuildDefinitionResponse data = new VSTSBuildDefinitionResponse();
		List<VSTSBuildDefinition> expected = new ArrayList<>();
		data.setValue(expected);
		ResponseEntity<VSTSBuildDefinitionResponse> response = new ResponseEntity<VSTSBuildDefinitionResponse>(data,
				HttpStatus.ACCEPTED);
		URI Uri = URI.create("test");
		when(urlUtility.getApiBuildDefinitions(anyString())).thenReturn(Uri);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.GET),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSBuildDefinitionResponse.class))).thenReturn(response);
		// Act
		List<VSTSBuildDefinition> actual = azureDevOpsClientImpl.getBuildDefinitions();
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildDefinitionsException() throws Exception {

		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getApiBuildDefinitions(anyString());
		// Act
		azureDevOpsClientImpl.getBuildDefinitions();
	}

	@Test
	public void testGetBuilds() throws Exception {
		// Arrange
		VSTSBuildResponse data = new VSTSBuildResponse();
		List<VSTSBuild> expected = new ArrayList<>();
		data.setValue(expected);
		ResponseEntity<VSTSBuildResponse> response = new ResponseEntity<VSTSBuildResponse>(data, HttpStatus.ACCEPTED);
		URI Uri = URI.create("test");
		when(urlUtility.getApiBuildsbyBuildDefinition(anyString(), anyString())).thenReturn(Uri);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.GET),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSBuildResponse.class))).thenReturn(response);
		// Act
		List<VSTSBuild> actual = azureDevOpsClientImpl.getBuilds("123");
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildsException() throws Exception {
		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getApiBuildsbyBuildDefinition(anyString(), anyString());
		// Act
		azureDevOpsClientImpl.getBuilds("123");

	}

	@Test
	public void testGetBuild() throws Exception {
		// Arrange
		VSTSBuild expected = new VSTSBuild();
		ResponseEntity<VSTSBuild> response = new ResponseEntity<VSTSBuild>(expected, HttpStatus.ACCEPTED);
		URI Uri = URI.create("test");
		when(urlUtility.getApiBuild(anyString())).thenReturn(Uri);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.GET),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSBuild.class))).thenReturn(response);
		// Act
		VSTSBuild actual = azureDevOpsClientImpl.getBuild("123");
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildException() throws Exception {
		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getApiBuild(anyString());
		// Act
		azureDevOpsClientImpl.getBuild("123");
	}

	@Test
	public void testGetBuildChanges() throws Exception {
		// Arrange
		VSTSBuildChangesResponse data = new VSTSBuildChangesResponse();
		List<VSTSBuildChanges> expected = new ArrayList<>();
		data.setValue(expected);
		ResponseEntity<VSTSBuildChangesResponse> response = new ResponseEntity<VSTSBuildChangesResponse>(data,
				HttpStatus.ACCEPTED);
		URI Uri = URI.create("test");
		when(urlUtility.getApiBuildChangesbyBuildId(anyString(), anyString())).thenReturn(Uri);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.GET),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSBuildChangesResponse.class))).thenReturn(response);
		// Act
		List<VSTSBuildChanges> actual = azureDevOpsClientImpl.getBuildChanges("123");
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildChangesException() throws Exception {
		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getApiBuildChangesbyBuildId(anyString(), anyString());
		// Act
		azureDevOpsClientImpl.getBuildChanges("123");
	}

	@Test
	public void testGetBuildWorkItemRef() throws Exception {
		// Arrange
		String key = "key";
		URI Uri = URI.create("test");
		when(urlUtility.getArtifactUriQuery()).thenReturn(Uri);
		when(urlUtility.getCommitUrl(anyString(), anyString())).thenReturn(key);
		VSTSBuildWorkItemsResponse data = new VSTSBuildWorkItemsResponse();
		List<VSTSBuildWorkItems> expected = new ArrayList<>();
		Map<String, List<VSTSBuildWorkItems>> value = new HashedMap<>();
		value.put(key, expected);
		data.setArtifactUrisQueryResult(value);
		ResponseEntity<VSTSBuildWorkItemsResponse> response = new ResponseEntity<VSTSBuildWorkItemsResponse>(data,
				HttpStatus.ACCEPTED);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.POST),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSBuildWorkItemsResponse.class))).thenReturn(response);
		// Act
		List<VSTSBuildWorkItems> actual = azureDevOpsClientImpl.getBuildWorkItemRef("123", "123");
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildWorkItemRefException() throws Exception {
		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getArtifactUriQuery();
		// Act
		azureDevOpsClientImpl.getBuildWorkItemRef("123", "123");
	}

	@Test
	public void testGetBuildWorkItem() throws Exception {
		// Arrange
		URI Uri = URI.create("test");
		when(urlUtility.getApiWorkItems(anyListOf(String.class))).thenReturn(Uri);

		VSTSWorkItemsResponse data = new VSTSWorkItemsResponse();
		List<VSTSWorkItem> expected = new ArrayList<>();
		data.setValue(expected);
		ResponseEntity<VSTSWorkItemsResponse> response = new ResponseEntity<VSTSWorkItemsResponse>(data,
				HttpStatus.ACCEPTED);
		when(restOperations.exchange(Matchers.isA(URI.class), Matchers.eq(HttpMethod.GET),
				Matchers.isA(HttpEntity.class), Matchers.eq(VSTSWorkItemsResponse.class))).thenReturn(response);

		List<String> ids = new ArrayList<String>();
		// Act
		List<VSTSWorkItem> actual = azureDevOpsClientImpl.getBuildWorkItem(ids);
		// Assert
		assertEquals(expected, actual);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testGetBuildWorkItemException() throws Exception {
		// Arrange
		doThrow(new NullPointerException()).when(urlUtility).getApiWorkItems(anyListOf(String.class));
		List<String> ids = new ArrayList<String>();
		// Act
		azureDevOpsClientImpl.getBuildWorkItem(ids);
	}

}
