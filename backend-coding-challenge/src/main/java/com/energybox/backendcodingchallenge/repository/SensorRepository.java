package com.energybox.backendcodingchallenge.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.energybox.backendcodingchallenge.domain.Sensor;

public interface SensorRepository extends Neo4jRepository<Sensor, Long> {
	
	@Query(" MATCH (s:Sensor)-[:CONNECTED_TO]->(g:Gateway{ id: $gatewayId }) RETURN s")
	public List<Sensor> findByGatewayId(@Param("gatewayId") Long gatewayId);
	
	@Query("MATCH (s:Sensor) WHERE $type IN s.types RETURN s")
	public List<Sensor> findByType(@Param("type") String type);

}
