package com.example.hp.moviesnearyou;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean checknetwork;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private StringBuilder stringBuilder = new StringBuilder();
    private MovieDetailsAdapter movieDetailsAdapter;
    private String url;
    private ArrayList<MovieDetailsAttributes> arrayList = new ArrayList();
    private static final int READTIMEOUT = 10000;
    private static final int CONNECTIONTIMEOUT = 15000;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setTitle(getResources().getString(R.string.videotitle));
        Intent intent = getIntent();
        id = intent.getStringExtra(getResources().getString(R.string.iddetail));
        progressBar = (ProgressBar) findViewById(R.id.load);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclervideos);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        NetworkConnection_Test networkConnection_test = new NetworkConnection_Test(this);
        checknetwork = networkConnection_test.isNetworkAvailable();
        if (checknetwork) {
            stringBuilder.append(getResources().getString(R.string.URL_GETKEY));
            stringBuilder.append(String.valueOf(id));
            stringBuilder.append(getResources().getString(R.string.videos));
            stringBuilder.append(getResources().getString(R.string.API_KEY));
            url = stringBuilder.toString();
            getDatainBackground obj = new getDatainBackground(url);
            obj.execute();
        } else {
            displaymessage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaymessage() {
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.Network_Error), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.NetworkAction), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        }).show();
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class getDatainBackground extends AsyncTask<Object, Object, MovieDetailsAttributes> implements MovieAdapter.ListIemListener {
        String url;

        public getDatainBackground(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected MovieDetailsAttributes doInBackground(Object... urls) {
            MovieDetailsAttributes movieDetailsAttributes = null;
            try {
                URL finalurl = new URL(url);
                String json = "";
                HttpURLConnection urlConnection = null;
                InputStream inputStream = null;
                try {
                    StringBuilder out = new StringBuilder();
                    urlConnection = (HttpURLConnection) finalurl.openConnection();
                    urlConnection.setRequestMethod(getResources().getString(R.string.reqmethod));
                    urlConnection.setReadTimeout(READTIMEOUT);
                    urlConnection.setConnectTimeout(CONNECTIONTIMEOUT);
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        String line = reader.readLine();
                        while (line != null) {
                            out.append(line);
                            line = reader.readLine();
                        }
                    }
                    json = out.toString();
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray(getResources().getString(R.string.jsonresults));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String key = jsonObject1.getString(getResources().getString(R.string.jsonvideoid));
                        String name = jsonObject1.getString(getResources().getString(R.string.jsonvideoname));
                        movieDetailsAttributes = new MovieDetailsAttributes(key, name);
                        arrayList.add(movieDetailsAttributes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieDetailsAttributes;
        }

        @Override
        protected void onPostExecute(MovieDetailsAttributes movieAttributes) {
            progressBar.setVisibility(View.GONE);
            movieDetailsAdapter = new MovieDetailsAdapter(getBaseContext(), arrayList, this);
            recyclerView.setAdapter(movieDetailsAdapter);
            movieDetailsAdapter.notifyDataSetChanged();
        }

        @Override
        public void onListItem(int position) {

        }
    }
}
