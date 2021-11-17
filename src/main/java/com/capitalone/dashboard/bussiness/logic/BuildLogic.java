package com.capitalone.dashboard.bussiness.logic;

import java.util.List;

import com.capitalone.dashboard.model.BuildComparable;

public interface BuildLogic {

	public List<BuildComparable> getBuildsPending(List<BuildComparable> currents, List<BuildComparable> azure);

	public List<BuildComparable> buildPropagation(List<BuildComparable> builds);

}
