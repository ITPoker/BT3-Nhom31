package com.nhom31.tbdd.bt3_nhom31.BT3;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom31.tbdd.bt3_nhom31.R;

public class Sensor_Light extends AppCompatActivity {
    private TextView txtViewLight;
    private float lightValue = 0.5f;
    private WindowManager.LayoutParams layoutParams;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_light);
        getSupportActionBar().hide();

        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        StringBuilder stringBuilder = new StringBuilder(2048);
        layoutParams = getWindow().getAttributes();
        txtViewLight = (TextView) findViewById(R.id.textViewLight);
        setBright(lightValue);

        //call type of sensor
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            Toast.makeText(Sensor_Light.this, "No Light Sensor show", Toast.LENGTH_SHORT).show();
        } else {
            float max = lightSensor.getMaximumRange();
            Log.v("Bright", "Bright max: " + String.valueOf(max));
            sensorManager.registerListener(lightSensorEvenListener, lightSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    SensorEventListener lightSensorEvenListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                //show value of sensor what u choose
                float currentLight = event.values[0];
                txtViewLight.setText(String.valueOf(currentLight));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void setBright(float lightValue) {
        if (lightValue > 0 && lightValue <= 1) {
            layoutParams.screenBrightness = lightValue;
            getWindow().setAttributes(layoutParams);
        }
    }
}
