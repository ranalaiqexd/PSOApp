package com.inspections.pso.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inspections.pso.R;
import com.inspections.pso.adapter.ImagesAdapter;
import com.inspections.pso.adapter.NewCalculationAdapter;
import com.inspections.pso.adapter.StationAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.netcome.CheckNetworkStateClass;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ConstantLib;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.ItemOffsetDecoration;
import com.inspections.pso.utils.OnItemClickListener;
import com.inspections.pso.utils.RemoteRequestResponse;
import com.inspections.pso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


/**
 * Created by mobiweb on 1/3/16.
 */
public class NewInspectionActivity extends InspectionBaseActivity implements  View.OnClickListener, LocationListener {

    Context context;
    private Toolbar toolBar;
    private Spinner spnrFulInspctn;
    private TextView rtailOutlt;
    private TextView txtDte;
    private TextView txtTme;
    private Button btnTnkRdng;
    private EditText et_comment;
    private ImageView imgVw;
    private String selectedImagePath = "";
    private static final int REQUEST_CAMERA = 100;
    private static final int CAPTURE = 1001;
    private static final int SELECT_FILE = 200;
    private static final int HSESurvay = 1002,FORCOURTSurvay = 1003,ADD_TANK=1004;

    private Button btnHseSurv;
    private Button btnForSurv;
    private  TextView hseTxtRtng;
    private  TextView forcortTxtrtng;
    private Button btnNwInspSave;
    private String strlatitude = "";
    private String strlongitude = "";

    private String station_Id = "",station_Name="";

    private Button btnSbmtt;
    TextView edTnkRdng,tv_all_values_tag;
    private EditText edtPurpose;
    private int sak = 0;

    int CAMERA = 112;

    RelativeLayout tempLayout;
    ImageView img12;
    TextView tv12;
    private TextView txtVwGps;
    private AppSession appSession;
    private Button btn_captr;
    private int callFrom=0;


    private String[] calTitle,calDescription;

    LinearLayoutManager layoutManager;
    RecyclerView recycler_view,recycler_view_calculation;
    ImagesAdapter imagesAdapter;
    NewCalculationAdapter calculationAdapter;
    String imagePosition;

    InspectionDTO inspectionDTO=null,oldInspectionDTO;

