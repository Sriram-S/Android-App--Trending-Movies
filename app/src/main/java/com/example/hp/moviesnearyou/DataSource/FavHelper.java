package com.example.hp.moviesnearyou.DataSource;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 21-01-2017.
 */

public class FavHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "fav.db";
    private static final int DATABASE_VERSION = 1;
    String SQL_CREATE_FAV_TABLE = "CREATE TABLE " + FavContract.FavEntry.TABLE_NAME + " ("
            + FavContract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FavContract.FavEntry.MOVIE_NAME + " TEXT, "
            + FavContract.FavEntry.Photo + " TEXT, "
            + FavContract.FavEntry.MOVIE_OVERVIEW + " TEXT, "
            + FavContract.FavEntry.RATING + " INTEGER, "
            + FavContract.FavEntry.VOTES + " INTEGER, "
            + FavContract.FavEntry.DATE + " TEXT);";

    public FavHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
