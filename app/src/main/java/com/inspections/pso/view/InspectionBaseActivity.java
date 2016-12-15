package com.inspections.pso.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.inspections.pso.R;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.Utils;

import java.text.DecimalFormat;


/**
 * Created by asus on 18/12/15.
 * This base activity is extended in other activity which include
 * 1) Internet connection check
 */
public class InspectionBaseActivity extends AppCompatActivity implements LocationListener {
    public static String TAG = "InspectionBaseActivity";

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    final int PERMISSION_REQUEST_CODE_FINE_LOCATION=19;
    final int PERMISSION_REQUEST_CODE_PHONE_STATE=20;
    final int PERMISSION_REQUEST_CAMERA=15;
    final int PERMISSION_REQUEST_STORAGE=10;
    final int PERMISSION_READ_PHONE_STATE=1217;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 0 meters

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 10 * 1; // 1 second

    // Declaring a Location Manager
    protected LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    AlertDialog alert;
    @Override
    protected void onStart() {
        super.onStart();
        boolean check_internet = isInternetConnected(getApplicationContext());
        Log.d(TAG, check_internet + " ");
        if (!check_internet) {
          showInternetAlert();
        }else {
            if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation()) {
                ActivityCompat.requestPermissions(InspectionBaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_FINE_LOCATION);
            }else {
                boolean check_gps = isGPSConnected(getApplicationContext());
                if (!check_gps) {
                    showSettingsAlert();
                }else{
                    getLocation();
                }
            }
        }
        //alertDialogManager = new AlertDialogManager();
        //  alertDialogManager.showAlertDialog(NoornetBaseActivity.this, getResources().getString(R.string.connection_not_available));
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

    public String getDeviceId(){
        String dvCid="";
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        dvCid=telephonyManager.getDeviceId();
        System.out.println("deviceIdddddddddddd"+dvCid);

        if (dvCid.equals("000000000000000"))
            return "357629064788636";

        return dvCid;

    }
    public boolean canAccessPhoneState() {
        return(hasPermission(Manifest.permission.READ_PHONE_STATE));
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


    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle(this.getString(R.string.GPSsettings));

            // Setting Dialog Message
            alertDialog.setMessage(this.getString(R.string.GPSEnable));

            // On pressing Settings button
            alertDialog.setPositiveButton(this.getString(R.string.Settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static boolean isGPSConnected(final Context mContext) {

        try {
            final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Utils.setPref(this, Constants.USER_LONGITUDE, "" + longitude);
                            Utils.setPref(this, Constants.USER_LATITUDE, "" + latitude);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if ( Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Utils.setPref(this, Constants.USER_LONGITUDE, "" + longitude);
                                Utils.setPref(this, Constants.USER_LATITUDE, "" + latitude);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.sendExceptionReport(e, this);
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        if (locationManager != null) {
            locationManager.removeUpdates(InspectionBaseActivity.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;

    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Utils.setPref(this, Constants.USER_LONGITUDE, "" + longitude);
        Utils.setPref(this, Constants.USER_LATITUDE, "" + latitude);
        MIN_TIME_BW_UPDATES = 1000 * 5; // 5 second
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
