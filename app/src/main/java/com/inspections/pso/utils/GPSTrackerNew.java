package com.inspections.pso.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


import com.inspections.pso.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Pintu Kumar Patil 9977368049
 * @author 14-May-2015
 */
public class GPSTrackerNew extends Service implements LocationListener {
    private final Context mContext;
    // private final short ACCURACY = 50;
    // flag for GPS status
    public boolean  isGPSSupport = false, isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false, isGPSlocation = false,
            isNWUpdateLocation = false;
    private Location location; // location
    public static double latitude = 0.0; // latitude
    public static double longitude = 0.0; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 12;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 12 * 1;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    AppSession appSession = null;

    public GPSTrackerNew(Context context) {
        this.mContext = context;
        appSession = new AppSession(context);
        getLocation();
    }

    /**
     * Use loc.getAccuracy() method to check the accuracy level of location you
     * received. If the value is less then 10(or less than that) then you can
     * consider it , otherwise wait for location Lister to fetch another
     * location. getLastKnownLocation is your last known location, dont use just
     * getAccuracy also check the time. Better dont use getLastKnownLocation if
     * you need only accurate location.
     */

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            final List<String> providers = locationManager.getAllProviders();

            if (providers == null) {

            } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                isGPSSupport = true;
                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSSupport && !isGPSEnabled) {

            } else if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.i("GPS",
                                    "location.getAccuracy() "
                                            + location.getAccuracy());
                            Log.i("GPS",
                                    "location.getTime() " + location.getTime());
                            Log.i("GPS",
                                    "Time difference "
                                            + (System.currentTimeMillis() - location
                                            .getTime()));
                            if ((System.currentTimeMillis() - location
                                    .getTime()) < (2 * 60 * 1000)) {
                                this.isGPSlocation = true;
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                        Log.i(getClass().getName(), "GPS Enabled " + latitude
                                + " , " + longitude);
                    }
                }
                if (isNetworkEnabled
                        && (latitude == 0.0 || longitude == 0.0)) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.i("Network", "location.getAccuracy() "
                                    + location.getAccuracy());
                            Log.i("Network",
                                    "location.getTime() " + location.getTime());
                            Log.i("Network",
                                    "Time difference "
                                            + (System.currentTimeMillis() - location
                                            .getTime()));
                            if ((System.currentTimeMillis() - location
                                    .getTime()) < (2 * 60 * 1000)) {
                                this.isNWUpdateLocation = true;
                            }
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        Log.i(getClass().getName(), "Network Enabled "
                                + latitude + " , " + longitude);
                    }
                }
                if (latitude == 0.0 || longitude == 0.0) {
                    locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        if (location != null) {
                            Log.i("Passive", "location.getAccuracy() "
                                    + location.getAccuracy());
                            Log.i("Passive",
                                    "location.getTime() " + location.getTime());
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        Log.i(getClass().getName(), "PASSIVE_PROVIDER "
                                + latitude + " , " + longitude);
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTrackerNew.this);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public boolean isGPSLocation() {
        return this.isGPSlocation;
    }

    public boolean isNWUpdateLocation() {
        return this.isNWUpdateLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */

    public void showSettingsAlert(final Context mContext) {
        Builder alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("warning");

            // Setting Dialog Message
            alertDialog.setMessage(""
                    + mContext.getResources().getString(R.string.v_gps_enable));

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ((Activity)mContext).startActivityForResult(intent,1001);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                ((Activity)mContext).startActivityForResult(intent, 1001);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            this.location = location;

            if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {

            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String strlatitude = "" + new DecimalFormat("###.0####").format(latitude);
                String strlongitude = "" + new DecimalFormat("###.0####").format(longitude);
                Utils.setPref(this, Constants.USER_LONGITUDE, "" + strlongitude);
                Utils.setPref(this, Constants.USER_LATITUDE, "" + strlatitude);
            }
            Log.i(getClass().getName(), "onLocationChanged " + latitude + " , " + longitude);


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public boolean isMockSettingsON() {
        try { // returns true if mock location enabled, false if not enabled.
            if (Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                return false;
            else
                return true;
        } catch (Exception e) {
        }
        return false;
    }
}
