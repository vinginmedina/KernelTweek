package com.ving.kerneltweek;

import java.util.ArrayList;
import java.util.List;

public class KernelValue {
	private Boolean fastCharge;
	private Boolean performanceAudio;
	private int batteryCharge;

	KernelValue() {
		fastCharge = false;
		performanceAudio = false;
		batteryCharge = 100;
	}

	public void setFastCharge(Boolean fastCharge) {
		this.fastCharge = fastCharge;
	}

	public void setPerformanceAudio(Boolean performanceAudio) {
		this.performanceAudio = performanceAudio;
	}

	public void setBatteryCharge(int batteryCharge) {
		this.batteryCharge = batteryCharge;
	}

	public Boolean fastCharge() {
		return fastCharge;
	}

	public int fastChargeValue() {
		int rtn;
		if (fastCharge) {
			rtn = 1;
		} else {
			rtn = 0;
		}
		return rtn;
	}

	public Boolean performanceAudio() {
		return performanceAudio;
	}

	public int performaceAudioValue() {
		int rtn;
		if (performanceAudio) {
			rtn = 1;
		} else {
			rtn = 0;
		}
		return rtn;
	}

	public int batteryCharge() {
		return batteryCharge;
	}

	public List<String> updateCommands() {
		List<String> commands = new ArrayList<String>();
		String command = "echo \"" + this.fastChargeValue()
				+ "\" > /sys/kernel/fast_charge/force_fast_charge";
		commands.add(command);
		command = "echo \""
				+ this.performaceAudioValue()
				+ "\" > /sys/devices/virtual/misc/soundcontrol/highperf_enabled";
		commands.add(command);
		command = "echo \"" + batteryCharge
				+ "\" > /sys/class/misc/batterylifeextender/charging_limit";
		commands.add(command);

		return commands;
	}

}
