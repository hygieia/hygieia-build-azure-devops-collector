package com.capitalone.dashboard.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.config.ApiTokenConfig;

/**
 * Bean to hold settings specific to the collector.
 */
@Component
@ConfigurationProperties(prefix = "azure-build")
public class BuildSettings extends ApiTokenConfig {

	private String cron;
	private String protocol;
	private String port;
	private String host;
	private String account;
	private String projectId;

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String project) {
		this.projectId = project;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
