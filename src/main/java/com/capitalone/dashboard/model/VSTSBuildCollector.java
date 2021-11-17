package com.capitalone.dashboard.model;

import java.util.HashMap;
import java.util.Map;

import com.capitalone.dashboard.collector.BuildSettings;

/**
 * Extension of Collector that stores current build server configuration.
 */
public class VSTSBuildCollector extends Collector {

	public static final String NAME = "VSTSBuild";
	private static final String HOST = "host";
	private static final String ACCOUNT = "account";
	private static final String PROJECT = "project";

	public static VSTSBuildCollector prototype(BuildSettings settings) {
		VSTSBuildCollector protoType = new VSTSBuildCollector();
		protoType.setName(NAME);
		protoType.setCollectorType(CollectorType.Build);
		protoType.setOnline(true);
		protoType.setEnabled(true);

		Map<String, Object> options = new HashMap<>();
		options.put(HOST, settings.getHost());
		options.put(ACCOUNT, settings.getAccount());
		options.put(PROJECT, settings.getProjectId());
		protoType.setAllFields(options);
		protoType.setUniqueFields(options);
		return protoType;
	}
}
