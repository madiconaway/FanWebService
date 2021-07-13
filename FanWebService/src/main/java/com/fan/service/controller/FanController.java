package com.fan.service.controller;

import java.util.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fan.service.bean.Fan;
import com.fan.timers.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class FanController {

	Fan fan = new Fan();
    Timer timer;
    FanOnTimer fanOnTimer = new FanOnTimer(fan);
    FanOffTimer fanOffTimer = new FanOffTimer(fan);
    FanAutoTimer fanAutoTimer = new FanAutoTimer(fan);
    
	@GetMapping(path="/getupstairstemp")
	public Double getUpstairsTemp() {
		return fan.getUpstairsTemp();
	}
	
	@GetMapping(path="/getdownstairstemp")
	public Double getDownstairsTemp() {
		return fan.getDownstairsTemp();
	}
	
	@GetMapping(path="/gettriggerdiff")
	public Double getTriggerDiff() { 
		return fan.getTriggerDiff();
	}
	
	@GetMapping(path="/getstatus")
	public boolean getStatus() { //true=fan is running, false=fan is not running
		return fan.getStatus();
	}
	
	@PostMapping(path="/posttriggerdiff")
	public ResponseEntity<HttpStatus> setTriggerDiff(@RequestParam("triggerDiff") Double triggerDiff) {
	    fan.setTriggerDiff(triggerDiff);	
	    return ResponseEntity.ok(HttpStatus.OK);
	}
	
	//this function is run when the user clicks the ON or OFF button
	@PostMapping(path="/postfansetting")
	public ResponseEntity<HttpStatus> setFanSetting(@RequestParam("fanSetting") String fanSetting) {
		try {
			timer.cancel();	
			timer.purge();
		} catch(Exception e) {}
		timer = new Timer(true);
		if (fanSetting.equals("ON")) {
			fan = fanOffTimer.getFan();
			fan.setStatus(true); //turn fan on
			fanOnTimer = new FanOnTimer(fan);
			timer.scheduleAtFixedRate(fanOnTimer, 1000, 1000);
		} else if (fanSetting.equals("OFF")){
			fan = fanOnTimer.getFan();
			fan.setStatus(false); //turn fan off			
			fanOffTimer = new FanOffTimer(fan);
			timer.scheduleAtFixedRate(fanOffTimer, 1000, 1000);			
		} else { //AUTO
			fanAutoTimer = new FanAutoTimer(fan);
			timer.scheduleAtFixedRate(fanAutoTimer, 1000, 1000);			
		}
		fan.setFanSetting(fanSetting);
	    return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping(path="/getfansetting")
	public String getFanSetting() { //ON, OFF, or AUTO
		return fan.getFanSetting();
	}
}