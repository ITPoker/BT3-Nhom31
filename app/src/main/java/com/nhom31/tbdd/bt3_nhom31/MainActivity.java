package com.nhom31.tbdd.bt3_nhom31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nhom31.tbdd.bt3_nhom31.BT1.MainActivityBT1;
import com.nhom31.tbdd.bt3_nhom31.BT2.MainActivityBT2;
import com.nhom31.tbdd.bt3_nhom31.BT3.MainActivityBT3;
import com.nhom31.tbdd.bt3_nhom31.BT4.MainActivityBT4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnBT1, btnBT2, btnBT3, btnBT4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        init();
        initListeners();

    }

    private void init() {
        btnBT1 = (Button) findViewById(R.id.btnBT1);
        btnBT2 = (Button) findViewById(R.id.btnBT2);
        btnBT3 = (Button) findViewById(R.id.btnBT3);
        btnBT4 = (Button) findViewById(R.id.btnBT4);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        btnBT1.setOnClickListener(this);
        btnBT2.setOnClickListener(this);
        btnBT3.setOnClickListener(this);
        btnBT4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnBT1:
                intent = new Intent(MainActivity.this, MainActivityBT1.class);
                startActivity(intent);
                break;
            case R.id.btnBT2:
                intent = new Intent(MainActivity.this, MainActivityBT2.class);
                startActivity(intent);
                break;
            case R.id.btnBT3:
                intent = new Intent(MainActivity.this, MainActivityBT3.class);
                startActivity(intent);
                break;
            case R.id.btnBT4:
                intent = new Intent(MainActivity.this, MainActivityBT4.class);
                startActivity(intent);
                break;
        }
    }
}

