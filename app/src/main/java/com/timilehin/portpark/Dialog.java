package com.timilehin.portpark;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Dialog extends AppCompatActivity {

    private Button okBtn, cancleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        okBtn = findViewById(R.id.okBtn);
        cancleBtn = findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ringtone ringtone = getIntent().getParcelableExtra("abc");
                //ringtone.stop();
                com.timilehin.portpark.RingtoneManager.getRingtone().stop();
//                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
//                ringtone.stop();
                Intent in = new Intent(v.getContext(), MainActivity.class);
                startActivity(in);
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
                ringtone.stop();
                onStop();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
