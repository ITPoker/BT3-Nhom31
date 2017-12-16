package com.nhom31.tbdd.bt3_nhom31.BT2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class smsBroadcastReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    String smsBody;
    String address;

    public void onReceive(Context context, Intent intent){
        Bundle intentExtras = intent.getExtras();
        if(intentExtras != null){
            Object[] sms  = (Object[]) intentExtras.get(SMS_BUNDLE);
            for(int i = 0; i <sms.length; i++){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
            }

            MainActivityBT2 inst = MainActivityBT2.instance();
            inst.checkSMS(address, smsBody);
        }
    }
}
