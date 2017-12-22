package com.example.almohanna.coloryourphotograph.Database;

import android.provider.BaseColumns;

public class ColorYourPhotoContract {

    private ColorYourPhotoContract() {
    }

    public static final class DifficultyEntry implements BaseColumns {
        public final static String TABLE_NAME = "Difficulty";
        public final static String COLUMN_LEVEL = "level";

    }

    public static final class GalleryEntry implements BaseColumns {
        public final static String TABLE_NAME = "Gallery";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COLORING_PAGE = "image";

    }
}
