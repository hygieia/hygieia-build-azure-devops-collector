package com.capitalone.dashboard.model;

public class VSTSWorkItem {

	private String id;
	private String url;
	private VSTSFields fields;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public VSTSFields getFields() {
		return fields;
	}

	public void setFields(VSTSFields fields) {
		this.fields = fields;
	}
	
}
