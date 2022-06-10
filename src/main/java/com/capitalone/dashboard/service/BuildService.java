package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.VSTSBuildJob;

public interface BuildService {

	public void cleanBuilds(VSTSBuildJob collectorItem, List<BuildComparable> azureBuilds);

	public BuildComparable addChangeSets(BuildComparable build);
	
	public BuildComparable addWorkItems(BuildComparable build);

	public List<BuildComparable> getBuilds(VSTSBuildJob collectorItem);

	public List<BuildComparable> getBuildComparable(List<Build> builds);
	
	public void saveBuild(Build build);
}
