package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDatabase";
    private static final int DATABASE_VERSION = 1;
    // Table names and columns
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_ENTRIES = "Entries";
    // Recycle Bin table name and columns
    private static final String TABLE_RECYCLE_BIN = "RecycleBin";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_URL = "url";


    // Create Recycle Bin table SQL query
    private static final String CREATE_RECYCLE_BIN_TABLE = "CREATE TABLE " + TABLE_RECYCLE_BIN + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_URL + " TEXT"
            + ")";

    // Create table SQL queries
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_ENTRIES_TABLE = "CREATE TABLE " + TABLE_ENTRIES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_URL + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ENTRIES_TABLE);
        db.execSQL(CREATE_RECYCLE_BIN_TABLE); // Create recycle bin table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECYCLE_BIN);
        onCreate(db);
    }
    public void deleteFromRecycleBin(int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        int rows = database.delete(TABLE_RECYCLE_BIN, COLUMN_ID+"=?", new String[]{id+""});
        database.close();
    }

    public boolean restoreEntryFromRecycleBin(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_RECYCLE_BIN, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        boolean restored = false;

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            // Check if the column exists before retrieving its value
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int urlIndex = cursor.getColumnIndex(COLUMN_URL);

            if (usernameIndex >= 0 && passwordIndex >= 0 && urlIndex >= 0) {
                values.put(COLUMN_USERNAME, cursor.getString(usernameIndex));
                values.put(COLUMN_PASSWORD, cursor.getString(passwordIndex));
                values.put(COLUMN_URL, cursor.getString(urlIndex));

                // Insert into main table
                long result = db.insert(TABLE_ENTRIES, null, values);

                if (result != -1) {
                    restored = true;
                }
            }

            cursor.close();

            if (restored) {
                // Delete the restored entry from the recycle bin table
                db.delete(TABLE_RECYCLE_BIN, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            }
        }

        db.close();
        return restored;
    }


    // Method to delete a password entry and move it to the recycle bin
    public int deleteEntryAndMoveToRecycleBin(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ENTRIES,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        int deletedRows = 0;
        ContentValues values = new ContentValues();
        if (cursor != null && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int urlIndex = cursor.getColumnIndex(COLUMN_URL);

            if (usernameIndex >= 0 && passwordIndex >= 0 && urlIndex >= 0) {
                values.put(COLUMN_USERNAME, cursor.getString(usernameIndex));
                values.put(COLUMN_PASSWORD, cursor.getString(passwordIndex));
                values.put(COLUMN_URL, cursor.getString(urlIndex));

                cursor.close();

                // Insert into recycle bin
                long result = db.insert(TABLE_RECYCLE_BIN, null, values);

                // Delete from main table
                deletedRows = db.delete(TABLE_ENTRIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            } else {
                // Handle the case where one or more columns are missing
                Log.e("DeleteAndMoveToRecycleBin", "One or more columns not found in the cursor result set.");
            }
        } else {
            // Handle the case where the cursor is null or empty
            Log.e("DeleteAndMoveToRecycleBin", "Cursor is null or empty.");
        }

        db.close();
        return deletedRows;
    }



    public int updateEntry(int id, String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_URL, url);
        int updatedRows = db.update(TABLE_ENTRIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return updatedRows;
    }


    public long addEntry(String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_URL, url);
        long result = db.insert(TABLE_ENTRIES, null, values);
        db.close();
        return result;
    }

    public ArrayList<passwordEntryModel> readAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<passwordEntryModel> records = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ENTRIES, null);
        int id_Index = cursor.getColumnIndex(COLUMN_ID);
        int username_Index = cursor.getColumnIndex(COLUMN_USERNAME);
        int password_Index = cursor.getColumnIndex(COLUMN_PASSWORD);
        int URL_Index = cursor.getColumnIndex(COLUMN_URL);

        if(cursor.moveToFirst())
        {
            do{
                passwordEntryModel c = new passwordEntryModel();

                c.setId(cursor.getInt(id_Index));
                c.setUsername(cursor.getString(username_Index));
                c.setPassword(cursor.getString(password_Index));
                c.setURL(cursor.getString(URL_Index));

                records.add(c);
            }while(cursor.moveToNext());
        }

        cursor.close();


        db.close();
        return records;
    }

    public ArrayList<passwordEntryModel> readRecyclebin() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<passwordEntryModel> records = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_RECYCLE_BIN, null);
        int id_Index = cursor.getColumnIndex(COLUMN_ID);
        int username_Index = cursor.getColumnIndex(COLUMN_USERNAME);
        int password_Index = cursor.getColumnIndex(COLUMN_PASSWORD);
        int URL_Index = cursor.getColumnIndex(COLUMN_URL);

        if(cursor.moveToFirst())
        {
            do{
                passwordEntryModel c = new passwordEntryModel();

                c.setId(cursor.getInt(id_Index));
                c.setUsername(cursor.getString(username_Index));
                c.setPassword(cursor.getString(password_Index));
                c.setURL(cursor.getString(URL_Index));

                records.add(c);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the user already exists
        if (!isUserExists(db, username)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD, password);
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        } else {
            // User already exists, return -1 (indicating failure)
            db.close();
            return -1;
        }
    }

    // Helper method to check if a user exists in the database
    private boolean isUserExists(SQLiteDatabase db, String username) {
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }


    // Method to check if a user exists and validate credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null,
                null,
                null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return userExists;
    }
}

