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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBirthdayNotification extends AppCompatActivity {

    EditText date, time, name, phone, title, message;
    Calendar myCalendar1;
    Button save;
    long alarm_id;
    BirthdayDatabaseHelper birthdayDatabaseHelper;
    String str_name, str_date, str_time, str_title, str_message, str_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday_notification);

        birthdayDatabaseHelper = new BirthdayDatabaseHelper(this);
        date = findViewById(R.id.get_event_date);

        time = findViewById(R.id.get_event_time);
        name = findViewById(R.id.set_name);
        phone = findViewById(R.id.set_phone);
        title = findViewById(R.id.set_message_title);
        message = findViewById(R.id.set_message);

        save = findViewById(R.id.save);

        setTimeAndDate();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_current="";
                str_name = name.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();
                str_title = title.getText().toString();
                str_message = message.getText().toString();
                str_phone = phone.getText().toString();
                if (CheackTest(str_name, str_date, str_time, str_title, str_message, str_phone)) {


                    alarm_id = generateAlarm_id();

                    GetDifferenceOfTime getDifferenceOfTime=new GetDifferenceOfTime();

                    final Calendar myCalendar1 = Calendar.getInstance();

                    int year = myCalendar1.get(Calendar.YEAR);
                    int month = myCalendar1.get(Calendar.MONTH);
                    int day = myCalendar1.get(Calendar.DAY_OF_MONTH);

                    str_current = year + ":" + month + ":" + day;
                    long alarm_time =getDifferenceOfTime.getDifferece(str_current,str_date,str_time) ;

                    Log.v("kkkk",alarm_time+" pp ");

                    setAlarm(alarm_time, alarm_id, str_message,str_title);

                    long i = birthdayDatabaseHelper.insertData(str_name, str_date + " at " + str_time, str_title, str_message, str_phone, alarm_id + "");

                    if (i > 0) {
                        Toast.makeText(AddBirthdayNotification.this, "Data saved successfully!", Toast.LENGTH_LONG).show();

                        name.setText("");
                        date.setText("");
                        time.setText("");
                        title.setText("");
                        message.setText("");
                        phone.setText("");

                    } else {
                        Toast.makeText(AddBirthdayNotification.this, "Data could not saved!", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(AddBirthdayNotification.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void setAlarm(long alarm_time, long alarm_id, String str_m, String str_t) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



        Intent intent = new Intent(AddBirthdayNotification.this, BirthdayAlarmRecever.class);


        intent.putExtra("message", str_m);
        intent.putExtra("alarm_id", alarm_id);
        intent.putExtra("phone", str_phone);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddBirthdayNotification.this, (int) alarm_id, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarm_time * 1000, pendingIntent);


        Toast.makeText(AddBirthdayNotification.this, "Alamr is set", Toast.LENGTH_LONG).show();

    }

    private long generateAlarm_id() {

//todo cancel alarm if it id deleted
        long a_i = System.currentTimeMillis()%1000;

        Log.v("mmmm",a_i+"");
        return a_i;
    }

    private boolean CheackTest(String n, String d, String t, String ti, String m, String p) {


        if (n.isEmpty() || d.isEmpty() || ti.isEmpty() || t.isEmpty() || m.isEmpty() || p.isEmpty()) {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddBirthdayNotification.this, new TimePickerDialog.OnTimeSetListener() {
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
                new DatePickerDialog(AddBirthdayNotification.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }
}
