package com.newinspections.pso.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.dao.UtilDAO;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.dto.MyInspectionDTO;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.utils.GPSTrackerNew;
import com.newinspections.pso.utils.GeoAddress;
import com.newinspections.pso.utils.RemoteRequestResponse;

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

        if (Build.VERSION.SDK_INT >= 23 && !canAccessPhoneState()) {
            ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_PHONE_STATE);
            return;
        }else {
            if (gpsTracker==null)
            gpsTracker=new GPSTrackerNew(context);
        }
        if (gpsTracker!=null){
            latitude=gpsTracker.getLatitude();
            longitude=gpsTracker.getLongitude();
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
                    if (appSession.getMyInspectionDTO()!=null){
                        Intent intent = new Intent(context, MyInspectionActivity.class);
                        startActivity(intent);
                    }else {
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.network_error), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
                break;
            case R.id.llayoutNwInspctn:

                if (CheckNetworkStateClass.isOnline(context)) {

                    //first synchronized old data after create new inspection.
                    if (TextUtils.isEmpty(appSession.getStationIds())){
                        clickOn=1;

                        callTask();
                    }else {
                        Toast toast = Toast.makeText(context, "Please synchronize old inspections first.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

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

        if (gpsTracker==null)
        gpsTracker=new GPSTrackerNew(context);

        if (gpsTracker!=null){
            latitude=gpsTracker.getLatitude();
            longitude=gpsTracker.getLongitude();
        }
        if (latitude== 0.0 && longitude == 0.0) {
            locationAlert();
            return;
        }

        if (CheckNetworkStateClass.isOnline(this)) {
            if (!gpsTracker.isGPSLocation()) {
                alertGPSAlertMessage();
                return;
            }else {
                if (taskForAddress != null && !taskForAddress.isCancelled())
                    taskForAddress.cancel(true);
                taskForAddress = new TaskForAddress();
                taskForAddress.execute();
            }
        } else {
            Toast toast = Toast.makeText(context, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    public void alertGPSAlertMessage() {
        new android.app.AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.v_gps_validate))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskForAddress != null && !taskForAddress.isCancelled())
                            taskForAddress.cancel(true);
                        taskForAddress = new TaskForAddress();
                        taskForAddress.execute();
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
            //Industry House, Agra Bombay Rd, New Palasia, Indore, Madhya Pradesh 452001, India
            return new UtilDAO().getAddress(latitude,longitude);
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

                    if (clickOn==1) {
//                        edited by exd
//                        Intent intent2=new Intent(context,NewInspectionActivity.class);
//                        startActivity(intent2);
                        taskForLogin.execute(dto.getFullAddress(), "2");

//                        InspectionDTO dto1 =appSession.getInspectionDTO();
//                        if (dto1.getStatusMode().equals("0")){
//                            if (dto!=null
//                                    && dto1.getStations()!=null
//                                    && dto1.getStations().size()>0
//                                    && dto1.getStations().get(0).getStationsDetalis().getUserStUserid()!=null){
//
//                                Intent intent2=new Intent(context,NewInspectionActivity.class);
//                                startActivity(intent2);
//                            }else {
//                                Toast.makeText(context,"Stations not available",Toast.LENGTH_SHORT).show();
//                            }
//                        }else {
//                            errorAlert("Offline",getResources().getString(R.string.network_error_offline));
//                        }
                    }
                    else if (clickOn==2) {
//                        edited by exd
                        taskForLogin.execute(dto.getFullAddress(), "3");

//                        InspectionDTO dto1 =appSession.getInspectionDTO();
//                        if (dto1.getStatusMode().equals("0")){
//                            if (dto!=null
//                                    && dto1.getStations()!=null
//                                    && dto1.getStations().size()>0
//                                    && dto1.getStations().get(0).getStationsDetalis().getUserStUserid()!=null){
//
//                                Intent intent2=new Intent(context,SapSalesActivity.class);
//                                Log.d("exd", "NewInspectionTop");
//                                startActivity(intent2);
//                            }else {
//                                Toast.makeText(context,"Stations not available",Toast.LENGTH_SHORT).show();
//                            }
//                        }else {
//                            errorAlert("Offline",getResources().getString(R.string.network_error_offline));
//                        }
                    }
                    else if (clickOn==3)
                        taskForLogin.execute(dto.getFullAddress(),"4");

                } else {
                    if (taskForLogin!=null && !taskForLogin.isCancelled())
                        taskForLogin.cancel(true);
                    taskForLogin=new TaskForLogin();

                    if (clickOn==1)
                        taskForLogin.execute(latitude+","+longitude,"2");
                    else if (clickOn==2)
                        taskForLogin.execute(latitude+","+longitude,"3");
                    else if (clickOn==3)
                        taskForLogin.execute(latitude+","+longitude,"4");
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
            nvp.add(new BasicNameValuePair("macAddress",getDeviceId(context)));
            nvp.add(new BasicNameValuePair("user_latitude",latitude+""));
            nvp.add(new BasicNameValuePair("user_longitude",longitude+""));
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

                            if (clickOn==1){
                                Toast toast = Toast.makeText(context, dto.getMessage(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Intent intent2=new Intent(context,NewInspectionActivity.class);
                                startActivityForResult(intent2,5001);
                            }else if (clickOn==2){
                                Toast toast = Toast.makeText(context, dto.getMessage(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Intent intent2=new Intent(context,SapSalesActivity.class);
                                startActivity(intent2);
                            }else if (clickOn==3){
                                Toast toast = Toast.makeText(context,"Session update successfully.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 5001) {
                clickOn=3;

                if (taskForLogin!=null && !taskForLogin.isCancelled())
                    taskForLogin.cancel(true);
                taskForLogin=new TaskForLogin();
                taskForLogin.execute("","4");

            }
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
