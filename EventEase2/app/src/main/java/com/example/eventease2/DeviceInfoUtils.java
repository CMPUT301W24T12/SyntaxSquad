package com.example.eventease2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.UUID;


/**
 * Utility class for retrieving device information such as ICCID, IMSI, and IMEI.
 * <p>
 * This class provides methods to retrieve unique identifiers associated with the device's SIM card
 * (such as ICCID), the device itself (such as IMEI), and a combination of the device's Android ID
 * and ICCID (such as IMSI). It also handles permission checks and compatibility issues with
 * different Android SDK versions.
 * </p>
 * <p>
 * Note: Some methods in this class require the 'READ_PHONE_STATE' permission in the application's manifest.
 * </p>
 *
 * @author yovi.putra
 * @version 1.0
 * @since 2020
 */
@SuppressLint({"HardwareIds","MissingPermission"})
public class DeviceInfoUtils {
    /**
     * Retrieves the ICCID (Integrated Circuit Card Identifier) of the device's SIM card.
     * <p>
     * This method returns the ICCID of the SIM card inserted in the device. For devices with
     * dual SIM support (Android SDK >= Lollipop MR1), it retrieves ICCIDs of all active SIM cards
     * and concatenates them. For devices with SDK < Lollipop MR1, it retrieves the SIM serial number.
     * </p>
     *
     * @param context The context of the application.
     * @return A string representing the ICCID of the device's SIM card(s), or null if unavailable.
     */
    public static String getICCID(Context context) {
        if (isPermissionGranted(context)) {
            if (isICCIDGranted()) {
                StringBuilder iccid = new StringBuilder();
                List<SubscriptionInfo> subscription = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscription == null) return null;


                for (SubscriptionInfo info : subscription) {
                    iccid.append(info.getIccId());
                }
                return iccid.toString().isEmpty() ? null : iccid.toString();
            } else {
                //dual sim not support SDK < lollipop
                return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
            }
        }
        return null;
    }
    /**
     * Generates the IMSI (International Mobile Subscriber Identity) using the ICCID and Android ID.
     * <p>
     * This method generates a unique identifier for the device using the ICCID and Android ID.
     * </p>
     *
     * @param context The context of the application.
     * @return A string representing the IMSI of the device.
     */
    public static String getIMSI(Context context) {
        String iccid = getICCID(context);
        iccid = iccid == null ? "" : iccid;
        return new UUID(getAndroidId(context).hashCode(), iccid.hashCode()).toString();
    }
    /**
     * Generates a pseudo-IMEI (International Mobile Equipment Identity) based on device attributes.
     * <p>
     * This method generates a pseudo-IMEI by combining various device attributes such as board,
     * brand, device, etc., and hashing them with the Android ID.
     * </p>
     *
     * @param context The context of the application.
     * @return A string representing the pseudo-IMEI of the device.
     */
    public static String getIMEI(Context context) {
        return generateIMEI(context);
    }


    private static boolean isPermissionGranted(Context context) {
        String wantPermission = Manifest.permission.READ_PHONE_STATE;
        return ActivityCompat.checkSelfPermission(context, wantPermission) == PackageManager.PERMISSION_GRANTED;
    }


    private static boolean isICCIDGranted() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }


    private static String generateIMEI(Context context) {
        String uniquePseudoID = "" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        return new UUID(uniquePseudoID.hashCode(), getAndroidId(context).hashCode()).toString();
    }


    private static String getAndroidId(Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }
}
