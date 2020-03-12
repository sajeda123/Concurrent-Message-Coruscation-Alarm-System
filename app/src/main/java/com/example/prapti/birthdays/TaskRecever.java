package com.example.prapti.birthdays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TaskRecever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context.getApplicationContext(), TaskAfterReceverActivity.class);
        String message=intent.getStringExtra("message");
        String title=intent.getStringExtra("title");

        intent1.putExtra("title",title);
        intent1.putExtra("message", message);
        context.startActivity(intent1);
    }
}
