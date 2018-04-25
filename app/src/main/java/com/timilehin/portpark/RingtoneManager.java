package com.timilehin.portpark;

import android.media.Ringtone;

public class RingtoneManager {

    static private Ringtone ringtone;

    static public void setRingtone(Ringtone ringtone) {
        RingtoneManager.ringtone = ringtone;
    }

    static public Ringtone getRingtone() {
        return RingtoneManager.ringtone;
    }
}
