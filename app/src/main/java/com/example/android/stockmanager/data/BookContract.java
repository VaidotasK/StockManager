package com.example.android.stockmanager.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.stockmanager";
    /**
     * Constant CONTENT_AUTHORITY is framework  of URI's which app will use to reach
     * content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Data type, for content provider uri(appended to {@link #BASE_CONTENT_URI}
     * ex. content://com.example.android.stockmanager/books/
     */
    public static final String PATH_BOOKS = "books";

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        /**
         * The content URI to access the book's data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * Name of table
         */
        public static final String TABLE_NAME = "books";

        /**
         * Entries ID
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Constants for column names
         */
        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_PRODUCT_NAME = "product_name";
        public static final String COLUMN_BOOK_PUBLICATION_YEAR = "publication_year";
        public static final String COLUMN_BOOK_LANGUAGE = "language";

        /**
         * Possible values {@link #TYPE_KINDLE}, {@link #TYPE_POCKET}, {@link #TYPE_STANDARD};
         */
        public static final String COLUMN_BOOK_TYPE = "type";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_AVAILABILITY = "availability";
        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        /**
         * Constants for different book types
         */
        public static final int TYPE_KINDLE = 0;
        public static final int TYPE_POCKET = 1;
        public static final int TYPE_STANDARD = 2;

        /**
         * Constants for availability
         */
        public static final int AVAILABILITY_IN_STOCK = 1;
        public static final int AVAILABILITY_OUT_OF_STOCK = 0;

    }
}
