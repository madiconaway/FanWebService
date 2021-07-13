package com.fan.service.bean;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="fan")
public class Fan {
	private String name;
	private double upstairsTemp = 85;
	private double downstairsTemp = 75;
	private double triggerDifferential = 3;
	private double maxTemp = upstairsTemp;
	private boolean status = false;
    private String fanSetting = "OFF";
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUpstairsTemp() {
		return upstairsTemp;
	}

	public void setUpstairsTemp(double upstairsTemp) {
		this.upstairsTemp = upstairsTemp;
	}

	public double getDownstairsTemp() {
		return downstairsTemp;
	}

	public void setDownstairsTemp(double downstairsTemp) {
		this.downstairsTemp = downstairsTemp;
	}

	public double getTriggerDiff() {
		return triggerDifferential;
	}

	public void setTriggerDiff(double triggerDifferential) {
		this.triggerDifferential = triggerDifferential;
	}
	
	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}
	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getFanSetting() {
		return fanSetting;
	}

	public void setFanSetting(String fanSetting) {
		this.fanSetting = fanSetting;
	}
}

