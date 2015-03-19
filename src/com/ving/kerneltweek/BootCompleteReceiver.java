package com.ving.kerneltweek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BroadcastReceiver","Got the message");
		Intent startIntent = new Intent(context,StartOnBoot.class);
        context.startService(startIntent);
        Log.i("Autostart", "started");
	}

}
