package com.capitalone.dashboard.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;

@RunWith(MockitoJUnitRunner.class)
public class GenericClientTest {

	@Mock
	private RestOperations restOperations;

	@Mock
	private BuildSettings collectorSettings;

	@Mock
	private RestOperationsSupplier restOperationsSupplier;

	private GenericClient genericClient;

	@Before
	public void setup() {
		when(restOperationsSupplier.get()).thenReturn(restOperations);
		genericClient = new GenericClient(collectorSettings, restOperationsSupplier);
	}

	@Test
	public void testMakeGetRestCall() throws Exception {
		// Arrange
		when(collectorSettings.getApiToken()).thenReturn("test");
		URI url = URI.create("http://test");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "test");
		headers.set(HttpHeaders.ACCEPT, "application/json");
		String reponse = "Hello World!";
		ResponseEntity<String> value = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class)).thenReturn(value);
		// Act
		ResponseEntity<String> actual = genericClient.makeGetRestCall(url, String.class);
		// Assert
		verify(restOperations).exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
		assertEquals(actual.getBody(), reponse);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testMakeGetRestCallException() throws Exception {
		// Arrange
		when(collectorSettings.getApiToken()).thenReturn("test");
		URI url = URI.create("http://test");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "test");
		headers.set(HttpHeaders.ACCEPT, "application/json");
		doThrow(new NullPointerException()).when(restOperations).exchange(url, HttpMethod.GET,
				new HttpEntity<>(headers), String.class);
		// Act
		genericClient.makeGetRestCall(url, String.class);
	}

	@Test
	public void testMakePostRestCall() throws Exception {
		// Arrange
		String data = "test";
		when(collectorSettings.getApiToken()).thenReturn("test");
		URI url = URI.create("http://test");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "test");
		headers.set(HttpHeaders.ACCEPT, "application/json");
		String reponse = "Hello World!";
		ResponseEntity<String> value = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(restOperations.exchange(url, HttpMethod.POST, new HttpEntity<>(data, headers), String.class))
				.thenReturn(value);
		// Act
		ResponseEntity<String> actual = genericClient.makePostRestCall(url, data, String.class);
		// Assert
		verify(restOperations).exchange(url, HttpMethod.POST, new HttpEntity<>(data, headers), String.class);
		assertEquals(actual.getBody(), reponse);
	}

	@Test(expected = AzureDevOpsApiException.class)
	public void testMakePostRestCallException() throws Exception {
		// Arrange
		String data = "test";
		when(collectorSettings.getApiToken()).thenReturn("test");
		URI url = URI.create("http://test");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "test");
		headers.set(HttpHeaders.ACCEPT, "application/json");
		doThrow(new NullPointerException()).when(restOperations).exchange(url, HttpMethod.POST,
				new HttpEntity<>(data, headers), String.class);
		// Act
		genericClient.makePostRestCall(url, data, String.class);
	}

	@Test
	public void testGetCollectorSettings() throws Exception {
		// Act
		BuildSettings actual = genericClient.getCollectorSettings();
		// Assert
		assertEquals(collectorSettings, actual);
	}

	@Test
	public void testGetRestOperations() throws Exception {
		// Act
		RestOperations actual = genericClient.getRestOperations();
		// Assert
		assertEquals(restOperations, actual);
	}

}
