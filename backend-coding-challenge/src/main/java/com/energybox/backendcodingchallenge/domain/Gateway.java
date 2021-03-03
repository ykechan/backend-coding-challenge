package com.energybox.backendcodingchallenge.domain;

import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Gateway {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Relationship(type="CONNECTED_TO")
	public Set<Sensor> sensors;
	
}
