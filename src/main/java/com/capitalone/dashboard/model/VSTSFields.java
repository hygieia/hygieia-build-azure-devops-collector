package com.capitalone.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VSTSFields {

	@JsonProperty("System.AreaPath")
	private String systemAreaPath;
	@JsonProperty("System.TeamProject")
	private String systemTeamProject;
	@JsonProperty("System.IterationPath")
	private String systemIterationPath;
	@JsonProperty("System.WorkItemType")
	private String systemWorkItemType;
	@JsonProperty("System.State")
	private String systemState;
	@JsonProperty("System.CreatedDate")
	private String systemCreatedDate;

	public String getSystemAreaPath() {
		return systemAreaPath;
	}

	public void setSystemAreaPath(String systemAreaPath) {
		this.systemAreaPath = systemAreaPath;
	}

	public String getSystemTeamProject() {
		return systemTeamProject;
	}

	public void setSystemTeamProject(String systemTeamProject) {
		this.systemTeamProject = systemTeamProject;
	}

	public String getSystemIterationPath() {
		return systemIterationPath;
	}

	public void setSystemIterationPath(String systemIterationPath) {
		this.systemIterationPath = systemIterationPath;
	}

	public String getSystemWorkItemType() {
		return systemWorkItemType;
	}

	public void setSystemWorkItemType(String systemWorkItemType) {
		this.systemWorkItemType = systemWorkItemType;
	}

	public String getSystemState() {
		return systemState;
	}

	public void setSystemState(String systemState) {
		this.systemState = systemState;
	}

	public String getSystemCreatedDate() {
		return systemCreatedDate;
	}

	public void setSystemCreatedDate(String systemCreatedDate) {
		this.systemCreatedDate = systemCreatedDate;
	}

}