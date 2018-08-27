package com.example.android2.noteswithsql.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class databaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NAME = "notes_table";
    public static final String COL_1 = "NOTE_ID";
    public static final String COL_2 = "NOTE_TEXT";
    public static final String COL_3 = "NOTE_DATE";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,NOTE_TEXT TEXT, NOTE_DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String note_text,String note_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,note_text);
        contentValues.put(COL_3,note_date);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_NAME,null);
        return cursor;
    }
    public boolean updateData(String note_id,String note_text,String note_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,note_id);
        contentValues.put(COL_2,note_text);
        contentValues.put(COL_3,note_date);
        db.update(TABLE_NAME,contentValues,"NOTE_ID = ?",new String[] { note_id });

        return true;
    }
    public Integer deleteData(String note_id){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME,"NOTE_ID = ?",new String[] { note_id });
    }
}
