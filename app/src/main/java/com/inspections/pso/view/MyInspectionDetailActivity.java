package com.inspections.pso.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import com.inspections.pso.adapter.CalculationAdapter;
import com.inspections.pso.adapter.ImagesAdapter;
import com.inspections.pso.adapter.MyInspectionImagesAdapter;
import com.inspections.pso.adapter.StationAdapter;
import com.inspections.pso.adapter.ViewHSESurvayAdapter;
import com.inspections.pso.adapter.ViewTankAdapter;
import com.inspections.pso.dto.CalculationDTO;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.MyInspectionDTO;
import com.inspections.pso.netcome.CheckNetworkStateClass;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ConstantLib;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by mobiweb on 1/3/16.
 */
public class MyInspectionDetailActivity extends InspectionBaseActivity implements  View.OnClickListener, LocationListener {

    Context context;
    private Toolbar toolBar;
    private TextView tv_time,tv_date,tv_comment,tv_purpose,tv_station,tv_sap_code;
    private Button btnTnkRdng;

    private Button btnHseSurv;
    private Button btnForSurv;
    private  TextView hseTxtRtng;
    private  TextView forcortTxtrtng;


    private AppSession appSession;


    private String[] calTitle,calDescription;

    LinearLayoutManager layoutManager;
    RecyclerView recycler_view,recycler_view_calculation;
    List<String> imagesDTOs=new ArrayList<>();
    MyInspectionImagesAdapter imagesAdapter;
    CalculationAdapter calculationAdapter;

    ArrayList<CalculationDTO> calculationDTOs=null;
    MyInspectionDTO.Response inspectionDTO=null;

