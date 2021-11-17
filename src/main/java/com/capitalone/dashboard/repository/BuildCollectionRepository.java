package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capitalone.dashboard.model.Build;

public interface BuildCollectionRepository extends CrudRepository<Build, ObjectId> {

	@Query(value = "{ 'collectorItemId' : { $nin: ?0 } }")
	List<Build> findBuildWithDifferentCollectorItemsId(List<ObjectId> collectorItemIds);

	List<Build> findBuildsBycollectorItemId(ObjectId collectorItemId);

	@Query(value = "{ 'collectorItemId' : ?0 , 'number' : { $nin: ?1 } }")
	List<Build> findBuildsOlderByCollectorItemId(ObjectId collectorItemId, List<String> numbers);

}
