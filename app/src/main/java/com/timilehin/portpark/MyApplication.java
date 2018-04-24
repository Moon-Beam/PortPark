package com.timilehin.portpark;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * Created by work on 24/04/2018.
 */

public class MyApplication extends Application {
    private RequestQueue requestQueue;
    private static MyApplication mInstance;

    public void onCreate() {
        super.onCreate();
        mInstance=this;

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = newRequestQueue(getApplicationContext());

        return requestQueue;
    }

    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }


    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }
}
