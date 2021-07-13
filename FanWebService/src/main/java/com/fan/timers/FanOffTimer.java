package com.fan.timers;

import java.util.TimerTask;
import com.fan.service.bean.Fan;

public class FanOffTimer extends TimerTask{
	
	Fan fan;
	
	public FanOffTimer(Fan fan) {
		this.fan = fan;	
	}
	
 	@Override
	public void run() {
        double upstairsTemp = fan.getUpstairsTemp();
        double maxTemp = fan.getMaxTemp();
        upstairsTemp = upstairsTemp + .5;
        if (upstairsTemp >= maxTemp) {
            fan.setUpstairsTemp(maxTemp);
        	cancel(); //no need to run timer if fan is off and maxtemp is reached
        } else {
        	fan.setUpstairsTemp(upstairsTemp);
        }
	}
 	
 	public Fan getFan() {
 		return this.fan;
 	}
}