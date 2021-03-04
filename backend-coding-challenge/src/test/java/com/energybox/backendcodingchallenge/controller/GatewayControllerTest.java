package com.energybox.backendcodingchallenge.controller;

import java.util.LinkedHashSet;
import java.util.Optional;

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
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class GatewayControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GatewayRepository mockGatewayRepo;
	
	@MockBean
	private SensorRepository mockSensorRepo;
	
	@Test
	public void shouldBeAbleToCreateAGateway() throws Exception {
		Mockito.when(this.mockGatewayRepo.save(Mockito.any(Gateway.class))).then(inv -> {
			Gateway temp = new Gateway();
			temp.setId(42L);
			return temp;
		});
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/gateways").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(42)));
	}
	
	@Test
	public void shouldBeAbleToRetrieveAGatewayWithSensors() throws Exception {
		Mockito.when(this.mockGatewayRepo.findById(Mockito.eq(42L))).then(inv -> {
			Gateway temp = new Gateway();
			temp.setId(42L);
			temp.sensors = new LinkedHashSet<>();
			
			Sensor sensor0 = new Sensor();
			sensor0.setId(43L);
			temp.sensors.add(sensor0);
			
			Sensor sensor1 = new Sensor();
			sensor1.setId(44L);
			temp.sensors.add(sensor1);
			return Optional.of(temp);
		});
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/gateways/42").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(42)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[0].id", Matchers.is(43)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[1].id", Matchers.is(44)));
	}
	
	@Test
	public void shouldBeAbleToAssignASensorToGateway() throws Exception {
		Mockito.when(this.mockGatewayRepo.findById(Mockito.eq(42L))).then(inv -> {
			Gateway temp = new Gateway();
			temp.setId(42L);
			temp.sensors = new LinkedHashSet<>();
			
			Sensor sensor0 = new Sensor();
			sensor0.setId(43L);
			temp.sensors.add(sensor0);
			
			Sensor sensor1 = new Sensor();
			sensor1.setId(44L);
			temp.sensors.add(sensor1);
			return Optional.of(temp);
		});
		
		Mockito.when(this.mockSensorRepo.findById(Mockito.eq(77L))).then(inv -> {
			Sensor sensor2 = new Sensor();
			sensor2.setId(77L);
			sensor2.setTypes(new String[] {"mock"});
			return Optional.of(sensor2);
		});
		
		Mockito.when(this.mockGatewayRepo.save(Mockito.any(Gateway.class))).then(inv -> inv.getArgument(0));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/gateways/42?sensorId={0}", 77).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(42)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[0].id", Matchers.is(43)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[1].id", Matchers.is(44)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[2].id", Matchers.is(77)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sensors[2].types[0]", Matchers.is("mock")));
	}
	
	@Test
	public void shouldReturn404WhenGatewayNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/gateways/42").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenGatewayIdIsInvalid() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/gateways/abc").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenSensorNotFound() throws Exception {
		Mockito.when(this.mockGatewayRepo.findById(Mockito.eq(42L))).then(inv -> {
			Gateway temp = new Gateway();
			temp.setId(42L);
			temp.sensors = new LinkedHashSet<>();
			
			return Optional.of(temp);
		});
		Mockito.when(this.mockGatewayRepo.save(Mockito.any(Gateway.class))).then(inv -> inv.getArgument(0));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/gateways/42?sensorId={0}", 77).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenSensorIdIsInvalid() throws Exception {
		Mockito.when(this.mockGatewayRepo.findById(Mockito.eq(42L))).then(inv -> {
			Gateway temp = new Gateway();
			temp.setId(42L);
			temp.sensors = new LinkedHashSet<>();
			
			return Optional.of(temp);
		});
		Mockito.when(this.mockGatewayRepo.save(Mockito.any(Gateway.class))).then(inv -> inv.getArgument(0));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/gateways/42?sensorId={0}", "xyz").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
