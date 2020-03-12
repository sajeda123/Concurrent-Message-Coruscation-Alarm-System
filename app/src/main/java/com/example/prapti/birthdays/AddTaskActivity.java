package com.example.prapti.birthdays;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    Calendar myCalendar1;
    EditText title, message;
    EditText date, time;
    String id;
    boolean update;
    TaskDatabaseHelper databaseHelper;
    Button saveRemaindder_btn;
    String str_current = "";
    static int alarm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        date = (EditText) findViewById(R.id.get_event_date);

        time = (EditText) findViewById(R.id.get_event_time);

        title = (EditText) findViewById(R.id.get_event_title);

        message = (EditText) findViewById(R.id.get_event_message);

        databaseHelper = new TaskDatabaseHelper(this);
        saveRemaindder_btn = (Button) findViewById(R.id.save_event);

        setTimeAndDate();

        saveRemaindder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveRemainder();

            }
        });

    }


    public void saveRemainder() {

        final String str_date = date.getText().toString();
        final String str_time = time.getText().toString();
        final String str_title = title.getText().toString();
        final String str_message = message.getText().toString();
        final Calendar myCalendar1 = Calendar.getInstance();

        int year = myCalendar1.get(Calendar.YEAR);
        int month = myCalendar1.get(Calendar.MONTH);
        int day = myCalendar1.get(Calendar.DAY_OF_MONTH);

        str_current = year + ":" + month + ":" + day;
        GetDifferenceOfTime getDifferenceOfTime = new GetDifferenceOfTime();
        alarm_id = generateAlarm_id();
        if (CheackTest(str_date, str_time, str_title, str_message)) {
            databaseHelper.insertRemainder(str_date, str_title, str_message, alarm_id + "", str_time);
            long alarm_time = getDifferenceOfTime.getDifferece(str_current, str_date, str_time);

            setAlarm(alarm_time, alarm_id, str_message, str_title);

            startActivity(new Intent(AddTaskActivity.this, TaskActivity.class));
        } else {
            Toast.makeText(AddTaskActivity.this, "Fill all the fields", Toast.LENGTH_LONG).show();

        }

    }


    private void setAlarm(long alarm_time, long alarm_id, String str_m, String str_t) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(AddTaskActivity.this, TaskRecever.class);

        intent.putExtra("message", str_m);
        intent.putExtra("title", str_t);
        intent.putExtra("alarm_id", alarm_id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTaskActivity.this, (int) alarm_id, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarm_time * 1000, pendingIntent);


        Toast.makeText(AddTaskActivity.this, "Alamr is set", Toast.LENGTH_LONG).show();

    }

    private int generateAlarm_id() {

        int a_i = (int) System.currentTimeMillis() % 1000;
        if(a_i<0){
            a_i=-a_i;

        }

        Log.v("mmmm", a_i + "");
        return a_i;
    }

    private boolean CheackTest(String d, String t, String ti, String m) {


        if ( d.isEmpty() || ti.isEmpty() || t.isEmpty() || m.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void setTimeAndDate() {

        myCalendar1 = Calendar.getInstance();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + " : " + minute);

                    }
                }, hour, minutes, true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd:MM:yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                date.setText(sdf.format(myCalendar1.getTime()));
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }
}
