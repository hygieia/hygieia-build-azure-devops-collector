package com.capitalone.dashboard.bussiness.workflow;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.bussiness.logic.BuildLogic;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.CollectionError;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BuildCollectionRepository;
import com.capitalone.dashboard.service.BuildService;
import com.capitalone.dashboard.util.BuildUtil;

@Component
public class BuildBWImpl implements BuildBW {
	
	private static final Log LOG = LogFactory.getLog(BuildBWImpl.class);


	private final BuildLogic buildLogic;
	private final BuildService buildService;
	private final BuildCollectionRepository buildCollectionRepository;
	private final BuildUtil buildUtil;

	@Autowired
	public BuildBWImpl(BuildLogic buildLogic, BuildService buildService,
			BuildCollectionRepository buildCollectionRepository, BuildUtil buildUtil) {
		super();
		this.buildLogic = buildLogic;
		this.buildService = buildService;
		this.buildCollectionRepository = buildCollectionRepository;
		this.buildUtil = buildUtil;
	}

	@Override
	public void workflow(VSTSBuildJob collectorItem){
		try {
			collectorItem.getErrors().clear();
			List<BuildComparable> comparableAzure = buildService.getBuilds(collectorItem);
			buildService.cleanBuilds(collectorItem, comparableAzure);
			List<Build> currents = buildCollectionRepository.findBuildsBycollectorItemId(collectorItem.getId());
			List<BuildComparable> comparableDb = buildService.getBuildComparable(currents);
			List<BuildComparable> comparablePending = buildLogic.getBuildsPending(comparableDb, comparableAzure);
			List<BuildComparable> comparableNews = buildUtil.getDifferentElementsBetweenList(comparableAzure,
					comparableDb);

			Collection<BuildComparable> newAndPending = CollectionUtils.union(comparableNews, comparablePending);

			newAndPending.stream().map(buildService::addChangeSets)
					.collect(Collectors.groupingByConcurrent(BuildComparable::getStartedCommit)).values().stream()
					.map(buildLogic::buildPropagation).flatMap(List::stream).map(buildService::addWorkItems)
					.forEach(buildService::saveBuild);

		} catch (Exception e) {
			CollectionError error = new CollectionError(e.getLocalizedMessage(), e.getMessage());
			collectorItem.getErrors().add(error);
			LOG.debug(String.format(
					"Processing the %s with Id: %s  in updateCollectorItem - Error information %s",
					collectorItem.getJobName(), collectorItem.getJobId(), e.getMessage()), e);
		} finally {
			collectorItem.setLastUpdated(System.currentTimeMillis());
		}
	}

}
