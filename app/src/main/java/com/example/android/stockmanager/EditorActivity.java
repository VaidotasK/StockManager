package com.example.android.stockmanager;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.stockmanager.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity {

    public static final String LOG_TAG = EditorActivity.class.getSimpleName();

    private EditText authorEditText;
    private EditText productNameEditText;
    private EditText yearEditText;
    private EditText languageEditText;

    private Spinner typeSpinner;
    private int type = BookEntry.TYPE_STANDARD;

    private EditText quantityEditText;
    private EditText priceEditText;

    private Spinner availabilitySpinner;
    private int availability = BookEntry.AVAILABILITY_AVAILABLE;

    private EditText supplierNameEditText;
    private EditText supplierPhoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        authorEditText = findViewById(R.id.editor_author_edit_text);
        productNameEditText = findViewById(R.id.editor_product_name_edit_text);
        yearEditText = findViewById(R.id.editor_year_edit_text);
        languageEditText = findViewById(R.id.editor_language_edit_text);
        typeSpinner = findViewById(R.id.editor_type_spinner);
        quantityEditText = findViewById(R.id.editor_quantity_edit_text);
        priceEditText = findViewById(R.id.editor_price_edit_text);
        availabilitySpinner = findViewById(R.id.editor_availability_spinner);
        supplierNameEditText = findViewById(R.id.editor_supplier_name_edit_text);
        supplierPhoneNumberEditText = findViewById(R.id.editor_supplier_phone_edit_text);

        setupTypeSpinner();
        setupAvailabilitySpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Action Save
            case R.id.menu_editor_action_save:
                saveBook();
                finish();
                return true;
            //Action Delete
            case R.id.menu_editor_action_delete:
                //TODO code for delete method
                // Do nothing for now
                return true;
            // If "Up" arrow clicked in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user's input from editor and save new book into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String authorString = authorEditText.getText().toString().trim();
        String productNameString = productNameEditText.getText().toString().trim();
        String yearString = yearEditText.getText().toString().trim();
        String languageString = languageEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String supplierNameString = supplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = supplierPhoneNumberEditText.getText().toString().trim();
        int year;
        int quantity;
        int price;
        int supplierPhoneNumber;

        try {
            year = Integer.parseInt(yearString);
            quantity = Integer.parseInt(quantityString);
            price = Integer.parseInt(priceString);
            supplierPhoneNumber = Integer.parseInt(supplierPhoneNumberString);
        } catch (NumberFormatException e) {
            year = 0;
            quantity = 0;
            price = 0;
            supplierPhoneNumber = 0;
            Log.v(LOG_TAG, "Error taking inputs from EditText" + e);
        }

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, authorString);
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, productNameString);
        values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, year);
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, languageString);
        values.put(BookEntry.COLUMN_BOOK_TYPE, type);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, availability);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        // Insert new book into provider, and return the content URI for the new book.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            //Error message
            Toast.makeText(this, "Error while saving",
                    Toast.LENGTH_SHORT).show();
        } else {
            //Insertion was successful
            Toast.makeText(this, "Book saved successfully",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup the dropdown spinner for book type
     */
    private void setupTypeSpinner() {
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_spinner_options, android.R.layout.simple_spinner_item);

        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(typeSpinnerAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_standard_book))) {
                        type = BookEntry.TYPE_STANDARD;
                    } else if (selection.equals(getString(R.string.type_pocket_book))) {
                        type = BookEntry.TYPE_POCKET;
                    } else {
                        type = BookEntry.TYPE_E_BOOK;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = BookEntry.TYPE_STANDARD;
            }
        });
    }

    /**
     * Setup the dropdown spinner for book availability
     */
    private void setupAvailabilitySpinner() {
        ArrayAdapter availabilitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_availability_spinner_options, android.R.layout.simple_spinner_item);

        availabilitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        availabilitySpinner.setAdapter(availabilitySpinnerAdapter);

        availabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.availability_not_available))) {
                        type = BookEntry.AVAILABILITY_NOT_AVAILABLE;
                    } else {
                        type = BookEntry.AVAILABILITY_AVAILABLE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = BookEntry.AVAILABILITY_AVAILABLE;
            }
        });
    }


}
