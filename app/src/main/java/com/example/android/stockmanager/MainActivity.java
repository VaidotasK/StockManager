package com.example.android.stockmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.stockmanager.data.ManagerContract.ManagerEntry;
import com.example.android.stockmanager.data.ManagerDbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ManagerDbHelper managerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO add floating button for entering editor activity

        managerDbHelper = new ManagerDbHelper(this);

        //Button for testing - add dummy data
        Button buttonAdd = findViewById(R.id.button_add_book);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBook();
            }
        });

        //Button for testing refresh button - calls displayDataBase method
        Button buttonDisplay = findViewById(R.id.button_display);
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatabaseInfo();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = managerDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query. "*" value means, all
        String[] projection = {
                "*"};

        // Perform a query on books table
        Cursor cursor = db.query(
                ManagerEntry.TABLE_NAME,   // Table name to query
                projection,                // Columns to return
                null,              // Columns for the WHERE clause
                null,           // Values for the WHERE clause
                null,              // Don't group the rows
                null,               // Don't filter by row groups
                null);             // Sort order

        TextView displayView = findViewById(R.id.book_text_view);

        try {
            displayView.setText("Test if data entries working " + cursor.getCount() + " books.\n\n");
            displayView.append(ManagerEntry._ID + " - " +
                    ManagerEntry.COLUMN_BOOK_AUTHOR + " - " +
                    ManagerEntry.COLUMN_BOOK_PRODUCT_NAME + " - " +
                    ManagerEntry.COLUMN_BOOK_PUBLICATION_YEAR + " - " +
                    ManagerEntry.COLUMN_BOOK_LANGUAGE + " - " +
                    ManagerEntry.COLUMN_BOOK_TYPE + " - " +
                    ManagerEntry.COLUMN_BOOK_PRICE + " - " +
                    ManagerEntry.COLUMN_BOOK_QUANTITY + " - " +
                    ManagerEntry.COLUMN_BOOK_AVAILABILITY + " - " +
                    ManagerEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    ManagerEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + "\n");

            // Get index of each column
            int idColumnIndex = cursor.getColumnIndex(ManagerEntry._ID);
            int authorColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_AUTHOR);
            int productNameColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_PRODUCT_NAME);
            int publicationYearColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_PUBLICATION_YEAR);
            int languageColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_LANGUAGE);
            int typeColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_TYPE);
            int priceColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_QUANTITY);
            int availabilityColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_AVAILABILITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ManagerEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                //Extract data from cursor using column index
                int currentID = cursor.getInt(idColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                int currentPublicationYear = cursor.getInt(publicationYearColumnIndex);
                String currentLanguage = cursor.getString(languageColumnIndex);
                int currentType = cursor.getInt(typeColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentAvailability = cursor.getInt(availabilityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierPhoneNumber = cursor.getInt(supplierPhoneNumberColumnIndex);


                // Display data from each column of the current row in the cursor
                displayView.append(("\n" + currentID + " - " +
                        currentAuthor + " - " +
                        currentProductName + " - " +
                        currentPublicationYear + " - " +
                        currentLanguage + " - " +
                        currentType + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentAvailability + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhoneNumber));
            }
        } finally {
            //Close cursor to free resources;

            cursor.close();
        }
    }
    public void insertBook() {
        // Gets the database in write mode
        SQLiteDatabase db = managerDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and info about book is values.
        //Dummy data was used
        ContentValues values = new ContentValues();
        values.put(ManagerEntry.COLUMN_BOOK_AUTHOR, "Author");
        values.put(ManagerEntry.COLUMN_BOOK_PRODUCT_NAME, "Book");
        values.put(ManagerEntry.COLUMN_BOOK_PUBLICATION_YEAR, 2000);
        values.put(ManagerEntry.COLUMN_BOOK_LANGUAGE, "Language");
        values.put(ManagerEntry.COLUMN_BOOK_TYPE, 2);
        values.put(ManagerEntry.COLUMN_BOOK_PRICE, 12);
        values.put(ManagerEntry.COLUMN_BOOK_QUANTITY, 1);
        values.put(ManagerEntry.COLUMN_BOOK_AVAILABILITY, 1);
        values.put(ManagerEntry.COLUMN_BOOK_SUPPLIER_NAME, "Supplier");
        values.put(ManagerEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, 863838484);

        long newRowId = db.insert(ManagerEntry.TABLE_NAME, null, values);
        Log.v(LOG_TAG, "Number of entry rows: " + newRowId);
    }
}
