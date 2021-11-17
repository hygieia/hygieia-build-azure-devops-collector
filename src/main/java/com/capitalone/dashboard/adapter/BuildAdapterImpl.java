package com.capitalone.dashboard.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.util.BuildUtil;

@Component
public class BuildAdapterImpl implements BuildAdapter {

	private final BuildUtil buildUtil;

	@Autowired
	public BuildAdapterImpl(BuildUtil buildUtil) {
		this.buildUtil = buildUtil;
	}

	@Override
	public VSTSBuildJob convertVSTSBuildDefinitionToVSTSBuildJob(VSTSBuildDefinition definition) {
		VSTSBuildJob vSTSBuildJob = new VSTSBuildJob();
		vSTSBuildJob.setInstanceUrl(definition.getUrl());
		vSTSBuildJob.setJobName(definition.getName());
		vSTSBuildJob.setJobUrl(definition.getUrl());
		vSTSBuildJob.setJobId(definition.getId());
		vSTSBuildJob.setDescription(definition.getName());
		vSTSBuildJob.setNiceName(definition.getName());
		return vSTSBuildJob;
	}

	@Override
	public BuildComparable convertVSTSBuildToBuild(VSTSBuildJob collectorItem, VSTSBuild vstsbuild) {
		BuildComparable build = new BuildComparable();
		build.setCollectorItemId(collectorItem.getId());
		build.setTimestamp(System.currentTimeMillis());
		build.setNumber(vstsbuild.getId());
		build.setStartedCommit(vstsbuild.getSourceVersion());
		build.setBranch(vstsbuild.getSourceBranch());
		build.setBuildUrl(vstsbuild.getLinks().getWeb().getHref());

		long startTime = new DateTime(vstsbuild.getStartTime()).getMillis();
		long finishTime = new DateTime(vstsbuild.getFinishTime()).getMillis();

		build.setStartTime(startTime);
		build.setEndTime(finishTime);
		build.setDuration(build.getEndTime() - build.getStartTime());

		build.setBuildStatus(buildUtil.getBuildStatus(vstsbuild.getResult()));
		build.setStartedBy(vstsbuild.getRequestedFor().getDisplayName());
		build.setLog(vstsbuild.getBuildNumber());

		return build;
	}

	@Override
	public SCM convertVSTSBuildChangesToSCM(VSTSBuildChanges change) {

		SCM scm = new SCM();
		long timestamp = new DateTime(change.getTimestamp()).getMillis();
		scm.setScmAuthor(change.getAuthor().getDisplayName());
		scm.setScmCommitLog(change.getMessage());
		scm.setScmCommitTimestamp(timestamp);
		scm.setScmRevisionNumber(change.getId());
		scm.setScmUrl(change.getLocation());
		return scm;
	}

	@Override
	public List<BuildComparable> convertBuildListToComparableList(List<Build> builds) {

		return builds.stream().map(BuildComparable::casting).collect(Collectors.toList());
	}

}
