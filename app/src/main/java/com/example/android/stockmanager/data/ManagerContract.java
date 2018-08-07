package com.example.android.stockmanager.data;

import android.provider.BaseColumns;

public final class ManagerContract {

    private ManagerContract() {
    }

    public static final class ManagerEntry implements BaseColumns {

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
