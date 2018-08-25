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
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        if (null != getContext()) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
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

        // Take value of every column and store in new variables, then check if they are valid
        String author = values.getAsString(BookEntry.COLUMN_BOOK_AUTHOR);
        String productName = values.getAsString(BookEntry.COLUMN_BOOK_PRODUCT_NAME);
        Integer year = values.getAsInteger(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR);
        String language = values.getAsString(BookEntry.COLUMN_BOOK_LANGUAGE);
        Integer type = values.getAsInteger(BookEntry.COLUMN_BOOK_TYPE);
        Integer price = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
        Integer quantity = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
        Integer availability = values.getAsInteger(BookEntry.COLUMN_BOOK_AVAILABILITY);
        String supplierName = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        Integer supplierPhone = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);

        //TODO fill validation closes
        if (author == null) {
            throw new IllegalArgumentException("Book requires an author");
        }

        // Insert the new book with the given values
        long id = db.insert(BookEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (id == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            Toast.makeText(getContext(), "Error with saving book", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            // Notify all listeners that the data has changed for the pet content URI
            if (null != getContext()) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(getContext(), "Book was saved", Toast.LENGTH_SHORT).show();
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                // For the BOOK_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update books in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more books).
     * Return the number of rows that were successfully updated.
     */
    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        //TODO do all the sanity checks
        // If the {@link BookEntry#COLUMN_BOOK_AUTHOR} key is present,
        // check that the name value is not null.
        if (values.containsKey(BookEntry.COLUMN_BOOK_AUTHOR)) {
            String name = values.getAsString(BookEntry.COLUMN_BOOK_AUTHOR);
            if (name == null) {
                throw new IllegalArgumentException("Book requires author");
            }
        }


        // Otherwise, get writable database to update the data
        SQLiteDatabase db = bookDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = db.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (0 < rowsUpdated) {
            if (null != getContext()) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = bookDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = db.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (0 < rowsDeleted) {
            if (null != getContext()) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        //Return number of deleted rows
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


}