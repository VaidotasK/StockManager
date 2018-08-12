package com.example.android.stockmanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.stockmanager.data.BookContract.BookEntry;


/**
 * {@link ContentProvider} for Pets app.
 */
public class BookProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    //TODO add int constants for matching uri 
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int BOOKS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int BOOK_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        //TODO add uris
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);

    }

    /**
     * Database helper object
     */
    private BookDbHelper bookDbHelper;


    @Override
    public boolean onCreate() {
        bookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = bookDbHelper.getReadableDatabase();

        // Cursor will hold result of the query
        Cursor cursor;

        // Match uri to specific code, if not found throw an exception
        int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:

                cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                //For every ? mark we need to prepare 1 String in selectionArgs
                selection = BookEntry._ID + "=?";
                // From last path's part takes ID and stores in string array.
                // For WHERE selection = value, where value is string from StringArray (selectionArgs)
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the books table with specific ID number
                // returns cursor containing that row of the table.
                cursor = db.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a book into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertBook(Uri uri, ContentValues values) {
        // Get writable database
        SQLiteDatabase db = bookDbHelper.getWritableDatabase();

        // Insert the new book with the given values
        long id = db.insert(BookEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (id == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            Toast.makeText(getContext(), "Error with saving book", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(getContext(), "Book was saved", Toast.LENGTH_SHORT).show();
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}