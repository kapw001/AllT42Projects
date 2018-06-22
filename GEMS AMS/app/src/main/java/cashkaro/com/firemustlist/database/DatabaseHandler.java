package cashkaro.com.firemustlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cashkaro.com.firemustlist.model.PersonData;
import cashkaro.com.firemustlist.model.SchoolList;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 21/8/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "visitorinfo";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_Stud_class = "Stud_class";
    private static final String KEY_Photo = "Photo";
    private static final String KEY_Name = "Name";
    private static final String KEY_Type = "Type";
    private static final String KEY_Id = "PersonDataId";
    private static final String KEY_Is_safe = "Is_safe";
    private static final String KEY_server_is_safe_updated = "server_is_safe_updated";


    // Contacts table name
    private static final String TABLE_SCHOOLLIST = "schoollist";

    // Contacts Table Columns names
    private static final String KEY_SCHOOLID = "schoolid";
    private static final String KEY_SCHOOLNAME = "schoolname";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_SCHOOLLIST = "CREATE TABLE "
            + TABLE_SCHOOLLIST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_SCHOOLID
            + " INTEGER," + KEY_SCHOOLNAME + " TEXT" + ")";

    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_SCHOOLID + " INTEGER,"
            + KEY_Stud_class + " TEXT,"
            + KEY_Photo + " VARCHAR,"
            + KEY_Name + " TEXT,"
            + KEY_Type + " TEXT,"
            + KEY_Id + " INTEGER,"
            + KEY_Is_safe + " boolean NOT NULL default 0,"
            + KEY_server_is_safe_updated + " boolean NOT NULL default 0" + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_TABLE_SCHOOLLIST);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOLLIST);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addPersonData(PersonData contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Name, contact.getName()); // Contact Name
