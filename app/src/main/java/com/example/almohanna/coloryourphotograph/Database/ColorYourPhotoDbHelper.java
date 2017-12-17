package com.example.almohanna.coloryourphotograph.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.DifficultyEntry;
import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.GalleryEntry;
import static com.example.almohanna.coloryourphotograph.Database.ColorYourPhotoContract.ToolsEntry;

/**
 * Created by Reem on 12-Oct-17.
 */

public class ColorYourPhotoDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ColorYourPhotoDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "ColorYourPhoto.db";
    private static final int DATABASE_VERSION = 6;
    private ArrayList<byte[]> listofImages = new ArrayList<byte[]>();

    public ColorYourPhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "in Color Your Photo database ");
    }

    String CREATE_TABLE_Difficulty = "CREATE TABLE IF NOT EXISTS " + DifficultyEntry.TABLE_NAME +
            " ( " + DifficultyEntry.COLUMN_LEVEL + " TEXT NOT NULL " + " ); ";

    String CREATE_TABLE_Gallery = "CREATE TABLE IF NOT EXISTS " + GalleryEntry.TABLE_NAME +
            " ( " + GalleryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + GalleryEntry.COLUMN_COLORING_PAGE + " BLOB NOT NULL " + " ); ";

    String CREATE_TABLE_TOOLS = "CREATE TABLE IF NOT EXISTS " + ToolsEntry.TABLE_NAME +
            " ( " + ToolsEntry.COLUMN_NAME + " TEXT ," +
            ToolsEntry.COLUMN_COLOR + " TEXT ," +
            ToolsEntry.COLUMN_SIZE + " INTEGER " + " ); ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_Difficulty);
        sqLiteDatabase.execSQL(CREATE_TABLE_Gallery);
        sqLiteDatabase.execSQL(CREATE_TABLE_TOOLS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DifficultyEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GalleryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ToolsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertDifficultyLevel(String level) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DifficultyEntry.COLUMN_LEVEL, level);
        db.insert(DifficultyEntry.TABLE_NAME, null, values);
    }

    public void insertTools(String name, String color, int size) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToolsEntry.COLUMN_NAME, name);
        values.put(ToolsEntry.COLUMN_COLOR, color);
        values.put(String.valueOf(ToolsEntry.COLUMN_SIZE), size);
        db.insert(ToolsEntry.TABLE_NAME, null, values);
    }

    public void insertImage(byte[] image) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GalleryEntry.COLUMN_COLORING_PAGE, image);
        db.insert(GalleryEntry.TABLE_NAME, null, values);
    }

    public Cursor readLevel() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {DifficultyEntry.COLUMN_LEVEL,};
        Cursor cursor = db.query(
                DifficultyEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readTools() {
        SQLiteDatabase db = getReadableDatabase();
        //String[] projection = {DifficultyEntry.COLUMN_LEVEL,};
        Cursor cursor = db.query(
                ToolsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public ArrayList<byte[]> retrieveAllImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        //String[] projection = {GalleryEntry.COLUMN_COLORING_PAGE,};
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
                ArrayList<String> id = new ArrayList<String>();

                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(GalleryEntry.COLUMN_COLORING_PAGE));
                    int idd= cursor.getInt(cursor.getColumnIndex(GalleryEntry._ID));
                    String columnId= String.valueOf(idd);
                    id.add(columnId);
                    Log.i("database", " id: " + id);

                    listofImages.add(blob);
                }
                while (cursor.moveToNext());
            }
        }
        return listofImages;
    }

    public void DeleteImage(long imageId) {
        int i = (int) imageId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GalleryEntry.TABLE_NAME,
                GalleryEntry._ID + "=?",
                new String[]{String.valueOf(i)});
    }


    // Getting images Count
    public int getImagesCount() {
        String countQuery = "SELECT  * FROM " + GalleryEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    /*

    public Boolean DeleteImage(long imageId) {
        int i = (int) imageId;
        SQLiteDatabase database = this.getWritableDatabase();
        if (i > 0) {
            database.delete(GalleryEntry.TABLE_NAME, "_ID=?", new String[]{Integer.toString(i)});
            return true;
        }
        return false;
        /*
        String selection = ColorYourPhotoContract.GalleryEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(i)};
        int rowsDeleted = database.delete(ColorYourPhotoContract.GalleryEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;

    }



    public int Dd(int imageId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int r = database.delete(ColorYourPhotoContract.GalleryEntry.TABLE_NAME, ColorYourPhotoContract.GalleryEntry._ID + "=" + imageId, null);
        return r;
    }
    */

}
