package com.capitalone.dashboard.bussiness.logic;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.VSTSBuildJob;

@org.springframework.stereotype.Component
public class CollectorLogicImpl implements CollectorLogic {

	private static final int PERCENTAGE = 100;
	private static final int MODULO = 50;
	private static final Log LOG = LogFactory.getLog(CollectorLogicImpl.class);

	@Override
	public List<VSTSBuildJob> disableUnconfiguredCollectorItems(ObjectId collectorId, List<Component> components,
			List<VSTSBuildJob> collectorItems) {

		Set<ObjectId> configuredCollectorItemIds = components.stream()
				.map(component -> component.getCollectorItems().get(CollectorType.Build)).flatMap(List::stream)
				.filter(collectorItem -> collectorItem.getCollectorId().equals(collectorId))
				.map(collectorItem -> collectorItem.getId()).collect(Collectors.toSet());

		collectorItems.stream().forEach(
				collectorItem -> collectorItem.setEnabled(configuredCollectorItemIds.contains(collectorItem.getId())));

		return collectorItems;
	}

	@Override
	public List<VSTSBuildJob> updateOrCreateCollectorItems(ObjectId collectorId, List<VSTSBuildJob> azureCollectorItems,
			List<VSTSBuildJob> dbCollectorItems) {

		return azureCollectorItems.stream()
				.map(item -> updateItem(collectorId, dbCollectorItems, item))
				.collect(Collectors.toList());
	}

	private VSTSBuildJob updateItem(ObjectId collectorId, List<VSTSBuildJob> dbCollectorItems,
			VSTSBuildJob collectorItemInAzure) {
		if (dbCollectorItems.contains(collectorItemInAzure)) {
			int position = dbCollectorItems.indexOf(collectorItemInAzure);
			VSTSBuildJob update = dbCollectorItems.get(position);
			update.setInstanceUrl(collectorItemInAzure.getInstanceUrl());
			update.setJobUrl(collectorItemInAzure.getJobUrl());
			update.setJobName(collectorItemInAzure.getJobName());
			update.setDescription(collectorItemInAzure.getDescription());
			update.setNiceName(collectorItemInAzure.getNiceName());
			return update;
		} else {
			collectorItemInAzure.setCollectorId(collectorId);
			return collectorItemInAzure;
		}
	}

	@Override
	public void progressing(AtomicInteger progress, int total) {
		int value = progress.incrementAndGet();
		if (value % MODULO == 0) {
			int percentage = (value * PERCENTAGE) / total;
			LOG.info(String.format("%d%% - %d processor elements of %d ", percentage, value, total));
		}

	}

}
