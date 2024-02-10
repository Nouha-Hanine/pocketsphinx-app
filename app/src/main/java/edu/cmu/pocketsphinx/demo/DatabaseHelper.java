package edu.cmu.pocketsphinx.demo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, DBname, null, 1);
    }

    public static final String JOKES_TABLE_NAME = "jokes";
    public static final String JOKES_COLUMN_ID = "id";
    public static final String JOKES_COLUMN_CONTENT = "content";
    public static final String TIMETABLE_TABLE_NAME = "timetable";
    public static final String TIMETABLE_COLUMN_ID = "id";
    public static final String TIMETABLE_COLUMN_SUBJECT = "subject";
    public static final String TIMETABLE_COLUMN_DAY = "day";
    public static final String TIMETABLE_COLUMN_TIME = "time";
    public static final String TIMETABLE_COLUMN_ROOM = "room";


    public static final String DBname= "signin.db";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table users(email TEXT primary key, username TEXT, password TEXT)"
        );
        db.execSQL(
                "CREATE TABLE " + JOKES_TABLE_NAME + " (" +
                        JOKES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        JOKES_COLUMN_CONTENT + " TEXT)"
        );
        insertJoke( "Why did the chicken cross the road? To get to the other side.");
        insertJoke( "What do you call cheese that isn't yours? Nacho cheese.");
        insertJoke( "What did 0 say to 8? Nice belt!.");
        insertJoke( "What does a spy do when he is cold? He goes undercover.");
        insertJoke( "What do you call a sad cup of coffee? Depresso.");
        insertJoke( " What did 50 Cent do when he was hungry? 58!");
        insertJoke( "What does a baby computer call his father? Data!");
        insertJoke( "How do celebrities stay cool? They have many fans.");
        insertJoke( "Why was six afraid of seven? Because 7-8-9");
        insertJoke( "What do you call a magician that looses his magic? Ian.");

        db.execSQL(
                "CREATE TABLE " + TIMETABLE_TABLE_NAME + " (" +
                        TIMETABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TIMETABLE_COLUMN_SUBJECT + " TEXT, " +
                        TIMETABLE_COLUMN_DAY+ " TEXT, " +
                        TIMETABLE_COLUMN_TIME + " TEXT,"+
                        TIMETABLE_COLUMN_ROOM + " TEXT)"
        );
        insertTimetableEntry("is", "Tuesday", "11:30 AM", "Td Room 12");
        insertTimetableEntry( "rip", "Monday", "08:30 AM", "Td Room 9");
        insertTimetableEntry("is", "Monday", "10:00 AM", "Td Room 7");
        insertTimetableEntry("rip", "Monday", "11:30 AM", "Lab 2");
        insertTimetableEntry("iam", "Monday", "14:00 PM", "Td Room 5");
        insertTimetableEntry("abd", "Monday", "15:30 PM", "Td Room 20");


        insertTimetableEntry("ro", "Tuesday", "08:00 AM", "Amphi 1");
        insertTimetableEntry("ro", "Tuesday", "09:30 AM", "Lab 1");
        insertTimetableEntry("is", "Tuesday", "11:30 AM", "Td Room 12");

        insertTimetableEntry("rip", "Wednesday", "08:00 PM", "amphi 1");
        insertTimetableEntry("comp", "Wednesday", "10:00 AM", "Td Room 18");
        insertTimetableEntry("gl2", "Wednesday", "14:00 PM", "Td Room 19");
        insertTimetableEntry("rip", "Wednesday", "16:00 PM", "Lab 2");

        insertTimetableEntry("abd", "Thursday", "09:00 AM", "Td Room 22");
        insertTimetableEntry("iam", "Thursday", "11:30 AM", "Td Room 30");
        insertTimetableEntry("ro", "Thursday", "14:00 PM", "Lab 1");

        insertTimetableEntry("is", "Saturday", "10:00 AM", "Td Room 3");
        insertTimetableEntry("comp", "Saturday", "13:00 PM", "Td Room 10");
        insertTimetableEntry("gl2", "Saturday", "15:00 PM", "Amphi 2");

        //TODO add more timetables DONE ?
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "drop table if exists users"
        );
    }

    public boolean insertUser(String email, String username, String password){
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = DB.insert("users", null, contentValues);
    if(result== -1) {return false;}else{return true;}//check if success or not
    }

    //we check if account already exist?
    public boolean checkEmail(String email){
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor = DB.rawQuery(
                "select * from users where email =?", new String[]{email}
        );
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean checkAccount(String username, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery(
            "select * from users where username = ? and password =?",  new String[]{username, password}
        );
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    public void insertJoke(String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JOKES_COLUMN_CONTENT, content);
        db.insert(JOKES_TABLE_NAME, null, contentValues);
    }


    public void insertTimetableEntry(String subject,String day ,String time , String room) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMETABLE_COLUMN_SUBJECT, subject);
        contentValues.put(TIMETABLE_COLUMN_DAY, day);
        contentValues.put(TIMETABLE_COLUMN_TIME, time);
        contentValues.put(TIMETABLE_COLUMN_ROOM, room);
        db.insert(TIMETABLE_TABLE_NAME, null, contentValues);
    }
    @SuppressLint("Range")
    public String getJoke() {
        SQLiteDatabase db = getReadableDatabase();
        String joke = null;

        Cursor cursor = db.query(JOKES_TABLE_NAME, new String[]{JOKES_COLUMN_CONTENT},
                null, null, null, null, "RANDOM()", "1");

        if (cursor != null && cursor.moveToFirst()) {
            joke = cursor.getString(cursor.getColumnIndex(JOKES_COLUMN_CONTENT));
            cursor.close();
        }

        return joke;
    }
    @SuppressLint("Range")
    public String getCurrentStudySubject() {
        //TODO use both ucrrent time and day to have the right next study sub DONE ?

        String currentTime = getCurrentTime();
        String currentDay= getCurrentDay();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + TIMETABLE_COLUMN_SUBJECT + ", " + TIMETABLE_COLUMN_ROOM +
                " FROM " + TIMETABLE_TABLE_NAME +
                " WHERE " + TIMETABLE_COLUMN_DAY + " = ? AND " + TIMETABLE_COLUMN_TIME + " <= ?" +
                " ORDER BY " + TIMETABLE_COLUMN_TIME + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{currentDay, currentTime});

        String subject = null;
        String room = null;

        if (cursor.moveToFirst()) {
            subject = cursor.getString(cursor.getColumnIndex(TIMETABLE_COLUMN_SUBJECT));
            room = cursor.getString(cursor.getColumnIndex(TIMETABLE_COLUMN_ROOM));
        }

        cursor.close();
        return (subject != null && room != null) ? subject + " in " + room : null;
    }
     String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dF= new SimpleDateFormat("EEEE", Locale.getDefault());
        return dF.format(calendar.get((Calendar.DATE)));
    }

    //TODO add a getcurrent day func DONE- ?
}
