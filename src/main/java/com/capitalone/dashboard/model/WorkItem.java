package com.capitalone.dashboard.model;

public class WorkItem {
	private String idWorkItem;
	private String url;
	private Fields fields;
	
	public String getIdWorkItem() {
		return idWorkItem;
	}
	
	public void setIdWorkItem(String idWorkItem) {
		this.idWorkItem = idWorkItem;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idWorkItem == null) ? 0 : idWorkItem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WorkItem))
			return false;
		WorkItem other = (WorkItem) obj;
		if (idWorkItem == null) {
			if (other.idWorkItem != null)
				return false;
		} else if (!idWorkItem.equals(other.idWorkItem))
			return false;
		return true;
	}
	
}
