package com.timilehin.portpark;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by timilehin on 03/02/2018.
 */

final class Constants {

    /**
     * Constant values reused.
     */
    static final int SUCCESS_RESULT = 0;

    static final int FAILURE_RESULT = 1;
    static final String TAG = MainActivity.class.getSimpleName();
    static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    static final String KEY_CAMERA_POSITION = "camera_position";
    static final String KEY_LOCATION = "location";
    // Used for selecting the current place.
    static final int M_MAX_ENTRIES = 5;
    static final int DEFAULT_ZOOM = 15;
    private static final String PACKAGE_NAME =
            "com.timilehin.portpark";
    static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    // A default location (UK, Europe) and default zoom to use when location permission is
    // not granted.
    final LatLng mDefaultLocation = new LatLng(52.355518, -1.17432);

}