    private int inspectionPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_inspection_detail);
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
            Log.i("inspectionPosition","inspectionPosition "+inspectionPosition+"");
        }
        calTitle=getResources().getStringArray(R.array.calculation_title);
        calDescription=getResources().getStringArray(R.array.calculation_description);
        context = this;
        appSession = new AppSession(context);


        //initiated inspections
        inspectionDTO=appSession.getMyInspectionDTO().getResponse().get(inspectionPosition);

        initView();

        setStationDetails();
        setImages();
        setHseSurvey();
        setForeCordSurvey();
        setCalculationTable();
        setCommentPurpose();


    }
    private void setStationDetails(){

        if (inspectionDTO.getStationsDetalis()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getStationsDetalis().getStationName())){
                tv_station.setText("N/A");
            }else {
                tv_station.setText(inspectionDTO.getStationsDetalis().getStationName());
            }
            if (TextUtils.isEmpty(inspectionDTO.getStationsDetalis().getSapCode())){
                tv_sap_code.setText("N/A");
            }else {
                tv_sap_code.setText(inspectionDTO.getStationsDetalis().getSapCode());
            }

            if (TextUtils.isEmpty(inspectionDTO.getStationsDetalis().getSapCode())){
                tv_sap_code.setText("N/A");
            }else {
                tv_sap_code.setText(inspectionDTO.getStationsDetalis().getSapCode());
            }

            tv_date.setText(Utils.getDateLogByFormat(inspectionDTO.getStationsDetalis().getInspectionTimestamp()));
            tv_time.setText(Utils.getTimeLogByFormat(inspectionDTO.getStationsDetalis().getInspectionTimestamp()));
        }

    }
    private void setImages(){

        if (inspectionDTO.getCommonObject()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getAttachement())){
                recycler_view.setVisibility(View.GONE);
            }else {
                if (imagesDTOs==null){
                    imagesDTOs=new ArrayList<>();
                }
                String[] images=inspectionDTO.getCommonObject().getAttachement().split(",");
                for (int i=0;i<images.length;i++){
                    imagesDTOs.add(images[i]);
                }

                if (imagesDTOs.size()==0){
                    recycler_view.setVisibility(View.GONE);
                }else {
                    recycler_view.setVisibility(View.VISIBLE);
                    imagesAdapter = new MyInspectionImagesAdapter(context, imagesDTOs,inspectionDTO.getCommonObject().getPath());
                    recycler_view.setAdapter(imagesAdapter);
                }
            }
        }

    }
    private void setCommentPurpose(){

        if (inspectionDTO.getCommonObject()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getComment())){
                tv_comment.setText("N/A");
            }else {
                tv_comment.setText(inspectionDTO.getCommonObject().getComment());
            }
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getPurpose())){
                tv_purpose.setText("N/A");
            }else {
                tv_purpose.setText(inspectionDTO.getCommonObject().getPurpose());
            }
        }
    }

    private void setHseSurvey(){

        if (inspectionDTO.getCommonObject()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getHseSurvey())){
                hseTxtRtng.setText("N/A");
            }else if (inspectionDTO.getCommonObject().getHseSurvey().contains("(")){
                hseTxtRtng.setText(inspectionDTO.getCommonObject().getHseSurvey());
            }else if (Integer.parseInt(inspectionDTO.getCommonObject().getHseSurvey())>10){
                hseTxtRtng.setText("SAFE" + "(" + inspectionDTO.getCommonObject().getHseSurvey()+ "/13)");
            }else{
                hseTxtRtng.setText("UNSAFE" + "(" + inspectionDTO.getCommonObject().getHseSurvey()+ "/13)");
            }
        }
    }

    private void setForeCordSurvey(){
        if (inspectionDTO.getCommonObject()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getForcastSurvey())){
                forcortTxtrtng.setText("N/A");
            }else if (inspectionDTO.getCommonObject().getForcastSurvey().contains("(")){
                forcortTxtrtng.setText(inspectionDTO.getCommonObject().getForcastSurvey());
            }else if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>9){
                forcortTxtrtng.setText("GOOD" + "(" +inspectionDTO.getCommonObject().getForcastSurvey()+ "/14)");
            }else if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>6){
                forcortTxtrtng.setText("AVERAGE" + "(" +inspectionDTO.getCommonObject().getForcastSurvey()+ "/14)");
            }else{
                forcortTxtrtng.setText("BELOW AVERAGE" + "(" + inspectionDTO.getCommonObject().getForcastSurvey()+ "/14)");
            }
        }

    }

    private void setCalculationTable(){
        if (inspectionDTO.getCalculation()==null ||inspectionDTO.getCalculation().size()==0)
            return;

        if (calculationDTOs==null)
            calculationDTOs=new ArrayList<>();

        calculationDTOs.clear();

        for (int i=0;i<calDescription.length;i++){
            CalculationDTO dto=new CalculationDTO();
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
                    dto.setHsd(inspectionDTO.getCalculation().get(0).getAHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getAPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getAHobc());
                    break;
                case 2:
                    //calculation for B

                    dto.setHsd(inspectionDTO.getCalculation().get(0).getBHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getBPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getBHobc());
                    break;
                case 3:
                    //calculation for C=A+B

                    dto.setHsd(inspectionDTO.getCalculation().get(0).getCHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getCPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getCHobc());
                    break;
                case 4:
                    //calculation for D

                    dto.setHsd(inspectionDTO.getCalculation().get(0).getDHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getDPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getDHobc());

                    break;
                case 5:
                    //calculation for E=C-D
                    dto.setHsd(inspectionDTO.getCalculation().get(0).getEHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getEPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getEHobc());
                    break;
                case 6:
                    //calculation for F all current reading

                    dto.setHsd(inspectionDTO.getCalculation().get(0).getFHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getFPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getFHobc());
                    break;

                case 7:
                    //calculation for F-E
                    dto.setHsd(inspectionDTO.getCalculation().get(0).getResultHsd());
                    dto.setPmg(inspectionDTO.getCalculation().get(0).getResultPmg());
                    dto.setHobc(inspectionDTO.getCalculation().get(0).getResultHobc());

                    break;

            }
            calculationDTOs.add(dto);
        }

        calculationAdapter = new CalculationAdapter(context, calculationDTOs);
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


        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);

        tv_purpose = (TextView) findViewById(R.id.tv_purpose);
        btnTnkRdng = (Button) findViewById(R.id.btnTnkRdg);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_sap_code = (TextView) findViewById(R.id.tv_sap_code);


         btnHseSurv = (Button) findViewById(R.id.btnHSE);
        btnForSurv = (Button) findViewById(R.id.btnFORE);
        hseTxtRtng = (TextView) findViewById(R.id.txthseRtng);
        forcortTxtrtng = (TextView) findViewById(R.id.txtForCOrtRtng);
        tv_station = (TextView) findViewById(R.id.tv_station);

        btnTnkRdng.setOnClickListener(this);
        btnHseSurv.setOnClickListener(this);
        btnForSurv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnTnkRdg:

                Intent intent = new Intent(context, ViewTankActivity.class);
                intent.putExtra("inspectionPosition",inspectionPosition);
                startActivity(intent);
                break;

            case R.id.btnHSE:

                Intent intent2 = new Intent(context, ViewHSESurvayActivity.class);
                intent2.putExtra("inspectionPosition",inspectionPosition);
                startActivity(intent2);

                break;

            case R.id.btnFORE:

                Intent intent3 = new Intent(context, ViewFORCOURTSurvayActivity.class);
                intent3.putExtra("inspectionPosition",inspectionPosition);
                startActivity(intent3);
                break;

        }

    }

}
