package com.timilehin.portpark;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddParkingInfoActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private SwitchCompat switchCompatTimer;
    private LinearLayout linearLayoutSetReminderTime;
    private TextView textViewReminderTime;
    private Button buttonSaveParking;
    private Integer timePickerSelectedHour = null;
    private Integer timePickerSelectedMinute = null;
    private LatLng carParkLocationLatLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carParkLocationLatLng = getIntent().getParcelableExtra(MainActivity.CarParkLocationLatLng);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_parking_info);

        switchCompatTimer = findViewById(R.id.switchCompatTimer);
        linearLayoutSetReminderTime = findViewById(R.id.linearLayoutSetReminderTime);
        textViewReminderTime = findViewById(R.id.textViewReminderTime);
        buttonSaveParking = findViewById(R.id.buttonSaveParking);

        switchCompatTimerSetOnCheckedChangeListener();
        linearLayoutSetReminderTimeSetOnClickListener();
        buttonSaveParkingSetOnClickListener();
    }

    private void disableLinearLayoutSetReminderTime() {
        linearLayoutSetReminderTime.setEnabled(false);
        linearLayoutSetReminderTime.setAlpha((float)0.1);
    }

    private void enableLinearLayoutSetReminderTime() {
        linearLayoutSetReminderTime.setEnabled(true);
        linearLayoutSetReminderTime.setAlpha((float)1.0);
    }

    private void switchCompatTimerSetOnCheckedChangeListener() {
        switchCompatTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    enableLinearLayoutSetReminderTime();
                } else {
                    disableLinearLayoutSetReminderTime();
                }
            }
        });
    }

    private void linearLayoutSetReminderTimeSetOnClickListener() {
        linearLayoutSetReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
    }

    private void buttonSaveParkingSetOnClickListener() {
        buttonSaveParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCarparkLatLngInDB();
                createAlarm();
                finish();
                String toastMessage = "Parking information saved";
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCarparkLatLngInDB() {
        if (carParkLocationLatLng == null) { return; }

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.saveLocation(carParkLocationLatLng.latitude, carParkLocationLatLng.longitude);
    }

    private void createAlarm() {
        if (linearLayoutSetReminderTime.isEnabled() == false) { return; }

        Calendar calendar = Calendar.getInstance();
        long timeInMil = TimeUnit.HOURS.toMillis(timePickerSelectedHour.intValue()) +
                TimeUnit.MINUTES.toMillis(timePickerSelectedMinute.intValue());
        calendar.setTimeInMillis(timeInMil);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMil, pendingIntent);
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = timePickerSelectedHour != null ? timePickerSelectedHour.intValue() : calendar.get(Calendar.HOUR_OF_DAY);
        int minute = timePickerSelectedMinute != null ? timePickerSelectedMinute : calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                this,
                hourOfDay,
                minute,
                true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        timePickerSelectedHour = i;
        timePickerSelectedMinute = i1;

        String timeString = timePickerSelectedHour.toString() + " : " + timePickerSelectedMinute.toString();
        textViewReminderTime.setText(timeString);
    }
}
