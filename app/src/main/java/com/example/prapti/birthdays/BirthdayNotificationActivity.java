package com.example.prapti.birthdays;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BirthdayNotificationActivity extends AppCompatActivity{


    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    BirthdayDatabaseHelper birthdayDatabaseHelper;
    List list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, 1);

            }else{
                Log.d("permission", "permission acccess to SEND_SMS - requesting it");

            }
        }


        floatingActionButton=findViewById(R.id.add_birthday_activity_btn);

        recyclerView=findViewById(R.id.birthday_list);

        birthdayDatabaseHelper =new BirthdayDatabaseHelper(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BirthdayNotificationActivity.this,AddBirthdayNotification.class);
                startActivity(intent);
            }
        });
        list=new ArrayList();

        getPopulated();
    }


    public void getPopulated(){
        list.clear();
        Cursor cursor= birthdayDatabaseHelper.displayAllData();
        if(cursor.getCount()==0){
            Toast.makeText(BirthdayNotificationActivity.this,"List is empty!",Toast.LENGTH_LONG).show();
        }else {
            while (cursor.moveToNext()){
                Birthday_Model eventModel=new Birthday_Model(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                list.add(eventModel);
            }
        }


        Myadapter adapter=new Myadapter(list);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> implements View.OnClickListener {
        List<Birthday_Model> eventList;
        public Myadapter(List<Birthday_Model> list){
            eventList=list;
        }

        @Override
        public Myadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View inflater= LayoutInflater.from(BirthdayNotificationActivity.this).inflate(R.layout.birthday_row_item,parent,false);

            inflater.setOnClickListener(this);
            inflater.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int itemPosition = recyclerView.getChildLayoutPosition(v);
                    final Birthday_Model item = eventList.get(itemPosition);

                    AlertDialog.Builder builder=new AlertDialog.Builder(BirthdayNotificationActivity.this).setTitle("Confirmation message")
                            .setMessage("Are you want to delete this alarm?").setNegativeButton("No",null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(birthdayDatabaseHelper.deleteData(item.getId())>0){
                                        deleteAlarm(item.getAlarm_id());
                                        getPopulated();
                                        Toast.makeText(BirthdayNotificationActivity.this,"1 remainder deleted!",Toast.LENGTH_LONG).show();

                                    }else {
                                        Toast.makeText(BirthdayNotificationActivity.this,"Could not delete!",Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                    builder.show();
                    return true;
                }
            });
            Myadapter.MyViewHolder myViewHolder=new Myadapter.MyViewHolder(inflater);
            return myViewHolder;
        }



        @Override
        public void onBindViewHolder(Myadapter.MyViewHolder holder, int position) {


            Birthday_Model e=eventList.get(position);
            holder.title.setText(e.getName());
            holder.date.setText(e.getBirthday());

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        @Override
        public void onClick(View v) {


            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Birthday_Model item = eventList.get(itemPosition);
            Toast.makeText(BirthdayNotificationActivity.this,item.name,Toast.LENGTH_LONG).show();
           Intent intent=new Intent(BirthdayNotificationActivity.this,UpdateBirthdayNotification.class);
//            intent.putExtra("id",item.getId());
//            intent.putExtra("date",item.getDate());
//            intent.putExtra("time",item.getTime());//intent.putExtra("title",item.title);
//            intent.putExtra("message",item.getMessage());
//            intent.putExtra("alarmId",item.getAlarmId());
//            AlertDialog.Builder builder=new AlertDialog.Builder(RemainderListActivity.this)
//                    .setTitle("Alarm!")
//                    .setMessage(item.getTitle()+" : "+item.getMessage())
//                    .setIcon(R.drawable.ic_alarm_on_black_24dp)
//                    .setPositiveButton("Ok",null);
//            builder.show();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title,date;
            public MyViewHolder(View itemView) {
                super(itemView);
                title= (TextView) itemView.findViewById(R.id.show_title);
                date= (TextView) itemView.findViewById(R.id.show_birthdate);
            }
        }
    }

    private void deleteAlarm(String alarm_id) {
        int alrm_id=Integer.parseInt(alarm_id);

        AlarmManager am= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Log.v("notification_delete",alarm_id);

        Intent intent=new Intent(BirthdayNotificationActivity.this,BirthdayAlarmRecever.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alrm_id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
        pendingIntent.cancel();


    }
}
