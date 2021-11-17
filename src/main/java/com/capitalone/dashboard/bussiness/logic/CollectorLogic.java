package com.capitalone.dashboard.bussiness.logic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.VSTSBuildJob;

public interface CollectorLogic {

	public List<VSTSBuildJob> disableUnconfiguredCollectorItems(ObjectId collectorId, List<Component> components,
			List<VSTSBuildJob> collectorItems);

	public List<VSTSBuildJob> updateOrCreateCollectorItems(ObjectId collectorId, List<VSTSBuildJob> azureCollectorItems,
			List<VSTSBuildJob> dbCollectorItems);

	public void progressing(AtomicInteger progress, int total);

}
