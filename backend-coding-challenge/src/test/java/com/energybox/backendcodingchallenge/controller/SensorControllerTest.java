package com.energybox.backendcodingchallenge.controller;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class SensorControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SensorRepository mockSensorRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void shouldBeAbleToCreateSensor() throws Exception {

		Mockito.when(this.mockSensorRepo.save(Mockito.any(Sensor.class))).then(inv -> {
			Sensor temp = inv.getArgument(0);
			temp.setId(42L);
			return temp;
		});
		
		Sensor sensor = new Sensor();
		sensor.setTypes(new String[] {"Type A", "Type B"});
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/sensors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(sensor)))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(42)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.types[0]", Matchers.is("Type A")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.types[1]", Matchers.is("Type B")));
	}
	
	@Test
	public void shouldBeAbleToQuerySensorByType() throws Exception {
		Mockito.when(this.mockSensorRepo.findByType(Mockito.startsWith("Mock-"))).then(inv -> {
			Sensor temp = new Sensor();
			String type = inv.getArgument(0);
			temp.setId(Long.valueOf(type.replace("Mock-", "")));
			temp.setTypes(new String[] { type });
			return Arrays.asList(temp);
		});
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/sensors?type={0}", "Mock-123")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(123)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].types[0]", Matchers.is("Mock-123")));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/sensors?type={0}", "abc")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(0)));
	}

}
