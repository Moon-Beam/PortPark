package com.timilehin.portpark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddParkingInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

       // getWindow().setLayout((int)(width*.8),(int)(height*.25));
        setContentView(R.layout.activity_add_parking_info);

    }

    //Intent intent = new Intent(this, FindCar.class);
    //startActivity(intent)

}
