package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Component;

public interface BuildComponentRepository extends CrudRepository<Component, ObjectId> {

	@Query(value = "{ 'collectorItems.Build' : { $exists: true, $ne: [] }, 'collectorItems.Build.collectorId': ?0 }")
	List<Component> findByCollectorId(ObjectId collectorId);

}
