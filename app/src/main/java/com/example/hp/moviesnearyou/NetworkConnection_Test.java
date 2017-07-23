package com.example.hp.moviesnearyou;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.ConnectException;

/**
 * Created by HP on 18-01-2017.
 */

public class NetworkConnection_Test {
    private Context context;

    NetworkConnection_Test(Context c) {
        context = c;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        } else
            return true;
    }
}
