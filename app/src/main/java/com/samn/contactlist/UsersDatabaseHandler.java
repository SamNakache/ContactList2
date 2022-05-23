
package com.samn.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class UsersDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usersManager";
    private static final String TABLE_USERS = "users";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    public UsersDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase dbase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USERNAME + " TEXT, "+ KEY_PASSWORD + " TEXT" + ")";

        dbase.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername()); // Username
        values.put(KEY_PASSWORD, user.getPassword()); // Username

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    // code to get all contacts in a list view
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUsername(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                // Adding user to list
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }

    public boolean confirmed(String username, String password){
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "+KEY_USERNAME+"=? " +
                "AND "+KEY_PASSWORD+"=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(username), String.valueOf(password)});

        // looping through all rows and adding to list
        return cursor.moveToFirst();
    }

    public boolean isExist(String username){
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "+KEY_USERNAME+"=? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(username)});

        // looping through all rows and adding to list
        return cursor.moveToFirst();
    }



    // Deleting single contact
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USERNAME + " = ? AND " + KEY_PASSWORD + " = ?",
                new String[] { String.valueOf(user.getUsername()), String.valueOf(user.getPassword())});
        db.close();
    }

}