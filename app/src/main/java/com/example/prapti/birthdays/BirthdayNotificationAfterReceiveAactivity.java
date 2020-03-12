package com.example.prapti.birthdays;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class BirthdayNotificationAfterReceiveAactivity extends AppCompatActivity {

    Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_receive);
        setTitle("Birthday Alarm");

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(this, notification);
        r.play();

       String phoneNumber= (getIntent().getStringExtra("phone"));
       String msg_body= (getIntent().getStringExtra("message"));

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        PendingIntent sentPI = PendingIntent.getBroadcast(BirthdayNotificationAfterReceiveAactivity.this, 0,
                new Intent(BirthdayNotificationAfterReceiveAactivity.this,SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(BirthdayNotificationAfterReceiveAactivity.this, 0,
                new Intent(BirthdayNotificationAfterReceiveAactivity.this, SmsDeliveredReceiver.class), 0);


        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phoneNumber, null, msg_body, sentPI, deliveredPI);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(BirthdayNotificationAfterReceiveAactivity.this);
        builder.setSmallIcon(R.drawable.ic_cake_black_24dp);
        builder.setContentTitle("Birthday notification");
        builder.setContentText("Message sent to "+phoneNumber+"\n"+msg_body);
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        r.stop();
        finish();
        return true;
    }

//
//    private void sendSMS(String phoneNumber, String message) {
//
//        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
//        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
//
//        try {
//            SmsManager sms = SmsManager.getSmsManagerForSubscriptionId(2);
//
//            ArrayList<String> mSMSMessage = sms.divideMessage(message);
//            for (int i = 0; i < mSMSMessage.size(); i++) {
//                sentPendingIntents.add(i, sentPI);
//                deliveredPendingIntents.add(i, deliveredPI);
//            }
//            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
//                    sentPendingIntents, deliveredPendingIntents);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            Toast.makeText(getBaseContext(), "SMS sending failed...",Toast.LENGTH_SHORT).show();
//        }
//
//    }
}
