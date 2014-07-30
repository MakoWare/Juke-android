package com.makoware.keyo.Other;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.parse.Parse;

/**
 * Created by doman412 on 4/14/14.
 */
public class App extends Application {
    public static final String TAG = "AppVolley";

    private RequestQueue mRequestQueue;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Parse.initialize(this, "GU8DuOP6RzlnFFNBNOVnB5qrf6HCqxpJXSbDyN3W", "UPgsMzQYf73zz5TVTmHJjvI7WWSrzgrGqP4H7cn3");
    }

    public static App getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
