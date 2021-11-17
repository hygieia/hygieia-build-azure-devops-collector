package com.capitalone.dashboard.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;


@Component
public class GenericClient {

	private final RestOperations restOperations;

	private final BuildSettings collectorSettings;

	@Autowired
	public GenericClient(BuildSettings collectorSettings, RestOperationsSupplier restOperationsSupplier) {

		this.collectorSettings = collectorSettings;
		this.restOperations = restOperationsSupplier.get();
	}

	@Retryable(maxAttempts = 3, value = {
			AzureDevOpsApiException.class }, backoff = @Backoff(delay = 1_000, multiplier = 2, maxDelay = 900_000))
	public <T> ResponseEntity<T> makeGetRestCall(URI url, Class<T> responseType) throws AzureDevOpsApiException {
		try {

			String apiToken = collectorSettings.getApiToken();
			HttpHeaders headers = createHeaders(apiToken);
			return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType);

		} catch (Exception e) {
			throw new AzureDevOpsApiException(url.toString(), e);
		}
	}
	
	@Retryable(maxAttempts = 3, value = {
			AzureDevOpsApiException.class }, backoff = @Backoff(delay = 1_000, multiplier = 2, maxDelay = 900_000))
	public <T, K> ResponseEntity<T> makePostRestCall(URI url, K requestType, Class<T> responseType)
			throws AzureDevOpsApiException {
		try {

			String apiToken = collectorSettings.getApiToken();
			HttpHeaders headers = createHeaders(apiToken);
			return restOperations.exchange(url, HttpMethod.POST, new HttpEntity<>(requestType, headers), responseType);

		} catch (Exception e) {
			throw new AzureDevOpsApiException(url.toString(), e);
		}
	}
	
	public HttpHeaders createHeaders(final String authHeader) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, authHeader);
		headers.set(HttpHeaders.ACCEPT, "application/json");
		return headers;
	}

	public BuildSettings getCollectorSettings() {
		return collectorSettings;
	}

	public RestOperations getRestOperations() {
		return restOperations;
	}

}
