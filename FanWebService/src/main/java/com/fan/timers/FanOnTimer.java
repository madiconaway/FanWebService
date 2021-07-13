package com.fan.timers;

import java.util.TimerTask;
import com.fan.service.bean.Fan;

public class FanOnTimer extends TimerTask{
	
	Fan fan;
	
	public FanOnTimer(Fan fan) {
		this.fan = fan;	
	}
	
 	@Override
	public void run() {
        double upstairsTemp = fan.getUpstairsTemp();
        double downstairsTemp = fan.getDownstairsTemp();
        upstairsTemp = upstairsTemp - .5;
        if (upstairsTemp <= downstairsTemp) {
            fan.setUpstairsTemp(downstairsTemp);
        } else {
        	fan.setUpstairsTemp(upstairsTemp);
        }
	}
 	
 	public Fan getFan() {
 		return this.fan;
 	}
}