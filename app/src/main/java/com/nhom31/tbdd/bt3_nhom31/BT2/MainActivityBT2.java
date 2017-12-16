package com.nhom31.tbdd.bt3_nhom31.BT2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom31.tbdd.bt3_nhom31.R;

import java.util.ArrayList;

public class MainActivityBT2 extends AppCompatActivity {
    final String DATABASE_NAME = "smsServerDB.sqlite";
    SQLiteDatabase database;
    ArrayList<SinhVien> list;
    AdapterSinhVien adapter;
    Button btn;
    TextView txtv ;
    int checkStart = 0;

    private static MainActivityBT2 inst;
    public static MainActivityBT2 instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bt2);
        getSupportActionBar().hide();

        txtv = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.btnStart);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0){
                if (btn.getText().equals("Start")) {
                    btn.setText("Stop");
                    checkStart = 1;
                    txtv.setText("SMS Sever is start");
                } else {
                    btn.setText("Start");
                    checkStart = 0;
                    txtv.setText("SMS Sever is stop");
                }
            }
        });
        addControls();
    }

    public void addControls(){
        list = new ArrayList<>();
        adapter = new AdapterSinhVien(this, list);
    }

    public void checkSMS(String address, String smsBody) {
        String phoneNo = address;
        String messageErr = "Cú pháp không hợp lệ";

        try {
            if (checkStart == 1) {
                database = Database.initDatabase(this, DATABASE_NAME);
                Cursor cursor = database.rawQuery("select * from SinhVien", null);

                while (cursor.moveToNext()) {
                    String mssv = cursor.getString(0);
                    String toan = cursor.getString(1);
                    String ly = cursor.getString(2);
                    String hoa = cursor.getString(3);
                    list.add(new SinhVien(mssv, toan, ly, hoa));
                }

                int temp = 0;
                SmsManager smsManager = SmsManager.getDefault();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (smsBody.equals("diemthi " + list.get(i).getMssv() )) {
                        smsManager.sendTextMessage(phoneNo, null, list.get(i)
                                .toString(), null, null);
                        Toast.makeText(getApplicationContext(),
                                "Đã gửi tin nhắn"  , Toast.LENGTH_LONG).show();

                        temp = 1;
                        break;
                    }
                }
                if (temp == 0) {
                    smsManager.sendTextMessage(phoneNo, null, messageErr, null,
                            null);
                    Toast.makeText(getApplicationContext(), "Đã gửi tin nhắn",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Không gửi được" ,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
