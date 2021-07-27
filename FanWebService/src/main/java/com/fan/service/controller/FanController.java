package com.fan.service.controller;

import com.fan.service.bean.Fan;
import com.fan.timers.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class FanController {

	Fan fan = new Fan();
    Timer timer;
    FanOnTimer fanOnTimer = new FanOnTimer(fan);
    FanOffTimer fanOffTimer = new FanOffTimer(fan);
    FanAutoTimer fanAutoTimer = new FanAutoTimer(fan);
    
	@GetMapping(path="/getupstairstemp")
	public ObjectNode getUpstairsTemp() {
        ObjectMapper mapper = new ObjectMapper();
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("upstairs-temp", fan.getUpstairsTemp());
		return objectNode;
	}
	
	@GetMapping(path="/getdownstairstemp")
	public ObjectNode getDownstairsTemp() {
        ObjectMapper mapper = new ObjectMapper();
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("downstairs-temp", fan.getDownstairsTemp());
		return objectNode;
	}
	
	@GetMapping(path="/gettriggerdiff")
	public ObjectNode getTriggerDiff() { 
        ObjectMapper mapper = new ObjectMapper();
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("trigger-diff", fan.getTriggerDiff());
		return objectNode;
	}
	
	@GetMapping(path="/getstatus")
	public ObjectNode getStatus() { //true=fan is running, false=fan is not running 
        ObjectMapper mapper = new ObjectMapper();
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("fan-status", fan.getStatus());
		return objectNode;
	}

	@GetMapping(path="/getfansetting")
	public ObjectNode getFanSetting() { //ON, OFF, or AUTO
        ObjectMapper mapper = new ObjectMapper();
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("fan-setting", fan.getFanSetting());
		return objectNode;
	}
	
	@PostMapping(path="/posttriggerdiff")
	public ResponseEntity<HttpStatus> setTriggerDiff(@RequestBody String triggerDiff) {
		Double trigger = 0.0;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode;
			jsonNode = objectMapper.readTree(triggerDiff);
			trigger = jsonNode.get("trigger-diff").asDouble();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    fan.setTriggerDiff(trigger);	
	    return ResponseEntity.ok(HttpStatus.OK);
	}
	
	//this function is run when the user clicks the ON or OFF button
	@PostMapping(path="/postfansetting")
	public ResponseEntity<HttpStatus> setFanSetting(@RequestBody String fanSetting) {
		try {
			timer.cancel();	
			timer.purge();
		} catch(Exception e) {}

		String setting = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode;
			jsonNode = objectMapper.readTree(fanSetting);
			setting = jsonNode.get("fan-setting").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}

		timer = new Timer(true);
		if (setting.equals("ON")) {
			fan = fanOffTimer.getFan();
			fan.setStatus(true); //turn fan on
			fanOnTimer = new FanOnTimer(fan);
			timer.scheduleAtFixedRate(fanOnTimer, 1000, 1000);
		} else if (setting.equals("OFF")){
			fan = fanOnTimer.getFan();
			fan.setStatus(false); //turn fan off			
			fanOffTimer = new FanOffTimer(fan);
			timer.scheduleAtFixedRate(fanOffTimer, 1000, 1000);			
		} else { //AUTO
			fanAutoTimer = new FanAutoTimer(fan);
			timer.scheduleAtFixedRate(fanAutoTimer, 1000, 1000);			
		}
		fan.setFanSetting(setting);
	    return ResponseEntity.ok(HttpStatus.OK);
	}
}