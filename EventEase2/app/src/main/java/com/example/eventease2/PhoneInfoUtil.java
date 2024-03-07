package com.example.eventease2;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfoUtil {
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                return telephonyManager.getImei();
            } else {
                // For devices below Android Oreo, use getDeviceId() which may return IMEI for GSM phones.
                return telephonyManager.getDeviceId();
            }
        }
        return null;
    }
}

