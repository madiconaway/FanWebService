package com.fan.timers;

import java.util.TimerTask;
import com.fan.service.bean.Fan;

public class FanAutoTimer extends TimerTask{
	
	Fan fan;
	
	public FanAutoTimer(Fan fan) {
		this.fan = fan;	
	}
	
 	@Override
	public void run() {
        double downstairsTemp = fan.getDownstairsTemp();
        double upstairsTemp = fan.getUpstairsTemp();
        double triggerDifferential = fan.getTriggerDiff();
        int tempDirection=0;
        
        if (fan.getStatus()) { //fan is on, temp goes down
            upstairsTemp = upstairsTemp - .5;
            tempDirection = -1;
        } else { //fan is off, temp goes up
            upstairsTemp = upstairsTemp + .5;
            tempDirection = 1;
        }
        fan.setUpstairsTemp(upstairsTemp);

        if (downstairsTemp < upstairsTemp) {
            if (!fan.getStatus()) {
                //only turn on the fan if the temp is increasing and the downstairs temp + trigger temp is less than the upstairs temp
                if (tempDirection==1 && (downstairsTemp + triggerDifferential<= upstairsTemp )) {
                    fan.setStatus(true); //turn fan on
                }
            }
        } else {
            if (fan.getStatus()) {
                fan.setStatus(false); //turn fan off
            }
        }
 	}
 	
 	public Fan getFan() {
 		return this.fan;
 	}
 	
}