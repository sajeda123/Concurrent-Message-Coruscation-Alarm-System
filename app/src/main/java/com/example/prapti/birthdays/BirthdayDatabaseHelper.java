package com.example.prapti.birthdays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Prapti on 5/22/2018.
 */

public class BirthdayDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="birthday.db";
    private static final String TABLE_NAME="birthday_details_table";
    private static final String ID="id";
    private static final String NAME="name";
    private static final String BIRTH_DATE="birthday";
    private static final String TITlE="title";
    private static final String MESSAGE="message";
    private static final String PHONE="phone";
    private static final String ALARM_ID="alarm_id";

    private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" text not null ,"+BIRTH_DATE+" text not null,"+TITlE+" text not null ,"+MESSAGE+" text not null,"+PHONE+" text not null,"+ALARM_ID+" text not null "+");";
    private static final String DROP_TABLE="DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String SELECT_ALL="SELECT * FROM"+TABLE_NAME;
    private static final int VERSION_NUMBRE=1;
    private Context context;

    public BirthdayDatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION_NUMBRE);
       this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

  try {
      Toast.makeText(context,"Oncreate is called",Toast.LENGTH_LONG).show();
      sqLiteDatabase.execSQL(CREATE_TABLE);
  }
  catch(Exception e){
      Toast.makeText(context,"Exception"+e,Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            Toast.makeText(context,"onUpgrade is called",Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
        catch(Exception e){
            Toast.makeText(context,"Exception"+e,Toast.LENGTH_LONG).show();
        }
    }

    public long insertData(String name,String birthdate,String title,String message,String phone,String alarm_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(BIRTH_DATE,birthdate);
        contentValues.put(TITlE,title);
        contentValues.put(MESSAGE,message);
        contentValues.put(PHONE,phone);
        contentValues.put(ALARM_ID,alarm_id);
        long rowId=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowId;

    }

    public Cursor displayAllData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(" SELECT * FROM "+TABLE_NAME,null);
        return  cursor;
    }

    public boolean updateDate(String id,String name,String birthdate,String title,String message,String phone,String alarm_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(BIRTH_DATE,birthdate);
        contentValues.put(TITlE,title);
        contentValues.put(MESSAGE,message);
        contentValues.put(PHONE,phone);
        contentValues.put(ALARM_ID,alarm_id);

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID+"=?",new String[]{id});
        return true;

    }

    public Integer deleteData(String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,ID+"=?",new String[]{id});
    }
}
