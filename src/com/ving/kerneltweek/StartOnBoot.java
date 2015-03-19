package com.ving.kerneltweek;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class StartOnBoot extends Service
{
    private static final String TAG = "StartOnBoot";
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
//        Intent intents = new Intent(getBaseContext(),hello.class);
//        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intents);
        Log.d(TAG, "onStart");
        onBootComplete();
    }

	protected void onBootComplete() {
		SharedPreferences settings = getSharedPreferences("MainActivity",
				MODE_PRIVATE);
		Boolean setOnBoot = settings.getBoolean("setOnBoot", false);
		Log.i("KernelTweek", "Set on Boot set to " + setOnBoot);
		if (setOnBoot) {
			KernelValue kernelValue = new KernelValue();
			Log.i("KernelTweek", "Getting and Setting kernel values");
			Boolean fastCharge = settings.getBoolean("fastCharge", false);
			Log.i("KernelTweek", "Fast Charge set to " + fastCharge);
			kernelValue.setFastCharge(fastCharge);
			Boolean performanceAudio = settings.getBoolean("performanceAudio",
					false);
			Log.i("KernelTweek", "Performance Audio set to " + performanceAudio);
			kernelValue.setPerformanceAudio(performanceAudio);
			int batSel = settings.getInt("battery", 68);
			int batValue = batSel + 30;
			Log.i("KernelTweek", "Battery set to " + batValue);
			kernelValue.setBatteryCharge(batValue);
			Thread setValue = new Thread(new SetKernelValues(kernelValue));
			setValue.start();
		}
	}

}
