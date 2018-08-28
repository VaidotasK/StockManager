package com.example.android.stockmanager.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.stockmanager";
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
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;

        /**
         * Constants for column names
         */
        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_PRODUCT_NAME = "product_name";
        public static final String COLUMN_BOOK_PUBLICATION_YEAR = "publication_year";
        public static final String COLUMN_BOOK_LANGUAGE = "language";

        /**
         * Possible values {@link #TYPE_E_BOOK}, {@link #TYPE_POCKET}, {@link #TYPE_STANDARD};
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
        public static final int TYPE_STANDARD = 0;
        public static final int TYPE_POCKET = 1;
        public static final int TYPE_E_BOOK = 2;

        /**
         * Constants for availability
         */
        public static final int AVAILABILITY_NOT_AVAILABLE = 0;
        public static final int AVAILABILITY_AVAILABLE = 1;


    }
}
