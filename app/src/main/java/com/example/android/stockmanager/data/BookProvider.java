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

import com.example.android.stockmanager.R;
import com.example.android.stockmanager.data.BookContract.BookEntry;


/**
 * {@link ContentProvider} for Book app.
 */
public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();
    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

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
     * Insert a book into the database with the given content values. Return new content URI
     * for that specific row in the database.
     */
    private Uri insertBook(Uri uri, ContentValues values) {
        SQLiteDatabase db = bookDbHelper.getWritableDatabase();
        // Insert the new book with the given values
        long id = db.insert(BookEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            Toast.makeText(getContext(), R.string.error_msg_while_inserting_book, Toast.LENGTH_SHORT).show();
            return null;
        } else {
            // Notify all listeners that the data has changed for the book content URI
            if (null != getContext()) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
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
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update book in database
     */
    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Check if there are values, if not, don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
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

    //Delete the book
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