package com.example.hp.moviesnearyou;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.moviesnearyou.DataSource.FavContract;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView posterImage;
    private TextView MovieName;
    private TextView MovieDate;
    private TextView MovieRating;
    private TextView MovieOverview;
    private CoordinatorLayout coordinatorLayout;
    private TextView MovieVote;
    private Toolbar toolbar;
    private Button fav;
    private Intent intent;
    private String name;
    private String poster;
    private String release;
    private String overview;
    private Button video;
    private int vote;
    private int rating;
    private AppBarLayout appBarLayout;
    private Button review;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        posterImage = (ImageView) findViewById(R.id.imageView);
        MovieName = (TextView) findViewById(R.id.moviename);
        MovieDate = (TextView) findViewById(R.id.movierelease);
        MovieVote = (TextView) findViewById(R.id.movievote);
        MovieRating = (TextView) findViewById(R.id.movierating);
        MovieOverview = (TextView) findViewById(R.id.movieoverview);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        review = (Button) findViewById(R.id.reviews);
        fav = (Button) findViewById(R.id.fav);
        video = (Button) findViewById(R.id.video);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        name = intent.getStringExtra(getResources().getString(R.string.moviename));
        poster = intent.getStringExtra(getResources().getString(R.string.posterdetail));
        overview = intent.getStringExtra(getResources().getString(R.string.movieplot));
        rating = intent.getIntExtra(getResources().getString(R.string.Ratings), 0);
        vote = intent.getIntExtra(getResources().getString(R.string.Votes), 0);
        id = intent.getIntExtra(getResources().getString(R.string.iddetail), 0);
        release = intent.getStringExtra(getResources().getString(R.string.release));
        appBarLayout = (AppBarLayout) findViewById(R.id.collapsingappbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtool);
        collapsingToolbarLayout.setTitle(name);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int trackscroll = 0;
                if (trackscroll == 0) {
                    trackscroll = appBarLayout.getTotalScrollRange();
                }
                if (trackscroll + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(name);
                }
            }
        });
        Picasso.with(getBaseContext()).load(poster).into(posterImage);
        MovieName.setText(name);
        MovieDate.setText(release);
        MovieVote.setText(String.valueOf(vote));
        MovieRating.setText(String.valueOf(rating));
        MovieOverview.setText(overview);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), VideoActivity.class);
                intent.putExtra(getResources().getString(R.string.iddetail), String.valueOf(id));
                startActivity(intent);
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                intent.putExtra(getResources().getString(R.string.iddetail), id);
                startActivity(intent);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namefav = name;
                String dates = release;
                String projection[] = {FavContract.FavEntry.MOVIE_NAME};
                Cursor cursor = getContentResolver().query(FavContract.FavEntry.CONTENT_URI, projection, null, null, null);
                int flag = 0;
                if (cursor != null) {
                    cursor.moveToFirst();
                    int size = cursor.getCount();
                    for (int i = 0; i < size; i++) {
                        int temp = cursor.getColumnIndex(FavContract.FavEntry.MOVIE_NAME);
                        String s = cursor.getString(temp);
                        Log.v(s, namefav);
                        if (s.equals(namefav)) {
                            Snackbar.make(coordinatorLayout,getResources().getString(R.string.adddedinfav), Snackbar.LENGTH_LONG).show();
                            flag = 1;
                            cursor.close();
                            break;
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                if (flag == 0) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavContract.FavEntry.MOVIE_NAME, namefav);
                    contentValues.put(FavContract.FavEntry.DATE, dates);
                    contentValues.put(FavContract.FavEntry.Photo, bytes);
                    contentValues.put(FavContract.FavEntry.VOTES, vote);
                    contentValues.put(FavContract.FavEntry.RATING, rating);
                    contentValues.put(FavContract.FavEntry.MOVIE_OVERVIEW, overview);
                    Uri uri = getContentResolver().insert(FavContract.FavEntry.CONTENT_URI, contentValues);
                    if (uri != null) {
                        Snackbar.make(coordinatorLayout,getResources().getString(R.string.addedsuc), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(coordinatorLayout,getResources().getString(R.string.erroradding), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.alreadypresent), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.favourite:
                Intent intent = new Intent(getBaseContext(), FavActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
