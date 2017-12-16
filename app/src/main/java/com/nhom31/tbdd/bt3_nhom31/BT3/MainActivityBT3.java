package com.nhom31.tbdd.bt3_nhom31.BT3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nhom31.tbdd.bt3_nhom31.R;

public class MainActivityBT3 extends AppCompatActivity {
    private Button btnShowLight;
    private Button btnShowAccelerometer;
    private Button btnShowProximity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bt3);
        getSupportActionBar().hide();

        btnShowLight = (Button) findViewById(R.id.buttonShowLight);
        btnShowLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityBT3.this, Sensor_Light.class);
                startActivity(intent);
            }
        });
        btnShowAccelerometer = (Button) findViewById(R.id.buttonShowAccelerometer);
        btnShowAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityBT3.this, Sensor_Accelerometer.class);
                startActivity(intent);
            }
        });
        btnShowProximity = (Button) findViewById(R.id.buttonShowProximity);
        btnShowProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityBT3.this, Sensor_Proximity.class);
                startActivity(intent);
            }
        });
    }
}
