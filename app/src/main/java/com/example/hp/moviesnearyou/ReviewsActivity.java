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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ReviewAdapter arrayAdapter;
    private CoordinatorLayout coordinatorLayout;
    private TextView noreview;
    private int idvalue;
    private static final int READTIMEOUT = 10000;
    private static final int CONNECTIONTIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.reviewtitle));
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        idvalue = intent.getIntExtra(getResources().getString(R.string.iddetail), 0);
        progressBar = (ProgressBar) findViewById(R.id.load);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        noreview = (TextView) findViewById(R.id.noreview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NetworkConnection_Test networkConnectionTest = new NetworkConnection_Test(this);
        boolean check = networkConnectionTest.isNetworkAvailable();
        if (check) {
            ReviewAsync reviewAsync = new ReviewAsync();
            reviewAsync.execute();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class ReviewAsync extends AsyncTask<Void, Void, ReviewAttributes> implements MovieAdapter.ListIemListener {
        private ArrayList<ReviewAttributes> reviewattr = new ArrayList<>();

        ReviewAsync() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ReviewAttributes doInBackground(Void... voids) {
            ReviewAttributes current = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.URL_GETKEY));
            stringBuilder.append(idvalue);
            stringBuilder.append(getResources().getString(R.string.reviewscall));
            stringBuilder.append(getResources().getString(R.string.API_KEY));
            String url = stringBuilder.toString();
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
                    if (jsonArray.length() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noreview.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                return;
                            }
                        });

                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String author = jsonObject1.getString(getResources().getString(R.string.jsonauthorreview));
                        String content = jsonObject1.getString(getResources().getString(R.string.jsoncontentreview));
                        current = new ReviewAttributes(author, content);
                        reviewattr.add(current);
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
            return current;
        }

        protected void onPostExecute(ReviewAttributes review) {

            progressBar.setVisibility(View.INVISIBLE);
            if (reviewattr == null) {
                return;
            } else {
                arrayAdapter = new ReviewAdapter(getBaseContext(), reviewattr);
                recyclerView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onListItem(int position) {
        }
    }
}


