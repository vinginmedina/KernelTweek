package com.ving.kerneltweek;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.app.Activity;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	private SharedPreferences settings = null;
	private SharedPreferences.Editor editor = null;
	private Boolean setOnBoot;
	private KernelValue kernelValue = null;
	private Spinner bat;
	private MyData batPer[];
	private int batSel;
	private int batValue;
	private String batKey;
	private Switch fastChargeSwitch;
	private Switch performanceAudioSwitch;
	private CheckBox setOnBootCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kernelValue = new KernelValue();
		Log.i("KernelTweek", "in onCreate");
	}

	public void getSavedValues() {
		settings = getPreferences(MODE_PRIVATE);
		editor = settings.edit();
		Boolean fastCharge = settings.getBoolean("fastCharge", false);
		Log.i("KernelTweek", "Fast Charge set to " + fastCharge);
		kernelValue.setFastCharge(fastCharge);
		Boolean performanceAudio = settings.getBoolean("performanceAudio",
				false);
		Log.i("KernelTweek", "Performance Audio set to " + performanceAudio);
		kernelValue.setPerformanceAudio(performanceAudio);
		batSel = settings.getInt("battery", 68);
		Log.i("KernelTweek", "Battery set to " + batSel);
		kernelValue.setBatteryCharge(batSel + 30);
		setOnBoot = settings.getBoolean("setOnBoot", false);
		Log.i("KernelTweek", "Set on Boot set to " + setOnBoot);
	}

	public void prepValues() {
		setContentView(R.layout.activity_main);
		fastChargeSwitch = (Switch) findViewById(R.id.fastChargeToggle);
		fastChargeSwitch.setChecked(kernelValue.fastCharge());
		performanceAudioSwitch = (Switch) findViewById(R.id.audioToggle);
		performanceAudioSwitch.setChecked(kernelValue.performanceAudio());
		setOnBootCheckBox = (CheckBox) findViewById(R.id.setOnBoot);
		setOnBootCheckBox.setChecked(setOnBoot);
		bat = (Spinner) findViewById(R.id.batPercentage);
		batPer = new MyData[71];
		for (int i = 30; i < 101; i++) {
			batPer[i - 30] = new MyData(i);
		}
		ArrayAdapter<MyData> batAdapter = new ArrayAdapter<MyData>(this,
				android.R.layout.simple_spinner_item, batPer);
		batAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bat.setAdapter(batAdapter);
		bat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				MyData d = batPer[position];

				// Get selected value of key
				batValue = d.getValue();
				batKey = d.getSpinnerText();
				editor.putInt("battery", position);
				editor.commit();
				kernelValue.setBatteryCharge(batValue);
				Log.i("KernelTweek", "Battery set to " + position);
				setKernelValue();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		bat.setSelection(batSel);
		batValue = batPer[batSel].getValue();
	}

	public void setKernelValue() {

		Log.i("KernelTweek", "This is where Fast Charge would be set to "
				+ kernelValue.fastCharge());
		Log.i("KernelTweek", "This is where Performance Audio would be set to "
				+ kernelValue.performanceAudio());
		Log.i("KernelTweek", "This is where Battery Charge would be set to "
				+ kernelValue.batteryCharge());
		Thread setValue = new Thread(new SetKernelValues(kernelValue));
		setValue.start();
	}

	public void switchClicked(View view) {
		switch (view.getId()) {
		case R.id.fastChargeToggle:
			Boolean fastCharge = ((Switch) view).isChecked();
			editor.putBoolean("fastCharge", fastCharge);
			editor.commit();
			kernelValue.setFastCharge(fastCharge);
			Log.i("KernelTweek", "Fast Charge set to " + fastCharge);
			setKernelValue();
			break;
		case R.id.audioToggle:
			Boolean performanceAudio = ((Switch) view).isChecked();
			editor.putBoolean("performanceAudio", performanceAudio);
			editor.commit();
			kernelValue.setPerformanceAudio(performanceAudio);
			Log.i("KernelTweek", "Performance Audio set to " + performanceAudio);
			setKernelValue();
			break;
		case R.id.setOnBoot:
			setOnBoot = ((CheckBox) view).isChecked();
			editor.putBoolean("setOnBoot", setOnBoot);
			editor.commit();
			Log.i("KernelTweek", "Set on Boot set to " + setOnBoot);
			break;
		}
	}

	class MyData {
		public MyData(int value) {
			this.spinnerText = String.valueOf(value);
			this.value = value;
		}

		public String getSpinnerText() {
			return spinnerText;
		}

		public int getValue() {
			return value;
		}

		public String toString() {
			return spinnerText;
		}

		String spinnerText;
		int value;
	}

	@Override
	public void onResume() {
		Log.i("KernelTweek", "In onResume");
		super.onResume();
		getSavedValues();
		prepValues();
		setKernelValue();
	}
}
