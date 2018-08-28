package com.example.android.stockmanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.stockmanager.data.BookContract.BookEntry;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 1;

    private BookCursorAdapter cursorAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        //Add empty view to list view
        listView = findViewById(R.id.list_view_books);
        View emptyView = findViewById(R.id.empty_view);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        listView.setEmptyView(emptyView);

        cursorAdapter = new BookCursorAdapter(this, null);
        listView.setAdapter(cursorAdapter);

        //Set onClickListener to open clicked list item(book) in Editor mode
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.menu_main_add_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.menu_main_delete_all_entries:
                showDeleteAllConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertBook() {
        // Create a ContentValues object where column names are the keys,
        // and info about book is values.
        //Dummy data was used
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, "Vaidotas K.");
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, "THE Book");
        values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, 2000);
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, "Lithuanian");
        values.put(BookEntry.COLUMN_BOOK_TYPE, 0);
        values.put(BookEntry.COLUMN_BOOK_PRICE, 9);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 10);
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, 1);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Vaidotas");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, 863838494L);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete_dialog_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllBooks();
            }
        });
        builder.setNegativeButton(R.string.delete_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Helper method to delete all books in the database.
     */
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        if (0 == rowsDeleted) {
            Toast.makeText(this, getString(R.string.main_delete_all_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.main_delete_all_successful), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query. In projection can use "*" value which means, all
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_PRODUCT_NAME,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_PUBLICATION_YEAR,
                BookEntry.COLUMN_BOOK_QUANTITY};
        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }
}
