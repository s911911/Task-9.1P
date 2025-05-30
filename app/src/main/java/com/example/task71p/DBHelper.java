package com.example.task71p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "lost_found.db";
    private static final int    DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "postType TEXT," +
                        "personName TEXT," +
                        "phone TEXT," +
                        "description TEXT," +
                        "date TEXT," +
                        "location TEXT," +
                        "latitude REAL," +
                        "longitude REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }

    public void insertItem(String postType, String name, String phone,
                           String description, String date, String location,
                           double lat, double lng) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("postType",    postType);
        cv.put("personName",  name);
        cv.put("phone",       phone);
        cv.put("description", description);
        cv.put("date",        date);
        cv.put("location",    location);
        cv.put("latitude",    lat);
        cv.put("longitude",   lng);
        db.insert("items", null, cv);
    }

    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM items", null);
        while (c.moveToNext()) {
            list.add(new Item(
                    c.getInt(0),    // id
                    c.getString(1), // postType
                    c.getString(2), // personName
                    c.getString(3), // phone
                    c.getString(4), // desc
                    c.getString(5), // date
                    c.getString(6), // loc
                    c.getDouble(7), // lat
                    c.getDouble(8)  // lng
            ));
        }
        c.close();
        return list;
    }

    public Item getItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE id=?", new String[]{String.valueOf(id)});
        Item item = null;
        if (cursor.moveToFirst()) {
            item = new Item(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7),    // latitude
                    cursor.getDouble(8)     // longitude
            );
        }
        cursor.close();
        return item;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "id=?", new String[]{String.valueOf(id)});
    }
}

