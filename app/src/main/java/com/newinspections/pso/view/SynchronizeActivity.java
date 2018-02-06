package com.newinspections.pso.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.adapter.SynchronizeAdapter;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.utils.OnItemClickListener;
import com.newinspections.pso.utils.RemoteRequestResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mobiweb on 1/3/16.
 */
public class SynchronizeActivity extends InspectionBaseActivity {

    Context context;
    private Toolbar toolBar;
    private String usr_id = "";
    LinearLayoutManager layoutManager;
    RecyclerView recycler_view;

    SynchronizeAdapter synchronizeAdapter;
    InspectionDTO localInspectionDTO;
    AppSession appSession=null;
    BroadcastReceiver mReceiver;
    List<String> stationIds=null;
    public  static String actionSynchronize="com.inspections.pso.view.SynchronizeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.synchronize_activity);

        context = this;
        appSession=new AppSession(context);
        if (TextUtils.isEmpty(usr_id)
                &&  appSession.getInspectionDTO()!=null
                && appSession.getInspectionDTO().getStations()!=null
                && appSession.getInspectionDTO().getStations().size()>0
                && appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid()!=null){

            usr_id=appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid();
        }

        localInspectionDTO=new InspectionDTO();


        Log.i("ssssssssssss","   ssssssssssss "+appSession.getStationIds());

        stationIds=new ArrayList<>(Arrays.asList(appSession.getStationIds().split(",")));

        for (int i=0;i<stationIds.size();i++){
            localInspectionDTO.getStations().add(appSession.getStation(stationIds.get(i)));
        }

        initView();

        setSupportActionBar(toolBar);
        try {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setValue();

        IntentFilter intentFilter = new IntentFilter(actionSynchronize);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String data = intent.getStringExtra("data");
                int position = intent.getIntExtra("position",0);

                Log.i("SynchronizeActivity","data "+data);
                Log.i("SynchronizeActivity","position "+position);
                if (stationIds.size()>position){

                    InspectionDTO inspectionDTO=appSession.getInspectionDTO();
                    if (inspectionDTO!=null){
                        int  inspectionPosition=containsPosition(inspectionDTO.getStations(),stationIds.get(position));
                        if (inspectionPosition<inspectionDTO.getStations().size()){
                            inspectionDTO.getStations().get(inspectionPosition).setInspectionCompleted(true);
                            appSession.setInspectionDTO(inspectionDTO);
                        }
                    }

                    stationIds.remove(position);
                    appSession.setStationIds(TextUtils.join(",", stationIds));
                    localInspectionDTO.getStations().remove(position);
                    synchronizeAdapter.notifyDataSetChanged();

                    updateSessionAlert(SynchronizeActivity.this);
                }
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
    int containsPosition(List<InspectionDTO.Station> list, String id) {
        for (InspectionDTO.Station item : list) {
            if (!TextUtils.isEmpty(item.getStationsDetalis().getStationId()) && item.getStationsDetalis().getStationId().equals(id)) {
                return list.indexOf(item);
            }
        }
        return 0;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }

    private void initView(){
        toolBar = (Toolbar) findViewById(R.id.toolBar_syn);

        recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);



    }

    private void setValue(){
        synchronizeAdapter = new SynchronizeAdapter(context, localInspectionDTO.getStations(),onItemClickCallback);
        recycler_view.setAdapter(synchronizeAdapter);

    }

    NewInspectionActivity.TaskForNewInspection taskForNewInspection=null;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            if (CheckNetworkStateClass.isOnline(context)) {
                if (taskForNewInspection!=null && !taskForNewInspection.isCancelled())
                    taskForNewInspection.cancel(true);
                taskForNewInspection=new NewInspectionActivity().new TaskForNewInspection(position,actionSynchronize,SynchronizeActivity.this,localInspectionDTO.getStations().get(position));
                taskForNewInspection.execute();
            }else {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (taskForNewInspection!=null && !taskForNewInspection.isCancelled())
            taskForNewInspection.cancel(true);

        super.onBackPressed();
    }

    private void updateSessionAlert(final Context context){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("Synchronize Data");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure, You want to synchronize your data from server.")
                .setCancelable(false)
                .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (taskForLogin!=null && !taskForLogin.isCancelled())
                            taskForLogin.cancel(true);
                        taskForLogin=new TaskForLogin();
                        taskForLogin.execute("","4");
                    }
                }).setNegativeButton("NOT NOW",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    private void errorAlert(String title,String message){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SynchronizeActivity.this);

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
                }).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });
        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    TaskForLogin taskForLogin = null;

    private ProgressDialog pd;
    public class TaskForLogin extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(SynchronizeActivity.this);
            pd.setMessage("Please wait for synchronize data...");
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
            Log.d("exdtest", result);
            if (result!=null)
            {
                try {
                    InspectionDTO dto=new Gson().fromJson(result, new TypeToken<InspectionDTO>() {}.getType());
                    if (dto!=null){
                        if (dto.getStatus()==1){
                            appSession.setInspectionDTO(dto);
                            appSession.setLogin(true);

                            Toast toast = Toast.makeText(context,"Session update successfully.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else if (dto.getStatus()==2){
                            errorMacAlert("MAC Address",dto.getMessage());
                        }
                        else {
                            errorAlert("Error",dto.getMessage());
                        }
                    }
                    else {
                        errorAlert("Error","Data parsing error");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorAlert("Error",e.getMessage());
                }
            }
            else {
                errorAlert("Error","Internal server error");
            }
            cancel(true);
        }
    }
    private void errorMacAlert(String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SynchronizeActivity.this);

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
