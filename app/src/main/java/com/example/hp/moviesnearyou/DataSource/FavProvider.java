package com.example.hp.moviesnearyou.DataSource;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by HP on 21-01-2017.
 */

public class FavProvider extends ContentProvider {

    private static final int FAVOURITE = 100;
    private static final int FAVOURITE_ID = 101;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(FavContract.CONTENT_AUTHORITY, FavContract.PATH_INVENTORY, FAVOURITE);
        sURIMatcher.addURI(FavContract.CONTENT_AUTHORITY, FavContract.PATH_INVENTORY + "/#", +FAVOURITE_ID);
    }

    @Override
    public boolean onCreate() {

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        FavHelper favHelper = new FavHelper(getContext());
        SQLiteDatabase sqLiteDatabase = favHelper.getReadableDatabase();
        Cursor cursor;
        int match = sURIMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                Log.v("ahkhk", String.valueOf(uri));
                cursor = sqLiteDatabase.query(FavContract.FavEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;

            case FAVOURITE_ID:
                s = FavContract.FavEntry._ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(FavContract.FavEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;

            default:
                return null;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        FavHelper favHelper = new FavHelper(getContext());
        final int match = sURIMatcher.match(uri);
        long id;
        switch (match) {
            case FAVOURITE:
                SQLiteDatabase sqLiteDatabase = favHelper.getWritableDatabase();
                id = sqLiteDatabase.insert(FavContract.FavEntry.TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        FavHelper favHelper = new FavHelper(getContext());
        SQLiteDatabase sqLiteDatabase = favHelper.getWritableDatabase();
        int flag = -1;
        int match = sURIMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                flag = sqLiteDatabase.delete(FavContract.FavEntry.TABLE_NAME, null, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return flag;

            case FAVOURITE_ID:
                s = FavContract.FavEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                flag = sqLiteDatabase.delete(FavContract.FavEntry.TABLE_NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return flag;

            default:
                throw new IllegalArgumentException("Not Valid URI" + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
