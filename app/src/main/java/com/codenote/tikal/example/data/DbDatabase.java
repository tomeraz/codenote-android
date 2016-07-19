package com.codenote.tikal.example.data;

/**
 * @author ortal
 * @date 2015-07-12
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbDatabase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MoviesDB";

    public DbDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create  table

        final String CREATE_MOVIES = "CREATE TABLE " + DbContract.Tables.TABLE_MOVIES + " (" +
                DbContract.MoviesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MoviesColumns.MOVIE_ID + " INTEGER NOT NULL ," +
                DbContract.MoviesColumns.IS_ADULT + " INTEGER DEFAULT 0 ," +
                DbContract.MoviesColumns.ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                DbContract.MoviesColumns.OVERVIEW + " TEXT DEFAULT '' , " +
                DbContract.MoviesColumns.RELEASE_DATE + " TEXT DEFAULT '' , " +
                DbContract.MoviesColumns.TITLE + " TEXT NOT NULL, " +
                DbContract.MoviesColumns.VOTE_AVERAGE + " REAL NOT NULL, " +
                DbContract.MoviesColumns.POSTER_PATH + " TEXT NOT NULL, " +
                DbContract.MoviesColumns.POPULARITY + " REAL NOT NULL, " +
                " UNIQUE (" + DbContract.MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE);";

        // create  table
        db.execSQL(CREATE_MOVIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

        }
        this.onCreate(db);
    }

}