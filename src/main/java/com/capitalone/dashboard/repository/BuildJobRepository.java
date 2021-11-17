package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.capitalone.dashboard.model.VSTSBuildJob;

public interface BuildJobRepository extends JobRepository<VSTSBuildJob> {

	List<VSTSBuildJob> findCollectorItemsByCollectorId(ObjectId collectorId);

	@Query(value = "{ 'collectorId' : ?0, enabled: true}")
	List<VSTSBuildJob> findEnabledCollectorItems(ObjectId collectorId);

}
