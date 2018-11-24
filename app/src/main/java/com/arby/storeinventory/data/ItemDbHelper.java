package com.arby.storeinventory.data;

/**
 * Created by rares on 25.08.2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arby.storeinventory.data.ItemContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                    ItemEntry.COLUMN_ITEM_PRICE + " TEXT NOT NULL DEFAULT 0, " +
                    ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER DEFAULT 0, " +
                    ItemEntry.COLUMN_ITEM_SUPPLIER + " TEXT NOT NULL, " +
                    ItemEntry.COLUMN_ITEM_IMAGE + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
