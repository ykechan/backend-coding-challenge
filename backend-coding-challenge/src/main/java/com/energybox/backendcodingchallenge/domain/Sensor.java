package com.energybox.backendcodingchallenge.domain;


import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Sensor {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String[] types;

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}


}
