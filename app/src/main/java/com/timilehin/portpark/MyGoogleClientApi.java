package com.timilehin.portpark;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlacesOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by work on 24/04/2018.
 */

public class MyGoogleClientApi extends FragmentActivity implements OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TEST
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        getCurrentPlaceItems();
        //END TEST

    }

    //TEST
    protected PlaceDetectionClient placeDetectionClient;

    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            getCurrentPlaceData();
        } else {
            requestLocationAccessPermission();
        }
    }

    private void getCurrentPlaceData() {
        ArrayList<String> restrictToRestaurants = new ArrayList<>();
        restrictToRestaurants.add(Integer.toString(Place.TYPE_PARKING));
        PlaceFilter pf = new PlaceFilter(true, restrictToRestaurants);

        Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);

        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                Log.d("DDD", "current location places info");
                List<Place> placesList = new ArrayList<Place>();
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    placesList.add(placeLikelihood.getPlace().freeze());
                }
                likelyPlaces.release();
                Log.d("DDD", placesList.toString());
            }
        });
    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }
    //END TEST

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }
}