//        values.put(KEY_Photo, contact.getPhoto().toString()); // Contact Phone
        values.put(KEY_Stud_class, contact.getStudClass()); // Contact Phone
        values.put(KEY_Type, contact.getType()); // Contact Phone
        values.put(KEY_Id, contact.getId()); // Contact Phone
        values.put(KEY_Is_safe, contact.getIsSafe()); // Contact Phone
        values.put(KEY_server_is_safe_updated, contact.getServer_is_safe_updated()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


    // Adding new contact
    public void addAllSchoolList(List<SchoolList> list) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            SchoolList contact = list.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_SCHOOLNAME, contact.getName());
            values.put(KEY_SCHOOLID, contact.getId());
            db.insert(TABLE_SCHOOLLIST, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close(); // Closing database connection

    }

    // Getting All Contacts
    public List<SchoolList> getAllSchoolList() {
        List<SchoolList> contactList = new ArrayList<SchoolList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHOOLLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SchoolList schoolList = new SchoolList();
                schoolList.setId(cursor.getInt(1));
                schoolList.setName(cursor.getString(2));
                contactList.add(schoolList);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Adding new contact
    public void addAllContact(List<PersonData> list) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            PersonData contact = list.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_SCHOOLID, contact.getSchoolId());
            values.put(KEY_Name, contact.getName()); // Contact Name
            try {

                String p = contact.getPhoto().toString() != null ? contact.getPhoto().toString() : "";
                values.put(KEY_Photo, p);
            } catch (NullPointerException e) {
                Log.e(TAG, "addAllContact: " + e.getMessage());
                values.put(KEY_Photo, "");
            }
            // Contact Phone
            values.put(KEY_Stud_class, contact.getStudClass()); // Contact Phone
            values.put(KEY_Type, contact.getType()); // Contact Phone
            values.put(KEY_Id, contact.getId()); // Contact Phone
            int safeflag = (contact.getIsSafe()) ? 1 : 0;
            values.put(KEY_Is_safe, safeflag); // Contact Phone
            int server_is_safe_updatedflag = (contact.getServer_is_safe_updated()) ? 1 : 0;
            values.put(KEY_server_is_safe_updated, server_is_safe_updatedflag); // Contact Phone

            // Inserting Row
            db.insert(TABLE_CONTACTS, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close(); // Closing database connection

    }


    // Getting single contact
    PersonData getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_Stud_class, KEY_Photo, KEY_Name, KEY_Type, KEY_Id, KEY_Is_safe, KEY_server_is_safe_updated,}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        boolean is_safe = cursor.getInt(7) > 0;
        boolean server_is_safe_updated = cursor.getInt(8) > 0;

        PersonData contact = new PersonData(
                cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), is_safe, server_is_safe_updated);
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<PersonData> getAllPersonData(int schoolid) {
        List<PersonData> contactList = new ArrayList<PersonData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " where " + KEY_SCHOOLID + "=" + schoolid;

        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PersonData contact = new PersonData();
                contact.setSchoolId(cursor.getInt(1));
                contact.setStudClass(cursor.getString(2));
                contact.setPhoto(cursor.getString(3));
                contact.setName(cursor.getString(4));
                contact.setType(cursor.getString(5));
                contact.setId(cursor.getInt(6));
                boolean is_safe = cursor.getInt(7) > 0;
                boolean server_is_safe_updated = cursor.getInt(8) > 0;
                contact.setIsSafe(is_safe);
                contact.setServer_is_safe_updated(server_is_safe_updated);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

//        db.setTransactionSuccessful();
//        db.endTransaction();
        cursor.close();
        db.close();

        // return contact list
        return contactList;
    }


    // Getting All Contacts
    public List<PersonData> getAllPersonDataBasedOnServerUpdated() {
        List<PersonData> contactList = new ArrayList<PersonData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " where " + KEY_server_is_safe_updated + "=1";

        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PersonData contact = new PersonData();
                contact.setSchoolId(cursor.getInt(1));
                contact.setStudClass(cursor.getString(2));
                contact.setPhoto(cursor.getString(3));
                contact.setName(cursor.getString(4));
                contact.setType(cursor.getString(5));
                contact.setId(cursor.getInt(6));
                boolean is_safe = cursor.getInt(7) > 0;
                boolean server_is_safe_updated = cursor.getInt(8) > 0;
                contact.setIsSafe(is_safe);
                contact.setServer_is_safe_updated(server_is_safe_updated);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

//        db.setTransactionSuccessful();
//        db.endTransaction();
        cursor.close();
        db.close();

        // return contact list
        return contactList;
    }


    // Updating single contact
    public int updatePersonData(PersonData contact) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Name, contact.getName()); // Contact Name
        try {
            values.put(KEY_Photo, contact.getPhoto().toString()); // Contact Phone
        } catch (NullPointerException e) {
            values.put(KEY_Photo, "");

        }
        values.put(KEY_Stud_class, contact.getStudClass()); // Contact Phone
        values.put(KEY_Type, contact.getType()); // Contact Phone
        values.put(KEY_Id, contact.getId()); // Contact Phone
        int safeflag = (contact.getIsSafe()) ? 1 : 0;
        values.put(KEY_Is_safe, safeflag); // Contact Phone
        int server_is_safe_updatedflag = (contact.getServer_is_safe_updated()) ? 1 : 0;
        values.put(KEY_server_is_safe_updated, server_is_safe_updatedflag); // Contact Phone

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_Id + " = ?",
                new String[]{String.valueOf(contact.getId())});


    }


    // Updating single contact
    public void updateAllPersonData(List<PersonData> list) {


        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            PersonData contact = list.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_SCHOOLID, contact.getSchoolId());
            values.put(KEY_Name, contact.getName()); // Contact Name
            try {
                values.put(KEY_Photo, contact.getPhoto().toString()); // Contact Phone
            } catch (NullPointerException e) {
                values.put(KEY_Photo, "");

            }
            values.put(KEY_Stud_class, contact.getStudClass()); // Contact Phone
            values.put(KEY_Type, contact.getType()); // Contact Phone
            values.put(KEY_Id, contact.getId()); // Contact Phone
            int safeflag = (contact.getIsSafe()) ? 1 : 0;
            values.put(KEY_Is_safe, safeflag); // Contact Phone
            int server_is_safe_updatedflag = (contact.getServer_is_safe_updated()) ? 1 : 0;
            values.put(KEY_server_is_safe_updated, server_is_safe_updatedflag); // Contact Phone

            // updating row
            int id = db.update(TABLE_CONTACTS, values, KEY_Id + " = ?",
                    new String[]{String.valueOf(contact.getId())});
            Log.e(TAG, "updateAllPersonData: " + id);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


    }


    // Deleting single contact
    public void deleteContact(PersonData contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_Id + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }


    // Deleting single contact
    public void deleteBasedOnSchoolId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        db.delete(TABLE_CONTACTS, KEY_SCHOOLID + " = ?",
                new String[]{String.valueOf(id)});
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public void deleteAllRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();

        Log.e("Record Deleted ", "deleteAllRecord: ");
    }

    public void deleteAllSchoolListRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHOOLLIST, null, null);
        db.close();

        Log.e("Record Deleted ", "deleteAllRecord: ");
    }

    // Getting contacts Count
    public int getVisitorCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}