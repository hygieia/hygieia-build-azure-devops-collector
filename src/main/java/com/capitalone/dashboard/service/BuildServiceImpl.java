package com.capitalone.dashboard.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.adapter.BuildAdapter;
import com.capitalone.dashboard.adapter.WorkItemAdapter;
import com.capitalone.dashboard.client.AzureDevOpsClient;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.model.VSTSBuildWorkItems;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.WorkItem;
import com.capitalone.dashboard.repository.BuildCollectionRepository;

@Component
public class BuildServiceImpl implements BuildService {
	
	private static final List<String> WORKITEMTYPE = Arrays.asList("User Story", "Habilitador");
	
	private static final Log LOG = LogFactory.getLog(BuildServiceImpl.class);

	private final AzureDevOpsClient azureDevOpsClient;
	private final BuildAdapter buildAdapter;
	private final WorkItemAdapter workItemAdapter;
	private final BuildCollectionRepository buildCollectionRepository;

	@Autowired
	public BuildServiceImpl(AzureDevOpsClient azureDevOpsClient, BuildAdapter buildAdapter,
			BuildCollectionRepository buildCollectionRepository, WorkItemAdapter workItemAdapter) {
		this.azureDevOpsClient = azureDevOpsClient;
		this.buildAdapter = buildAdapter;
		this.buildCollectionRepository = buildCollectionRepository;
		this.workItemAdapter = workItemAdapter;
	}

	@Override
	public void cleanBuilds(VSTSBuildJob collectorItem, List<BuildComparable> azureBuilds) {

		List<String> buildIds = azureBuilds.stream().map(Build::getNumber).collect(Collectors.toList());
		List<Build> oldBuild = buildCollectionRepository.findBuildsOlderByCollectorItemId(collectorItem.getId(),
				buildIds);
		buildCollectionRepository.delete(oldBuild);
	}

	@Override
	public void saveBuild(Build build) {
		try {
			buildCollectionRepository.save(build);
		} catch (Exception e) {
			LOG.error(String.format(
					"Processing the build  %s with Collector Item Id: %s  in saveBuild"
							+ "has a commit size of %d - Error information %s",
					build.getNumber(), build.getCollectorItemId(), build.getSourceChangeSet().size(), e.getMessage()),
					e);
		}
	}

	@Override
	public BuildComparable addChangeSets(BuildComparable build) {
		try {
			List<VSTSBuildChanges> vstsChanges = azureDevOpsClient.getBuildChanges(build.getNumber());
			List<SCM> changes = vstsChanges.stream().map(buildAdapter::convertVSTSBuildChangesToSCM)
					.collect(Collectors.toList());
			build.setSourceChangeSet(changes);
			return build;
		} catch (AzureDevOpsApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<BuildComparable> getBuilds(VSTSBuildJob collectorItem) {
		try {
			List<VSTSBuild> vstsbuilds = azureDevOpsClient.getBuilds(collectorItem.getJobId());
			List<BuildComparable> builds = vstsbuilds.stream()
					.filter(build -> StringUtils.isNotEmpty(build.getSourceVersion()))
					.map(build -> buildAdapter.convertVSTSBuildToBuild(collectorItem, build))
					.collect(Collectors.toList());
			return builds;
		} catch (AzureDevOpsApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<BuildComparable> getBuildComparable(List<Build> builds) {
		return buildAdapter.convertBuildListToComparableList(builds);
	}

	@Override
	public BuildComparable addWorkItems(BuildComparable build) {
		try {
			SCM startedCommit = build.getSourceChangeSet().stream().filter(
					commit -> StringUtils.equalsIgnoreCase(commit.getScmRevisionNumber(), build.getStartedCommit()))
					.findAny().orElse(null);
			if(startedCommit != null) {
				String repositoryId = StringUtils.substringBetween(startedCommit.getScmUrl(), "/repositories/", "/commits/");
				String scmRevisionNumber  = startedCommit.getScmRevisionNumber();
				List<VSTSBuildWorkItems> workItemsRef = azureDevOpsClient.getBuildWorkItemRef(repositoryId, scmRevisionNumber);
				if(CollectionUtils.isNotEmpty(workItemsRef)) {
					List<String> workItemIds = workItemsRef.stream().map(VSTSBuildWorkItems::getId).collect(Collectors.toList());
					List<VSTSWorkItem> vstsWorkItems = azureDevOpsClient.getBuildWorkItem(workItemIds).stream()
							.filter(workItem -> workItem.getFields() != null
									&& WORKITEMTYPE.contains(workItem.getFields().getSystemWorkItemType()))
							.collect(Collectors.toList());
					List<WorkItem> workItems = vstsWorkItems.stream().map(workItemAdapter::getWorkItem).collect(Collectors.toList());
					build.setWorkItems(workItems);
				}
			}

			return build;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