    private int inspectionPosition=0;
//https://www.airpair.com/android/taking-pictures-android-fragment-intents
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_inspection);
        calTitle=getResources().getStringArray(R.array.calculation_title);
        calDescription=getResources().getStringArray(R.array.calculation_description);
        context = this;
        appSession = new AppSession(context);
        getLocation();
        //initiated inspections
        inspectionDTO=appSession.getInspectionDTO();
        oldInspectionDTO=appSession.getInspectionDTO();

        initView();
        setOldInspection();
    }


    private void setOldInspection(){

        if (latitude==0.0 && longitude==0.0){
            txtVwGps.setText("GPS unable to get current location.");
            txtVwGps.setVisibility(View.VISIBLE);
            spnrFulInspctn.setVisibility(View.GONE);
            return;
        }

        if (oldInspectionDTO!=null && oldInspectionDTO.getStations()!= null && oldInspectionDTO.getStations().size() > 0) {
            for (int i=0;i< oldInspectionDTO.getStations().size();i++){
                try {
                    double lat = Double.parseDouble(oldInspectionDTO.getStations().get(i).getStationsDetalis().getStationLatitude());
                    double longg = Double.parseDouble(oldInspectionDTO.getStations().get(i).getStationsDetalis().getStationLongitude());


                    float[] distance = new float[2];
                    Location.distanceBetween(latitude,longitude, lat,longg, distance);

                    Log.i("distance[0]",oldInspectionDTO.getStations().get(i).getStationsDetalis().getStationName()+" distance "+distance[0]);

                    if (distance[0]>500) {
                       oldInspectionDTO.getStations().remove(i);
                      i--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (oldInspectionDTO!=null && oldInspectionDTO.getStations()!= null && oldInspectionDTO.getStations().size() > 0) {
            txtVwGps.setText("Select Station");
            txtVwGps.setVisibility(View.VISIBLE);
            spnrFulInspctn.setVisibility(View.GONE);

            StationAdapter ful = new StationAdapter(context, oldInspectionDTO.getStations(),latitude, longitude);
            spnrFulInspctn.setAdapter(ful);
        } else {
            txtVwGps.setText("GPS coordinates did not match with your current location.");
            txtVwGps.setVisibility(View.VISIBLE);
            spnrFulInspctn.setVisibility(View.GONE);
        }

        spnrFulInspctn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (oldInspectionDTO!=null && oldInspectionDTO.getStations()!= null && oldInspectionDTO.getStations().size() > 0) {

                    station_Id =oldInspectionDTO.getStations().get(position).getStationsDetalis().getStationId();
                    inspectionPosition=containsPosition(inspectionDTO.getStations(),station_Id);

                    station_Name = inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getStationName();
                    rtailOutlt.setText(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getSapCode());
                    setValue();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    int containsPosition(List<InspectionDTO.Station> list, String id) {
        for (InspectionDTO.Station item : list) {
            if (!TextUtils.isEmpty(item.getStationsDetalis().getStationId()) && item.getStationsDetalis().getStationId().equals(id)) {
                return list.indexOf(item);
            }
        }
        return 0;
    }
    private void setValue(){
        //get current inspection from Preferences
        inspectionDTO=appSession.getInspectionDTO();


        //if current inspection are empty then create a new inspection
        if (inspectionDTO==null){
            inspectionDTO=new InspectionDTO();
        }


        if (inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getImages().size()==0){
            recycler_view.setVisibility(View.GONE);
        }else {
            recycler_view.setVisibility(View.VISIBLE);
        }

        imagesAdapter = new ImagesAdapter(context, inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getImages(),onItemClickCallback);
        recycler_view.setAdapter(imagesAdapter);



        getLocation();

        strlatitude = "" + new DecimalFormat("###.0####").format(latitude);
        strlongitude = "" + new DecimalFormat("###.0####").format(longitude);
        setTime();
        setHseSurvey();
        setForeCordSurvey();
        edTnkRdng.setText("");
        if (inspectionDTO.getStations().get(inspectionPosition).getTanks()!=null &&inspectionDTO.getStations().get(inspectionPosition).getTanks().size()>0){
            for (int i=0;i<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();i++){
                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(i).getNozzle()!=null
                        && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(i).getNozzle().size()>0){
                       if (isCompleted(false)){
                            edTnkRdng.setText("Completed");
                            if (recycler_view_calculation.getVisibility()==View.VISIBLE) {
                                setCalculationTable();
                            }
                        }
                    break;
                }
            }
        }
    }

    private void setHseSurvey(){

        if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHseSurveyRating())){
            hseTxtRtng.setText("Select");
        }else if (Integer.parseInt(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHseSurveyRating())>10){
            hseTxtRtng.setText("SAFE" + "(" + inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHseSurveyRating()+ "/13)");
        }else{
            hseTxtRtng.setText("UNSAFE" + "(" + inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHseSurveyRating()+ "/13)");
        }
    }

    private void setForeCordSurvey(){

        if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating())){
            forcortTxtrtng.setText("Select");
        }else if (Integer.parseInt(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating())>9){
             forcortTxtrtng.setText("GOOD" + "(" + inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating()+ "/14)");
        }else if (Integer.parseInt(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating())>6){
            forcortTxtrtng.setText("AVERAGE" + "(" + inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating()+ "/14)");
        }else{
            forcortTxtrtng.setText("BELOW AVERAGE" + "(" + inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating()+ "/14)");
        }
    }

    private void setCalculationTable(){
        inspectionDTO.getStations().get(inspectionPosition).getCalculation().clear();

        for (int i=0;i<calDescription.length;i++){
            InspectionDTO.Station.Calculation dto=inspectionDTO.getStations().get(inspectionPosition).new Calculation();
            dto.setTitle(calTitle[i]);
            dto.setDescription(calDescription[i]);
            dto.setHsd("");
            dto.setPmg("");
            dto.setHobc("");
            switch (i){
                case 0:
                    dto.setHsd("HSD");
                    dto.setPmg("PMG");
                    dto.setHobc("HOBC");
                    break;
                case 1:

                    //calculation for A

                    //only for HSD

                    Double aHsdb=inspectionDTO.getStations().get(inspectionPosition).getA().getHsd().getHsdb();

                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HSD")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aHsdb=aHsdb+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());
                        }

                        //for old flush tank calculation(add calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("HSD")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HSD")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aHsdb=aHsdb+Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());
                        }

                        //for old flush tank calculation(subtract calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("HSD")
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HSD")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())>0){

                            aHsdb=aHsdb-Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity());
                        }


                    }
                    dto.setHsd(""+aHsdb);

                    //only for PMG
                    Double aPmgb=inspectionDTO.getStations().get(inspectionPosition).getA().getPmg().getPmgb();

                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("PMG")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aPmgb=aPmgb+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());

                        }
                        //for old flush tank calculation(add calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("PMG")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("PMG")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aPmgb=aPmgb+Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());
                        }

                        //for old flush tank calculation(subtract calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("PMG")
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("PMG")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())>0){

                            aPmgb=aPmgb-Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity());
                        }
                    }
                    dto.setPmg(""+aPmgb);

                    //only for HDBC
                    Double aHobcb=inspectionDTO.getStations().get(inspectionPosition).getA().getHobc().getHobcb();
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HOBC")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aHobcb=aHobcb+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());

                        }
                        //for old flush tank calculation(add calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("HOBC")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HOBC")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance())>0){

                            aHobcb=aHobcb+Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOpeningBalance());
                        }

                        //for old flush tank calculation(subtract calculation of old tank)
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getOldTankType().equals("HOBC")
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getFlush().equals("1")
                                && !inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HOBC")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity())>0){

                            aHobcb=aHobcb-Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankAllquantity());
                        }
                    }
                    dto.setHobc(""+aHobcb);

                    break;
                case 2:
                    //calculation for B

                    dto.setHsd(""+inspectionDTO.getStations().get(inspectionPosition).getB().getHsd().getSapPreviousData());
                    dto.setPmg(""+inspectionDTO.getStations().get(inspectionPosition).getB().getPmg().getSapPreviousData());
                    dto.setHobc(""+inspectionDTO.getStations().get(inspectionPosition).getB().getHobc().getSapPreviousData());
                    break;
                case 3:
                    //calculation for C=A+B

                    dto.setHsd(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(1).getHsd())+Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(2).getHsd())+"");
                    dto.setPmg(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(1).getPmg())+Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(2).getPmg())+"");
                    dto.setHobc(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(1).getHobc())+Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(2).getHobc())+"");
                    break;
                case 4:
                    //calculation for D


                    //for HSD tanks
                    Double dHsd=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HSD") && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle()!=null){
                            for (int k=0;k<inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().size();k++){
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterCurrentVolume("0");
                                }
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterPreviousVolume("0");
                                }
                                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterDefect().equals("1")){
                                    //if nozzel are defected then not involve in calculations
                                    continue;
                                }
                                if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())==0){
                                    if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dHsd=dHsd+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD()));
                                    }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dHsd=dHsd+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }
                                }else {

                                    if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getResetStatus().equals("1")){
                                       //if nozzle will bee reset
                                        dHsd=dHsd+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getSpecialReading())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }else {
                                        if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dHsd=dHsd+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume()));
                                        }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dHsd=dHsd+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                    +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    dto.setHsd(dHsd+"");



                    //for PMG tanks
                    Double dPmg=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("PMG") && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle()!=null){
                            for (int k=0;k<inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().size();k++){
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterCurrentVolume("0");
                                }
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterPreviousVolume("0");
                                }
                                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterDefect().equals("1")){
                                    //if nozzel are defected then not involve in calculations
                                    continue;
                                }
                                if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())==0){
                                    if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dPmg=dPmg+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD()));
                                    }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dPmg=dPmg+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }
                                }else {
                                    if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getResetStatus().equals("1")){
                                        //if nozzle will bee reset
                                        dPmg=dPmg+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getSpecialReading())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }else {
                                        if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dPmg=dPmg+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume()));
                                        }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dPmg=dPmg+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                    +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                        }
                                    }
                                }
                            }

                        }
                    }
                    dto.setPmg(dPmg+"");



                    //for PMG tanks
                    Double dHobc=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HOBC") && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle()!=null){
                            for (int k=0;k<inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().size();k++){
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterCurrentVolume("0");
                                }
                                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).setDumeterPreviousVolume("0");
                                }
                                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterDefect().equals("1")){
                                    //if nozzel are defected then not involve in calculations
                                    continue;
                                }
                                if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())==0){
                                    if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dHobc=dHobc+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD()));
                                    }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                            <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())){
                                        dHobc=dHobc+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getOpeningBalanceD())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }
                                }else {
                                    if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getResetStatus().equals("1")){
                                        //if nozzle will bee reset
                                        dHobc=dHobc+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getSpecialReading())
                                                -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                    }else {
                                        if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                >Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dHobc=dHobc+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume()));
                                        }else  if (Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume())
                                                <Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())){
                                            dHobc=dHobc+(Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterMaxValue())
                                                    -Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume())
                                                    +Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    dto.setHobc(""+dHobc);

                    break;
                case 5:
                    //calculation for E=C-D

                    dto.setHsd(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(3).getHsd())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(4).getHsd())+"");
                    dto.setPmg(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(3).getPmg())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(4).getPmg())+"");
                    dto.setHobc(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(3).getHobc())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(4).getHobc())+"");
                    break;
                case 6:
                    //calculation for F all current reading


                    Double fHsdb=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HSD")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())>0){

                            fHsdb=fHsdb+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData());
                        }
                    }
                    dto.setHsd(""+fHsdb);


                    Double fpmg=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("PMG")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())>0){

                            fpmg=fpmg+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData());
                        }
                    }
                    dto.setPmg(""+fpmg);

                    Double fhobc=0.0;
                    for (int j=0;j<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();j++){
                        if (!TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType())
                                && inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankType().equals("HOBC")
                                && !TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())
                                && Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())>0){

                            fhobc=fhobc+ Double.parseDouble(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData());
                        }
                    }
                    dto.setHobc(""+fhobc);
                    break;

                case 7:
                    //calculation for F-E
                    dto.setHsd(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(6).getHsd())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(5).getHsd())+"");
                    dto.setPmg(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(6).getPmg())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(5).getPmg())+"");
                    dto.setHobc(Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(6).getHobc())-Double.parseDouble( inspectionDTO.getStations().get(inspectionPosition).getCalculation().get(5).getHobc())+"");
                    break;

            }
            inspectionDTO.getStations().get(inspectionPosition).getCalculation().add(dto);
        }

        calculationAdapter = new NewCalculationAdapter(context,  inspectionDTO.getStations().get(inspectionPosition).getCalculation());
        recycler_view_calculation.setAdapter(calculationAdapter);
    }

    private void initView(){

        toolBar = (Toolbar) findViewById(R.id.toolBar_myInspctn);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //set layout for images
        recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);


        //set calculation
        recycler_view_calculation=(RecyclerView) findViewById(R.id.recycler_view_calculation);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset_1);
        recycler_view_calculation.addItemDecoration(itemDecoration);
        recycler_view_calculation.setLayoutManager(layoutManager);
        recycler_view_calculation.setNestedScrollingEnabled(false);

        recycler_view_calculation.setVisibility(View.GONE);
        tempLayout = (RelativeLayout) findViewById(R.id.rl_1_1);
        img12 = (ImageView) findViewById(R.id.img1_1);
        tv12 = (TextView) findViewById(R.id.txt_1_1);

        tv_all_values_tag = (TextView) findViewById(R.id.tv_all_values_tag);

        tv_all_values_tag.setVisibility(View.GONE);

        spnrFulInspctn = (Spinner) findViewById(R.id.spnrFul_stn);
        rtailOutlt = (TextView) findViewById(R.id.txtvRtloutet);
        txtDte = (TextView) findViewById(R.id.txt_autoDte);
        txtTme = (TextView) findViewById(R.id.txtAuto_time);

        edtPurpose = (EditText) findViewById(R.id.edtPurpose);
        edTnkRdng = (TextView) findViewById(R.id.edtTnkRdng);
        btnTnkRdng = (Button) findViewById(R.id.btnTnkRdg);
        et_comment = (EditText) findViewById(R.id.et_comment);
        imgVw = (ImageView) findViewById(R.id.imgAttchmnt);

        btnHseSurv = (Button) findViewById(R.id.btnHSE);
        btnForSurv = (Button) findViewById(R.id.btnFORE);
        hseTxtRtng = (TextView) findViewById(R.id.txthseRtng);
        forcortTxtrtng = (TextView) findViewById(R.id.txtForCOrtRtng);
        btnNwInspSave = (Button) findViewById(R.id.btnSave);
        btnSbmtt = (Button) findViewById(R.id.btnSbmt);

        btn_captr = (Button) findViewById(R.id.btnCapture);
        txtVwGps = (TextView) findViewById(R.id.gpsTxt);

        btnTnkRdng.setOnClickListener(this);
        imgVw.setOnClickListener(this);
        btnHseSurv.setOnClickListener(this);
        btnForSurv.setOnClickListener(this);
        btnNwInspSave.setOnClickListener(this);
        btnSbmtt.setOnClickListener(this);

        btn_captr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(station_Id)){
                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                callFrom = CAMERA;
                checkPermission();

            }
        });

        txtVwGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtVwGps.getText().toString().equalsIgnoreCase("Select Station")){
                    txtVwGps.setVisibility(View.GONE);
                    spnrFulInspctn.setVisibility(View.VISIBLE);
                    spnrFulInspctn.performClick();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
   private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
       @Override
       public void onItemClicked(View view, int position) {


       }
   };
    String inspectionTime="";
    public void setTime() {
        final Calendar c = Calendar.getInstance();
        inspectionTime=getUTCTimeFormat(c.getTime());

        String time = "",date="";
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        date=df1.format(c.getTime());
        time=df2.format(c.getTime());
        txtDte.setText(date);
        txtTme.setText(time);
    }
    public String getUTCTimeFormat(Date date) {
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            YYYY_MM_DD_HH_MM_SS.setTimeZone(TimeZone.getTimeZone("UTC"));
            return YYYY_MM_DD_HH_MM_SS.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return "N/A";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }

    @Override
    public void onClick(View v) {


        if (TextUtils.isEmpty(station_Id)){
            Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        switch (v.getId()) {
            case R.id.imgAttchmnt:

                callFrom =0;
                checkPermission();
                break;

            case R.id.btnTnkRdg:
                appSession.setInspectionDTO(inspectionDTO);
                //add tank
              // Intent intent = new Intent(context, TankerReading_activity.class);
                Intent intent = new Intent(context, AddTankerActivity.class);
                intent.putExtra("inspectionPosition",inspectionPosition);
                startActivityForResult(intent,ADD_TANK);
                break;

            case R.id.btnHSE:
                appSession.setInspectionDTO(inspectionDTO);
                //for HSE Survey screen
                Intent intent2 = new Intent(context, HSESurvayActivity.class);
                intent2.putExtra("inspectionPosition",inspectionPosition);
                startActivityForResult(intent2,HSESurvay);

                break;

            case R.id.btnFORE:
                //for FORE COURT Survey screen
                appSession.setInspectionDTO(inspectionDTO);
                Intent intent3 = new Intent(context, FORCOURTSurvayActivity.class);
                intent3.putExtra("inspectionPosition",inspectionPosition);
                startActivityForResult(intent3,FORCOURTSurvay);
                break;

            case R.id.btnSave:
                inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setComment(et_comment.getText().toString());
                inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setPurpose(edtPurpose.getText().toString());
                inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setTime(inspectionTime);



                if (isValid(true)) {
                    recycler_view_calculation.setVisibility(View.VISIBLE);
                    tv_all_values_tag.setVisibility(View.VISIBLE);
                    appSession.setInspectionDTO(inspectionDTO);
                    setCalculationTable();
                }
                break;
            case R.id.btnSbmt:

                inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setComment(et_comment.getText().toString());
                inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setPurpose(edtPurpose.getText().toString());

                if (recycler_view_calculation.getVisibility()!=View.VISIBLE) {
                    Toast toast = Toast.makeText(context, "Please save inspection before submit.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if (isValid(true)) {
                    if (CheckNetworkStateClass.isOnline(context)) {
                        if (TextUtils.isEmpty(appSession.getStationIds())){
                            if (taskForNewInspection!=null && !taskForNewInspection.isCancelled())
                                return;

                            taskForNewInspection=new TaskForNewInspection(0,null,NewInspectionActivity.this,inspectionDTO.getStations().get(inspectionPosition));
                            taskForNewInspection.execute();
                        }else {
                            Toast toast = Toast.makeText(context, "Please synchronize old inspections first.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {

                        //save in session

                        if (inspectionDTO!=null && inspectionDTO.getStatusMode().equals("0")){

                            //work in offline mode

                            appSession.setInspectionDTO(inspectionDTO);
                            appSession.setStation(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getStationId(),inspectionDTO.getStations().get(inspectionPosition));

                            if (TextUtils.isEmpty(appSession.getStationIds())){
                                appSession.setStationIds(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getStationId());
                            }else {

                                List<String> stationIds=new ArrayList<>(Arrays.asList(appSession.getStationIds().split(",")));

                                if (!stationIds.contains(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getStationId())){
                                    stationIds.add(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getStationId());
                                }
                                appSession.setStationIds(TextUtils.join(",", stationIds));
                            }

                            Toast toast = Toast.makeText(context, "Inspection save successfully.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            finish();
                        }else {
                            errorAlert("Offline",getResources().getString(R.string.network_error_offline));
                        }
                    }
                }
                break;
        }
    }

    private boolean isCompleted(Boolean show){
        if (inspectionDTO.getStations().get(inspectionPosition).getTanks()!=null) {
            for (int j = 0; j < inspectionDTO.getStations().get(inspectionPosition).getTanks().size(); j++) {

                if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData())|| inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankCurrentData().equals("0")) {
                    if (show){
                        Toast toast = Toast.makeText(context, "Please enter current reading of "+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankName(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    return false;
                }
                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle() != null) {
                    if (inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().size()>0) {
                        for (int k = 0; k < inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().size(); k++) {
                            if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume()) || inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume().equals("0")) {
                                if (show){
                                    Toast toast = Toast.makeText(context, "Please enter current reading of "+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getNozzle().get(k).getDumeterName()+" of "+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankName(), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                return false;
                            }
                        }
                    }else {
                        if (show){
                            Toast toast = Toast.makeText(context, "Please add at least one nozzle in "+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankName(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        return false;
                    }
                }else {
                    if (show){
                        Toast toast = Toast.makeText(context, "Please add at least one nozzle in "+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(j).getTankName(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    return false;
                }
            }
        }else {
            if (show){
                Toast toast = Toast.makeText(context, "Please add at least one tank.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        }
        return true;
    }

    private boolean isValid(Boolean show){
        if (TextUtils.isEmpty(station_Id)) {
            if (show){
                Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        }else if (inspectionDTO.getStations().get(inspectionPosition).getTanks().size()<1) {
            if (show){
                Toast toast = Toast.makeText(context, "Please add at least one tank.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        }else if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHseSurveyRating())) {
            if (show){
                Toast toast = Toast.makeText(context, "First Do HSE Survey", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        } else if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getForeCordSurveyRating())) {
            if (show){
                Toast toast = Toast.makeText(context, "First Do FOR COURT Survey", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        } else if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getPurpose())) {
            if (show){
                Toast toast = Toast.makeText(context, "Please enter purpose.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        } else if (TextUtils.isEmpty(inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getComment())) {
            if (show){
                Toast toast = Toast.makeText(context, "Please enter comment.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        } else if (inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getImages()!=null && inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getImages().size()==0) {
            if (show){
                Toast toast = Toast.makeText(context, "Please select at least one attachment or take picture.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return false;
        }
        return isCompleted(true);
    }

    TaskForNewInspection taskForNewInspection=null;



    public class TaskForNewInspection extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd;
        private Context context=null;
        private InspectionDTO.Station station=null;
        private String[] forcourtSurveyKey,hseSurveyKey;
        private  String actionName=null;
        private int position=0;
        TaskForNewInspection(int position,String actionName,Context context,InspectionDTO.Station station){
            this.context=context;
            this.station=station;
            this.actionName=actionName;
            this.position=position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setTitle("Please wait...");
            pd.setMessage("Inspection are saving...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> imageList = new ArrayList<NameValuePair>();
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

            try {
                JSONArray jsonArray=new JSONArray();

                if (station.getTanks()!=null){
                    for (int j=0;j<station.getTanks().size();j++){
                        JSONObject jsonObject=new JSONObject();

                        jsonObject.put("tank_id",station.getTanks().get(j).getTankId());
                        jsonObject.put("opening_balance",station.getTanks().get(j).getOpeningBalance());
                        jsonObject.put("tank_name",station.getTanks().get(j).getTankName());
                        jsonObject.put("tank_allquantity",station.getTanks().get(j).getTankCurrentData());
                        jsonObject.put("dispensary",station.getTanks().get(j).getDispensary());
                        jsonObject.put("tank_type",station.getTanks().get(j).getTankType());
                        jsonObject.put("tank_timezone",station.getTanks().get(j).getTankTimezone());
                        jsonObject.put("dealer_code",station.getTanks().get(j).getDelearCode());
                        jsonObject.put("remark",station.getTanks().get(j).getRemark());
                        jsonObject.put("remark_date",station.getTanks().get(j).getRemarkDate());
                        jsonObject.put("tank_current_data",station.getTanks().get(j).getTankCurrentData());
                        jsonObject.put("tank_opening_balance",station.getTanks().get(j).getOpeningBalance());
                        jsonObject.put("tank_maximum_value",station.getTanks().get(j).getTankMaximumValue());
                        jsonObject.put("tank_product",station.getTanks().get(j).getTankType());
                        jsonObject.put("tank_flush",station.getTanks().get(j).getFlush());
                        if (station.getTanks().get(j).getNozzle()!=null){
                            JSONArray jsonArray1=new JSONArray();
                            for (int k=0;k<station.getTanks().get(j).getNozzle().size();k++) {
                                JSONObject jsonObject1=new JSONObject();
                                jsonObject1.put("tank_id",station.getTanks().get(j).getTankId());
                                jsonObject1.put("dumeter_id",station.getTanks().get(j).getNozzle().get(k).getDumeterId());
                                jsonObject1.put("opening_balance_d",station.getTanks().get(j).getNozzle().get(k).getOpeningBalanceD());
                                jsonObject1.put("dispensary_d",station.getTanks().get(j).getNozzle().get(k).getDispensaryD());
                                jsonObject1.put("dumeter_allquantity",station.getTanks().get(j).getNozzle().get(k).getDumeterAllquantity());
                                jsonObject1.put("dumeter_name",station.getTanks().get(j).getNozzle().get(k).getDumeterName());
                                jsonObject1.put("dumeter_type",station.getTanks().get(j).getTankType());
                                jsonObject1.put("sap_code",station.getTanks().get(j).getNozzle().get(k).getSapCode());
                                jsonObject1.put("dumeter_timezone",station.getTanks().get(j).getNozzle().get(k).getDumeterTimezone());
                                jsonObject1.put("dumeter_product",station.getTanks().get(j).getTankType());
                                jsonObject1.put("dumeter_accuracy",station.getTanks().get(j).getNozzle().get(k).getDumeterAccuracy());
                                jsonObject1.put("dumeter_defect",station.getTanks().get(j).getNozzle().get(k).getDumeterDefect());
                                jsonObject1.put("dumeter_max_value",station.getTanks().get(j).getNozzle().get(k).getDumeterMaxValue());
                                jsonObject1.put("opening_bal",station.getTanks().get(j).getNozzle().get(k).getOpeningBalanceD());
                                jsonObject1.put("dumeter_current_volume",station.getTanks().get(j).getNozzle().get(k).getDumeterCurrentVolume());
                                jsonObject1.put("dumeter_previous_volume",station.getTanks().get(j).getNozzle().get(k).getDumeterPreviousVolume());
                                jsonObject1.put("special_reading_reset",station.getTanks().get(j).getNozzle().get(k).getSpecialReading());
                                jsonObject1.put("comments_reset",station.getTanks().get(j).getNozzle().get(k).getCommentsReset());
                                jsonObject1.put("reset_status",station.getTanks().get(j).getNozzle().get(k).getResetStatus());

                                jsonArray1.put(jsonObject1);
                            }
                            jsonObject.put("Nozzle",jsonArray1);
                        }
                        jsonArray.put(jsonObject);
                    }
                }





                if (station.getStationsDetalis()!=null) {

                    nvp.add(new BasicNameValuePair("user_id", station.getStationsDetalis().getUserStUserid()));
                    nvp.add(new BasicNameValuePair("dealer_code", station.getStationsDetalis().getSapCode()));
                    nvp.add(new BasicNameValuePair("inspection_resion", station.getStationsDetalis().getSapCode()));
                    nvp.add(new BasicNameValuePair("status", "1"));
                    nvp.add(new BasicNameValuePair("station_id", station.getStationsDetalis().getStationId()));
                    nvp.add(new BasicNameValuePair("station_latitude", station.getStationsDetalis().getStationLatitude()));
                    nvp.add(new BasicNameValuePair("station_longitude",station.getStationsDetalis().getStationLongitude()));
                    nvp.add(new BasicNameValuePair("tanks_reading", "Completed"));
                    nvp.add(new BasicNameValuePair("dumeter_reading", ""));

                    nvp.add(new BasicNameValuePair("hse_survey_rating", station.getStationsDetalis().getHseSurveyRating()));
                    nvp.add(new BasicNameValuePair("custmer_survey_forcort_rating",  station.getStationsDetalis().getForeCordSurveyRating()));
                    nvp.add(new BasicNameValuePair("comment",  station.getStationsDetalis().getComment()));
                    nvp.add(new BasicNameValuePair("purpose", station.getStationsDetalis().getPurpose()));
                    nvp.add(new BasicNameValuePair("insp_timestamp", station.getStationsDetalis().getTime()));

                    if (TextUtils.isEmpty(actionName)){
                        nvp.add(new BasicNameValuePair("user_status", "1"));
                    }else {
                        nvp.add(new BasicNameValuePair("user_status", "0"));
                    }
                }


                //set fore court survey
                forcourtSurveyKey=context.getResources().getStringArray(R.array.for_court_survey_key);
                String[] FORCOURTData=null;
                if (!TextUtils.isEmpty(station.getStationsDetalis().getFORCOURTSurvayDATA())){
                    FORCOURTData=station.getStationsDetalis().getFORCOURTSurvayDATA().split(",");
                }
                for (int i=0;i<forcourtSurveyKey.length;i++){
                    nvp.add(new BasicNameValuePair(forcourtSurveyKey[i], FORCOURTData[i]));
                }

                //set hse survey
                hseSurveyKey=context.getResources().getStringArray(R.array.hse_survey_key);
                String[] HSEData=null;
                if (!TextUtils.isEmpty(station.getStationsDetalis().getHSESurvayDATA())){
                    HSEData=station.getStationsDetalis().getHSESurvayDATA().split(",");
                }

                if (HSEData!=null && hseSurveyKey!=null){
                    for (int i=0;i<hseSurveyKey.length;i++){
                        nvp.add(new BasicNameValuePair(hseSurveyKey[i], HSEData[i]));
                    }
                }

                for (int i=0;i<station.getCalculation().size();i++) {
                    switch (i) {
                        case 1:
                            nvp.add(new BasicNameValuePair("a_hsd",station.getCalculation().get(1).getHsd()));
                            nvp.add(new BasicNameValuePair("a_pmg", station.getCalculation().get(1).getPmg()));
                            nvp.add(new BasicNameValuePair("a_hobc", station.getCalculation().get(1).getHobc()));
                            break;
                        case 2:
                            nvp.add(new BasicNameValuePair("b_hsd",station.getCalculation().get(2).getHsd()));
                            nvp.add(new BasicNameValuePair("b_pmg", station.getCalculation().get(2).getPmg()));
                            nvp.add(new BasicNameValuePair("b_hobc", station.getCalculation().get(2).getHobc()));
                            break;
                        case 3:
                            nvp.add(new BasicNameValuePair("c_hsd",station.getCalculation().get(3).getHsd()));
                            nvp.add(new BasicNameValuePair("c_pmg",station.getCalculation().get(3).getPmg()));
                            nvp.add(new BasicNameValuePair("c_hobc",station.getCalculation().get(3).getHobc()));
                            break;
                        case 4:
                            nvp.add(new BasicNameValuePair("d_hsd",station.getCalculation().get(4).getHsd()));
                            nvp.add(new BasicNameValuePair("d_pmg", station.getCalculation().get(4).getPmg()));
                            nvp.add(new BasicNameValuePair("d_hobc", station.getCalculation().get(4).getHobc()));
                            break;
                        case 5:
                            nvp.add(new BasicNameValuePair("e_hsd",station.getCalculation().get(5).getHsd()));
                            nvp.add(new BasicNameValuePair("e_pmg",station.getCalculation().get(5).getPmg()));
                            nvp.add(new BasicNameValuePair("e_hobc",station.getCalculation().get(5).getHobc()));
                            break;
                        case 6:
                            nvp.add(new BasicNameValuePair("f_hsd",station.getCalculation().get(6).getHsd()));
                            nvp.add(new BasicNameValuePair("f_pmg", station.getCalculation().get(6).getPmg()));
                            nvp.add(new BasicNameValuePair("f_hobc",station.getCalculation().get(6).getHobc()));
                            break;
                        case 7:
                            nvp.add(new BasicNameValuePair("result_hsd",station.getCalculation().get(7).getHsd()));
                            nvp.add(new BasicNameValuePair("result_pmg",station.getCalculation().get(7).getPmg()));
                            nvp.add(new BasicNameValuePair("result_hobc",station.getCalculation().get(7).getHobc()));
                            break;
                    }
                }
                nvp.add(new BasicNameValuePair("tank_full_data", jsonArray.toString()));


                for (int i=0;i<station.getStationsDetalis().getImages().size();i++){
                    if (!TextUtils.isEmpty(station.getStationsDetalis().getImages().get(i).getName())){
                        imageList.add(new BasicNameValuePair("attachments[]",station.getStationsDetalis().getImages().get(i).getName()));
                    }
                }

                for (int i=0;i<nvp.size();i++){
                    if (nvp.get(i).getValue()==null){
                        Log.i("params ",nvp.get(i).getName()+" is null");
                    }
                }
                for (int i=0;i<imageList.size();i++){
                    if (imageList.get(i).getValue()==null){
                        Log.i("params ",imageList.get(i).getName()+" is null");
                    }
                }

                Log.i("params ","nvp "+nvp.toString());
                Log.i("params ","imageList "+imageList.toString());

                return new RemoteRequestResponse().httpPost(ConstantLib.BASE_URL + ConstantLib.URL_NEWINSPCTN, nvp, imageList,null);

            }catch (JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
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
                    JSONObject jsonObject=new JSONObject(result);
                    if (jsonObject.has("status")){
                        if (jsonObject.getString("status").equals("1")){
                            Toast toast = Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            if (!TextUtils.isEmpty(actionName)){
                                Intent i = new Intent(actionName);
                                i.putExtra("data",result);
                                i.putExtra("position",position);
                                context.sendBroadcast(i);
                            }else {

                                ((Activity)context).finish();
                            }
                        }else {
                            Toast toast = Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast toast = Toast.makeText(context, e.getMessage()+" Response "+result, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
               /* {"message":"success","status":1}*/
                Log.i("result ","result "+result);
            }else {
                Toast toast = Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            cancel(true);
        }
    }


    private Uri mImageCaptureUri;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "AbcFuel" + String.valueOf(System.currentTimeMillis())+ ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);
                    if (mImageCaptureUri==null){
                        Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (item==1) {
                    Intent intent1 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent1.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent1, "Select File"),SELECT_FILE);
                } else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_TANK) {
                setValue();
                Log.i("ADD_TANK", "ADD_TANK" + ADD_TANK);
            }else if (requestCode == HSESurvay) {
                Log.i("HSESurvay", "HSESurvay" + HSESurvay);
                setValue();
            }else if (requestCode == FORCOURTSurvay) {
                Log.i("FORCOURTSurvay", "FORCOURTSurvay " + FORCOURTSurvay);
                setValue();
            }else if (requestCode == CAPTURE) {
                if (mImageCaptureUri==null){
                    Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                }else {
                    selectedImagePath = mImageCaptureUri.getPath();// from Camera
                    Log.i("selectedImagePath", "selectedImagePath CAPTURE " + sak + ":     " + selectedImagePath);
                    setImages();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                if (mImageCaptureUri==null){
                    Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                }else {
                    selectedImagePath = mImageCaptureUri.getPath();// from Camera
                    Log.i("selectedImagePath", "selectedImagePath REQUEST_CAMERA " + sak + ":     " + selectedImagePath);
                    setImages();
                }
            } else if (requestCode == SELECT_FILE) {
                selectedImagePath = getPathFromURI(data.getData());
                Log.i("selectedImagePath", "selectedImagePath SELECT_FILE" + selectedImagePath);

                if (selectedImagePath==null){
                    Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                }else {
                    setImages();
                }
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        if (contentUri==null)
            return null;
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void setImages() {
        Bitmap bm = null;
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            tempLayout.setVisibility(View.VISIBLE);

            if (bm!=null){
                img12.setImageBitmap(bm);
                tv12.setText(strlatitude + "  " + strlongitude + "  " + "\n" + "  " + station_Name + "  " + "\n" + "  " + txtDte.getText().toString());
                tv12.setBackgroundColor(Color.parseColor("#60111111"));
                tempLayout.setDrawingCacheEnabled(true);
                tempLayout.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(tempLayout.getDrawingCache());

                selectedImagePath = saveToInternalStorage(b);

                if (selectedImagePath!=null){
                    recycler_view.setVisibility(View.VISIBLE);

                    InspectionDTO.Station.StationsDetalis.Image image=inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().new Image();
                    image.setName(selectedImagePath);
                    if (callFrom==CAMERA){
                        image.setStatus("1");
                    }
                    image.setName(selectedImagePath);
                    image.setText(strlatitude + "  " + strlongitude + "  " + "\n" + "  " + station_Name + "  " + "\n" + "  " + txtDte.getText().toString());
                    inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getImages().add(image);
                    imagesAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewInspectionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        selectedImagePath=null;
        mImageCaptureUri=null;

        tempLayout.setVisibility(View.GONE);

    }

    public void checkPermission(){
        if (!hasPermission(Manifest.permission.CAMERA) && Build.VERSION.SDK_INT >= 23 && !TextUtils.isEmpty(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(NewInspectionActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        } else if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && Build.VERSION.SDK_INT >= 23 && !TextUtils.isEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }else {
            if (callFrom==CAMERA){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "AbcFuel" + String.valueOf(System.currentTimeMillis())
                        + ".jpg");
                mImageCaptureUri = Uri.fromFile(file);

                if (mImageCaptureUri==null){
                    Toast.makeText(NewInspectionActivity.this,"Please try again.",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, CAPTURE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                selectImage();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);


        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String pth = file.getAbsolutePath();
        selectedImagePath = compressImage(pth);

        return file.getAbsolutePath();
    }


    public String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Fuel/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 0 meters

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 10 * 1; // 1 second

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            String strlatitude = "" + new DecimalFormat("###.0####").format(latitude);
                            String strlongitude = "" + new DecimalFormat("###.0####").format(longitude);
                            Utils.setPref(this, Constants.USER_LONGITUDE, "" + strlongitude);
                            Utils.setPref(this, Constants.USER_LATITUDE, "" + strlatitude);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                String strlatitude = "" + new DecimalFormat("###.0####").format(latitude);
                                String strlongitude = "" + new DecimalFormat("###.0####").format(longitude);
                                /*Utils.setPref(this, Constants.USER_LONGITUDE, "" + strlongitude);
                                Utils.setPref(this, Constants.USER_LATITUDE, "" + strlatitude);*/

                                //   txtVwLoctn.setText(new DecimalFormat("###.0####").format(longitude) + " " + new DecimalFormat("###.0####").format(latitude));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.sendExceptionReport(e, this);
        }

        return location;
    }



    private void errorAlert(String title,String message){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(NewInspectionActivity.this);

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
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
