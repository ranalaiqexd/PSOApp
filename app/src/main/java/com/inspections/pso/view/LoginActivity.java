package com.inspections.pso.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspections.pso.R;
import com.inspections.pso.dao.UtilDAO;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.netcome.CheckNetworkStateClass;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ConstantLib;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.GeoAddress;
import com.inspections.pso.utils.RemoteRequestResponse;
import com.inspections.pso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by mobiweb on 29/2/16.
 */
public class LoginActivity extends InspectionBaseActivity implements View.OnClickListener {

    Context context;
    private Button btnEntr;
    private EditText edtUsrNm;
    private EditText edtPswrd;
    String GPS_LAT = "", GPS_LONG = "";
    private AppSession appSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_latlong_enter);

        context= this;
        edtUsrNm=(EditText)findViewById(R.id.edtUsrNme);
        edtPswrd=(EditText)findViewById(R.id.edtPsswrd);
        btnEntr=(Button)findViewById(R.id.btnEntr);
        appSession= new AppSession(context);
        btnEntr.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (InspectionBaseActivity.isGPSConnected(getApplicationContext())) {
            GPS_LAT = Utils.getPref(getApplicationContext(), Constants.USER_LATITUDE, "0.0");
            GPS_LONG = Utils.getPref(getApplicationContext(), Constants.USER_LONGITUDE, "0.0");
            Log.d(TAG, " 1" + GPS_LONG + " " + GPS_LAT);
            if (!GPS_LAT.equalsIgnoreCase("0.0") && !GPS_LONG.equalsIgnoreCase("0.0")) {
                Log.d(TAG, " " + GPS_LONG + " " + GPS_LAT);
            }
        }

    }
    String user_name = "";
    String password = "";
    @Override
    public void onClick(View v) {

         user_name = edtUsrNm.getText().toString();
         password = edtPswrd.getText().toString();

        switch (v.getId()){

            case R.id.btnEntr:

                if (user_name.equals("")){
                    edtUsrNm.setError("Insert user name");
                }else if (password.equals("")){
                    edtPswrd.setError("Insert password");
                }else {
                    if (CheckNetworkStateClass.isOnline(context)) {
                        callTask();
                    } else {
                        InspectionDTO dto=appSession.getInspectionDTO();
                        if (dto!=null && dto.getStatusMode().equals("0")){

                            if (appSession.getUserName().equals(edtUsrNm.getText().toString())&& appSession.getPassword().equals(edtPswrd.getText().toString())){
                                Toast toast = Toast.makeText(context, "Success", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Intent intent = new Intent(context, DashBoardActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast toast = Toast.makeText(context, "Invalid User Name or Password.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }else {
                            Toast toast = Toast.makeText(context, "You don't have permission in offline mode, Please contact to admin or enable network.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }

                break;
        }

    }


    private void callTask() {
        GPS_LAT = Utils.getPref(getApplicationContext(), Constants.USER_LATITUDE, "0.0");
        GPS_LONG = Utils.getPref(getApplicationContext(), Constants.USER_LONGITUDE, "0.0");

        if (TextUtils.isEmpty(GPS_LAT)||GPS_LAT.equals("0.0") || TextUtils.isEmpty(GPS_LONG) || GPS_LONG.equals("0.0")) {
            Toast toast = Toast.makeText(context, getResources().getString(R.string.v_gps_enable), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (CheckNetworkStateClass.isOnline(this)) {
                if (taskForAddress != null && !taskForAddress.isCancelled())
                    taskForAddress.cancel(true);
                taskForAddress = new TaskForAddress();
                taskForAddress.execute();
            } else {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

    }
    TaskForAddress taskForAddress = null;

    ProgressDialog pd;
    private class TaskForAddress extends AsyncTask<String, Void, GeoAddress> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();
            pd=new ProgressDialog(LoginActivity.this);
            pd.setTitle("Please wait...");
            pd.setMessage("Fetching location...");
            pd.show();
        }

        @Override
        protected GeoAddress doInBackground(String... params) {
            return new UtilDAO().getAddress(Double.parseDouble(GPS_LAT),Double.parseDouble(GPS_LONG));
        }

        @Override
        protected void onPostExecute(GeoAddress dto) {
            super.onPostExecute(dto);

            if (pd!=null && pd.isShowing())
                pd.dismiss();

            if (dto != null) {
                if (dto.getStatus().equals("1")) {

                    if (taskForLogin!=null && !taskForLogin.isCancelled())
                        taskForLogin.cancel(true);
                    taskForLogin=new TaskForLogin();
                    taskForLogin.execute(dto.getFullAddress(),"1");
              } else {
                    Toast toast = Toast.makeText(context, "Address not find.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            cancel(true);
        }
    }


    TaskForLogin taskForLogin = null;

    public class TaskForLogin extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("username",user_name));
            nvp.add(new BasicNameValuePair("password",password));
            nvp.add(new BasicNameValuePair("macAddress",getDeviceId()));
            nvp.add(new BasicNameValuePair("user_latitude",GPS_LAT));
            nvp.add(new BasicNameValuePair("user_longitude",GPS_LONG));
            nvp.add(new BasicNameValuePair("location",params[0]));
            nvp.add(new BasicNameValuePair("logtype",params[1]));

            Log.i("params ","nvp "+nvp.toString());

            return new RemoteRequestResponse().httpPost(ConstantLib.BASE_URL + ConstantLib.URL_LOGIN, nvp, null,null);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd!=null&& pd.isShowing())
                pd.dismiss();

            if (result!=null)
            {
                try {
                    InspectionDTO dto=new Gson().fromJson(result, new TypeToken<InspectionDTO>() {}.getType());
                    if (dto!=null){
                        if (dto.getStatus()==1){
                            appSession.setInspectionDTO(dto);
                            appSession.setLogin(true);
                            appSession.setUserName(user_name);
                            appSession.setPassword(password);
                            Toast toast = Toast.makeText(context, dto.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            Intent intent = new Intent(context, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (dto.getStatus()==2){
                            errorMacAlert("MAC Address",dto.getMessage());
                        }else {
                            errorAlert("Error",dto.getMessage());
                        }
                    }else {
                        errorAlert("Error","Data parsing error");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorAlert("Error",e.getMessage());
                }
            }else {
                errorAlert("Error","Internal server error");
            }
            cancel(true);
        }
    }

    private void errorAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    private void errorMacAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        appSession.setImei(false);
                        Intent intent = new Intent(context, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
