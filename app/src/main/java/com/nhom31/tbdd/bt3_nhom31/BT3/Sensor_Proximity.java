package com.nhom31.tbdd.bt3_nhom31.BT3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nhom31.tbdd.bt3_nhom31.R;

public class Sensor_Proximity extends AppCompatActivity implements SensorEventListener {
    TextView txtvShowProximity;
    Sensor sensorProximity;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_proximity);
        getSupportActionBar().hide();

        txtvShowProximity = (TextView) findViewById(R.id.textViewShowProximity);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //call type of sensor
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //show value of sensor what u choose
        txtvShowProximity.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
