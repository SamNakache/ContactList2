
package com.samn.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class ContactsDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_GENDER = "gender";

    public ContactsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase dbase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT,"
                + KEY_PH_NO + " TEXT, "+ KEY_USERNAME + " TEXT," + KEY_GENDER + " TEXT" + ")";

        dbase.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, contact.get_firstname()); // Contact FirstName
        values.put(KEY_LASTNAME, contact.get_lastname()); // Contact LastName
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(KEY_USERNAME, contact.get_username()); // Username
        values.put(KEY_GENDER, contact.get_gender()); // Gender

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    // code to get all contacts in a list view
    public List<Contact> getAllContacts(String username) {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "+KEY_USERNAME+"=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(username)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.set_firstname(cursor.getString(1));
                contact.set_lastname(cursor.getString(2));
                contact.setPhoneNumber(cursor.getString(3));
                contact.set_username(cursor.getString(4));
                contact.set_gender(cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public void updateContact(Contact prevContact, Contact newContact) {
        this.deleteContact(prevContact);
        this.addContact(newContact);

    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_FIRSTNAME + " = ? AND " + KEY_LASTNAME + " = ? AND " +
                        KEY_PH_NO + " = ? AND " + KEY_USERNAME + " = ?",
                new String[] { String.valueOf(contact.get_firstname()), String.valueOf(contact.get_lastname()),
                        String.valueOf(contact.getPhoneNumber()), String.valueOf(contact.get_username())});
        db.close();
    }

}