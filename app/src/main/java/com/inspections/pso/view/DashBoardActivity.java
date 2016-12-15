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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspections.pso.R;
import com.inspections.pso.dao.UtilDAO;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.MyInspectionDTO;
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
public class DashBoardActivity extends InspectionBaseActivity implements View.OnClickListener {

    Context context;
    private LinearLayout myInspectionLayout;
    private LinearLayout newInspctnLayout;
    private String user_id="";
    private LinearLayout getStationLayout;
    private String GPS_LAT="";
    private String GPS_LONG="";
    private LinearLayout dashBoard;
    private AppSession appSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        context= this;

        myInspectionLayout=(LinearLayout)findViewById(R.id.llayoutMyInspection);
        newInspctnLayout=(LinearLayout)findViewById(R.id.llayoutNwInspctn);
        getStationLayout=(LinearLayout)findViewById(R.id.llayoutGetStation);
        dashBoard=(LinearLayout)findViewById(R.id.lLayoutDashBoard);

        appSession= new AppSession(context);

        if (TextUtils.isEmpty(user_id)
                &&  appSession.getInspectionDTO()!=null
                && appSession.getInspectionDTO().getStations()!=null
                && appSession.getInspectionDTO().getStations().size()>0
                && appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid()!=null){

            user_id=appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid();
        }

        myInspectionLayout.setOnClickListener(this);
        newInspctnLayout.setOnClickListener(this);
        getStationLayout.setOnClickListener(this);
        dashBoard.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (InspectionBaseActivity.isGPSConnected(getApplicationContext())) {
            GPS_LAT = Utils.getPref(getApplicationContext(), Constants.USER_LATITUDE, "0.0");
            GPS_LONG = Utils.getPref(getApplicationContext(), Constants.USER_LONGITUDE, "0.0");
            Log.d(InspectionBaseActivity.TAG, " 1" + GPS_LONG + " " + GPS_LAT);
            if (!GPS_LAT.equalsIgnoreCase("0.0") && !GPS_LONG.equalsIgnoreCase("0.0")) {
                Log.d(InspectionBaseActivity.TAG, " " + GPS_LONG + " " + GPS_LAT);
            }
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.llayoutMyInspection:

                if (CheckNetworkStateClass.isOnline(context)) {
                    if (TextUtils.isEmpty(user_id)){
                        Toast toast = Toast.makeText(context, "Stations not available. ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        if (taskForMyInspection!=null && !taskForMyInspection.isCancelled())
                            taskForMyInspection.cancel(true);

                        taskForMyInspection=new TaskForMyInspection();
                        taskForMyInspection.execute();

                    }
                }else {
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.network_error), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                break;
            case R.id.llayoutNwInspctn:

                if (CheckNetworkStateClass.isOnline(context)) {
                    clickOn=1;
                    callTask();
                }else {
                    InspectionDTO dto =appSession.getInspectionDTO();
                    if (dto.getStatusMode().equals("0")){
                        if (dto!=null
                                && dto.getStations()!=null
                                && dto.getStations().size()>0
                                && dto.getStations().get(0).getStationsDetalis().getUserStUserid()!=null){

                            Intent intent2=new Intent(context,NewInspectionActivity.class);
                            startActivity(intent2);
                        }else {
                            Toast.makeText(context,"Stations not available",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        errorAlert("Offline",getResources().getString(R.string.network_error_offline));
                    }
                }


                break;

            case R.id.llayoutGetStation:


                if (CheckNetworkStateClass.isOnline(context)) {
                    clickOn=2;
                    callTask();
                }else {
                    InspectionDTO dto =appSession.getInspectionDTO();

                    if (dto!=null && dto.getStatusMode().equals("0")){

                        if (dto!=null
                                && dto.getStations()!=null
                                && dto.getStations().size()>0
                                && dto.getStations().get(0).getStationsDetalis().getUserStUserid()!=null){

                            Intent intent2=new Intent(context,SapSalesActivity.class);
                            startActivity(intent2);
                        }else {
                            Toast.makeText(context,"Stations not available",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        errorAlert("Offline",getResources().getString(R.string.network_error_offline));
                    }
                }

                break;

            case R.id.lLayoutDashBoard:

                if (TextUtils.isEmpty(appSession.getStationIds())){
                    Toast toast = Toast.makeText(context, "No record available.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {

                    InspectionDTO dto =appSession.getInspectionDTO();

                    if (dto!=null && dto.getStatusMode().equals("0")){
                        Intent intent = new Intent(context,SynchronizeActivity.class);
                        startActivity(intent);
                    }else {
                        errorAlert("Offline",getResources().getString(R.string.network_error_offline));
                    }
                }
                break;
        }
    }


    private void callTask() {
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
    TaskForAddress taskForAddress = null;
    private  int clickOn=1;

    private class TaskForAddress extends AsyncTask<String, Void, GeoAddress> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(DashBoardActivity.this);
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

                    if (clickOn==1)
                        taskForLogin.execute(dto.getFullAddress(),"2");
                    else
                        taskForLogin.execute(dto.getFullAddress(),"3");

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

    TaskForMyInspection taskForMyInspection=null;

    ProgressDialog pd;
    public class TaskForMyInspection extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(DashBoardActivity.this);
            pd.setTitle("Please wait...");
            pd.setMessage("Fetching inspection...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("user_id",user_id));
            nvp.add(new BasicNameValuePair("address",""));
            nvp.add(new BasicNameValuePair("date",""));
            nvp.add(new BasicNameValuePair("searchKey",""));

            Log.i("params ","nvp "+nvp.toString());

            return new RemoteRequestResponse().httpPost(ConstantLib.BASE_URL + ConstantLib.URL_NW_INSPCTN, nvp, null,null);
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
                    MyInspectionDTO dto=new Gson().fromJson(result, new TypeToken<MyInspectionDTO>() {}.getType());
                    if (dto!=null){
                        if (dto.getStatus().equals("1")){
                            appSession.setMyInspectionDTO(dto);
                            Intent intent = new Intent(context, MyInspectionActivity.class);
                            startActivity(intent);

                        }else {
                            Toast toast = Toast.makeText(context,dto.getMessage()+"", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }else {
                        Toast toast = Toast.makeText(context, "Data parsing error", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }else {
                Toast toast = Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT);
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

            pd=new ProgressDialog(DashBoardActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("username",appSession.getUserName()));
            nvp.add(new BasicNameValuePair("password",appSession.getPassword()));
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
                            Toast toast = Toast.makeText(context, dto.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            if (clickOn==1){
                                Intent intent2=new Intent(context,NewInspectionActivity.class);
                                startActivity(intent2);
                            }else if (clickOn==2){
                                Intent intent2=new Intent(context,SapSalesActivity.class);
                                startActivity(intent2);
                            }
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
    private void errorMacAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);

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
    private void errorAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);

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

}
