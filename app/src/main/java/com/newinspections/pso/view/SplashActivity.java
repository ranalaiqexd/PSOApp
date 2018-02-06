package com.newinspections.pso.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newinspections.pso.R;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.utils.GPSTrackerNew;
import com.newinspections.pso.utils.RemoteRequestResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class SplashActivity extends InspectionBaseActivity{


    Context context;
    private Button buttonMacAddress;
    private String deviceIemi = null;
    private String deviceName = null;

    AppSession appSession=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        appSession = new AppSession(context);

        buttonMacAddress = (Button)findViewById(R.id.button_macAddrss);
        deviceName = android.os.Build.MODEL;

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("exit")){
            if (bundle.getInt("exit")==1){
                finish();
                return;
            }
        }


//        if (appSession.isImei()){
//            Intent intent = new Intent(context, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        else
//        {
//
//        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAllPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        buttonMacAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetworkStateClass.isOnline(context)) {

                    if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation())
                    {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE_FINE_LOCATION);
                        return;
                    }
                    else if (Build.VERSION.SDK_INT >= 23 && !canAccessCrossLocation())
                    {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE_CROSS_LOCATION);
                        return;
                    }
                    else
                    {
                        if (gpsTracker==null)
                            gpsTracker=new GPSTrackerNew(context);
                        else
                            gpsTracker.getLocation();
                    }


                    if (Build.VERSION.SDK_INT >= 23 && !canAccessPhoneState()) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_PHONE_STATE);
                        return;
                    }
                    else {
                        getDeviceId(context);
                    }

                    if (gpsTracker!=null){
                        latitude=gpsTracker.getLatitude();
                        longitude=gpsTracker.getLongitude();
                    }

                    if (latitude== 0.0 && longitude == 0.0) {
                        locationAlert();
                        return;
                    }
                    if (!gpsTracker.isGPSLocation()) {
                        alertGPSAlertMessage();
                        return;
                    }
                    else
                    {
                        if (taskForMacAddress!=null && !taskForMacAddress.isCancelled())
                        {
                            taskForMacAddress.cancel(true);
                        }
                        taskForMacAddress=new TaskForMacAddress();
                        taskForMacAddress.execute();
                    }
                }else {
                    showInternetAlert();
                }
            }
        });
    }

    public void alertGPSAlertMessage() {
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.v_gps_validate))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskForMacAddress!=null && !taskForMacAddress.isCancelled())
                            taskForMacAddress.cancel(true);
                        taskForMacAddress=new TaskForMacAddress();
                        taskForMacAddress.execute();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void checkAllPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permissions="";
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                if (TextUtils.isEmpty(permissions))
                    permissions= Manifest.permission.READ_PHONE_STATE;
                else
                    permissions= permissions+","+Manifest.permission.READ_PHONE_STATE;
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if (TextUtils.isEmpty(permissions))
                    permissions= Manifest.permission.ACCESS_FINE_LOCATION;
                else
                    permissions= permissions+","+Manifest.permission.ACCESS_FINE_LOCATION;
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if (TextUtils.isEmpty(permissions))
                    permissions= Manifest.permission.ACCESS_COARSE_LOCATION;
                else
                    permissions= permissions+","+Manifest.permission.ACCESS_COARSE_LOCATION;
            }
            if (TextUtils.isEmpty(permissions)){
                //do something
                if (gpsTracker==null)
                gpsTracker=new GPSTrackerNew(context);
            }
            else {
                requestPermissions(permissions.split(","), CHECK_ALL_PERMISSION);
            }
        }
        else
        {
            //do something
            if (gpsTracker==null)
            gpsTracker=new GPSTrackerNew(context);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i("requestCode","requestCode "+requestCode);
        switch (requestCode) {
            case CHECK_ALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 23 && !canAccessPhoneState()) {
                       //do nothing
                    }else {
                        getDeviceId(context);
                    }
                    if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation()) {
                        //do nothing
                    }else {
                        if (gpsTracker==null)
                            gpsTracker=new GPSTrackerNew(context);
                    }
                }
                break;
            case PERMISSION_REQUEST_CODE_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gpsTracker==null)
                    gpsTracker=new GPSTrackerNew(context);
                }
                break;
            case PERMISSION_REQUEST_CODE_CROSS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gpsTracker==null)
                    gpsTracker=new GPSTrackerNew(context);
                }
                break;
            case PERMISSION_REQUEST_CODE_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceId(context);
                }
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check that request code matches ours:
        if (requestCode == GPSTrackerNew.PERMISSION_GPS_ON && resultCode == Activity.RESULT_OK) {
            // Get our saved file into a bitmap object:
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gpsTracker=new GPSTrackerNew(context);
                }
            },2000);

        }
    }

        TaskForMacAddress taskForMacAddress=null;

    ProgressDialog pd;
    public class TaskForMacAddress extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(SplashActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("device_name",deviceName));
            Log.d("IEMI", deviceName);
            nvp.add(new BasicNameValuePair("user_latitude",latitude+""));
            nvp.add(new BasicNameValuePair("user_longitude",longitude+""));
            nvp.add(new BasicNameValuePair("mac_address",getDeviceId(context)));

            Log.i("params ","nvp "+nvp.toString());

            return new RemoteRequestResponse().httpPost(ConstantLib.BASE_URL + ConstantLib.URL_MAC_ADDRSS, nvp, null,null);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd!=null&& pd.isShowing())
                pd.dismiss();
            if (result!=null) {
                try {
                    Object object = new JSONTokener(result).nextValue();
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("status"))
                        {
                            if (jsonObject.getString("status").equals("1")){

                                new AppSession(SplashActivity.this).setImei(true);
                                Intent intent = new Intent(context, LoginActivity.class);
                                startActivity(intent);

                                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else {
                        Toast.makeText(context,"Response is not in proper format "+result,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Response is not in proper format "+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
