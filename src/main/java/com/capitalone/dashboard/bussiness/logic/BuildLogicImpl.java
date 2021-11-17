package com.capitalone.dashboard.bussiness.logic;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.SCM;

@Component
public class BuildLogicImpl implements BuildLogic {

	private static final int MIN_SIZE = 1;

	@Override
	public List<BuildComparable> getBuildsPending(List<BuildComparable> currents, List<BuildComparable> azure) {

		return azure.stream()
				.filter(currents::contains)
				.collect(Collectors.toConcurrentMap(build -> build, build -> currents.get(currents.indexOf(build)),
						(existing, replacement) -> existing))
				.entrySet().stream().filter(entry -> BuildComparable.changeTo(entry.getKey(), entry.getValue()))
				.map(Map.Entry::getValue).collect(Collectors.toList());
	}

	@Override
	public List<BuildComparable> buildPropagation(List<BuildComparable> builds) {

		if (builds.size() > MIN_SIZE) {

			List<SCM> commits = builds.stream().map(Build::getSourceChangeSet).flatMap(List::stream)
					.collect(Collectors
							.toCollection(() -> new TreeSet<>(Comparator.comparing(SCM::getScmRevisionNumber))))
					.stream().collect(Collectors.toList());
			
			builds.stream().forEach(build -> build.setSourceChangeSet(commits));
		}

		return builds;
	}

}
