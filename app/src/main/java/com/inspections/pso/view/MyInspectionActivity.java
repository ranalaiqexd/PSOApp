package com.inspections.pso.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Calendar;

/**
 * Created by mobiweb on 1/3/16.
 */
public class MyInspectionActivity extends AppCompatActivity implements  View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    Context context;
    private Toolbar toolBar;
    private ImageButton btnByLoc;
    private ImageButton btnBystnNme;
    private ImageButton btnByDte;
    TextView tv_filter;

    private String usr_id = "";
    private Dialog dialog;
    private String GPS_LAT = "";
    private String GPS_LONG = "";

    LinearLayoutManager layoutManager;
    RecyclerView recycler_view;

    MyInspectionAdapter inspectionAdapter;
    MyInspectionDTO inspectionDTO;
    AppSession appSession=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_inspection);

        context = this;
        appSession=new AppSession(context);
        if (TextUtils.isEmpty(usr_id)
                &&  appSession.getInspectionDTO()!=null
                && appSession.getInspectionDTO().getStations()!=null
                && appSession.getInspectionDTO().getStations().size()>0
                && appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid()!=null){

            usr_id=appSession.getInspectionDTO().getStations().get(0).getStationsDetalis().getUserStUserid();
        }

        inspectionDTO=appSession.getMyInspectionDTO();
        initView();

        setSupportActionBar(toolBar);
        try {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setValue();
    }

    private void initView(){
        toolBar = (Toolbar) findViewById(R.id.toolBar_inspctn);
        btnByLoc = (ImageButton) findViewById(R.id.btnByLctn);
        btnBystnNme = (ImageButton) findViewById(R.id.btn_byStatn);
        btnByDte = (ImageButton) findViewById(R.id.btnDate);
        tv_filter=(TextView) findViewById(R.id.tv_filter);
        tv_filter.setOnClickListener(this);
        btnByLoc.setOnClickListener(this);
        btnByDte.setOnClickListener(this);
        btnBystnNme.setOnClickListener(this);

        recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);



    }

    private void setValue(){
        inspectionAdapter = new MyInspectionAdapter(context, inspectionDTO.getResponse(),onItemClickCallback);
        recycler_view.setAdapter(inspectionAdapter);

    }
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {

            Intent intent2 = new Intent(context, MyInspectionDetailActivity.class);
            intent2.putExtra("inspectionPosition",position);
            startActivity(intent2);


        }
    };
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
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_filter:

                callTask(ConstantLib.URL_NW_INSPCTN,"","","");

                break;
            case R.id.btnByLctn:

                atertDialogLocation();

                break;
            case R.id.btn_byStatn:

                atertDialogStation();
                break;

            case R.id.btnDate:

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyInspectionActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

                break;

        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        //String date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        String date = year + "-" + (++monthOfYear) + "-" + dayOfMonth;

        callTask(ConstantLib.URL_SRCH_BY_DTE,"",date,"");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }
    public void atertDialogLocation() {
        // custom dialog

        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cstm_dilogsrch_statn_name);

        final EditText edtEntrNm = (EditText) dialog.findViewById(R.id.edtTxt);
        final Button btnSrch = (Button) dialog.findViewById(R.id.srch);
        Button btnCncl = (Button) dialog.findViewById(R.id.cncl);

        btnCncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        edtEntrNm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSrch.performClick();
                    return true;
                }
                return false;
            }
        });

        btnSrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtEntrNm.getText().toString().trim())){
                    Toast toast = Toast.makeText(context, "Please enter location name.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    dialog.dismiss();
                    callTask(ConstantLib.URL_SRCH_INSPCTN_SRCHBY_LOC,edtEntrNm.getText().toString().trim(),"","");
                }
            }
        });

        dialog.show();
    }
    public void atertDialogStation() {
        // custom dialog

        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cstm_dilogsrch_statn_name);

        final EditText edtEntrNm = (EditText) dialog.findViewById(R.id.edtTxt);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText("Search by Station");
        edtEntrNm.setHint("Enter Station Name");

        final Button btnSrch = (Button) dialog.findViewById(R.id.srch);
        Button btnCncl = (Button) dialog.findViewById(R.id.cncl);

        btnCncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        edtEntrNm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSrch.performClick();
                    return true;
                }
                return false;
            }
        });

        btnSrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtEntrNm.getText().toString().trim())){
                    Toast toast = Toast.makeText(context, "Please enter station name.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    dialog.dismiss();
                    callTask(ConstantLib.URL_SRCH_BY_STATN,"","",edtEntrNm.getText().toString().trim());
                }

            }
        });

        dialog.show();
    }

    private void callTask(String method,String address,String date,String name){
        if (CheckNetworkStateClass.isOnline(context)) {
            if (TextUtils.isEmpty(usr_id)){
                Toast toast = Toast.makeText(context, "Stations not available. ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else {
                if (taskForMyInspection!=null && !taskForMyInspection.isCancelled())
                    taskForMyInspection.cancel(true);

                taskForMyInspection=new TaskForMyInspection();
                taskForMyInspection.execute(method,address,date,name);

            }
        }else {
            Toast toast = Toast.makeText(context,getResources().getString(R.string.network_error), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    TaskForMyInspection taskForMyInspection=null;

    ProgressDialog pd;
    public class TaskForMyInspection extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd!=null && pd.isShowing())
                pd.dismiss();

            pd=new ProgressDialog(MyInspectionActivity.this);
            pd.setTitle("Please wait...");
            pd.setMessage("Fetching inspection...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("user_id",usr_id));
            nvp.add(new BasicNameValuePair("address",params[1]));
            nvp.add(new BasicNameValuePair("date",params[2]));
            nvp.add(new BasicNameValuePair("searchKey",params[3]));

            Log.i("params ","nvp "+nvp.toString());

            return new RemoteRequestResponse().httpPost(ConstantLib.BASE_URL + params[0], nvp, null,null);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd!=null&& pd.isShowing())
                pd.dismiss();

            inspectionDTO.getResponse().clear();
            inspectionAdapter.notifyDataSetChanged();
            if (result!=null)
            {
                try {
                    MyInspectionDTO dto=new Gson().fromJson(result, new TypeToken<MyInspectionDTO>() {}.getType());
                    if (dto!=null){
                        if (dto.getStatus().equals("1") && dto.getResponse()!=null && dto.getResponse().size()>0){
                            appSession.setMyInspectionDTO(dto);
                            inspectionDTO=dto;
                            setValue();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyInspectionActivity.this);

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
