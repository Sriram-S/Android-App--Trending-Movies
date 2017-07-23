package com.example.hp.moviesnearyou;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String URL;
    private boolean getselectedoption;
    private static final int READTIMEOUT = 10000;
    private static final int CONNECTIONTIMEOUT = 15000;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<MovieAttributes> movieAttributes = new ArrayList<>();
    private MovieAdapter arrayAdapter;
    private NetworkConnection_Test networkConnection_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        networkConnection_test = new NetworkConnection_Test(this);
        boolean networkstate = networkConnection_test.isNetworkAvailable();
        URL = getResources().getString(R.string.URL_POPULAR);
        if (networkstate) {
            MoviesAsync moviesAsync = new MoviesAsync(URL);
            movieAttributes = new ArrayList<>();
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            moviesAsync.execute();
            getselectedoption = false;
        } else {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.orderby, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String URL;
        switch (item.getItemId()) {
            case R.id.rating: {
                if (getselectedoption == false) {
                    getselectedoption = true;
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.selected), Snackbar.LENGTH_LONG).show();
                    break;
                }
                if (arrayAdapter != null) {
                    recyclerView.setAdapter(null);
                    arrayAdapter.notifyDataSetChanged();
                }
                URL = getResources().getString(R.string.URL_TOP);
                networkConnection_test = new NetworkConnection_Test(this);
                boolean networkstate = networkConnection_test.isNetworkAvailable();
                if (networkstate) {
                    MoviesAsync moviesAsync = new MoviesAsync(URL);
                    movieAttributes = new ArrayList<>();
                    moviesAsync.execute();
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.Network_Error), Snackbar.LENGTH_LONG).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }).show();
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }
            case R.id.fav:
                Intent intent = new Intent(getBaseContext(), FavActivity.class);
                startActivity(intent);
                break;
            case R.id.popular: {
                if (getselectedoption == true) {
                    getselectedoption = false;
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.selected), Snackbar.LENGTH_LONG).show();
                    break;
                }
                if (arrayAdapter != null) {
                    recyclerView.setAdapter(null);
                    arrayAdapter.notifyDataSetChanged();
                }
                URL = getResources().getString(R.string.URL_POPULAR);
                networkConnection_test = new NetworkConnection_Test(this);
                boolean networkstate = networkConnection_test.isNetworkAvailable();
                if (networkstate) {
                    MoviesAsync moviesAsync = new MoviesAsync(URL);
                    movieAttributes = new ArrayList<>();
                    moviesAsync.execute();
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.Network_Error), Snackbar.LENGTH_LONG).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }).show();
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }
        }
        return true;
    }

    private class MoviesAsync extends AsyncTask<URL, Void, MovieAttributes> implements MovieAdapter.ListIemListener {
        private String url;
        private ArrayList<MovieAttributes> movieAttributes = new ArrayList<>();

        MoviesAsync(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieAttributes doInBackground(URL... urls) {
            MovieAttributes currentjson = null;
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
                        String posterpath = jsonObject1.getString(getResources().getString(R.string.jsonposterpath));
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(getResources().getString(R.string.Image_Path));
                        stringBuilder.append(posterpath);
                        String overview = jsonObject1.getString(getResources().getString(R.string.jsonoverview));
                        String release = jsonObject1.getString(getResources().getString(R.string.jsonrelease));
                        String title = jsonObject1.getString(getResources().getString(R.string.jsontitle));
                        int avg = jsonObject1.getInt(getResources().getString(R.string.jsonrating));
                        int total = jsonObject1.getInt(getResources().getString(R.string.jsontotalvote));
                        int id = jsonObject1.getInt(getResources().getString(R.string.jsonmovieid));
                        currentjson = new MovieAttributes(title, overview, stringBuilder.toString(), release, avg, total, id);
                        movieAttributes.add(currentjson);
                    }
                } catch (IOException e) {
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return currentjson;
        }

        protected void onPostExecute(MovieAttributes movie) {
            progressBar.setVisibility(View.INVISIBLE);
            if (movieAttributes == null) {
                return;
            } else {
                arrayAdapter = new MovieAdapter(getBaseContext(), movieAttributes, this);
                recyclerView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onListItem(int position) {
        }
    }
}
