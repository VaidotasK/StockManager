package com.example.android.stockmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.stockmanager.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 1;

    // String with SQL statement to delete table if it exists
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME + ";";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String that contains the SQL statement for creating the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PUBLICATION_YEAR + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_LANGUAGE + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_TYPE + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.COLUMN_BOOK_AVAILABILITY + " INTEGER NOT NULL DEFAULT 1, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        Log.v(LOG_TAG, " Books.db database create statement: " + SQL_CREATE_BOOKS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        Log.v(LOG_TAG, " Books.db database was deleted, statement: " + SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
