package com.capitalone.dashboard.collector;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.bussiness.workflow.CollectorBW;
import com.capitalone.dashboard.model.Configuration;
import com.capitalone.dashboard.model.VSTSBuildCollector;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.BuildCollectorRepository;
import com.capitalone.dashboard.repository.ConfigurationRepository;
import com.capitalone.dashboard.repository.CustomConfigurationRepository;

/**
 * CollectorTask that fetches Build information from Azure DevOps
 */
@Component
public class BuildCollectorTask extends CollectorTask<VSTSBuildCollector> {

	private static final Logger LOG = LoggerFactory.getLogger(BuildCollectorTask.class);

	private final BuildCollectorRepository buildCollectorRepository;
	private final BuildSettings buildSettings;
	private final ConfigurationRepository configurationRepository;
	private final CustomConfigurationRepository customConfigurationRepository;
	private final CollectorBW collectorBW;

	@Autowired
	public BuildCollectorTask(TaskScheduler taskScheduler, BuildCollectorRepository buildCollectorRepository,
			BuildSettings buildSettings, ConfigurationRepository configurationRepository, CollectorBW collectorBW,
			CustomConfigurationRepository customConfigurationRepository) {
		super(taskScheduler, VSTSBuildCollector.NAME);
		this.buildCollectorRepository = buildCollectorRepository;
		this.buildSettings = buildSettings;
		this.configurationRepository = configurationRepository;
		this.collectorBW = collectorBW;
		this.customConfigurationRepository = customConfigurationRepository;
	}

	@Override
	public VSTSBuildCollector getCollector() {

		Configuration collector = configurationRepository.findByCollectorName(VSTSBuildCollector.NAME);
		Configuration general = customConfigurationRepository.findByName(buildSettings.getConfigName());
		Configuration config = general != null ? general : collector;
		if (config != null) {
			config.decryptOrEncrptInfo();
			Set<Map<String, String>> info = config.getInfo();
			buildSettings.setApiTokensConfig(info);
		}

		return VSTSBuildCollector.prototype(buildSettings);
	}

	@Override
	public BaseCollectorRepository<VSTSBuildCollector> getCollectorRepository() {
		return buildCollectorRepository;
	}

	@Override
	public String getCron() {
		return buildSettings.getCron();
	}

	@Override
	public void collect(VSTSBuildCollector collector) {
		try {
			log("Starting...");
			long start = System.currentTimeMillis();
			log("Getting information from azure....");
			List<VSTSBuildJob> azureCollectorItems = collectorBW.getAzureCollectorItem();
			log("Pipelines in azure", start, azureCollectorItems.size());
			log("cleaning and Updating item collectors....");
			collectorBW.cleanAndUpdateCollectorItems(collector.getId(), azureCollectorItems);
			log("Purged and Updated collactorItems", start);
			log("Collecting information from collector items...");
			collectorBW.collectInfoFromEnableCollectorItems(collector.getId());
			log("Finished", start);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
