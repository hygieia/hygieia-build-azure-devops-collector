package com.capitalone.dashboard.adapter;

import java.util.List;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildJob;

public interface BuildAdapter {

	public VSTSBuildJob convertVSTSBuildDefinitionToVSTSBuildJob(VSTSBuildDefinition definition);

	public BuildComparable convertVSTSBuildToBuild(VSTSBuildJob collectorItem, VSTSBuild vstsbuild);

	public SCM convertVSTSBuildChangesToSCM(VSTSBuildChanges change);

	public List<BuildComparable> convertBuildListToComparableList(List<Build> builds);

}
