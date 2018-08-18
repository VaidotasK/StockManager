package com.example.android.stockmanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.stockmanager.data.BookContract.BookEntry;


/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView priceTextView = view.findViewById(R.id.list_view_price);
        TextView productNameTextView = view.findViewById(R.id.list_view_product_name);
        TextView authorTextView = view.findViewById(R.id.list_view_author);
        TextView yearTextView = view.findViewById(R.id.list_view_publishing_year);
        TextView quantityTextView = view.findViewById(R.id.list_view_quantity);

        int price = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRICE));
        String productName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRODUCT_NAME));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_AUTHOR));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));

        priceTextView.setText(String.valueOf(price));
        productNameTextView.setText(productName);
        authorTextView.setText(author);
        yearTextView.setText(String.valueOf(year));
        quantityTextView.setText(String.valueOf(quantity));
    }
}