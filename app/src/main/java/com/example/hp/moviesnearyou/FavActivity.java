package com.example.hp.moviesnearyou;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.moviesnearyou.DataSource.FavContract;


public class FavActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FavAdapter favAdapter;
    private ListView listView;
    private TextView empty;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.favdetailstitle));
        setContentView(R.layout.activity_fav);
        listView = (ListView) findViewById(R.id.listview);
        empty = (TextView) findViewById(R.id.empty);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        favAdapter = new FavAdapter(this, null);
        listView.setAdapter(favAdapter);
        getLoaderManager().initLoader(0, null, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FavActivity.this, FavDetailsActivity.class);
                Uri uri = ContentUris.withAppendedId(FavContract.FavEntry.CONTENT_URI, l);
                intent.setData(uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        MenuItem menuItem = menu.findItem(R.id.delete);
        invalidateOptionsMenu();
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.deleteall:
                int result;
                result = getContentResolver().delete(FavContract.FavEntry.CONTENT_URI, null, null);
                if (result > 0) {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.deletemsg), Snackbar.LENGTH_LONG).show();
                    final Intent intent = new Intent(FavActivity.this, MainActivity.class);
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
                } else {
                    Snackbar.make(coordinatorLayout,getResources().getString(R.string.norecord), Snackbar.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {FavContract.FavEntry._ID, FavContract.FavEntry.Photo, FavContract.FavEntry.MOVIE_NAME, FavContract.FavEntry.DATE, FavContract.FavEntry.VOTES, FavContract.FavEntry.RATING, FavContract.FavEntry.MOVIE_OVERVIEW};
        return new CursorLoader(this, FavContract.FavEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            favAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favAdapter.swapCursor(null);
    }
}
