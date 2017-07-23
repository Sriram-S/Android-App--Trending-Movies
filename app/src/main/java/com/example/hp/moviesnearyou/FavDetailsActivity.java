package com.example.hp.moviesnearyou;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.moviesnearyou.DataSource.FavContract;

import java.net.URL;

public class FavDetailsActivity extends AppCompatActivity {

    private Uri uri;
    private ImageView posterfav;
    private TextView namefav;
    private TextView overviewfav;
    private TextView datefav;
    private TextView votefav;
    private ScrollView scrollView;
    private TextView ratingfav;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_details);
        posterfav = (ImageView) findViewById(R.id.movieposterfav);
        namefav = (TextView) findViewById(R.id.movienamefav);
        overviewfav = (TextView) findViewById(R.id.movieoverviewfav);
        delete = (Button) findViewById(R.id.delete);
        scrollView = (ScrollView) findViewById(R.id.firstparent);
        datefav = (TextView) findViewById(R.id.moviedatefav);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ratingfav = (TextView) findViewById(R.id.movieratingfav);
        votefav = (TextView) findViewById(R.id.movievotesfav);
        final Intent intent = getIntent();
        uri = intent.getData();
        String[] projection = {FavContract.FavEntry._ID, FavContract.FavEntry.Photo, FavContract.FavEntry.MOVIE_NAME, FavContract.FavEntry.DATE, FavContract.FavEntry.VOTES, FavContract.FavEntry.RATING, FavContract.FavEntry.MOVIE_OVERVIEW};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int i = cursor.getColumnIndexOrThrow(FavContract.FavEntry.Photo);
        int j = cursor.getColumnIndexOrThrow(FavContract.FavEntry.MOVIE_NAME);
        int k = cursor.getColumnIndexOrThrow(FavContract.FavEntry.MOVIE_OVERVIEW);
        int l = cursor.getColumnIndexOrThrow(FavContract.FavEntry.DATE);
        int m = cursor.getColumnIndexOrThrow(FavContract.FavEntry.RATING);
        int n = cursor.getColumnIndexOrThrow(FavContract.FavEntry.VOTES);
        byte[] val = cursor.getBlob(i);
        Bitmap bmp = BitmapFactory.decodeByteArray(val, 0, val.length);
        posterfav.setImageBitmap(bmp);
        String moviename = cursor.getString(j);
        final SpannableStringBuilder str = new SpannableStringBuilder( getResources().getString(R.string.moviename)  +" \n" + moviename);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        namefav.setText(str);
        setTitle(moviename);
        String overview = cursor.getString(k);
        final SpannableStringBuilder str1 = new SpannableStringBuilder( getResources().getString(R.string.movieplot)  +"\n" + overview);
        str1.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        overviewfav.setText(str1);
        String date = cursor.getString(l);
        final SpannableStringBuilder str2 = new SpannableStringBuilder(getResources().getString(R.string.release)  +"\n" + date);
        str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        datefav.setText(str2);
        int vote = cursor.getInt(n);
        final SpannableStringBuilder str3 = new SpannableStringBuilder( getResources().getString(R.string.Votes)  +"\n" + String.valueOf(vote));
        str3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        votefav.setText(str3);
        int rating = cursor.getInt(m);
        final SpannableStringBuilder str4 = new SpannableStringBuilder( getResources().getString(R.string.Ratings) +"\n" + String.valueOf(rating));
        str4.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ratingfav.setText(str4);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res;
                String[] projection = {FavContract.FavEntry._ID, FavContract.FavEntry.Photo, FavContract.FavEntry.MOVIE_NAME, FavContract.FavEntry.DATE, FavContract.FavEntry.VOTES, FavContract.FavEntry.RATING, FavContract.FavEntry.MOVIE_OVERVIEW};
                res = getContentResolver().delete(uri, null, projection);
                if (res > 0) {
                    Snackbar.make(scrollView,getResources().getString(R.string.deleteone), Snackbar.LENGTH_LONG).show();
                    final Intent intent1 = new Intent(FavDetailsActivity.this, MainActivity.class);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                startActivity(intent1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    Snackbar.make(scrollView,getResources().getString(R.string.uncertain), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.delete:
                int res;
                String[] projection = {FavContract.FavEntry._ID, FavContract.FavEntry.Photo, FavContract.FavEntry.MOVIE_NAME, FavContract.FavEntry.DATE, FavContract.FavEntry.VOTES, FavContract.FavEntry.RATING, FavContract.FavEntry.MOVIE_OVERVIEW};
                res = getContentResolver().delete(uri, null, projection);
                if (res > 0) {
                    Snackbar.make(scrollView,getResources().getString(R.string.deleteone), Snackbar.LENGTH_LONG).show();
                    final Intent intent = new Intent(FavDetailsActivity.this, MainActivity.class);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
                break;
            case R.id.deleteall:
                int result;
                result = getContentResolver().delete(FavContract.FavEntry.CONTENT_URI, null, null);
                if (result > 0) {
                    Snackbar.make(scrollView, getResources().getString(R.string.deleteallfav), Snackbar.LENGTH_LONG).show();
                    final Intent intent = new Intent(FavDetailsActivity.this, MainActivity.class);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
