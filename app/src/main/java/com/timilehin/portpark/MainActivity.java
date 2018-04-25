package com.timilehin.portpark;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.timilehin.portpark.Models.CarPark;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private PlaceAutocompleteFragment autocompleteFragment;
    private boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewCurrentLocationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();

        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        textViewCurrentLocationAddress = (TextView) findViewById(R.id.currentLocationAddress);
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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
        if (map == null) {
            return;
        }
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

    private void mapSetMyLocationEnabled() {
        if (locationPermissionGranted == false) {
            return;
        }
        if (map == null) {
            return;
        }

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        getCurrentLocation();

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                getCurrentLocation();

                return false;
            }
        });
    }

    private void getCurrentLocation() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            updateTextViewCurrentLocationAddress(latLng);
                            //Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "location success: no location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private Address getAddressFrom(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); //CATCH EXCEPTION
            if (addressList == null || addressList.size() == 0) { return null; }
            return  addressList.get(0);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Geo coder error", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private void updateTextViewCurrentLocationAddress(LatLng latlng) {
        Address address = getAddressFrom(latlng);
        if (address == null) {
            textViewCurrentLocationAddress.setText("Getting current location address . . .");
            return;
        }
        String addressString = "Current location: " +
                address.getAddressLine(0).toString()+ ", " +
                address.getCountryName();
        textViewCurrentLocationAddress.setText(addressString);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapSetMyLocationEnabled();
        Log.d("AAA", "MAP IS READY");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    mapSetMyLocationEnabled();
                } else {
                    locationPermissionGranted = false;
                    requestPermissions();
                }
            }
        }
    }
}
