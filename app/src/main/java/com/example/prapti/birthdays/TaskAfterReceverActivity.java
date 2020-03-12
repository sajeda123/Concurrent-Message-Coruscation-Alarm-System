package com.example.prapti.birthdays;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import javax.xml.transform.Templates;

public class TaskAfterReceverActivity extends AppCompatActivity {
    Ringtone r;
TextView title,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_after_recever);

        setTitle("              Task Alarm");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(this, notification);
        r.play();
        title=findViewById(R.id.title_of_task);
        message=findViewById(R.id.task_message);
        title.setText(getIntent().getStringExtra("title"));
        message.setText(getIntent().getStringExtra("message"));



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        r.stop();
        finish();
        return true;
    }
}
