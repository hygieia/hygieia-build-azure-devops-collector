package com.capitalone.dashboard.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


public class BuildComparable extends Build implements Comparable<BuildComparable> {
	
	private List<WorkItem> workItems = new ArrayList<>();
    private String startedCommit;
    private String branch;
    
	public String getStartedCommit() {
		return startedCommit;
	}

	public void setStartedCommit(String startedCommit) {
		this.startedCommit = startedCommit;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}
	
	public static BuildComparable casting(Build build) {
		BuildComparable comparable = new BuildComparable();
		comparable.setId(build.getId());
		comparable.setCollectorItemId(build.getCollectorItemId());
		comparable.setTimestamp(build.getTimestamp());
		comparable.setNumber(build.getNumber());
		comparable.setBuildUrl(build.getBuildUrl());
		comparable.setStartTime(build.getStartTime());
		comparable.setEndTime(build.getEndTime());
		comparable.setDuration(build.getDuration());
		comparable.setBuildStatus(build.getBuildStatus());
		comparable.setStartedBy(build.getStartedBy());
		comparable.setLog(build.getLog());
		comparable.setCodeRepos(build.getCodeRepos());
		comparable.setSourceChangeSet(build.getSourceChangeSet());
		return comparable;
	}
	
	public static boolean changeTo(BuildComparable build, BuildComparable other) {
		
		if (build.equals(other)){
			
			return build.getStartTime() != other.getStartTime()
					|| !StringUtils.equalsIgnoreCase(build.getBuildStatus().toString(), other.getBuildStatus().toString())
					|| CollectionUtils.isEmpty(other.getSourceChangeSet());
		} else {
			throw new IllegalArgumentException("The elements are not the same");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Build other = (Build) obj;

		return StringUtils.equals(this.getNumber(), other.getNumber());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
		return result;
	}

	@Override
	public int compareTo(BuildComparable o) {
		return Integer.parseInt(this.getNumber()) - Integer.parseInt(o.getNumber());
	}
}
