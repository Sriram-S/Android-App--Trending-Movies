package com.example.hp.moviesnearyou.DataSource;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HP on 21-01-2017.
 */

public class FavContract {

    private FavContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.hp.moviesnearyou";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "favourite";

    public static final class FavEntry implements BaseColumns {
        public final static String TABLE_NAME = "Favourite";
        public final static String _ID = BaseColumns._ID;
        public final static String MOVIE_NAME = "MOVIE_NAME";
        public final static String MOVIE_OVERVIEW = "OVERVIEW";
        public final static String RATING = "RATING";
        public final static String VOTES = "VOTES";
        public final static String DATE = "RELEASE_DATE";
        public final static String Photo = "MOVIE_POSTER";
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
    }
}
