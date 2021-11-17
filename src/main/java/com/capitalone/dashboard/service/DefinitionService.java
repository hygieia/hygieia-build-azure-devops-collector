package com.capitalone.dashboard.service;

import java.util.List;

import com.capitalone.dashboard.model.VSTSBuildJob;

public interface DefinitionService {

	public List<VSTSBuildJob> getBuildDefinitions();

}
