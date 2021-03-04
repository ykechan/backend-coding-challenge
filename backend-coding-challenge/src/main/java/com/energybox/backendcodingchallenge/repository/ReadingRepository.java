package com.energybox.backendcodingchallenge.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.energybox.backendcodingchallenge.domain.Reading;

public interface ReadingRepository extends Neo4jRepository<Reading, Long> {
	
	@Query(" MATCH(r:Reading)-[READING_OF]->(s:Sensor) "
	     + " WHERE ID(s) = $sensorId and r.type = $readingType RETURN r "
		 + " ORDER BY r.timestamp desc LIMIT 1 ")
	public Optional<Reading> findLatestOfType(long sensorId, String readingType);

}
