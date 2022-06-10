package com.capitalone.dashboard.model;

import java.util.List;

public class VSTSWorkItemsResponse {

	private long count;
	private List<VSTSWorkItem> value;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<VSTSWorkItem> getValue() {
		return value;
	}

	public void setValue(List<VSTSWorkItem> value) {
		this.value = value;
	}

}
