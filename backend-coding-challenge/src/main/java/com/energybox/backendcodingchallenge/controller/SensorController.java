package com.energybox.backendcodingchallenge.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.service.GatewayService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping( value = "/sensors" )
public class SensorController {
	
	private GatewayService service;

	public SensorController(GatewayService service) {
		this.service = service;
	}
	
	@ApiOperation( value = "get all sensors that has certain sensor type ", response = Gateway.class )
	@RequestMapping( value = "/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<List<Sensor>> find(@RequestParam("type") String type) {
		List<Sensor> senors = this.service.findAllSensorsByType(type);
		return ResponseEntity.ok().body(senors);
	}
	
	@ApiOperation( value = "create a sensor", response = Gateway.class )
	@RequestMapping( value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Sensor> create(@RequestBody Sensor sensor) {
		Sensor newSensor = this.service.createSensor(sensor);
		return ResponseEntity.ok().body(newSensor);
	}
	
}
