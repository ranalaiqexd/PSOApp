package com.inspections.pso.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inspections.pso.R;
import com.inspections.pso.netcome.CheckNetworkStateClass;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ConstantLib;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.RemoteRequestResponse;
import com.inspections.pso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class SplashActivity extends InspectionBaseActivity{


    Context context;
    private Button btnMacAdrs;
    String GPS_LAT = "", GPS_LONG = "";
    private String dviCeNm="";

    AppSession appSession=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        appSession=new AppSession(context);

        btnMacAdrs=(Button)findViewById(R.id.button_macAddrss);
        dviCeNm = android.os.Build.MODEL;

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("exit")){
            if (bundle.getInt("exit")==1){
                finish();
                return;
            }
        }

        if (appSession.isImei()){
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btnMacAdrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNetworkStateClass.isOnline(context)) {

                    if (Build.VERSION.SDK_INT >= 23 && !canAccessPhoneState()) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_PHONE_STATE);
                        return;
                    }else {
                        boolean check_gps = isGPSConnected(getApplicationContext());
                        if (!check_gps) {
                            showSettingsAlert();
                        }else{
                            getLocation();
                        }
                    }
                    getLocationLogin();
                    if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation()) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE_FINE_LOCATION);
                        return;
                    }else {
                        getDeviceId();
                    }

                   if (taskForMacAddress!=null && !taskForMacAddress.isCancelled())
                       taskForMacAddress.cancel(true);

                    taskForMacAddress=new TaskForMacAddress();
                    taskForMacAddress.execute();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocationLogin();
    }

    private void getLocationLogin(){
        if (InspectionBaseActivity.isGPSConnected(getApplicationContext())) {
            GPS_LAT = Utils.getPref(getApplicationContext(), Constants.USER_LATITUDE, "0.0");
            GPS_LONG = Utils.getPref(getApplicationContext(), Constants.USER_LONGITUDE, "0.0");
            Log.d(TAG, " 1" + GPS_LONG + " " + GPS_LAT);
            if (!GPS_LAT.equalsIgnoreCase("0.0") && !GPS_LONG.equalsIgnoreCase("0.0")) {
                Log.d(TAG, " " + GPS_LONG + " " + GPS_LAT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        Log.i("requestCode","requestCode "+requestCode);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean check_gps = isGPSConnected(getApplicationContext());
                    if (!check_gps) {
                        showSettingsAlert();
                    }else{
                        getLocation();
                    }
                }
                break;
            case PERMISSION_REQUEST_CODE_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceId();
                }
                break;
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
            nvp.add(new BasicNameValuePair("device_name",dviCeNm));
            nvp.add(new BasicNameValuePair("user_latitude",GPS_LAT));
            nvp.add(new BasicNameValuePair("user_longitude",GPS_LONG));
            nvp.add(new BasicNameValuePair("mac_address",getDeviceId()));

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
                    } else {
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
