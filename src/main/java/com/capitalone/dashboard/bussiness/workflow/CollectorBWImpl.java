package com.capitalone.dashboard.bussiness.workflow;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.capitalone.dashboard.bussiness.logic.CollectorLogic;
import com.capitalone.dashboard.exception.BuildException;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BuildCollectionRepository;
import com.capitalone.dashboard.repository.BuildComponentRepository;
import com.capitalone.dashboard.repository.BuildJobRepository;
import com.capitalone.dashboard.service.DefinitionService;


@org.springframework.stereotype.Component
public class CollectorBWImpl implements CollectorBW {

	private static final Log LOG = LogFactory.getLog(CollectorBWImpl.class);

	private final DefinitionService definitionService;
	private final CollectorLogic collectorLogic;
	private final BuildJobRepository buildJobRepository;
	private final BuildComponentRepository buildComponentRepository;
	private final BuildCollectionRepository buildCollectionRepository;
	private final BuildBW buildBW;

	@Autowired
	public CollectorBWImpl(DefinitionService definitionService, CollectorLogic collectorLogic,
			BuildJobRepository buildJobRepository, BuildComponentRepository buildComponentRepository,
			BuildCollectionRepository buildCollectionRepository, BuildBW buildBW) {
		super();
		this.definitionService = definitionService;
		this.collectorLogic = collectorLogic;
		this.buildJobRepository = buildJobRepository;
		this.buildComponentRepository = buildComponentRepository;
		this.buildCollectionRepository = buildCollectionRepository;
		this.buildBW = buildBW;
	}

	@Override
	public List<VSTSBuildJob> getAzureCollectorItem() throws BuildException {
		List<VSTSBuildJob> collectorItems = definitionService.getBuildDefinitions();
		if (collectorItems.isEmpty()) {
			throw new BuildException("No information found in azure devops to create or update collector items");
		}
		return collectorItems;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void cleanAndUpdateCollectorItems(ObjectId collectorId, List<VSTSBuildJob> azureCollectorItems) {

		List<VSTSBuildJob> currentCollectorItems = buildJobRepository.findCollectorItemsByCollectorId(collectorId);
		
		LOG.info(String.format("Total current collectorItem before cleaning %d", currentCollectorItems.size()));

		Collection<VSTSBuildJob> toRemove = CollectionUtils.subtract(currentCollectorItems, azureCollectorItems);

		buildJobRepository.delete(toRemove);

		LOG.info(String.format("Total deleted collectorItems %d", toRemove.size()));

		List<VSTSBuildJob> dbCollectorItems = buildJobRepository.findCollectorItemsByCollectorId(collectorId);

		LOG.info(String.format("Total new collectorItems  %d", azureCollectorItems.size() - dbCollectorItems.size()));

		List<Component> components = buildComponentRepository
				.findByCollectorId(collectorId);
		
		List<VSTSBuildJob> currents = collectorLogic.disableUnconfiguredCollectorItems(collectorId, components,
				dbCollectorItems);
		
		List<VSTSBuildJob> collectorItems = collectorLogic.updateOrCreateCollectorItems(collectorId,
				azureCollectorItems, currents);
		
		buildJobRepository.save(collectorItems);
		
		LOG.info(String.format("Total CollectorItems in DB %d", collectorItems.size()));

		List<ObjectId> enableCollectorItemIds = currents.stream().filter(VSTSBuildJob::isEnabled)
				.map(VSTSBuildJob::getId).collect(Collectors.toList());

		List<Build> buildsToDelete = buildCollectionRepository
				.findBuildWithDifferentCollectorItemsId(enableCollectorItemIds);

		buildCollectionRepository.delete(buildsToDelete);
		
		LOG.info(String.format("Total of build deleted %d", buildsToDelete.size()));
	}

	@Override
	public void collectInfoFromEnableCollectorItems(ObjectId collectorId) throws BuildException {

		try {

			AtomicInteger progress = new AtomicInteger();

			List<VSTSBuildJob> enables = buildJobRepository.findEnabledCollectorItems(collectorId);

			int total = enables.size();
			
			LOG.info(String.format("Total collectorItems configured %d", total));

			enables.stream().peek(item -> collectorLogic.progressing(progress, total)).forEach(buildBW::workflow);

			buildJobRepository.save(enables);

		} catch (Exception e) {
			throw new BuildException(String.format("workflow fail: %s", e.getMessage()), e);
		}
	}

}
