package com.example.android.stockmanager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.stockmanager.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = EditorActivity.class.getSimpleName();

    private static final int EXISTING_BOOK_LOADER = 1;
    private boolean bookHasChanged = false;
    private Uri currentBookUri;

    private EditText authorEditText;
    private EditText productNameEditText;
    private EditText yearEditText;
    private EditText languageEditText;

    private Spinner typeSpinner;
    private int type = BookEntry.TYPE_STANDARD;

    private EditText quantityEditText;
    private int quantity;
    private EditText priceEditText;

    private Spinner availabilitySpinner;
    private int availability;

    private EditText supplierNameEditText;
    private EditText supplierPhoneNumberEditText;
    private String supplierPhoneNumberForCall;

    //Change bookHasChanged to true, if User touches a View (as if user is modifying the view)
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            bookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentBookUri = intent.getData();
        if (null == currentBookUri) {
            setTitle(getString(R.string.editor_activity_title_new_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_book));
            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

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
        Button incrementButton = findViewById(R.id.increment_button);
        Button decrementButton = findViewById(R.id.decrement_button);

        authorEditText.setOnTouchListener(touchListener);
        productNameEditText.setOnTouchListener(touchListener);
        yearEditText.setOnTouchListener(touchListener);
        languageEditText.setOnTouchListener(touchListener);
        typeSpinner.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        availabilitySpinner.setOnTouchListener(touchListener);
        supplierNameEditText.setOnTouchListener(touchListener);
        supplierPhoneNumberEditText.setOnTouchListener(touchListener);
        incrementButton.setOnTouchListener(touchListener);
        decrementButton.setOnTouchListener(touchListener);

        setupTypeSpinner();
        setupAvailabilitySpinner();
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
                        availability = BookEntry.AVAILABILITY_NOT_AVAILABLE;
                    } else {
                        availability = BookEntry.AVAILABILITY_AVAILABLE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                availability = BookEntry.AVAILABILITY_AVAILABLE;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, hide the "Delete" menu item.
        if (currentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.menu_editor_action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Action Save
            case R.id.menu_editor_action_save:
                saveBook();
                return true;
            //Action Delete
            case R.id.menu_editor_action_delete:
                showDeleteConfirmationDialog();
                return true;
            // If "Up" arrow clicked in the app bar
            case android.R.id.home:
                if (!bookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
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

        if (TextUtils.isEmpty(authorString) || TextUtils.isEmpty(productNameString)
                || TextUtils.isEmpty(yearString) || TextUtils.isEmpty(languageString)
                || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(supplierNameString) || TextUtils.isEmpty(supplierPhoneNumberString)) {
            Toast.makeText(this, R.string.msg_fill_all_data, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, authorString);
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, productNameString);
        int year;
        if (!TextUtils.isEmpty(yearString)) {
            year = Integer.parseInt(yearString);
            if (1500 <= year && year <= 2050) {
                values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, year);
            } else {
                Toast.makeText(this, R.string.msg_enter_valid_year, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            return;
        }
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, languageString);
        values.put(BookEntry.COLUMN_BOOK_TYPE, type);
        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, availability);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        long supplierPhoneNumber = 0;
        if (!TextUtils.isEmpty(supplierPhoneNumberString)) {
            supplierPhoneNumber = Long.parseLong(supplierPhoneNumberString);
        }
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        // Determine if this is a new or existing book
        if (currentBookUri == null) {
            // Insert new book.
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            //Error message
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.error_msg_while_inserting_book),
                        Toast.LENGTH_SHORT).show();
            } else {
                //Insertion was successful
                Toast.makeText(this, R.string.msg_insert_successful,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            //Update existing book
            int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);
            if (rowsAffected == 0) {
                //No rows were affected, there was an error with the update
                Toast.makeText(this, R.string.error_msg_while_updating,
                        Toast.LENGTH_SHORT).show();
            } else {
                //Update was successful
                Toast.makeText(this, R.string.msg_update_successful,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    //If data weren't saved - alert message
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.unsaved_changes_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.unsaved_changes_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //If user clicks keep editing, dismiss dialog and stay in Editor activity
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!bookHasChanged) {
            super.onBackPressed();
            return;
        }
        //Create clickListener for handling discard button.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked discard button, close editor activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * Delete book msg with delete action in it
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete_dialog_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //If press confirmation button
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.delete_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //If cancel button pressed
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of one book from the database
     */
    private void deleteBook() {
        if (currentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(currentBookUri, null, null);
            if (0 == rowsDeleted) {
                Toast.makeText(this, getString(R.string.editor_delete_book_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_PRODUCT_NAME,
                BookEntry.COLUMN_BOOK_PUBLICATION_YEAR,
                BookEntry.COLUMN_BOOK_LANGUAGE,
                BookEntry.COLUMN_BOOK_TYPE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_AVAILABILITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,
                currentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Check if cursor null or number of lines in it is less than 1
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            String author = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_AUTHOR));
            String productName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRODUCT_NAME));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR));
            String language = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_LANGUAGE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRICE));
            String supplierName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_SUPPLIER_NAME));
            long supplierPhoneNumber = cursor.getLong(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER));

            authorEditText.setText(author);
            productNameEditText.setText(productName);
            yearEditText.setText(Integer.toString(year));
            languageEditText.setText(language);
            quantityEditText.setText(Integer.toString(quantity));
            priceEditText.setText(Integer.toString(price));
            supplierNameEditText.setText(supplierName);
            supplierPhoneNumberEditText.setText(Long.toString(supplierPhoneNumber));
            supplierPhoneNumberForCall = Long.toString(supplierPhoneNumber);

            switch (type) {
                case BookEntry.TYPE_POCKET:
                    typeSpinner.setSelection(1);
                    break;
                case BookEntry.TYPE_E_BOOK:
                    typeSpinner.setSelection(2);
                    break;
                default:
                    typeSpinner.setSelection(0);
                    break;
            }

            if (0 < quantity) {
                availability = BookEntry.AVAILABILITY_AVAILABLE;
                availabilitySpinner.setSelection(BookEntry.AVAILABILITY_AVAILABLE);
            }
            switch (availability) {
                case BookEntry.AVAILABILITY_NOT_AVAILABLE:
                    availabilitySpinner.setSelection(0);
                    break;
                default:
                    availabilitySpinner.setSelection(1);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        authorEditText.setText("");
        productNameEditText.setText("");
        yearEditText.setText("");
        languageEditText.setText("");
        quantityEditText.setText("");
        priceEditText.setText("");
        supplierNameEditText.setText("");
        supplierPhoneNumberEditText.setText("");
        typeSpinner.setSelection(BookEntry.TYPE_STANDARD);
        availabilitySpinner.setSelection(BookEntry.AVAILABILITY_AVAILABLE);
    }

    public void makePhoneCall(View v) {

        Intent intentCallSupplier = new Intent(Intent.ACTION_DIAL);
        intentCallSupplier.setData(Uri.parse("tel:" + supplierPhoneNumberForCall));
        if (intentCallSupplier.resolveActivity(getPackageManager()) != null) {
            startActivity(intentCallSupplier);
        } else {
            Log.d(LOG_TAG, "No Intent available to handle action" + "Supplier phone number" +
                    supplierPhoneNumberForCall);
        }
        startActivity(intentCallSupplier);
    }

    public void decreaseProductQuantity(View view) {
        if (!TextUtils.isEmpty(quantityEditText.getText().toString())) {
            quantity = Integer.valueOf(quantityEditText.getText().toString());
        }
        switch (quantity) {
            case 0:
                Toast.makeText(this, getString(R.string.msg_out_of_stock), Toast.LENGTH_LONG).show();
                break;
            case 1:
                quantity--;
                quantityEditText.setText(String.valueOf(quantity));
                Toast.makeText(this, R.string.msg_book_bacame_out_of_stock, Toast.LENGTH_LONG).show();
                availabilitySpinner.setSelection(BookEntry.AVAILABILITY_NOT_AVAILABLE);
                break;
            default:
                quantity--;
                quantityEditText.setText(String.valueOf(quantity));
        }
    }

    public void incrementProductQuantity(View view) {
        if (!TextUtils.isEmpty(quantityEditText.getText().toString())) {
            quantity = Integer.valueOf(quantityEditText.getText().toString());
        }
        if (BookEntry.AVAILABILITY_NOT_AVAILABLE == availability) {
            availabilitySpinner.setSelection(BookEntry.AVAILABILITY_AVAILABLE);
        }
        quantity++;
        quantityEditText.setText(String.valueOf(quantity));
    }
}

