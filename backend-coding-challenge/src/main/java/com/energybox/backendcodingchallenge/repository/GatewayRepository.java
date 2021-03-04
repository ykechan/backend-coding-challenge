package com.energybox.backendcodingchallenge.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.energybox.backendcodingchallenge.domain.Gateway;

public interface GatewayRepository extends Neo4jRepository<Gateway, Long> {
	
	@Query(" MATCH (g:Gateway)-[CONNECTED_TO]->(s:Sensor) WHERE $sensorType IN s.types RETURN g ")
	public List<Gateway> findHavingSensorType(String sensorType);

}
