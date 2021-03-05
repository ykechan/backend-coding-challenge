package com.energybox.backendcodingchallenge.repository;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

@SpringBootTest
public class SensorRepositoryTest {
	
	@Autowired
	private GatewayRepository gatewayRepository;
	
	@Autowired
	private SensorRepository sensorRepository;
	
	@Test
	public void shouldBeAbleToCreateAndFindSensorByTypeAndGatewayId() {
		String uniqueType = UUID.randomUUID().toString();
		
		Sensor newSensor = new Sensor();
		newSensor.setTypes(new String[] {uniqueType, "test"});
		
		Sensor sensor = this.sensorRepository.save(newSensor);
		
		Gateway gateway = this.gatewayRepository.save(new Gateway());
		gateway.connectedTo(sensor);
		
		this.gatewayRepository.save(gateway);
		
		List<Sensor> byType = this.sensorRepository.findByType(uniqueType);
		Assertions.assertEquals(1, byType.size());
		Assertions.assertEquals(sensor.getId(), byType.get(0).getId());
		
		Assertions.assertTrue(this.sensorRepository.findByType(UUID.randomUUID().toString()).isEmpty());
	}

}
