package com.capitalone.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VSTSBuild {

	private String startTime;
	private String finishTime;
	private String status;
	private String result;
	private RequestedBuild requestedFor;
	private String buildNumber;
	private LinksBuild links;
	private String id;
	private String sourceVersion;
	private String sourceBranch;

	public String getSourceVersion() {
		return sourceVersion;
	}

	public void setSourceVersion(String sourceVersion) {

		this.sourceVersion = sourceVersion;
	}

	public String getSourceBranch() {
		return sourceBranch;
	}

	public void setSourceBranch(String sourceBranch) {
		this.sourceBranch = sourceBranch;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public RequestedBuild getRequestedFor() {
		return requestedFor;
	}

	public void setRequestedFor(RequestedBuild requestedFor) {
		this.requestedFor = requestedFor;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	@JsonProperty("_links")
	public LinksBuild getLinks() {
		return links;
	}

	@JsonProperty("_links")
	public void setLinks(LinksBuild links) {
		this.links = links;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
