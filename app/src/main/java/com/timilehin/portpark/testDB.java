package com.timilehin.portpark;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class testDB extends AppCompatActivity {

    private DBHelper database;

    private EditText latText, lngText;
    private Button saveBtn;
    private TextView latView, lngView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);

        database = new DBHelper(this);

        latText = findViewById(R.id.latTex);
        lngText = findViewById(R.id.lngTex);
        saveBtn = findViewById(R.id.button);
        latView = findViewById(R.id.latView);
        lngView = findViewById(R.id.lngView);

        showUser();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLoca();}
        });
    }

    private void showUser(){
        Cursor cursor = database.getLocation();

            if (cursor.moveToLast()){

                double lat = cursor.getDouble(1);//getColumnIndex(DBHelper.COLUMN_LATITUDE);
                double lng = cursor.getDouble(2);//getColumnIndex(DBHelper.COLUMN_LONGITUDE);

                latView.setText(String.valueOf(lat));
                lngView.setText(String.valueOf(lng));

            }

        cursor.close();
        database.close();
    }

    private void addLoca(){
        double lat = Double.valueOf(latText.getText().toString().trim());
        double lng = Double.valueOf(lngText.getText().toString().trim());

        if(database.saveLocation(lat, lng)){
            Toast.makeText(this, "Data Added", Toast.LENGTH_LONG).show();
        }else Toast.makeText(this, "Data not Added", Toast.LENGTH_LONG).show();
    }

}
