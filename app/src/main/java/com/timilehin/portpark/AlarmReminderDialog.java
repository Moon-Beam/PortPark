package com.timilehin.portpark;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlarmReminderDialog extends AppCompatActivity {

    private Button buttonOk, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_reminder_dialog);

        buttonOk = findViewById(R.id.buttonOk);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.timilehin.portpark.RingtoneManager.getRingtone().stop();
                Intent in = new Intent(v.getContext(), FindCar.class);
                startActivity(in);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.timilehin.portpark.RingtoneManager.getRingtone().stop();
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
