package com.example.complaintclose.Sqlite_Files;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class countrynamedb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBdropdowndata";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "countrynamedb";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARTYNAME = "countryname";

    public countrynamedb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PARTYNAME + " TEXT);";
        db.execSQL(createTableQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS tbl_reminder";
        db.execSQL(query);
        onCreate(db);

    }

    public String insertdata(String name) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PARTYNAME, name);

        float result = database.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }

    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);  // Recreate the table if needed
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public Cursor getdata() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from "+TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}
