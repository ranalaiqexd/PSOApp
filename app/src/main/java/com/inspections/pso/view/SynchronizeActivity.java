package com.inspections.pso.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspections.pso.R;
import com.inspections.pso.adapter.MyInspectionAdapter;
import com.inspections.pso.adapter.SynchronizeAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.MyInspectionDTO;
import com.inspections.pso.netcome.CheckNetworkStateClass;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ConstantLib;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.ItemOffsetDecoration;
import com.inspections.pso.utils.OnItemClickListener;
import com.inspections.pso.utils.RemoteRequestResponse;
import com.inspections.pso.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mobiweb on 1/3/16.
 */
public class SynchronizeActivity extends AppCompatActivity {

    Context context;
    private Toolbar toolBar;
    private String usr_id = "";
    LinearLayoutManager layoutManager;
    RecyclerView recycler_view;

    SynchronizeAdapter synchronizeAdapter;
    InspectionDTO inspectionDTO;
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

        inspectionDTO=new InspectionDTO();


        Log.i("ssssssssssss","   ssssssssssss "+appSession.getStationIds());

        stationIds=new ArrayList<>(Arrays.asList(appSession.getStationIds().split(",")));

        for (int i=0;i<stationIds.size();i++){
            inspectionDTO.getStations().add(appSession.getStation(stationIds.get(i)));
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
                    stationIds.remove(position);
                    appSession.setStationIds(TextUtils.join(",", stationIds));
                    inspectionDTO.getStations().remove(position);
                    synchronizeAdapter.notifyDataSetChanged();
                }
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
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
        synchronizeAdapter = new SynchronizeAdapter(context, inspectionDTO.getStations(),onItemClickCallback);
        recycler_view.setAdapter(synchronizeAdapter);

    }

    NewInspectionActivity.TaskForNewInspection taskForNewInspection=null;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            if (CheckNetworkStateClass.isOnline(context)) {
                if (taskForNewInspection!=null && !taskForNewInspection.isCancelled())
                    taskForNewInspection.cancel(true);
                taskForNewInspection=new NewInspectionActivity().new TaskForNewInspection(position,actionSynchronize,SynchronizeActivity.this,inspectionDTO.getStations().get(position));
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

    private void errorAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SynchronizeActivity.this);

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
