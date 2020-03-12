package com.example.prapti.birthdays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BirthdayAlarmRecever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context.getApplicationContext(), BirthdayNotificationAfterReceiveAactivity.class);
        String message=intent.getStringExtra("message");
        String phone=intent.getStringExtra("phone");
        int alarm_id =intent.getIntExtra("alarm_id",0);

        intent1.putExtra("phone",phone);
        intent1.putExtra("message", message);
        context.startActivity(intent1);
    }
}
