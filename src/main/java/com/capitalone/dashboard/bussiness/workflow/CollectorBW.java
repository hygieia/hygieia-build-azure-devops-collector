package com.capitalone.dashboard.bussiness.workflow;

import java.util.List;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.exception.BuildException;
import com.capitalone.dashboard.model.VSTSBuildJob;

public interface CollectorBW {

	public List<VSTSBuildJob> getAzureCollectorItem() throws BuildException;

	public void cleanAndUpdateCollectorItems(ObjectId collectorId, List<VSTSBuildJob> azureCollectorItems);

	public void collectInfoFromEnableCollectorItems(ObjectId collectorId) throws BuildException;

}
