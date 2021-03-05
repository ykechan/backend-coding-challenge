package com.energybox.backendcodingchallenge.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.energybox.backendcodingchallenge.domain.Reading;
import com.energybox.backendcodingchallenge.domain.Sensor;

@SpringBootTest
public class ReadingRepositoryTest {
	
	@Autowired
	private SensorRepository sensorRepository;
	
	@Autowired
	private ReadingRepository readingRepository;
	
	@Test
	public void shouldBeAbleToCreateAndQueryReading() {
		String uniqueType = UUID.randomUUID().toString();
		
		Sensor sensor = new Sensor();
		sensor.setTypes(new String[] {uniqueType, "test"});
		sensor = this.sensorRepository.save(sensor);
		
		Reading reading = new Reading();
		reading.setTimestamp(ZonedDateTime.now());
		reading.setType(uniqueType);
		reading.setValue(String.valueOf(2.718281828));
		
		reading = this.readingRepository.save(reading);
		reading.associateWith(sensor);
		reading = this.readingRepository.save(reading);
		
		Reading later = new Reading();
		later.setTimestamp(ZonedDateTime.now());
		later.setType(uniqueType);
		later.setValue(String.valueOf(3.1415926));
		
		later = this.readingRepository.save(later);
		later.associateWith(sensor);
		later = this.readingRepository.save(later);
		
		Optional<Reading> result = this.readingRepository.findLatestOfType(sensor.getId(), uniqueType);
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(uniqueType, result.get().getType());
		Assertions.assertEquals("3.1415926", result.get().getValue());
	}

}
