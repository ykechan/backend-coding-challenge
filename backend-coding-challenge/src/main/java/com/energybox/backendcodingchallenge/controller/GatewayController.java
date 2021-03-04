package com.energybox.backendcodingchallenge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.service.GatewayService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping( value = "/gateways" )
public class GatewayController {

    private final GatewayService service;

    public GatewayController( GatewayService service ) {
        this.service = service;
    }
    
    @ApiOperation( value = "get all gateways", response = Gateway.class )
    @RequestMapping( value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Gateway>> getAllGateways(
    		@RequestParam(name="sensorType", required=false) String sensorType
    	) {
    	List<Gateway> gateways = sensorType == null || sensorType.trim().isEmpty()
    			? this.service.findAllGateways()
    			: this.service.findAllGatewaysHaving(sensorType);
        return new ResponseEntity<>(gateways, HttpStatus.OK);
    }
    
    @ApiOperation( value = "get all sensors assigned to a gateway", response = Gateway.class )
    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Gateway> getAllSensorsAssigned(@PathVariable("id") String gatewayId) {
    	try {
    		long id = Long.valueOf(gatewayId);
    		return ResponseEntity.of(this.service.findGatewayById(id));
    	} catch(NumberFormatException ex) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    }

    @ApiOperation( value = "create a gateway", response = Gateway.class )
    @RequestMapping( value = "", method = RequestMethod.POST)
    public ResponseEntity<Gateway> create() {
    	Gateway gateway = this.service.createGateway();
        return new ResponseEntity<>(gateway, HttpStatus.CREATED);
    }
    
    @ApiOperation( value = "assign a sensor to a gateway", response = Gateway.class )
    @RequestMapping( value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Gateway> assign(@PathVariable("id") String gatewayId,  @RequestParam("sensorId") String sensorId
    	){
    	try {
    		Gateway gateway = this.service.connectSensorToGateway(Long.valueOf(gatewayId), Long.valueOf(sensorId))
    			.orElse(null);
    		
    		if(gateway == null) {
    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    		
    		return new ResponseEntity<>(gateway, HttpStatus.CREATED);
    	} catch(NumberFormatException ex) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    }
}
