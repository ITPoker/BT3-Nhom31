package com.nhom31.tbdd.bt3_nhom31.BT3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nhom31.tbdd.bt3_nhom31.R;

public class Sensor_Accelerometer extends AppCompatActivity implements SensorEventListener {
    private TextView txtvAccelerometer;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_accelerometer);
        getSupportActionBar().hide();

        txtvAccelerometer = (TextView) findViewById(R.id.textViewAccelerometer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //call type of sensor
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //show value of sensor what u choose
        float x, y, z;
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
       txtvAccelerometer.setText("\n" + "Value of X: " + x + "\n" + "Value of Y: " + y + "\n" + "Value of Z: " + z);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
