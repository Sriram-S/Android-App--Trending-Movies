package com.example.hp.moviesnearyou;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.moviesnearyou.DataSource.FavContract;


/**
 * Created by HP on 21-01-2017.
 */

public class FavAdapter extends CursorAdapter {

    ImageView imageView;
    TextView moviename;
    TextView releasedate;

    public FavAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.favdetails, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        imageView = (ImageView) view.findViewById(R.id.title);
        moviename = (TextView) view.findViewById(R.id.moviename);
        releasedate = (TextView) view.findViewById(R.id.date);
        int i = cursor.getColumnIndexOrThrow(FavContract.FavEntry.MOVIE_NAME);
        int j = cursor.getColumnIndexOrThrow(FavContract.FavEntry.DATE);
        int k = cursor.getColumnIndexOrThrow(FavContract.FavEntry.Photo);
        final String name = cursor.getString(i);
        final String date = cursor.getString(j);
        byte[] val = cursor.getBlob(k);
        Bitmap bmp = BitmapFactory.decodeByteArray(val, 0, val.length);
        imageView.setImageBitmap(bmp);
        moviename.setText(name);
        releasedate.setText(date);
    }
}
