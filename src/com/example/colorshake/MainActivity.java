package com.example.colorshake;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity implements SensorEventListener {
	public final static int[] COLORS = { Color.argb(0xff, 0xcc, 0x00, 0x00),
			Color.argb(0xff, 0x00, 0xcc, 0x00) };
	private float accel;
	private float accelCurrent;
	private float accelLast;
	private long lastShakeEvent;
	private int colorIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().getDecorView().getRootView()
				.setBackgroundColor(COLORS[0]);
		setContentView(R.layout.activity_main);
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		accel = 0.00f;
		accelCurrent = SensorManager.GRAVITY_EARTH;
		accelLast = SensorManager.GRAVITY_EARTH;

	}

	@Override
	protected void onResume() {
		super.onResume();
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// ignore motion events if a shake event occured less than 1 second ago
		if ((System.currentTimeMillis() - lastShakeEvent) < 1000L) {
			return;
		}
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		accelLast = accelCurrent;
		accelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
		float delta = accelCurrent - accelLast;
		accel = accel * 0.9f + delta;
		if (accel >= 2.0f) {
			fireShakeEvent();
		}

	}

	public void fireShakeEvent() {
		this.lastShakeEvent = System.currentTimeMillis();
		accel = 0.00f;
		accelCurrent = SensorManager.GRAVITY_EARTH;
		accelLast = SensorManager.GRAVITY_EARTH;

		colorIndex++;

		if (colorIndex > COLORS.length - 1) {
			colorIndex = 0;
		}
		this.getWindow().getDecorView().getRootView()
				.setBackgroundColor(COLORS[colorIndex]);
	}

}
