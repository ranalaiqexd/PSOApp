package com.newinspections.pso.newview;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.dao.UtilDAO;
import com.newinspections.pso.database.StationORM;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.utils.GPSTrackerNew;
import com.newinspections.pso.utils.GeoAddress;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.newinspections.pso.utils.ConstantLib.GET_USER_INSPECTIONS;
import static com.newinspections.pso.utils.ConstantLib.USER_BLOCK;
import static com.newinspections.pso.utils.ConstantLib.USER_LOGIN;

public class NewLoginActivity extends AppCompatActivity {

    private static final String TAG = "NewLoginActivity";

    Context context;
    AppSession appSession = null;
    GPSTrackerNew gpsTrackerNew = null;
    private String deviceIemi = null;
    private String deviceName = null;
    private EditText userNameText;
    private EditText userPasswordText;
    private Button buttonEnter;
    private String userName = "";
    private String userPassword = "";
    private double userLongitude = 0;
    private double userLatitude = 0;
    private String userAddress = "";
    private String userLoginParams = "";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 0;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private RequestQueue requestQueue;
    private Gson gson;
    private int blockCounter = 0;

    UserLoginTask userLoginTask = null;
    AddressTask addressTask = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latlong_enter);

        context = this;
        appSession = new AppSession(context);
        userNameText = (EditText)findViewById(R.id.edtUsrNme);
        userPasswordText = (EditText)findViewById(R.id.edtPsswrd);
        buttonEnter = (Button)findViewById(R.id.btnEntr);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        deviceIemi = appSession.getIemiNo();
        deviceName = appSession.getDevice();
        gpsTrackerNew = new GPSTrackerNew(context);
        CheckPermissions();


    }

    @Override
    protected void onResume() {
        super.onResume();

        userLongitude = gpsTrackerNew.getLongitude();
        userLatitude = gpsTrackerNew.getLatitude();

        buttonEnter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if((userNameText.getText().toString().isEmpty()) || (userPasswordText.getText().toString().isEmpty()))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                }
                else if(deviceIemi.isEmpty())
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Scan Mac Address First", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                    Intent intent = new Intent(context, MacActivity.class);
                    startActivity(intent);
                }
                else
                {
                    userName = userNameText.getText().toString();
                    userPassword = userPasswordText.getText().toString();
                    userLoginParams = "username="+userName+"&password="+userPassword+"&macaddress="+deviceIemi+"&device="+deviceName+"&longitude="+userLongitude+"&latitude="+userLatitude;
                    Log.d("Result", userName+userPassword+deviceIemi+deviceName);
                    Log.d("Result", ""+userLatitude+userLongitude);

                    if(CheckNetworkStateClass.isOnline(context))
                    {
                        callOnlineTasks();
                    }
                    else
                    {
                        if ((userName.equals(appSession.getUserName())) && (userPassword.equals(appSession.getPassword())) &&
                                (deviceIemi.equals(appSession.getIemiNo())))
                        {
                            Intent intent = new Intent(NewLoginActivity.this, NewDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(context, "Your username or password is incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    private void CheckPermissions()
    {
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
        else
        {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    // you may now do the action that requires this permission
                    gpsTrackerNew = new GPSTrackerNew(context);
                    userLongitude = gpsTrackerNew.getLongitude();
                    userLatitude = gpsTrackerNew.getLatitude();
                }
                else {
                    // permission denied
                    Toast.makeText(context, "Must Provide Permission First", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    // you may now do the action that requires this permission
                    gpsTrackerNew = new GPSTrackerNew(context);
                    userLongitude = gpsTrackerNew.getLongitude();
                    userLatitude = gpsTrackerNew.getLatitude();
                }
                else {
                    // permission denied
                    Toast.makeText(context, "Must Provide Permission First", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void callOnlineTasks() {

        addressTask = new AddressTask();
        addressTask.execute();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if ( progressDialog!=null && progressDialog.isShowing() )
//        {
//            progressDialog.cancel();
////            progressDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        if ( progressDialog!=null && progressDialog.isShowing() )
//        {
////            progressDialog.cancel();
//            progressDialog.dismiss();
//        }
//    }

    public class AddressTask extends AsyncTask<Void, Void, GeoAddress>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(NewLoginActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Fetching location...");
            progressDialog.show();
        }

        @Override
        protected GeoAddress doInBackground(Void... params) {
            return new UtilDAO().getAddress(userLatitude,userLongitude);
        }

        @Override
        protected void onPostExecute(GeoAddress geoAddress) {
            super.onPostExecute(geoAddress);

            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();

            }

            if (geoAddress!=null)
            {
                userAddress = geoAddress.getFullAddress();
                Log.d("status", geoAddress.getStatus());
                userLoginTask = new UserLoginTask();
                userLoginTask.execute();
            }
            else
            {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            cancel(true);
        }
    }

    String tag_json_arry = "json_array_req";
    String  tag_string_req = "string_req";
    String result = "";

    public class UserLoginTask extends AsyncTask<Void, Void, String>
    {
        GetInspectionsFromServer getInspectionsFromServer = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(NewLoginActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Authenticating User");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            Log.d("Url", ConstantLib.BASE_URL+""+USER_LOGIN+userLoginParams);
            Log.d("Url", "&address="+userAddress);
            if (userAddress==null)
            {
                userAddress = "";
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(ConstantLib.BASE_URL+""+USER_LOGIN+userLoginParams+"&address="+userAddress)
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(1000, TimeUnit.SECONDS);
            builder.readTimeout(1000, TimeUnit.SECONDS);
            builder.writeTimeout(1000, TimeUnit.SECONDS);
            client = builder.build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception Caught";
            }
            Log.d("Answer", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(result!=null)
                {

                    Log.d("Answer", result);
                    if (result.equals("2"))
                    {
                        errorAlert("Mac Error Alert", "Mac Address does not match. Contact Administrator");
                    }
//                    else if (result.equals("11"))
//                    {
//                        // has update, fetch data from server
//                        Toast.makeText(getApplicationContext(), "Authentication Successfull and has update", Toast.LENGTH_LONG).show();
//                        appSession.setUserName(userName);
//                        appSession.setPassword(userPassword);
//
//                        getInspectionsFromServer = new GetInspectionsFromServer();
//                        getInspectionsFromServer.execute();
//                    }
//                    else if (result.equals("10"))
//                    {
//                        // no update, fetch data from local database
//                        Toast.makeText(getApplicationContext(), "Authentication Successfull and has no update", Toast.LENGTH_LONG).show();
//                        appSession.setUserName(userName);
//                        appSession.setPassword(userPassword);
//                        Intent intent = new Intent(NewLoginActivity.this, NewDashboard.class);
//                        startActivity(intent);
//                        finish();
//                    }
                    else if (result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Authentication Successfull", Toast.LENGTH_SHORT).show();
                        appSession.setUserName(userName);
                        appSession.setPassword(userPassword);
                        Intent intent = new Intent(NewLoginActivity.this, NewDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (result.equals("0"))
                    {
                        errorAlert("Access Denied", "You are not allowed to login. Contact Administrator");
                    }
                    else if (result.equals("3"))
                    {
                        if (blockCounter<4)
                        {
                            errorAlert("Access Denied", "You are not allowed to login");
                            blockCounter++;
                        }
                        else
                        {
                            errorAlert("Access Denied", "You user has been blocked. Contact Administrator");
                            UserBlockTask();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public void UserBlockTask()
    {

        requestQueue = Volley.newRequestQueue(this);
        String url = ConstantLib.BASE_URL+""+USER_BLOCK+"username="+appSession.getUserName();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response", response);

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        });
//      Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    public class GetInspectionsFromServer extends AsyncTask<Void, Void, String>
    {

        String urlInspection = ConstantLib.BASE_URL+""+GET_USER_INSPECTIONS+"username="+appSession.getUserName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(NewLoginActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Fetching Inspections from Server");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("UrlTest", urlInspection);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlInspection)
                    .build();



            Response response = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
            Log.d("Answer", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            Log.d("checkhere", "Hello "+result);
            if(result!=null)
            {
                List<InspectionsModel> inspections = Arrays.asList(gson.fromJson(result, InspectionsModel.class));

                StationORM.truncateStationTable(context);
                for (InspectionsModel inspection : inspections) {
                    StationORM.insertStationsToDb(context, inspection);
                }

                InspectionsModel inspectionsModel = new Gson().fromJson(result, new TypeToken<InspectionsModel>() {}.getType());
                appSession.setInspectionModel(inspectionsModel);

                Intent intent = new Intent(NewLoginActivity.this, NewDashboard.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void errorAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewLoginActivity.this);


        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
