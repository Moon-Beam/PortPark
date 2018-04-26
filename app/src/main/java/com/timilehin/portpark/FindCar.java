package com.timilehin.portpark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class FindCar extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper database;
    private SupportMapFragment mapFragment;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_find_car);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        database = new DBHelper(this);
        getLocations();

//        Intent inte = new Intent(this, testDB.class);
//        startActivity(inte);
    }


    @Override
    protected void onPause() {
        database.close();
        super.onPause();
    }

    @Override
    protected void onStop() {
        database.close();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please turn on location settings!", Toast.LENGTH_LONG).show();
            return;
        }else googleMap.setMyLocationEnabled(true);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(latLng);
        markerOptions1.title("Your Car" + latLng);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(new LatLng(34.190295, 43.895786));
        markerOptions2.title("Your Location");


        googleMap.addMarker(markerOptions1);
        googleMap.addMarker(markerOptions2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.190295, 43.895786)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));



    }

    private void getLocations(){
        Cursor cursor = database.getLocation();

        if (cursor.moveToLast()){

            double lat = cursor.getDouble(1);//getColumnIndex(DBHelper.COLUMN_LATITUDE);
            double lng = cursor.getDouble(2);//getColumnIndex(DBHelper.COLUMN_LONGITUDE);

            latLng = new LatLng(lat, lng);

        }

        cursor.close();
        database.close();
    }

}
