package com.example.prapti.birthdays;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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


public class TaskActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    List list;
    TaskDatabaseHelper eventDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_remainder);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setTitle("Task List");




        floatingActionButton= (FloatingActionButton) findViewById(R.id.add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this,AddTaskActivity.class));
            }
        });

        eventDatabase=new TaskDatabaseHelper(this);
        list=new ArrayList();

        recyclerView= (RecyclerView) findViewById(R.id.event_recycleview);
        getPopulated();

    }


    public void getPopulated(){
        list.clear();
        Cursor cursor=eventDatabase.getAllData();
        if(cursor.getCount()==0){
            Toast.makeText(TaskActivity.this,"Empty list",Toast.LENGTH_LONG).show();
        }else {
            while (cursor.moveToNext()){
                TaskModel eventModel=new TaskModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(5),cursor.getString(4),cursor.getString(3));
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
        List<TaskModel> eventList;
        public Myadapter(List<TaskModel> list){
            eventList=list;
        }

        @Override
        public Myadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View inflater= LayoutInflater.from(TaskActivity.this).inflate(R.layout.task_row_item,parent,false);

            inflater.setOnClickListener(this);
            inflater.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int itemPosition = recyclerView.getChildLayoutPosition(v);
                    final TaskModel item = eventList.get(itemPosition);

                    AlertDialog.Builder builder=new AlertDialog.Builder(TaskActivity.this).setTitle("Confirmation message")
                            .setMessage("Delete task?").setNegativeButton("No",null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(eventDatabase.deleteData(item.getId())){
                                        Log.v("kkkkkk",item.alarmId+"kkk");
                                        deleteAlarm(item.alarmId);

                                        getPopulated();
                                        Toast.makeText(TaskActivity.this,"Task deleted!",Toast.LENGTH_LONG).show();

                                    }else {
                                        Toast.makeText(TaskActivity.this,"Task could not delete!",Toast.LENGTH_LONG).show();
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


            TaskModel e=eventList.get(position);
            holder.title.setText(e.getTitle());
            holder.date.setText(e.getDate());
            holder.time.setText(e.getTime());

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        @Override
        public void onClick(View v) {


            int itemPosition = recyclerView.getChildLayoutPosition(v);
            TaskModel item = eventList.get(itemPosition);
//            Intent intent=new Intent(RemainderListActivity.this,AddEvent.class);
//            intent.putExtra("id",item.getId());
//            intent.putExtra("date",item.getDate());
//            intent.putExtra("time",item.getTime());
//            intent.putExtra("title",);
//            intent.putExtra("message",item.getMessage());
//            intent.putExtra("alarmId",item.getAlarmId());
//            intent.putExtra("update","yes");

            AlertDialog.Builder builder=new AlertDialog.Builder(TaskActivity.this)
                    .setTitle("Alarm!")
                    .setMessage(item.getTitle()+" : "+item.getMessage())
                    .setIcon(R.drawable.ic_alarm_white_24dp)
                    .setPositiveButton("Ok",null);
            builder.show();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title,date,time;

            public MyViewHolder(View itemView) {
                super(itemView);
                title= (TextView) itemView.findViewById(R.id.event_title);
                date= (TextView) itemView.findViewById(R.id.event_date);
                time= (TextView) itemView.findViewById(R.id.event_time);
            }
        }
    }
    private void deleteAlarm(String alarm_id) {
        int alrm_id=Integer.parseInt(alarm_id);

        AlarmManager am= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Log.v("detete_alarm",alarm_id+"sdfsdf");

        Intent intent=new Intent(TaskActivity.this,TaskRecever.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alrm_id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
        pendingIntent.cancel();


    }

}
