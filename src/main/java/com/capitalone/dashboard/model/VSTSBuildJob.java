package com.capitalone.dashboard.model;

import org.apache.commons.lang3.StringUtils;

/**
 * CollectorItem extension to store the instance, build job and build url.
 */
public class VSTSBuildJob extends JobCollectorItem {

	protected static final String JOB_ID = "jobID";

	public String getJobId() {
		return (String) getOptions().get(JOB_ID);
	}

	public void setJobId(String jobId) {
		getOptions().put(JOB_ID, jobId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		VSTSBuildJob other = (VSTSBuildJob) obj;

		return StringUtils.equals(this.getJobId(), other.getJobId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
		return result;
	}

}
