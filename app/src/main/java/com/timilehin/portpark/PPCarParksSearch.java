package com.timilehin.portpark;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timilehin.portpark.Models.CarPark;

import org.json.JSONException;
import org.json.JSONObject;

public class PPCarParksSearch {
    private PPCarParksSearchEventListener mPPCarParksSearchEventListener;
    private Gson gson;

    public PPCarParksSearch(Context context, String lon, String lat) {
        String url = getURL(lon,lat);
        makeNetworkRequest(context, url);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public void setOnEventListener(PPCarParksSearchEventListener listener) {
        mPPCarParksSearchEventListener = listener;
    }

    private String getURL(String lon, String lat) {
        String url = "https://maps.googleapis.com/maps/api/place/" +
                "nearbysearch/json?location=" + lat + ","  + lon + "&" +
                "radius=1500&" +
                "type=parking&" +
                "key=AIzaSyBxAPuenJbi0fS3s8EJLkycGMjLoHfSsMk";

        return url;
    }

    private void makeNetworkRequest(Context context, String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String resultsJsonString = null;
                        try {
                            resultsJsonString = response.getJSONArray("results").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //TODO: ON ERROR HERE AS WELL
                            return;
                        }

                        if (mPPCarParksSearchEventListener != null) {
                            CarPark[] carParks = gson.fromJson(resultsJsonString, CarPark[].class);
                            mPPCarParksSearchEventListener.onSuccessful(carParks);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mPPCarParksSearchEventListener != null) {
                            mPPCarParksSearchEventListener.onFail(error);
                        }
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjReq);

    }
}
