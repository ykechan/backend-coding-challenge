package com.energybox.backendcodingchallenge.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;

@Service
public class GatewayService {
	
	private GatewayRepository gatewayRepository;
	
	private SensorRepository sensorRepository;
	
	public GatewayService(GatewayRepository gatewayRepository, SensorRepository sensorRepository) {
		this.gatewayRepository = gatewayRepository;
		this.sensorRepository = sensorRepository;
	}
	
	public Optional<Gateway> findById(Long id) {
		return this.gatewayRepository.findById(id);
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
	
}
