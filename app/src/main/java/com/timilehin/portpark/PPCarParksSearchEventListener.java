package com.timilehin.portpark;

import com.android.volley.VolleyError;
import com.timilehin.portpark.Models.CarPark;

public interface PPCarParksSearchEventListener {
    void onSuccessful(CarPark[] carPacks);
    void onFail(VolleyError error);
}
