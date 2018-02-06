package com.newinspections.pso.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.newinspections.pso.R;
import com.newinspections.pso.utils.GPSTrackerNew;

public class InspectionBaseActivity extends AppCompatActivity {
    public static String TAG = "InspectionBaseActivity";


    public static double latitude; // latitude
    public static double longitude; // longitude
    GPSTrackerNew gpsTracker;
    final int CHECK_ALL_PERMISSION=21;
    final int PERMISSION_REQUEST_CODE_FINE_LOCATION=19;
    final int PERMISSION_REQUEST_CODE_CROSS_LOCATION=22;
    final int PERMISSION_REQUEST_CODE_PHONE_STATE=20;
    final int PERMISSION_REQUEST_CAMERA=15;
    final int PERMISSION_REQUEST_STORAGE=10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean check_internet = isInternetConnected(getApplicationContext());
        Log.d(TAG, check_internet + " ");
        if (!check_internet) {
          showInternetAlert();
        }
    }
    public boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public boolean hasPermission(String perm) {
        try {
            if (Build.VERSION.SDK_INT >= 23)
                return (PackageManager.PERMISSION_GRANTED == getApplicationContext().checkSelfPermission(perm));
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getDeviceId(Context context){
        String dvCid="";
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        dvCid=telephonyManager.getDeviceId();
        System.out.println("deviceIdddddddddddd"+dvCid);

        if (dvCid.equals("000000000000000"))
            return "357629064788636";

        return dvCid;

    }

    public boolean canAccessPhoneState() {
        return(hasPermission(Manifest.permission.READ_PHONE_STATE));
    }
    public boolean canAccessCrossLocation() {
        return(hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }


    public void showInternetAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Not available Internet")
                .setMessage(getResources().getString(R.string.connection_not_available))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void locationAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Location error")
                .setMessage(getResources().getString(R.string.v_gps_try))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void alertMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public static boolean isInternetConnected(Context mContext) {

        try {
            ConnectivityManager connect = null;
            connect = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connect != null) {
                NetworkInfo resultMobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                NetworkInfo resultWifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                return (resultMobile != null && resultMobile.isConnectedOrConnecting()) || (resultWifi != null && resultWifi.isConnectedOrConnecting());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
