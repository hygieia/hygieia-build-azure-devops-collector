package com.capitalone.dashboard.config;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ApiTokenConfig {

	private static AtomicInteger iterator = new AtomicInteger(-1);

	private List<String> apiTokens;

	private String configName;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public List<String> getApiTokens() {
		return apiTokens;
	}

	public void setApiTokens(List<String> apiTokens) {
		List<String> tokens = apiTokens.stream().map(x -> getEncodeToken("hygieia", x)).collect(Collectors.toList());
		this.apiTokens = tokens;
	}

	public void setApiTokensConfig(Set<Map<String, String>> data) {
		List<String> tokens = data.stream().map(x -> getEncodeToken(x.get("userName"), x.get("password")))
				.collect(Collectors.toList());
		this.apiTokens = tokens;
	}

	private String getEncodeToken(String userName, String password) {
		Base64.Encoder encoder = Base64.getEncoder();
		return "Basic " + encoder.encodeToString(String.join(":", userName, password).getBytes());
	}

	public String getApiToken() {
		if (apiTokens.isEmpty()) {
			throw new RuntimeException("There are no configured tokens");
		} else {
			int index = iterator.updateAndGet(i -> (i >= (apiTokens.size() - 1) ? 0 : (i + 1)));
			String token = apiTokens.get(index);
			return token;
		}
	}

}
