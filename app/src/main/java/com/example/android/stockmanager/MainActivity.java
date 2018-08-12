package com.example.android.stockmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.stockmanager.data.BookContract.BookEntry;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO add floating button for entering editor activity

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

        TextView displayView = findViewById(R.id.book_text_view);

        try {
            displayView.setText("Test if data entries working " + cursor.getCount() + " books.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_AUTHOR + " - " +
                    BookEntry.COLUMN_BOOK_PRODUCT_NAME + " - " +
                    BookEntry.COLUMN_BOOK_PUBLICATION_YEAR + " - " +
                    BookEntry.COLUMN_BOOK_LANGUAGE + " - " +
                    BookEntry.COLUMN_BOOK_TYPE + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_AVAILABILITY + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + "\n");

            // Get index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR);
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRODUCT_NAME);
            int publicationYearColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR);
            int languageColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_LANGUAGE);
            int typeColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TYPE);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int availabilityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AVAILABILITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);

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
