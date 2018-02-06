package com.newinspections.pso.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.newinspections.pso.R;

/**
 * @author Pintu Kumar Patil 9977368049
 * @author 14-May-2015
 */
public class GPSTrackerNew extends Service implements LocationListener {
    private final Context mContext;

    public static final int PERMISSION_GPS_ON = 1019;
    // private final short ACCURACY = 50;
    // flag for GPS status
    public boolean isGPSEnabled = false;

//    edited by exd
    // flag for network status
    public boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false, isGPSlocation = false, isNWUpdateLocation = false;
    private Location location; // location
    public static double latitude = 0.0; // latitude
    public static double longitude = 0.0; // longitude


    // The minimum distance to change Updates in meters
    private static final long MIN_ACCURACY_OF_TIME =1000 * 60 * 5;// 5 minutes

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;// 2 meter

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1;//five second

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

    public void getLocation() {

        try {
            // flag for new location
            this.isNWUpdateLocation = false;

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            }
            else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                if ((System.currentTimeMillis() - location.getTime()) < MIN_ACCURACY_OF_TIME) {
                                    this.isGPSlocation = true;
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    this.isNWUpdateLocation = true;
                                } else {
                                    this.isNWUpdateLocation = false;

                                }
                            }
                        }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                if ((System.currentTimeMillis() - location.getTime()) < MIN_ACCURACY_OF_TIME) {
                                    this.isGPSlocation = false;
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    this.isNWUpdateLocation = true;
                                } else {
                                    this.isNWUpdateLocation = false;
                                }
                            }
                        }
                    }
                }
                if (latitude != 0.0 && longitude != 0.0) {
                    Utils.setPref(mContext, Constants.USER_LATITUDE, "" + latitude);
                    Utils.setPref(mContext, Constants.USER_LONGITUDE, "" + longitude);
                }
            }
            if (!isGPSEnabled) {
                showSettingsAlert(mContext);
                return;
            }else if (!isNWUpdateLocation()) {
                showSettingsOFFONAlert(mContext);
                return;
            }else if (!canGetLocation()) {
                alertMessage(mContext.getResources().getString(R.string.v_gps_enable));
                return;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void alertMessage(String message) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        if (latitude==0.0 && !TextUtils.isEmpty(Utils.getPref(mContext, Constants.USER_LATITUDE, "" +latitude))){
             return Double.parseDouble(Utils.getPref(mContext, Constants.USER_LATITUDE, "" +latitude));
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
        if (longitude==0.0 && !TextUtils.isEmpty(Utils.getPref(mContext, Constants.USER_LONGITUDE, "" +longitude))){
            return Double.parseDouble(Utils.getPref(mContext, Constants.USER_LONGITUDE, "" +longitude));
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
                                ((Activity)mContext).startActivityForResult(intent,PERMISSION_GPS_ON);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                ((Activity)mContext).startActivityForResult(intent, PERMISSION_GPS_ON);
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
    public void showSettingsOFFONAlert(final Context mContext) {
        Builder alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("warning");

            // Setting Dialog Message
            alertDialog.setMessage(""
                    + mContext.getResources().getString(R.string.v_gps_old_location));

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ((Activity)mContext).startActivityForResult(intent,PERMISSION_GPS_ON);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                ((Activity)mContext).startActivityForResult(intent, PERMISSION_GPS_ON);
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

            if (location==null)
                return;

            this.location = location;
            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Utils.setPref(mContext, Constants.USER_LATITUDE, "" + latitude);
                Utils.setPref(mContext, Constants.USER_LONGITUDE, "" + longitude);
            }
            Log.e(getClass().getName(), "onLocationChanged " + latitude + " , " + longitude);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
            if (android.os.Build.VERSION.SDK_INT >= 18) {
                if (location==null)
                    return false;

                return location.isFromMockProvider();
            } else {
               return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
            }
        } catch (Exception e) {
        }
        return false;
    }
}
