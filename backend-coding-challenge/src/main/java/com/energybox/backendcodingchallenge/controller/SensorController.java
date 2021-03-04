package com.energybox.backendcodingchallenge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energybox.backendcodingchallenge.domain.Reading;
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
	
	@ApiOperation( value = "get all sensors that has certain sensor type ", response = List.class )
	@RequestMapping( value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> find(@RequestParam("type") String type) {
		List<Sensor> senors = this.service.findAllSensorsByType(type);
		return ResponseEntity.ok().body(senors);
	}
	
	@ApiOperation( value = "create a sensor", response = Sensor.class )
	@RequestMapping( value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Sensor> create(@RequestBody Sensor sensor) {
		Sensor newSensor = this.service.createSensor(sensor);
		return new ResponseEntity<>(newSensor, HttpStatus.CREATED);
	}
	
	@ApiOperation( value = "get latest reading values of a sensor", response = Sensor.class )
	@RequestMapping( value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Reading>> getReading(@PathVariable("id") String sensorId) {
		try {
			return ResponseEntity.of(this.service.getLatestReadings(Long.valueOf(sensorId)));
		}catch(NumberFormatException ex){
			return ResponseEntity.notFound().build();
		}
	}
	
	@ApiOperation( value = "put a reading value to a sensor", response = Sensor.class )
	@RequestMapping( value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Reading> reading(@PathVariable("id") String sensorId, @RequestBody Reading reading) {
		try {
			return ResponseEntity.of(this.service.createReading(Long.valueOf(sensorId), reading));
		}catch(NumberFormatException ex){
			return ResponseEntity.notFound().build();
		}
	}
	
}
