package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Configuration;

public interface CustomConfigurationRepository extends CrudRepository<Configuration, ObjectId> {

	@Query(value = "{ 'name' : ?0 }")
	Configuration findByName(String name);

}