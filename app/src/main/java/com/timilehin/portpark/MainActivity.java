package com.timilehin.portpark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.VolleyError;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.timilehin.portpark.Models.CarPark;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        setupAutocompleteFragment();
    }

    private void setupAutocompleteFragment() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                double lng = place.getLatLng().longitude;
                double lat = place.getLatLng().latitude;
                String lngString = String.valueOf(lng);
                String latString = String.valueOf(lat);
                searchForCarParks(lngString, latString);
                zoomMap(new LatLng(lat, lng));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("AAA", "An error occurred: " + status);
            }
        });
    }

    private void zoomMap(LatLng latLng) {
        CameraPosition camPos = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14)
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        map.animateCamera(camUpd3);
    }

    private void searchForCarParks(String lng, String lat) {
        PPCarParksSearch pps = new PPCarParksSearch(this, lng, lat);
        PPCarParksSearchEventListener carParksSearchEventListener = new PPCarParksSearchEventListener() {
            @Override
            public void onSuccessful(CarPark[] carPacks) {
                Log.d("AAA", "onSuccessful: " + carPacks.toString());
                updateMapWithCarParks(carPacks);
            }

            @Override
            public void onFail(VolleyError error) {
                Log.d("AAA", "onFail: ");
            }
        };
        pps.setOnEventListener(carParksSearchEventListener);
    }

    private void updateMapWithCarParks(CarPark[] carParks) {
        if (map == null) { return; }
        for (int a = 0; a < carParks.length; a++) {
            CarPark carPark = carParks[a];
            double lat = carPark.getGeometry().getLocation().getLat();
            double lng = carPark.getGeometry().getLocation().getLng();
            String name = carPark.getName();

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.title(name);
            map.addMarker(markerOptions);
            Log.d("AAA", "MARKER ADDED");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d("AAA", "MAP IS READY");
    }
}
