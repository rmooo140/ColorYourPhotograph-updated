package com.example.almohanna.coloryourphotograph.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.almohanna.coloryourphotograph.ImageModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.DifficultyEntry;
import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.GalleryEntry;

public class ColorYourPhotoDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ColorYourPhotoDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "ColorYourPhoto.db";
    private static final int DATABASE_VERSION = 8;
    private ArrayList<ImageModel> listofImages = new ArrayList<>();

    public ColorYourPhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "in Color Your Photo database ");
    }

    String CREATE_TABLE_Difficulty = "CREATE TABLE IF NOT EXISTS " + DifficultyEntry.TABLE_NAME +
            " ( " + DifficultyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DifficultyEntry.COLUMN_LEVEL + " TEXT NOT NULL DEFAULT Easy " + " ); ";

    String CREATE_TABLE_Gallery = "CREATE TABLE IF NOT EXISTS " + GalleryEntry.TABLE_NAME +
            " ( " + GalleryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + GalleryEntry.COLUMN_COLORING_PAGE + " BLOB NOT NULL " + " ); ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_Difficulty);
        sqLiteDatabase.execSQL(CREATE_TABLE_Gallery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DifficultyEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GalleryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertDifficultyLevel(String level) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DifficultyEntry.COLUMN_LEVEL, level);
        db.insert(DifficultyEntry.TABLE_NAME, null, values);
    }


    public void insertImage(byte[] image) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GalleryEntry.COLUMN_COLORING_PAGE, image);
        db.insert(GalleryEntry.TABLE_NAME, null, values);
    }
/*
    public void updateLevel (String  id , String level) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DifficultyEntry.COLUMN_LEVEL, level);
        String selection = DifficultyEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(id) };

        db.update(DifficultyEntry.TABLE_NAME, values,selection,selectionArgs);
    }
    */

    public Cursor readLevel() {
        SQLiteDatabase db = getReadableDatabase();
        //String[] projection = {DifficultyEntry.COLUMN_LEVEL,};
        Cursor cursor = db.query(
                DifficultyEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public List<ImageModel> retrieveAllImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                GalleryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(GalleryEntry.COLUMN_COLORING_PAGE));
                    int idd = cursor.getInt(cursor.getColumnIndex(GalleryEntry._ID));
                    String columnId = String.valueOf(idd);

                    Log.i("database", " id: " + columnId);

                    listofImages.add(new ImageModel(blob, columnId));
                }
                while (cursor.moveToNext());
            }
        }
        return listofImages;
    }

    public void DeleteImage(String imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GalleryEntry.TABLE_NAME,
                GalleryEntry._ID + "=?",
                new String[]{imageId});
    }

    // Getting images Count
    public int getImagesCount() {
        String countQuery = "SELECT  * FROM " + GalleryEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }
}
