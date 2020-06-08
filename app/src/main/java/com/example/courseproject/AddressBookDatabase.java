package com.example.courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressBookDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "addressBook.db";
    private static final String TABLE_ACCOUNT = "Accounts";
    private static final String[] COLUMNS_ACCOUNT = {"UserId","Username","Password"};
    private static final String TABLE_CONTACT = "Contacts";
    private static final String[] COLUMNS_CONTACT = {"ContactId","Name","Address","City","Province","PostalCode","PhoneOne","PhoneTwo","PhoneThree","UserId"};

    @SuppressWarnings("All")
    public AddressBookDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String createAccounts =
                "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                "   UserId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   Username TEXT," +
                "   Password TEXT " +
                ");";
        String createContacts =
                "CREATE TABLE " + TABLE_CONTACT + " (" +
                "   ContactId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   Name TEXT," +
                "   Address TEXT," +
                "   City TEXT, " +
                "   Province TEXT," +
                "   PostalCode TEXT," +
                "   PhoneOne TEXT," +
                "   PhoneTwo TEXT," +
                "   PhoneThree TEXT," +
                "   UserId INTEGER," +
                "   FOREIGN KEY(UserId) REFERENCES Accounts(UserId)" +
                ");";
        db.execSQL(createAccounts);
        db.execSQL(createContacts);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete the tables when the database gets updated
        db.execSQL("DROP TABLE IF EXISTS Accounts");
        db.execSQL("DROP TABLE IF EXISTS Contacts");
        this.onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //will return the users Id on successfull login, -1 on fail
    @SuppressWarnings("All")
    public int validLogin(String username, String password) {
        try {
            String arguments[] = {username, password};

            SQLiteDatabase databaseRead = this.getReadableDatabase();
            Cursor cursor = databaseRead.query(TABLE_ACCOUNT, COLUMNS_ACCOUNT, COLUMNS_ACCOUNT[1] + " = ? AND " + COLUMNS_ACCOUNT[2] + " = ?", arguments, null, null, null, "1");

            if(cursor.getCount() != 0) {
                //an account exists with those credentials, log in successfull
                cursor.moveToFirst();
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMNS_ACCOUNT[0]));

                cursor.close();
                databaseRead.close();
                return userId;
            }
            else {
                //no account exists with those credentials, login failed
                cursor.close();
                databaseRead.close();
                return -1;
            }
        }
        catch (Exception e) {
            return -1;
        }
    }

    //will add a new unique user to the database and then log in, returning their user Id, -1 on failure
    @SuppressWarnings("All")
    public int validSignUp(String username, String password) {
        try {
            String arguments[] = {username};

            SQLiteDatabase databaseRead = this.getReadableDatabase();
            Cursor cursor = databaseRead.query(TABLE_ACCOUNT, COLUMNS_ACCOUNT, COLUMNS_ACCOUNT[1] + " = ?", arguments, null, null, null, "1");

            if(cursor.getCount() != 0) {
                //an account exists with that username, sign up failed
                cursor.close();
                databaseRead.close();
                return -1;
            }
            else {
                //no account exists with that username, insert new user account
                cursor.close();
                databaseRead.close();

                ContentValues values = new ContentValues();

                values.put(COLUMNS_ACCOUNT[1], username);
                values.put(COLUMNS_ACCOUNT[2], password);

                SQLiteDatabase databaseWrite = this.getWritableDatabase();
                databaseWrite.insert(TABLE_ACCOUNT, null, values);
                databaseWrite.close();

                //now login and return the userId
                return validLogin(username, password);
            }
        }
        catch(Exception e) {
            return -1;
        }
    }

    //will return Contact[] of all of the users contacts in their address book
    @SuppressWarnings("All")
    public Contact[] getUsersAddressBook(int userId) {
        try {
            String arguments[] = {Integer.toString(userId)};
            SQLiteDatabase databaseRead = this.getReadableDatabase();

            Cursor cursor = databaseRead.query(TABLE_CONTACT, COLUMNS_CONTACT, COLUMNS_CONTACT[9] + " = ?", arguments, null, null, null, null);

            Contact contacts[] = new Contact[cursor.getCount()];

            int contactId;
            String name;
            String address;
            String city;
            String postalCode;
            String province;
            String phoneOne;
            String phoneTwo;
            String phoneThree;

            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                contactId = cursor.getInt(cursor.getColumnIndex(COLUMNS_CONTACT[0]));
                name = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[1]));
                address = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[2]));
                city = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[3]));
                province = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[4]));
                postalCode = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[5]));
                phoneOne = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[6]));
                phoneTwo = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[7]));
                phoneThree = cursor.getString(cursor.getColumnIndex(COLUMNS_CONTACT[8]));

                contacts[i] = new Contact(contactId, name, address, city, province, postalCode, phoneOne, phoneTwo, phoneThree);

                cursor.moveToNext();
            }

            return contacts;
        }
        catch (Exception e) {
            return null;
        }
    }

    //takes a contact object and writes it to the database as a new entry, returning its new contactId, -1 on failure
    @SuppressWarnings("All")
    public int addNewContact(int userId, Contact contact) {
        try {
            //contact name is unique, add it to the database
            ContentValues values = new ContentValues();

            SQLiteDatabase databaseWrite = this.getWritableDatabase();

            values.put(COLUMNS_CONTACT[1], contact.getName());
            values.put(COLUMNS_CONTACT[2], contact.getAddress());
            values.put(COLUMNS_CONTACT[3], contact.getCity());
            values.put(COLUMNS_CONTACT[4], contact.getProvince());
            values.put(COLUMNS_CONTACT[5], contact.getPostalCode());
            values.put(COLUMNS_CONTACT[6], contact.getPhone1());
            values.put(COLUMNS_CONTACT[7], contact.getPhone2());
            values.put(COLUMNS_CONTACT[8], contact.getPhone3());
            values.put(COLUMNS_CONTACT[9], userId);

            databaseWrite.insert(TABLE_CONTACT, null, values);
            databaseWrite.close();

            //get the new contacts contactId
            String arguments[] = {Integer.toString(userId), contact.getName()};

            SQLiteDatabase databaseRead = this.getReadableDatabase();
            Cursor cursor = databaseRead.query(TABLE_CONTACT, COLUMNS_CONTACT, COLUMNS_CONTACT[9] + " = ? AND " + COLUMNS_CONTACT[1] + " = ?", arguments, null, null, COLUMNS_CONTACT[0] + " DESC", "1");
            cursor.moveToFirst();
            int contactId = cursor.getInt(cursor.getColumnIndex(COLUMNS_CONTACT[0]));

            cursor.close();
            databaseRead.close();

            return contactId;
        }
        catch(Exception e) {
            return -1;
        }
    }

    //takes a contact object and updates am exisiting databsae entry with the new information
    @SuppressWarnings("All")
    public boolean updateExistingContact(Contact contact) {
        try {
            String arguments[] = {Integer.toString(contact.getContactId())};
            ContentValues values = new ContentValues();

            SQLiteDatabase databaseWrite = this.getWritableDatabase();

            values.put(COLUMNS_CONTACT[1], contact.getName());
            values.put(COLUMNS_CONTACT[2], contact.getAddress());
            values.put(COLUMNS_CONTACT[3], contact.getCity());
            values.put(COLUMNS_CONTACT[4], contact.getProvince());
            values.put(COLUMNS_CONTACT[5], contact.getPostalCode());
            values.put(COLUMNS_CONTACT[6], contact.getPhone1());
            values.put(COLUMNS_CONTACT[7], contact.getPhone2());
            values.put(COLUMNS_CONTACT[8], contact.getPhone3());

            databaseWrite.update(TABLE_CONTACT, values, COLUMNS_CONTACT[0] + " = ?", arguments);
            databaseWrite.close();

            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @SuppressWarnings("All")
    public boolean deleteContact(int contactId) {
        try {
            String arguments[] = {Integer.toString(contactId)};

            SQLiteDatabase databaseWrite = this.getWritableDatabase();

            databaseWrite.delete(TABLE_CONTACT, COLUMNS_CONTACT[0] + " = ?", arguments);
            databaseWrite.close();

            return true;
        }
       catch(Exception e) {
            return false;
        }
    }
}
