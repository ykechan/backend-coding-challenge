package com.energybox.backendcodingchallenge.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Reading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.ReadingRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.google.common.base.Functions;

@Service
public class GatewayService {
	
	private GatewayRepository gatewayRepository;
	
	private SensorRepository sensorRepository;
	
	private ReadingRepository readingRepository;
	
	public GatewayService(GatewayRepository gatewayRepository, 
			SensorRepository sensorRepository,
			ReadingRepository readingRepository) {
		this.gatewayRepository = gatewayRepository;
		this.sensorRepository = sensorRepository;
		this.readingRepository = readingRepository;
	}
	
	public Optional<Gateway> findGatewayById(Long id) {
		return this.gatewayRepository.findById(id);
	}
	
	public Optional<Sensor> findSensorById(Long id) {
		return this.sensorRepository.findById(id);
	}

	public List<Gateway> findAllGateways() {
		return this.gatewayRepository.findAll();
	}
	
	public List<Sensor> findAllSensors() {
		return this.sensorRepository.findAll();
	}
	
	public List<Sensor> findAllSensorsByType(String sensorType) {
		return this.sensorRepository.findByType(sensorType);
	}
	
	public List<Gateway> findAllGatewaysHaving(String sensorType) {
		return this.gatewayRepository.findHavingSensorType(sensorType);
	}
	
	public Gateway createGateway() {
		return this.gatewayRepository.save(new Gateway());
	}
	
	public Sensor createSensor(Sensor sensor) {
		return this.sensorRepository.save(sensor);
	}
	
	public Optional<Gateway> connectSensorToGateway(long gatewayId, long sensorId) {
		Gateway gateway = this.gatewayRepository.findById(gatewayId).orElse(null);
		if(gateway == null) {
			return Optional.empty();
		}
				
		return this.sensorRepository.findById(sensorId)			
			.map(s -> {
				gateway.connectedTo(s);
				return this.gatewayRepository.save(gateway);
			});
	}
	
	public Optional<Reading> createReading(long sensorId, Reading reading) {
		Sensor sensor = this.findSensorById(sensorId).orElse(null);
		if(sensor == null) {
			return Optional.empty();
		}
		
		String[] sensorTypes = sensor.getTypes() == null ? new String[0] : sensor.getTypes();
		if(reading.getType() == null || Arrays.asList(sensorTypes).indexOf(reading.getType()) < 0){
			throw new IllegalArgumentException("Sensor does not have " + reading.getType() + " readings.");
		}
		
		Reading entity = new Reading();
		entity.setType(reading.getType());
		entity.setValue(reading.getValue());
		entity.setTimestamp(ZonedDateTime.now());
		entity.associateWith(sensor);
		return Optional.of(this.readingRepository.save(entity));
	}
	
	public Optional<Map<String, Reading>> getLatestReadings(long sensorId) {
		Sensor sensor = this.findSensorById(sensorId).orElse(null);
		if(sensor == null) {
			return Optional.empty();
		}
		
		Map<String, Reading> readings = Arrays.stream(sensor.getTypes() == null ? new String[0] : sensor.getTypes())
			.map(t -> this.readingRepository.findLatestOfType(sensorId, t))
			.map(t -> t.orElse(null))
			.filter(t -> t != null)
			.collect(Collectors.toMap(t -> t.getType(), Function.identity()));
		return Optional.of(readings);
	}
}
