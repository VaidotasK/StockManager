package com.example.android.stockmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.example.android.stockmanager.data.BookContract.BookEntry;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();


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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query. "*" value means, all
        String[] projection = {
                "*"};

        // Perform a query on books table
        Cursor cursor = getContentResolver().query(BookEntry.CONTENT_URI,
                projection,      // Columns to return
                null,     // Columns for the WHERE clause
                null,  // Values for the WHERE clause
                null);   // Sort order

        ListView listView = findViewById(R.id.list_view_books);
        BookCursorAdapter adapter = new BookCursorAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    //TODO change insertBook to private method
    public void insertBook() {
        // Create a ContentValues object where column names are the keys,
        // and info about book is values.
        //Dummy data was used
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, "Author");
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, "Book");
        values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, 2000);
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, "Language");
        values.put(BookEntry.COLUMN_BOOK_TYPE, 2);
        values.put(BookEntry.COLUMN_BOOK_PRICE, 12);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 1);
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, 1);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Supplier");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, 863838484);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

    }
}
