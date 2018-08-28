package com.example.android.stockmanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.stockmanager.data.BookContract.BookEntry;

import java.util.Currency;
import java.util.Locale;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context c, Cursor cursor) {
        TextView priceTextView = view.findViewById(R.id.list_view_price);
        TextView productNameTextView = view.findViewById(R.id.list_view_product_name);
        TextView authorTextView = view.findViewById(R.id.list_view_author);
        TextView yearTextView = view.findViewById(R.id.list_view_publishing_year);
        final TextView quantityTextView = view.findViewById(R.id.list_view_quantity);
        String currency = Currency.getInstance(Locale.GERMANY).getCurrencyCode();
        final Context context = c;

        final int id = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRICE));
        String productName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRODUCT_NAME));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_AUTHOR));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));

        priceTextView.setText(String.valueOf(price + " " + currency));
        productNameTextView.setText(productName);
        authorTextView.setText(author);
        yearTextView.setText(String.valueOf(year));
        quantityTextView.setText(String.valueOf(quantity));

        Button saleButton = view.findViewById(R.id.list_view_sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bookQuantityLeft = quantity;
                if (0 == bookQuantityLeft) {
                    Toast.makeText(context, R.string.msg_out_of_stock, Toast.LENGTH_SHORT).show();
                } else if (0 < bookQuantityLeft) {
                    bookQuantityLeft--;
                    String newQuantityString = Integer.toString(bookQuantityLeft);

                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, newQuantityString);

                    if (0 == bookQuantityLeft) {
                        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, BookEntry.AVAILABILITY_NOT_AVAILABLE);
                    }

                    Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                    int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);

                    if (rowsAffected != 0) {
                        quantityTextView.setText(String.valueOf(bookQuantityLeft));
                    } else {
                        Toast.makeText(context, R.string.button_sale_error_msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}