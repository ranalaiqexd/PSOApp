package com.newinspections.pso.newview;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.adapter.ImagesAdapter;
import com.newinspections.pso.adapter.NewCalculationAdapter;
import com.newinspections.pso.dao.UtilDAO;
import com.newinspections.pso.database.NozzlesORM;
import com.newinspections.pso.database.ProductsORM;
import com.newinspections.pso.database.PurchaseORM;
import com.newinspections.pso.database.TanksORM;
import com.newinspections.pso.model.ProductsModel;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.newadapter.LatestCalculationAdapter;
import com.newinspections.pso.newadapter.NewStationAdapter;
import com.newinspections.pso.database.StationORM;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.utils.GPSTrackerNew;
import com.newinspections.pso.utils.GeoAddress;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.utils.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.newinspections.pso.utils.ConstantLib.GET_USER_INSPECTIONS;
import static com.newinspections.pso.utils.ConstantLib.SUBMIT_USER_INSPECTIONS;
import static com.newinspections.pso.utils.ConstantLib.USER_LOCATION;
import static com.newinspections.pso.utils.ConstantLib.USER_LOGIN;
import static com.newinspections.pso.utils.ConstantLib.value;

public class LatestNewInspectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LatestNewInspectionActivity";

    Context context;
    private AppSession appSession;
    private Toolbar toolBar;

    private Spinner spnrFulInspctn;
    private GPSTrackerNew gpsTracker;
    double userLatitude;
    double userLongitude;
    String userAddress = "";

    private TextView rtailOutlt;
    private TextView txtDte;
    private TextView txtTme;
    private TextView hseTxtRtng;
    private TextView forcortTxtrtng;
    private TextView edTnkRdng;
    private TextView tv_all_values_tag;
    TextView tv12;
    private TextView txtVwGps;

    private Button btnTnkRdng;
    private Button btnHseSurv;
    private Button btnForSurv;
    private Button btn_captr;
    private Button btnNwInspSave;
//    private Button btnSaveDraft;
    private Button btnSbmtt;
    private CheckBox checkBoxTraining;

    private EditText et_comment;
    private EditText edtPurpose;

    private ImageView imgVw;
    private ImageView img12;

    private String station_Id = "";
    private String station_Name = "";
    private String station_Code = "";
    private String draftDate = "";
    private Uri captureImagePath;
    private String selectedImagePath = "";
    private String photoCaptureTime = "";
    private String strlatitude = "";
    private String strlongitude = "";
    private String hseSurveyResult = "";
    private String forecourtSurveyResult = "";
    private String hseDraftData = "";
    private String forecourtDraftData = "";
    private RequestQueue requestQueue;
    private String[] calTitle;
    private String[] calDescription;

    private static final int CAPTURE = 1001;
    private static final int SELECT_FILE = 200;
    private static final int HSESurvay = 1002;
    private static final int FORCOURTSurvay = 1003;
    private static final int ADD_TANK = 1004;
    private static final int IMAGE_CAMERA = 1;
    private static final int IMAGE_GALLERY = 2;

    private static final int PERMISSION_IMAGE_CAPTURE = 5;
    private static final int PERMISSION_READ_IMAGES = 6;
    private int CAMERA = 112;
    private int sak = 0;
    private int callFrom=0;
    private int stationPosition;
    private boolean isSaved = false;

    RelativeLayout tempLayout;
    LinearLayoutManager layoutManager;
    RecyclerView recycler_view,recycler_view_calculation;
    InspectionsModel inspectionsModel = null;
    InspectionsModel inspectionsModeDraft;
    ImagesAdapter imagesAdapter;
    LatestCalculationAdapter calculationAdapter;
    ProgressDialog progressDialog = null;
    AddressTask addressTask = null;

//    SubmitInspectionsToServer submitInspectionsToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_inspection);

        context= this;
        appSession= new AppSession(context);
        gpsTracker = new GPSTrackerNew(context);
        calTitle=getResources().getStringArray(R.array.calculation_title);
        calDescription=getResources().getStringArray(R.array.calculation_description);
        initView();
        initializeStationList();
        isSaved = false;

        TanksORM.deleteTemporaryTanks(context);
        TanksORM.updateTankCurrentReading(context);
        NozzlesORM.deleteTemporaryNozzles(context);
        NozzlesORM.updateNozzleCurrentReading(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userLatitude = gpsTracker.getLatitude();
        userLongitude = gpsTracker.getLongitude();
        initializeDateAndTime();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position)
        {

        }
    };

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

        rtailOutlt = (TextView) findViewById(R.id.txtvRtloutet);

        edtPurpose = (EditText) findViewById(R.id.edtPurpose);
        edTnkRdng = (TextView) findViewById(R.id.edtTnkRdng);
        btnTnkRdng = (Button) findViewById(R.id.btnTnkRdg);
        et_comment = (EditText) findViewById(R.id.et_comment);

        btnHseSurv = (Button) findViewById(R.id.btnHSE);
        btnForSurv = (Button) findViewById(R.id.btnFORE);
        hseTxtRtng = (TextView) findViewById(R.id.txthseRtng);
        forcortTxtrtng = (TextView) findViewById(R.id.txtForCOrtRtng);
        btnNwInspSave = (Button) findViewById(R.id.btnSave);
        btnSbmtt = (Button) findViewById(R.id.btnSbmt);
        btn_captr = (Button) findViewById(R.id.btnCapture);
//        btnSaveDraft = (Button) findViewById(R.id.btnDraft);
        checkBoxTraining = (CheckBox) findViewById(R.id.checkboxTraining);
        imgVw = (ImageView) findViewById(R.id.imgAttchmnt);

        btnTnkRdng.setOnClickListener(this);
        btnHseSurv.setOnClickListener(this);
        btnForSurv.setOnClickListener(this);
        btn_captr.setOnClickListener(this);
        imgVw.setOnClickListener(this);
        btnNwInspSave.setOnClickListener(this);
        btnSbmtt.setOnClickListener(this);
//        btnSaveDraft.setOnClickListener(this);

        inspectionsModel = appSession.getInspectionModel();

    }

    private void initializeStationList()
    {

//        gpsTracker = new GPSTrackerNew(context);
        userLatitude = gpsTracker.getLatitude();
        userLongitude = gpsTracker.getLongitude();

        final List<InspectionsModel.Stations> list = StationORM.getStations(context);
        if (CheckNetworkStateClass.isOnline(context))
        {
            for (int i=0; i<list.size(); i++)
            {
                double stationLatitude = Double.parseDouble(list.get(i).getStationLatitude());
                double stationLongitude = Double.parseDouble(list.get(i).getStationLongitude());
                Log.d("DistancePoint", String.valueOf(list.get(i).getStationName()+" / "+list.get(i).getStationCode()));

//                Location loc1 = new Location("Point A");
//                loc1.setLatitude(stationLatitude);
//                loc1.setLongitude(stationLongitude);
//                Location loc2 = new Location("Point B");
//                loc2.setLatitude(userLatitude);
//                loc2.setLongitude(userLongitude);
                Log.d("DistanceUser", String.valueOf(userLatitude+" / "+userLongitude));
                Log.d("DistanceStation", String.valueOf(stationLongitude+" / "+stationLatitude));
//                float distanceInMeters = loc2.distanceTo(loc1);
//                Log.d("Distance", String.valueOf(list.get(i).getStationName()+" / "+distanceInMeters));

                float results[] = new float[2];
                Location.distanceBetween(userLongitude, userLatitude, stationLatitude, stationLongitude, results);
                Log.d("Distance", String.valueOf(list.get(i).getStationName()+" / "+results[0]));
                if(CheckNetworkStateClass.isOnline(context))
                {
                    if (results[0]>5000)
//                    if (distanceInMeters>5000)
                    {
                        list.remove(i);
                        i--;
//                        Log.d("Distance", "Remove it");
                    }
                    else
                    {
//                        Log.d("Distance", "Not Remove it");
                    }
                }
            }
        }

        spnrFulInspctn = (Spinner) findViewById(R.id.spnrFul_stn);
        txtVwGps = (TextView) findViewById(R.id.gpsTxt);
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

        if (list!=null && list.size()>0)
        {
            txtVwGps.setText("Select Station");
            txtVwGps.setVisibility(View.VISIBLE);
            spnrFulInspctn.setVisibility(View.GONE);

            NewStationAdapter stationAdapter = new NewStationAdapter(context, list);
            spnrFulInspctn.setAdapter(stationAdapter);
        }
        else
        {
            txtVwGps.setText("GPS coordinates did not match with your current location.");
            txtVwGps.setVisibility(View.VISIBLE);
            spnrFulInspctn.setVisibility(View.GONE);
        }
        spnrFulInspctn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if (list!=null && list.size()>0)
                {
                    station_Id = list.get(position).getStationId();
                    stationPosition = containsPosition(list,station_Id);
                    station_Name = list.get(stationPosition).getStationName();
                    station_Code = list.get(stationPosition).getStationCode();
                    Log.d("StationPosition",stationPosition+" / "+station_Name);
                    txtVwGps.setText(station_Name);
                    rtailOutlt.setText(station_Code);
                    stationPosition = containsPositionUsingCode(inspectionsModel.getStations(), rtailOutlt.getText().toString());

//                    inspectionsModel = appSession.getInspectionModel();
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getStationName());
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getTanks().size());
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getTanks().get(0).getTankName());
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getTanks().get(0).getTankPreviousReading());
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getTanks().get(0).getTankCode());
//                    Log.d("checkhere", "Hello "+inspectionsModel.getStations().get(stationPosition).getTanks().get(0).getTankCreationDate());

                    Log.d("Exd1", station_Name+" / "+draftDate);
                    checkSavedDraft(station_Name, draftDate);

//                    txtVwGps.setVisibility(View.VISIBLE);
//                    spnrFulInspctn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkSavedDraft(String stationName, String date)
    {
        Log.d("Exd2", stationName+" / "+draftDate);

        ImagesAdapter imagesAdapter2;
        inspectionsModeDraft = appSession.getInspectionModelDraft();

        if (inspectionsModeDraft != null)
        {
            if (date.equals(inspectionsModeDraft.getStations().get(stationPosition).getInspectionDate()))
            {
                if (stationName.equals(inspectionsModeDraft.getStations().get(stationPosition).getStationName()))
                {
                    Log.d("Exd3", inspectionsModeDraft.getStations().get(stationPosition).getStationName()
                            +" / "+inspectionsModeDraft.getStations().get(stationPosition).getInspectionDate());

                    edtPurpose.setText(inspectionsModeDraft.getStations().get(stationPosition).getPurpose());
                    et_comment.setText(inspectionsModeDraft.getStations().get(stationPosition).getComments());
                    int isChecked = Integer.parseInt(inspectionsModeDraft.getStations().get(stationPosition).getWowTraining());
                    if (isChecked == 1)
                    {
                        checkBoxTraining.setChecked(true);
                    }
                    else
                    {
                        checkBoxTraining.setChecked(false);
                    }
                    checkTanksAndNozzles();
                    setHSERating();
                    setForecourtRating();

                    hseDraftData = inspectionsModeDraft.getStations().get(stationPosition).getHseData();
                    forecourtDraftData = inspectionsModeDraft.getStations().get(stationPosition).getForecourtData();

                    for (int i=0; i<inspectionsModeDraft.getStations().get(stationPosition).getImages().size(); i++)
                    {
                        String pathForImage = inspectionsModeDraft.getStations().get(stationPosition).getImages().get(i).getImagePath();
                        imagesAdapter2 = new ImagesAdapter(context, inspectionsModeDraft.getStations().get(stationPosition).getImages(), onItemClickCallback);
                        recycler_view.setAdapter(imagesAdapter2);
                        Bitmap bitmap = null;
                        try {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(pathForImage, options);
                            final int REQUIRED_SIZE = 200;
                            int scale = 1;
                            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                                scale *= 2;
                            options.inSampleSize = scale;
                            options.inJustDecodeBounds = false;
                            Log.d("pathForImage", pathForImage+" ;");
                            bitmap = BitmapFactory.decodeFile(pathForImage, options);

                            if (bitmap!=null)
                            {
                                img12.setImageBitmap(bitmap);
                                tv12.setText(inspectionsModeDraft.getStations().get(stationPosition).getImages().get(i).getImageText());
                                tv12.setBackgroundColor(Color.parseColor("#60111111"));
                                tempLayout.setDrawingCacheEnabled(true);
                                tempLayout.buildDrawingCache(true);

                                Bitmap b = Bitmap.createBitmap(tempLayout.getDrawingCache());
                                Log.i("selectedImagePath","selectedImagePath "+ pathForImage);
                                if (pathForImage !=null)
                                {
                                    recycler_view.setVisibility(View.VISIBLE);
                                    appSession.setInspectionModel(inspectionsModeDraft);
                                    imagesAdapter2.notifyDataSetChanged();
                                }

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LatestNewInspectionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        tempLayout.setVisibility(View.GONE);
                        appSession.setInspectionModel(inspectionsModeDraft);
                    }
                }
                else
                {

                }

            }
            if (stationName.equals(inspectionsModeDraft.getStations().get(stationPosition).getStationName()))
            {
                if (date != inspectionsModeDraft.getStations().get(stationPosition).getInspectionDate())
                {
//                    TanksORM.deleteTemporaryTanks(context);
//                    TanksORM.updateTankCurrentReading(context);
//                    NozzlesORM.deleteTemporaryNozzles(context);
//                    NozzlesORM.updateNozzleCurrentReading(context);
                }
            }

        }

    }

    private void initializeDateAndTime()
    {
        txtDte = (TextView) findViewById(R.id.txt_autoDte);
        txtTme = (TextView) findViewById(R.id.txtAuto_time);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());

        String temp[] = formattedDate.split(" ");
        String date = temp[0];
        String time = temp[1];
        draftDate = date;

        txtDte.setText(date);
        txtTme.setText(time);
    }

    int containsPosition(List<InspectionsModel.Stations> list, String id) {
        for (InspectionsModel.Stations item : list) {
            if (!TextUtils.isEmpty(item.getStationId()) && item.getStationId().equals(id)) {
                return list.indexOf(item);
            }
        }
        return 0;
    }

    int containsPositionUsingCode(List<InspectionsModel.Stations> list, String code) {
        Log.d("checkstation", code);
        for (InspectionsModel.Stations item : list) {
            if (!TextUtils.isEmpty(item.getStationCode()) && item.getStationCode().equals(code)) {
                return list.indexOf(item);
            }
        }
        return 0;
    }
    @Override
    public void onClick(View v) {

        if (TextUtils.isEmpty(station_Id)){
            Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        switch (v.getId())
        {
            case R.id.btnTnkRdg:
                //appSession.setInspectionModel(inspectionsModel);
                Log.d("StationPosition",station_Id+"");
                Intent intent = new Intent(context, NewAddTankActivity.class);
                intent.putExtra("StationPosition",stationPosition);
                intent.putExtra("StationId", station_Id);
                startActivityForResult(intent,ADD_TANK);
                break;
            case R.id.btnHSE:
                Intent intent2 = new Intent(context, NewHSESurveyActivity.class);
                intent2.putExtra("StationPosition",stationPosition);
                intent2.putExtra("StationId", station_Id);
                intent2.putExtra("hseDraft", hseDraftData);
                startActivityForResult(intent2,HSESurvay);
                break;
            case R.id.btnFORE:
                Intent intent3 = new Intent(context, NewForecourtActivity.class);
                intent3.putExtra("StationPosition",stationPosition);
                intent3.putExtra("StationId", station_Id);
                intent3.putExtra("forecourtDraft", forecourtDraftData);
                startActivityForResult(intent3,FORCOURTSurvay);
                break;
            case R.id.btnCapture:
                if (TextUtils.isEmpty(station_Id))
                {
                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                else
                {
                    checkCameraPermissions();
                }
                break;
            case R.id.imgAttchmnt:
                if (TextUtils.isEmpty(station_Id))
                {
                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                else
                {
                    selectPictureFromGallery();
                }
                break;
            case R.id.btnSave:
                if (TextUtils.isEmpty(station_Id))
                {
                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                else
                {
                    isSaved = true;

                    // Save Inspection as Draft
                    inspectionsModel = appSession.getInspectionModel();
                    inspectionsModel.getStations().get(stationPosition).setPurpose(edtPurpose.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setComments(et_comment.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setInspectionDate(txtDte.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setInspectionTime(txtTme.getText().toString());
                    String checkboxStatus = "0";
                    if (checkBoxTraining.isChecked())
                    {
                        checkboxStatus = "1";
                    }
                    else
                    {
                        checkboxStatus = "0";
                    }
                    inspectionsModel.getStations().get(stationPosition).setWowTraining(checkboxStatus);
                    appSession.setInspectionModelDraft(inspectionsModel);

                    for (int i=0; i<inspectionsModel.getStations().get(stationPosition).getTanks().size(); i++)
                    {
                        TanksORM.saveTanksDrafted(context, inspectionsModel.getStations().get(stationPosition).getTanks(), i, inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankCode());
                    }

                    recycler_view_calculation.setVisibility(View.VISIBLE);
                    tv_all_values_tag.setVisibility(View.VISIBLE);
                    setCalculation();
                }
                break;
//            case R.id.btnDraft:
//                if (TextUtils.isEmpty(station_Id))
//                {
//                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    return;
//                }
//                else
//                {
//                    // Save Inspection as Draft
//                    inspectionsModel = appSession.getInspectionModel();
//                    inspectionsModel.getStations().get(stationPosition).setPurpose(edtPurpose.getText().toString());
//                    inspectionsModel.getStations().get(stationPosition).setComments(et_comment.getText().toString());
//                    inspectionsModel.getStations().get(stationPosition).setInspectionDate(txtDte.getText().toString());
//                    inspectionsModel.getStations().get(stationPosition).setInspectionTime(txtTme.getText().toString());
//
//                    Toast toast = Toast.makeText(context, "Draft Saved Successfully", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    appSession.setInspectionModelDraft(inspectionsModel);
//                }
//                break;
            case R.id.btnSbmt:
                if (TextUtils.isEmpty(station_Id))
                {
                    Toast toast = Toast.makeText(context, "Please select station.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (isSaved == true)
                {
                    String checkboxStatus = "0";
                    if (checkBoxTraining.isChecked())
                    {
                        checkboxStatus = "1";
                    }
                    else
                    {
                        checkboxStatus = "0";
                    }
                    inspectionsModel = appSession.getInspectionModel();
                    inspectionsModel.getStations().get(stationPosition).setPurpose(edtPurpose.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setComments(et_comment.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setWowTraining(checkboxStatus);
                    inspectionsModel.getStations().get(stationPosition).setInspectionDate(txtDte.getText().toString());
                    inspectionsModel.getStations().get(stationPosition).setInspectionTime(txtTme.getText().toString());



                    for (int i=0; i<inspectionsModel.getStations().get(stationPosition).getTanks().size(); i++)
                    {
                        for (int j=0; j<inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size(); j++)
                        {
                            NozzlesORM.makeNozzlesPermanent(context, inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles(), j, inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().get(j).getNozzleCode());
                        }
                        TanksORM.makeTanksPermanent(context, inspectionsModel.getStations().get(stationPosition).getTanks(), i, inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankCode());
                    }


                    final List<ProductsModel.Products> productsList = ProductsORM.getProducts(context);
                    for (int i=0; i<productsList.size(); i++)
                    {
                        for (int j=0; j<inspectionsModel.getStations().get(stationPosition).getCalculation().size(); j++)
                        {
                            if (j==0)
                            {

                            }
                            else
                            {
                                if (inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getProduct().equals(productsList.get(i).getProductsName()) && inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getTitle().equals("A"))
                                {
                                    for (int k=0; k<inspectionsModel.getStations().get(stationPosition).getProductCalculations().size(); k++)
                                    {
                                        if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(k).getProductName().equals(productsList.get(i).getProductsName()))
                                        {
                                            Log.d("calculateASave", inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getValues());
                                            inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(k).setA(Double.valueOf(inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getValues()));
                                        }

                                    }
                                }
                            }
                        }
                    }

//                    saveCalculation();

                    appSession.setInspectionModel(inspectionsModel);

                    if (CheckNetworkStateClass.isOnline(context))
                    {
                        inspectionsModel = appSession.getInspectionModel();
                        String username = appSession.getUserName();
                        String devicename = appSession.getDevice();
                        String userCode = username + "#" + devicename;
                        Log.d("Stationcheck", inspectionsModel.getStations().get(stationPosition).getStationName() +"/"+stationPosition);

                        // Temporary
//                        Intent intent1 = new Intent(LatestNewInspectionActivity.this, NewDashboard.class);
//                        startActivity(intent1);
//                        finish();

                        SubmitInspectionsToServer(context, inspectionsModel.getStations().get(stationPosition), username, userCode);
                    }
                    else
                    {
                        Toast toast = Toast.makeText(context, "Inspection Submitted Successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    break;
                }
                else
                {
                    Toast toast = Toast.makeText(context, "Please save inspection first.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
        }
    }

    private void checkCameraPermissions()
    {
        if (Build.VERSION.SDK_INT >=23)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_IMAGE_CAPTURE);
            }
            else
            {
                takePictureFromCamera();
            }
        }
        else
        {
            takePictureFromCamera();
        }
    }

    private void checkReadStoragePermissions()
    {
        if (Build.VERSION.SDK_INT >=23)
        {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                Log.d("EXD", "I am here");
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_READ_IMAGES);
            }
            else
            {
                Log.d("EXD", "Now I am here");
                setImages();
            }
        }
        else
        {
            setImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_IMAGE_CAPTURE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                takePictureFromCamera();
            }
            else
            {
                Toast.makeText(context, "Don't have permission to use Camera", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PERMISSION_READ_IMAGES)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(context, "Read Pictures Permission granted", Toast.LENGTH_SHORT).show();
                setImages();
            }
            else
            {
                Toast.makeText(context, "Don't have permission to read pictures", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void checkTanksAndNozzles()
    {
        inspectionsModel = appSession.getInspectionModel();

        edTnkRdng.setText("");
        if ((inspectionsModel.getStations().get(stationPosition).getTanks() != null) && (inspectionsModel.getStations().get(stationPosition).getTanks().size() > 0))
        {
            for (int i=0; i<inspectionsModel.getStations().get(stationPosition).getTanks().size(); i++)
            {
                if( (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankCurrentReading())) || (inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankCurrentReading().equals("0")))
                {
                    Toast toast = Toast.makeText(context, "Please enter current reading of "+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankName(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                {
                    Log.d("Check420", "Nozzle size: "+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size());
                    if ((inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles() != null) && (inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size() > 0))
                    {
                        Log.d("Check420", "If main");
                        for (int j=0; j<inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size(); j++)
                        {
                            if( (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().get(j).getNozzleCurrentReading())) || (inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().get(j).getNozzleCurrentReading().equals("0")))
                            {
                                Toast toast = Toast.makeText(context, "Please enter current reading of "+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().get(j).getNozzleName()+ " of "+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankName(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                break;
                            }
                            else
                            {
                                edTnkRdng.setText("Completed");
                            }
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(context, "Please add at least one nozzle in "+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getTankName(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        }
        else
        {
            Toast toast = Toast.makeText(context, "Please add at least one tank.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void setHSERating()
    {
        inspectionsModel = appSession.getInspectionModel();
//        stationPosition = containsPositionUsingCode(inspectionsModel.getStations(), rtailOutlt.getText().toString());
        Log.d("checkstation", inspectionsModel.getStations().get(stationPosition).getStationName()+"/"+stationPosition);
        hseSurveyResult = "";
        hseDraftData = inspectionsModel.getStations().get(stationPosition).getHseData();

        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getHseRating()))
        {
            hseTxtRtng.setText("Select");
        }
        else if (Integer.parseInt(inspectionsModel.getStations().get(stationPosition).getHseRating()) > 10)
        {
            hseSurveyResult = "SAFE";
            hseTxtRtng.setText("SAFE" + "(" + inspectionsModel.getStations().get(stationPosition).getHseRating() +" / "+ inspectionsModel.getStations().get(stationPosition).getHseQuestions() + ")");
        }
        else
        {
            hseSurveyResult = "UNSAFE";
            hseTxtRtng.setText("UNSAFE" + "(" + inspectionsModel.getStations().get(stationPosition).getHseRating() +" / "+ inspectionsModel.getStations().get(stationPosition).getHseQuestions() + ")");
        }
    }

    private void setForecourtRating()
    {
        inspectionsModel = appSession.getInspectionModel();
        Log.d("checkstation", inspectionsModel.getStations().get(stationPosition).getStationName());
        forecourtSurveyResult = "";
        forecourtDraftData = inspectionsModel.getStations().get(stationPosition).getForecourtData();

        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getForecourtRating()))
        {
            forcortTxtrtng.setText("Select");
        }
        else if (Integer.parseInt(inspectionsModel.getStations().get(stationPosition).getForecourtRating()) > 9)
        {
            forecourtSurveyResult = "GOOD";
            forcortTxtrtng.setText("GOOD" + "(" + inspectionsModel.getStations().get(stationPosition).getForecourtRating() +" / "+ inspectionsModel.getStations().get(stationPosition).getForecourtQuestions() + ")");
        }
        else if (Integer.parseInt(inspectionsModel.getStations().get(stationPosition).getForecourtRating()) > 6)
        {
            forecourtSurveyResult = "AVERAGE";
            forcortTxtrtng.setText("AVERAGE" + "(" + inspectionsModel.getStations().get(stationPosition).getForecourtRating() +" / "+ inspectionsModel.getStations().get(stationPosition).getForecourtQuestions() + ")");
        }
        else
        {
            forecourtSurveyResult = "BELOW AVERAGE";
            forcortTxtrtng.setText("BELOW AVERAGE" + "(" + inspectionsModel.getStations().get(stationPosition).getForecourtRating() +" / "+ inspectionsModel.getStations().get(stationPosition).getForecourtQuestions() + ")");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK)
        {
            Log.d("Check", "I am in RESULT_OK");
            if (requestCode==ADD_TANK)
            {
                Log.d("Check", "I am in ADD_TANK");
                checkTanksAndNozzles();
            }
            else if (requestCode==HSESurvay)
            {
                Log.d("Check", "I am in HSESurvay");
                setHSERating();
            }
            else if (requestCode==FORCOURTSurvay)
            {
                Log.d("Check", "I am in FORCOURTSurvay");
                setForecourtRating();
            }
            else if(requestCode == IMAGE_CAMERA)
            {
                selectedImagePath = "";
                photoCaptureTime = "";
                Log.d("Check", "I am in IMAGE_CAMERA");
                selectedImagePath = captureImagePath.getPath();
                photoCaptureTime = DateFormat.getDateTimeInstance().format(new Date());
                Log.d("Check", selectedImagePath+" / "+photoCaptureTime);
                checkReadStoragePermissions();
            }
            else if (requestCode == IMAGE_GALLERY)
            {
                selectedImagePath = "";
                photoCaptureTime = "";
                Log.d("Check", "I am in IMAGE_GALLERY");
                Uri selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);
                File selectedFile = new File(selectedImagePath);
                Date date = new Date(selectedFile.lastModified());
                photoCaptureTime = new SimpleDateFormat("dd-MM-yyyy").format(date);
                photoCaptureTime = photoCaptureTime + " " + new SimpleDateFormat("h:mm a").format(date);
                Log.d("Check", selectedImagePath+" / "+photoCaptureTime);
                checkReadStoragePermissions();
            }
        }
        else if (resultCode==RESULT_CANCELED)
        {
            Log.d("Check", "I am in RESULT_CANCELED");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void takePictureFromCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "PSOFuel" + String.valueOf(System.currentTimeMillis())+ ".jpg");
        captureImagePath = Uri.fromFile(file);
        Log.d("Checkcapture", captureImagePath.toString());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImagePath);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, IMAGE_CAMERA);
        }
    }

    private void selectPictureFromGallery()
    {
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK);
        selectPictureIntent.setType("image/*");
        startActivityForResult(selectPictureIntent, IMAGE_GALLERY);
    }

    private String mCurrentPhotoPath;
    private File createImageFile()
    {

        File storageDir = Environment.getExternalStorageDirectory();
        File image = null;
        try {
            image = File.createTempFile(
                    "PSOFuel",  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String image=Base64.encodeToString(b, Base64.DEFAULT);
        return image;
    }

    private void setImages()
    {
        inspectionsModel = appSession.getInspectionModel();
        imagesAdapter = new ImagesAdapter(context, inspectionsModel.getStations().get(stationPosition).getImages(), onItemClickCallback);
        recycler_view.setAdapter(imagesAdapter);

        GPSTrackerNew gpsTracker = new GPSTrackerNew(context);
        Bitmap bm = null;
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            Log.d("Checkselect", selectedImagePath+" ;");
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            tempLayout.setVisibility(View.VISIBLE);

            if (bm!=null)
            {
                img12.setImageBitmap(bm);
                strlatitude = "" + new DecimalFormat("###.0####").format(gpsTracker.getLatitude());
                strlongitude = "" + new DecimalFormat("###.0####").format(gpsTracker.getLongitude());
                tv12.setText(strlatitude + "  " + strlongitude + "  " + "\n" + "  " + station_Name + "  " + "\n" + "  " + this.photoCaptureTime);

                tv12.setBackgroundColor(Color.parseColor("#60111111"));
                tempLayout.setDrawingCacheEnabled(true);
                tempLayout.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(tempLayout.getDrawingCache());
                selectedImagePath = saveToInternalStorage(b);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Log.i("selectedImagePath","selectedImagePath "+ selectedImagePath);

                if (selectedImagePath !=null)
                {
                    recycler_view.setVisibility(View.VISIBLE);

                    InspectionsModel.Stations.Images image = inspectionsModel.getStations().get(stationPosition). new Images();
                    image.setImageName(selectedImageName);
                    image.setImagePath(selectedImagePath);
//                    image.setImagePath(imageSring);
                    image.setImageText(strlatitude + " " + strlongitude + " " + station_Name + " " + photoCaptureTime);
                    Log.d("Imagetest", imageString);
                    image.setImageString(imageString);

                    Log.d("Picasso", "Here123: "+image.getImageName());
                    inspectionsModel.getStations().get(stationPosition).getImages().add(image);
                    appSession.setInspectionModel(inspectionsModel);
                    imagesAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(LatestNewInspectionActivity.this, "Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LatestNewInspectionActivity.this, "Please try again.",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LatestNewInspectionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        selectedImagePath = null;
        captureImagePath = null;
        photoCaptureTime = null;

        tempLayout.setVisibility(View.GONE);
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

    private String selectedImageName = "";
    private String saveToInternalStorage(Bitmap bitmapImage) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();

        String fname = "Image-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
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
//        selectedImagePath = compressImage(pth);
        selectedImagePath = BitMapToString(bitmapImage);
        selectedImageName = fname;

        return file.getAbsolutePath();
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
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
        {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Fuel/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        return uriSting;

    }

    private void saveCalculation()
    {
//        inspectionsModel = appSession.getInspectionModel();

        final List<ProductsModel.Products> productsList = ProductsORM.getProducts(context);
        for (int i=0; i<productsList.size(); i++)
        {
            for (int j=0; j<inspectionsModel.getStations().get(stationPosition).getCalculation().size(); j++)
            {
                if (j==0)
                {

                }
                else
                {
                    if (inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getProduct().equals(productsList.get(i).getProductsName()) && inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getTitle().equals("A"))
                    {
                        for (int k=0; k<inspectionsModel.getStations().get(stationPosition).getProductCalculations().size(); k++)
                        {
                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(k).getProductName().equals(productsList.get(i).getProductsName()))
                            {
                                Log.d("calculateASave", inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getValues());
                                inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(k).setA(Double.valueOf(inspectionsModel.getStations().get(stationPosition).getCalculation().get(j).getValues()));
                            }

                        }
                    }
                }
            }
        }



    }



    private void setCalculation()
    {
        inspectionsModel = appSession.getInspectionModel();
        inspectionsModel.getStations().get(stationPosition).getCalculation().clear();

        String stationId = inspectionsModel.getStations().get(stationPosition).getStationId();
        final List<InspectionsModel.Stations.Purchase> purchaseList = PurchaseORM.getPurchases(context, stationId);

        final List<ProductsModel.Products> productsList = ProductsORM.getProducts(context);
        for (int j=0; j<productsList.size(); j++)
        {
            Double valueA = 0.0;
            Double valueB = 0.0;
            Double valueC = 0.0;
            Double valueD = 0.0;
            Double valueE = 0.0;
            Double valueF = 0.0;
            Double valueResult = 0.0;
            for (int i=0;i<calDescription.length;i++)
            {

                if (i==0 && j==0)
                {
                    InspectionsModel.Stations.Calculation calculation = inspectionsModel.getStations().get(stationPosition).new Calculation();
                    calculation.setTitle(calTitle[i]);
                    calculation.setDescription(calDescription[i]);
                    calculation.setProduct("Products");
                    calculation.setValues("Values");

                    inspectionsModel.getStations().get(stationPosition).getCalculation().add(calculation);
                }
                if (i>0)
                {
                    InspectionsModel.Stations.Calculation calculation = inspectionsModel.getStations().get(stationPosition).new Calculation();

                    int productPosition = 0;
                    int purchasePosition = 0;
                    for (int pc=0; pc<inspectionsModel.getStations().get(stationPosition).getProductCalculations().size(); pc++)
                    {
                        if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(pc).getProductName().equals(productsList.get(j).getProductsName()))
                        {
                            productPosition = pc;
                            Log.d("productPosition", productPosition+"");
                            break;
                        }

                    }
                    for (int pc=0; pc<purchaseList.size(); pc++)
                    {
                        if (purchaseList.get(pc).getProductName().equals(productsList.get(j).getProductsName()))
                        {
                            purchasePosition = pc;
                            Log.d("purchasePosition", purchasePosition+"");
                            Log.d("purchasePosition", purchaseList.get(pc).getProductName()+"");
                            Log.d("purchasePosition", purchaseList.get(pc).getSapPrevious()+"");
                            break;
                        }
                    }


                    switch (i)
                    {
                        //Header
                        case 0:
                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues("0");
                            break;

                        //calculation for A
                        case 1:
                            Double calculateA = 0.0;
                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getA() == null)
                            {
                                calculateA = 0.0;
                            }
                            else
                            {
                                calculateA = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getA();
                                Log.d("calculateA", calculateA+" / "+inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getA());
                            }

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());

                            for (int calA=0; calA<inspectionsModel.getStations().get(stationPosition).getTanks().size(); calA++)
                            {

                                if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankId())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())
                                        && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())>0)
                                {
                                    Log.d("calculateABefore", calculateA+" / "+inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getA());
                                    calculateA = calculateA + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance());
                                    Log.d("calculateAAfter", calculateA+" / "+inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance());
                                }

                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankId())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())
                                        && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())>0)
                                {
                                    Log.d("calculateABefore", calculateA+" / "+inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getA());
                                    calculateA = calculateA + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance());
                                    Log.d("calculateAAfter", calculateA+" / "+inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance());
                                }

                                //for old flush tank calculation(add calculation of old tank)
                                if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankId())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOldProductType())
                                        && !inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOldProductType().equals(productsList.get(j).getProductsName())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankFlush().equals("1")
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())
                                        && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance())>0)
                                {

                                    calculateA = calculateA + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOpeningBalance());
                                }

                                //for old flush tank calculation(subtract calculation of old tank)
                                if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankId())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOldProductType())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankOldProductType().equals(productsList.get(j).getProductsName())
                                        && !inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankFlush().equals("1")
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankCurrentReading())
                                        && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankCurrentReading())>0)
                                {

                                    calculateA = calculateA - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calA).getTankCurrentReading());
                                }
                            }
                            valueA = calculateA;
//                            inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).setA(calculateA);
                            calculation.setValues(""+calculateA);
                            break;

                        //calculation for B
                        case 2:
                            Double calculateB = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getB() == null)
//                            {
//                                calculateB = 0.0;
//                            }
//                            else
//                            {
//                                calculateB = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getB();
//                            }
                            Log.d("SapPrevious", purchasePosition+"");
                            Log.d("SapPrevious", purchaseList.get(purchasePosition).getProductName()+"");
                            Log.d("SapPrevious", purchaseList.get(purchasePosition).getSapPrevious()+"");
                            calculateB = Double.valueOf(purchaseList.get(purchasePosition).getSapPrevious());
                            valueB = calculateB;

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues(""+calculateB);
                            break;

                        //calculation for C = A+B
                        case 3:
                            Double calculateC = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getC() == null)
//                            {
//                                calculateC = 0.0;
//                            }
//                            else
//                            {
//                                calculateC = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getC();
//
//                            }

                            calculateC = valueA + valueB;
                            valueC = calculateC;

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues(""+calculateC);
                            break;

                        //calculation for D
                        case 4:
                            Double calculateD = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getD() == null)
//                            {
//                                calculateD = 0.0;
//                            }
//                            else
//                            {
//                                calculateD = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getD();
//                            }

                            for (int TankSize=0; TankSize<inspectionsModel.getStations().get(stationPosition).getTanks().size(); TankSize++)
                            {
                                if (inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles()!=null)
                                {
                                    for (int NozzleSize=0; NozzleSize<inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().size(); NozzleSize++)
                                    {
                                        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading()))
                                        {
                                            inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).setNozzlePreviousReading("0");
                                        }
                                        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading()))
                                        {
                                            inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).setNozzleCurrentReading("0");
                                        }
                                        if (inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleDefected().equals("1"))
                                        {
                                            continue;
                                        }

                                        if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading()) == 0)
                                        {
                                            if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                    > Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleOpeningBalance()))
                                            {
                                                calculateD = calculateD +
                                                        (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                                - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleOpeningBalance()));

                                            }
                                            else if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                    < Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleOpeningBalance()))
                                            {
                                                calculateD = calculateD +
                                                        (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleMaximum())
                                                                - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleOpeningBalance())
                                                                + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading()));
                                            }

                                        }
                                        else
                                        {
                                            if (inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleReset().equals("1"))
                                            {
                                                calculateD = calculateD +
                                                        (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleSpecialReading())
                                                                - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading())
                                                                + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading()));
                                            }
                                            else
                                            {
                                                if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                        > Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading()))
                                                {
                                                    calculateD = calculateD +
                                                            (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                                    - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading()));
                                                }
                                                else if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading())
                                                        < Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading()))
                                                {
                                                    calculateD = calculateD +
                                                            (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleMaximum())
                                                                    - Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzlePreviousReading())
                                                                    + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(TankSize).getNozzles().get(NozzleSize).getNozzleCurrentReading()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            valueD = calculateD;
                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues(""+calculateD);
                            break;

                        //calculation for E = C-D
                        case 5:
                            Double calculateE = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getE() == null)
//                            {
//                                calculateE = 0.0;
//                            }
//                            else
//                            {
//                                calculateE = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getE();
//                            }

                            calculateE = valueC - valueD;
                            valueE = calculateE;

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues(""+calculateE);
                            break;

                        //calculation for F
                        case 6:
                            Double calculateF = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getF() == null)
//                            {
//                                calculateF = 0.0;
//                            }
//                            else
//                            {
//                                calculateF = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getF();
//                            }

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());

                            for (int calF=0; calF<inspectionsModel.getStations().get(stationPosition).getTanks().size(); calF++)
                            {
                                if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calF).getTankProductType())
                                        && inspectionsModel.getStations().get(stationPosition).getTanks().get(calF).getTankProductType().equals(productsList.get(j).getProductsName())
                                        && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(calF).getTankCurrentReading())
                                        && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calF).getTankCurrentReading())>0){

                                    calculateF = calculateF + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(calF).getTankCurrentReading());
                                }
                            }

                            valueF = calculateF;
                            calculation.setValues(""+calculateF);
                            break;

                        //calculation for Result (F-E)
                        case 7:
                            Double calculateResult = 0.0;
//                            if (inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getResult() == null)
//                            {
//                                calculateResult = 0.0;
//                            }
//                            else
//                            {
//                                calculateResult = inspectionsModel.getStations().get(stationPosition).getProductCalculations().get(productPosition).getResult();
//                            }

                            calculateResult = valueF - valueE;
                            valueResult = calculateResult;

                            calculation.setTitle(calTitle[i]);
                            calculation.setDescription(calDescription[i]);
                            calculation.setProduct(productsList.get(j).getProductsName());
                            calculation.setValues(""+calculateResult);
                            break;
                    }

                    inspectionsModel.getStations().get(stationPosition).getCalculation().add(calculation);
                }
            }
        }

        calculationAdapter = new LatestCalculationAdapter(context, inspectionsModel.getStations().get(stationPosition).getCalculation());
        recycler_view_calculation.setAdapter(calculationAdapter);

        appSession.setInspectionModel(inspectionsModel);
    }

//    private void setCalculationTable()
//    {
//        inspectionsModel = appSession.getInspectionModel();
//        inspectionsModel.getStations().get(stationPosition).getCalculation().clear();
//
//        for (int i=0;i<calDescription.length;i++){
//            InspectionsModel.Stations.Calculation calculation = inspectionsModel.getStations().get(stationPosition).new Calculation();
//            calculation.setTitle(calTitle[i]);
//            calculation.setDescription(calDescription[i]);
//            calculation.setHsd("");
//            calculation.setPmg("");
//            calculation.setHobc("");
//            switch (i){
//                case 0:
//                    calculation.setHsd("HSD");
//                    calculation.setPmg("PMG");
//                    calculation.setHobc("HOBC");
//                    break;
//                case 1:
//
//                    //calculation for A
//                    //only for HSD
//                    Double aHsdb = 0.0;
////                    aHsdb = inspectionsModel.getStations().get(stationPosition).getA().getHsd().getHsdb();
//
//                    for (int j=0; j<inspectionsModel.getStations().get(stationPosition).getTanks().size(); j++)
//                    {
//                        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HSD")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0)
//                        {
//                            Log.d("Calculate", "I am here");
//                            aHsdb = aHsdb + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//                        }
//
//                        //for old flush tank calculation(add calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType())
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("HSD")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HSD")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0)
//                        {
//
//                            aHsdb = aHsdb + Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//                        }
//
//                        //for old flush tank calculation(subtract calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("HSD")
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HSD")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())>0)
//                        {
//
//                            aHsdb = aHsdb-Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading());
//                        }
//
//
//                    }
//                    Log.d("Calculate", "aHsdb: "+aHsdb);
//                    calculation.setHsd(""+aHsdb);
//
//                    //only for PMG
//                    Double aPmgb = 0.0;
////                    aPmgb = inspectionsModel.getStations().get(stationPosition).getA().getPmg().getPmgb();
//
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("PMG")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0){
//
//                            aPmgb = aPmgb+ Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//
//                        }
//                        //for old flush tank calculation(add calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("PMG")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("PMG")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0){
//
//                            aPmgb=aPmgb+Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//                        }
//
//                        //for old flush tank calculation(subtract calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("PMG")
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("PMG")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity())>0){
//
//                            aPmgb = aPmgb-Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity());
//                        }
//                    }
//                    Log.d("Calculate", "aPmgb: "+aPmgb);
//                    calculation.setPmg(""+aPmgb);
//
//                    //only for HDBC
//                    Double aHobcb = 0.0;
////                    aHobcb = inspectionsModel.getStations().get(stationPosition).getA().getHobc().getHobcb();
//
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HOBC")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0){
//
//                            aHobcb=aHobcb+ Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//
//                        }
//                        //for old flush tank calculation(add calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType())
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("HOBC")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HOBC")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance())>0){
//
//                            aHobcb=aHobcb+Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOpeningBalance());
//                        }
//
//                        //for old flush tank calculation(subtract calculation of old tank)
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankId()) && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankOldProductType().equals("HOBC")
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankFlush().equals("1")
//                                && !inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HOBC")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity())>0){
//
//                            aHobcb = aHobcb-Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankAllQuantity());
//                        }
//                    }
//                    Log.d("Calculate", "aHobcb: "+aHobcb);
//                    calculation.setHobc(""+aHobcb);
//
//                    break;
//                case 2:
//                    //calculation for B
////                    calculation.setHsd(""+inspectionsModel.getStations().get(stationPosition).getB().getHsd().getSapPreviousData());
////                    calculation.setPmg(""+inspectionsModel.getStations().get(stationPosition).getB().getPmg().getSapPreviousData());
////                    calculation.setHobc(""+inspectionsModel.getStations().get(stationPosition).getB().getHobc().getSapPreviousData());
//                    calculation.setHsd(""+0);
//                    calculation.setPmg(""+0);
//                    calculation.setHobc(""+0);
//                    break;
//                case 3:
//                    //calculation for C=A+B
////                    calculation.setHsd(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(1).getHsd())+Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(2).getHsd())+"");
////                    calculation.setPmg(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(1).getPmg())+Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(2).getPmg())+"");
////                    calculation.setHobc(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(1).getHobc())+Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(2).getHobc())+"");
//                    calculation.setHsd(""+0);
//                    calculation.setPmg(""+0);
//                    calculation.setHobc(""+0);
//                    break;
//                case 4:
//                    //calculation for D
//                    //for HSD tanks
//                    Double dHsd=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HSD") && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles()!=null){
//                            for (int k=0;k<inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().size();k++){
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzleCurrentReading("0");
//                                }
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzlePreviousReading("0");
//                                }
//                                if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleDefected().equals("1")){
//                                    //if nozzel are defected then not involve in calculations
//                                    continue;
//                                }
//                                if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())==0){
//                                    if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance()))
//                                    {
//                                        dHsd=dHsd+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance()));
//                                    }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance()))
//                                    {
//                                        dHsd=dHsd+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }
//                                }
//                                else
//                                {
//
//                                    if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleReset().equals("1")){
//                                        //if nozzle will bee reset
//                                        dHsd=dHsd+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleSpecialReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }else {
//                                        if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dHsd=dHsd+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading()));
//                                        }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dHsd=dHsd+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                    +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    calculation.setHsd(dHsd+"");
//
//
//
//                    //for PMG tanks
//                    Double dPmg=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("PMG") && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles()!=null){
//                            for (int k=0;k<inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().size();k++){
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzleCurrentReading("0");
//                                }
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzlePreviousReading("0");
//                                }
//                                if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleDefected().equals("1")){
//                                    //if nozzel are defected then not involve in calculations
//                                    continue;
//                                }
//                                if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())==0){
//                                    if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())){
//                                        dPmg=dPmg+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance()));
//                                    }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())){
//                                        dPmg=dPmg+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }
//                                }else {
//                                    if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleReset().equals("1")){
//                                        //if nozzle will bee reset
//                                        dPmg=dPmg+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleSpecialReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }else {
//                                        if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dPmg=dPmg+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading()));
//                                        }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dPmg=dPmg+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                    +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//                    calculation.setPmg(dPmg+"");
//
//                    //for PMG tanks
//                    Double dHobc=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HOBC") && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles()!=null){
//                            for (int k=0;k<inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().size();k++){
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzleCurrentReading("0");
//                                }
//                                if (TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                    inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).setNozzlePreviousReading("0");
//                                }
//                                if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleDefected().equals("1")){
//                                    //if nozzel are defected then not involve in calculations
//                                    continue;
//                                }
//                                if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())==0){
//                                    if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())){
//                                        dHobc=dHobc+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance()));
//                                    }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                            <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())){
//                                        dHobc=dHobc+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleOpeningBalance())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }
//                                }else {
//                                    if (inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleReset().equals("1")){
//                                        //if nozzle will bee reset
//                                        dHobc=dHobc+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleSpecialReading())
//                                                -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                    }else {
//                                        if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                >Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dHobc=dHobc+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading()));
//                                        }else  if (Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading())
//                                                <Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())){
//                                            dHobc=dHobc+(Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleMaximum())
//                                                    -Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzlePreviousReading())
//                                                    +Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getNozzles().get(k).getNozzleCurrentReading()));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    calculation.setHobc(""+dHobc);
//
//                    break;
//                case 5:
//                    //calculation for E=C-D
//
////                    calculation.setHsd(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(3).getHsd())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(4).getHsd())+"");
////                    calculation.setPmg(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(3).getPmg())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(4).getPmg())+"");
////                    calculation.setHobc(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(3).getHobc())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(4).getHobc())+"");
//                    calculation.setHsd(""+0);
//                    calculation.setPmg(""+0);
//                    calculation.setHobc(""+0);
//                    break;
//                case 6:
//                    //calculation for F all current reading
//
//
//                    Double fHsdb=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HSD")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())>0){
//
//                            fHsdb=fHsdb+ Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading());
//                        }
//                    }
//                    calculation.setHsd(""+fHsdb);
//
//
//                    Double fpmg=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("PMG")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())>0){
//
//                            fpmg=fpmg+ Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading());
//                        }
//                    }
//                    calculation.setPmg(""+fpmg);
//
//                    Double fhobc=0.0;
//                    for (int j=0;j<inspectionsModel.getStations().get(stationPosition).getTanks().size();j++){
//                        if (!TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType())
//                                && inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankProductType().equals("HOBC")
//                                && !TextUtils.isEmpty(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())
//                                && Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading())>0){
//
//                            fhobc=fhobc+ Double.parseDouble(inspectionsModel.getStations().get(stationPosition).getTanks().get(j).getTankCurrentReading());
//                        }
//                    }
//                    calculation.setHobc(""+fhobc);
//                    break;
//
//                case 7:
//                    //calculation for F-E
////                    calculation.setHsd(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(6).getHsd())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(5).getHsd())+"");
////                    calculation.setPmg(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(6).getPmg())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(5).getPmg())+"");
////                    calculation.setHobc(Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(6).getHobc())-Double.parseDouble( inspectionsModel.getStations().get(stationPosition).getCalculation().get(5).getHobc())+"");
//                    calculation.setHsd(""+0);
//                    calculation.setPmg(""+0);
//                    calculation.setHobc(""+0);
//                    break;
//
//            }
//            inspectionsModel.getStations().get(stationPosition).getCalculation().add(calculation);
//        }
//
//        calculationAdapter = new LatestCalculationAdapter(context, inspectionsModel.getStations().get(stationPosition).getCalculation());
//        recycler_view_calculation.setAdapter(calculationAdapter);
//    }

    public void SubmitInspectionsToServer(final Context context, final InspectionsModel.Stations inspection, String userName, String userCode)
    {
//        InspectionsModel.Stations inspection = inspectionIncoming;

//        inspectionsModel = appSession.getInspectionModel();

        gpsTracker = new GPSTrackerNew(context);

        JSONObject jsonObject = new JSONObject();
        Log.d("stationcheck", inspection.getStationName().toString()+"/"+stationPosition);
        try
        {
            jsonObject.put("station_id", inspection.getStationId().toString());
            jsonObject.put("station_name", inspection.getStationName().toString());
            jsonObject.put("hse_data", inspection.getHseData().toString());
            jsonObject.put("hse_rating", inspection.getHseRating().toString());
            jsonObject.put("hse_questions", inspection.getHseQuestions().toString());
            jsonObject.put("hse_result", hseSurveyResult);
            jsonObject.put("forecourt_data", inspection.getForecourtData().toString());
            jsonObject.put("forecourt_rating", inspection.getForecourtRating().toString());
            jsonObject.put("forecourt_questions", inspection.getForecourtQuestions().toString());
            jsonObject.put("forecourt_result", forecourtSurveyResult);
            jsonObject.put("purpose", inspection.getPurpose().toString());
            jsonObject.put("comments", inspection.getComments().toString());
            jsonObject.put("wow_training", inspection.getWowTraining().toString());
            jsonObject.put("inspection_date", inspection.getInspectionDate().toString());
            jsonObject.put("inspection_time", inspection.getInspectionTime().toString());
            jsonObject.put("inspection_user", userName);
            jsonObject.put("user_code", userCode);

            JSONArray jsonArrayTanks = new JSONArray();
            for (int i=0; i<inspection.getTanks().size(); i++)
            {
//                if (inspection.getTanks().get(i).getTankFlush().equals(null) || inspection.getTanks().get(i).getTankFlush().equals(""))
//                {
//                    inspection.getTanks().get(i).setTankFlush("");
//                }

                JSONObject jsonObjectTank = new JSONObject();
                jsonObjectTank.put("tank_id", inspection.getTanks().get(i).getTankId());
                jsonObjectTank.put("tank_name", inspection.getTanks().get(i).getTankName());
                jsonObjectTank.put("tank_code", inspection.getTanks().get(i).getTankCode());
                jsonObjectTank.put("tank_openingBalance", inspection.getTanks().get(i).getTankOpeningBalance());
                jsonObjectTank.put("tank_maximum", inspection.getTanks().get(i).getTankMaximum());
                jsonObjectTank.put("tank_currentReading", inspection.getTanks().get(i).getTankCurrentReading());
                jsonObjectTank.put("tank_producttype", inspection.getTanks().get(i).getTankProductType());
                jsonObjectTank.put("tank_oldproducttype", inspection.getTanks().get(i).getTankOldProductType());
                jsonObjectTank.put("tank_flush", inspection.getTanks().get(i).getTankFlush());
                jsonObjectTank.put("tank_remarks", inspection.getTanks().get(i).getTankRemarks());
                jsonObjectTank.put("tank_remarks_date", inspection.getTanks().get(i).getTankRemarksDate());
                jsonObjectTank.put("tank_newlyCreated", 0);
                jsonObjectTank.put("tank_creationDate", inspection.getTanks().get(i).getTankCreationDate());
                jsonObjectTank.put("tank_createdBy", inspection.getTanks().get(i).getTankCreatedBy());
                jsonObjectTank.put("tank_updationDate", inspection.getTanks().get(i).getTankUpdationDate());
                jsonObjectTank.put("tank_updatedBy", inspection.getTanks().get(i).getTankUpdatedBy());

//                Log.d("Test", i+" / "+inspection.getTanks().get(i).getTankPreviousReading()+" / "+inspection.getTanks().get(i).getTankCurrentReading());

                JSONArray jsonArrayNozzles = new JSONArray();
                for (int j=0; j<inspection.getTanks().get(i).getNozzles().size(); j++)
                {
                    JSONObject jsonObjectNozzle = new JSONObject();
                    Log.d("Nozzleid", j+" / "+inspection.getTanks().get(i).getNozzles().get(j).getNozzleId());
                    Log.d("Nozzleid1", j+" / "+inspection.getTanks().get(i).getNozzles().get(j).getNozzleDefected());
                    Log.d("Nozzleid2", j+" / "+inspection.getTanks().get(i).getNozzles().get(j).getNozzleReset());
                    Log.d("Nozzleid3", j+" / "+inspection.getTanks().get(i).getNozzles().get(j).getNozzleSpecialReading());
                    Log.d("Nozzleid4", j+" / "+inspection.getTanks().get(i).getNozzles().get(j).getNozzleSpecialRemarks());
                    jsonObjectNozzle.put("nozzle_id", inspection.getTanks().get(i).getNozzles().get(j).getNozzleId());
                    jsonObjectNozzle.put("nozzle_name", inspection.getTanks().get(i).getNozzles().get(j).getNozzleName());
                    jsonObjectNozzle.put("nozzle_code", inspection.getTanks().get(i).getNozzles().get(j).getNozzleCode());
                    jsonObjectNozzle.put("nozzle_number", inspection.getTanks().get(i).getNozzles().get(j).getNozzleNumber());
                    jsonObjectNozzle.put("nozzle_openingBalance", inspection.getTanks().get(i).getNozzles().get(j).getNozzleOpeningBalance());
                    jsonObjectNozzle.put("nozzle_maximum", inspection.getTanks().get(i).getNozzles().get(j).getNozzleMaximum());
                    jsonObjectNozzle.put("nozzle_currentReading", inspection.getTanks().get(i).getNozzles().get(j).getNozzleCurrentReading());
                    jsonObjectNozzle.put("nozzle_producttype", inspection.getTanks().get(i).getNozzles().get(j).getNozzleProductType());
                    jsonObjectNozzle.put("nozzle_defected", inspection.getTanks().get(i).getNozzles().get(j).getNozzleDefected());
                    jsonObjectNozzle.put("nozzle_reset", inspection.getTanks().get(i).getNozzles().get(j).getNozzleReset());
                    jsonObjectNozzle.put("nozzle_specialReading", inspection.getTanks().get(i).getNozzles().get(j).getNozzleSpecialReading());
                    jsonObjectNozzle.put("nozzle_specialRemarks", inspection.getTanks().get(i).getNozzles().get(j).getNozzleSpecialRemarks());
                    jsonObjectNozzle.put("nozzle_newlyCreated", 0);
                    jsonObjectNozzle.put("nozzle_creationDate", inspection.getTanks().get(i).getNozzles().get(j).getNozzleCreationDate());
                    jsonObjectNozzle.put("nozzle_createdBy", inspection.getTanks().get(i).getNozzles().get(j).getNozzleCreatedBy());
                    jsonObjectNozzle.put("nozzle_updationDate", inspection.getTanks().get(i).getNozzles().get(j).getNozzleUpdationDate());
                    jsonObjectNozzle.put("nozzle_updatedBy", inspection.getTanks().get(i).getNozzles().get(j).getNozzleUpdatedBy());

                    jsonArrayNozzles.put(jsonObjectNozzle);
                }

                jsonObjectTank.put("Nozzles", jsonArrayNozzles);
                jsonArrayTanks.put(jsonObjectTank);
            }

            jsonObject.put("Tanks", jsonArrayTanks);

            JSONArray jsonArrayImages = new JSONArray();
            for (int x=0; x<inspection.getImages().size(); x++)
            {

                JSONObject jsonObjectImage = new JSONObject();
                jsonObjectImage.put("image_name", inspection.getImages().get(x).getImageName());
                jsonObjectImage.put("image_text", inspection.getImages().get(x).getImageText());
                jsonObjectImage.put("image_string", inspection.getImages().get(x).getImageString());
//                jsonObjectImage.put("image_string", "testing");

                jsonArrayImages.put(jsonObjectImage);
            }

            jsonObject.put("Images", jsonArrayImages);

            JSONArray jsonArrayProduct = new JSONArray();

            final List<ProductsModel.Products> productsList = ProductsORM.getProducts(context);
            for (int j=0; j<productsList.size(); j++)
            {
                JSONObject jsonObjectProduct = new JSONObject();
                jsonObjectProduct.put("product_name", productsList.get(j).getProductsName());

                JSONArray jsonArrayCalculation = new JSONArray();

                String jsonA=""; String jsonB=""; String jsonC=""; String jsonD=""; String jsonE=""; String jsonF=""; String jsonResult="";
                for (int y=0; y<inspection.getCalculation().size(); y++)
                {
                    if (inspection.getCalculation().get(y).getProduct().equals(productsList.get(j).getProductsName()))
                    {
                        JSONObject jsonObjectCalculation = new JSONObject();
                        if (inspection.getCalculation().get(y).getTitle().equals("A"))
                        {
//                            jsonObjectCalculation.put("A",inspection.getCalculation().get(y).getValues());
                            jsonA = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("B"))
                        {
//                            jsonObjectCalculation.put("B",inspection.getCalculation().get(y).getValues());
                            jsonB = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("C=A+B"))
                        {
//                            jsonObjectCalculation.put("C",inspection.getCalculation().get(y).getValues());
                            jsonC = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("D"))
                        {
//                            jsonObjectCalculation.put("D",inspection.getCalculation().get(y).getValues());
                            jsonD = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("E=C-D"))
                        {
//                            jsonObjectCalculation.put("E",inspection.getCalculation().get(y).getValues());
                            jsonE = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("F"))
                        {
//                            jsonObjectCalculation.put("F",inspection.getCalculation().get(y).getValues());
                            jsonF = inspection.getCalculation().get(y).getValues();
                        }
                        else if (inspection.getCalculation().get(y).getTitle().equals("F-E"))
                        {
//                            jsonObjectCalculation.put("Result",inspection.getCalculation().get(y).getValues());
                            jsonResult = inspection.getCalculation().get(y).getValues();
                        }

//                        jsonArrayCalculation.put(jsonObjectCalculation);
                    }
                }

                jsonObjectProduct.put("A", jsonA);
                jsonObjectProduct.put("B", jsonB);
                jsonObjectProduct.put("C", jsonC);
                jsonObjectProduct.put("D", jsonD);
                jsonObjectProduct.put("E", jsonE);
                jsonObjectProduct.put("F", jsonF);
                jsonObjectProduct.put("Result", jsonResult);

//                jsonObjectProduct.put("calculation", jsonArrayCalculation);

                jsonArrayProduct.put(jsonObjectProduct);
            }

            jsonObject.put("ProductCalculations", jsonArrayProduct);

            Log.d("Calculation", inspection.getCalculation().size()+"");
//            for (int y=0; y<inspection.getCalculation().size(); y++)
//            {
//
//                JSONObject jsonObjectCalculation = new JSONObject();
//                jsonObjectCalculation.put("calculation_title", inspection.getCalculation().get(y).getTitle());
//                jsonObjectCalculation.put("calculation_description", inspection.getCalculation().get(y).getDescription());
//                jsonObjectCalculation.put("calculation_product", inspection.getCalculation().get(y).getProduct());
//                jsonObjectCalculation.put("calculation_value", inspection.getCalculation().get(y).getValues());
//
//                jsonArrayCalculation.put(jsonObjectCalculation);
//            }
//
//            jsonObject.put("Calculations", jsonArrayCalculation);
        }
        catch (JSONException js)
        {
            js.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);
        String urlInspection = ConstantLib.BASE_URL+""+SUBMIT_USER_INSPECTIONS;
//        String urlInspection = "http://203.215.173.200/pso_files/setuserinspectionsdata.php";
        Log.d("Response", "Url: "+urlInspection);

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, urlInspection, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("Response", "response: "+response.toString());

                        try
                        {
                            String getResponse = response.getString("Response");

                            if (getResponse != null)
                            {

                                if (getResponse.equals("Submitted"))
                                {
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Inspection Submitted Successfully", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

//                                  Clear Images Cache
                                    inspectionsModel.getStations().get(stationPosition).setImages(null);
                                    appSession.setInspectionModel(inspectionsModel);

//                                  Clear Inspection Draft Cache
                                    inspectionsModeDraft = null;
                                    hseDraftData = "";
                                    forecourtDraftData = "";
                                    appSession.setInspectionModelDraft(inspectionsModeDraft);

//                                  Submit user location when submitting inspection
//                                  userLatitude = gpsTracker.getLatitude();
//                                  userLongitude = gpsTracker.getLongitude();
//                                  addressTask = new AddressTask();
//                                  addressTask.execute();

                                    Intent intent = new Intent(LatestNewInspectionActivity.this, NewDashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Response", "Error: "+error);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                    }
                });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);

        // Add the request to the RequestQueue.
        requestQueue.add(jsObjRequest);

        if (progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(LatestNewInspectionActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Submitting Inspections to Server");
        progressDialog.show();
    }

    public class AddressTask extends AsyncTask<Void, Void, GeoAddress>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(LatestNewInspectionActivity.this);
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
                String userName = appSession.getUserName();
                String userPassword = appSession.getPassword();
                submitUserLocation(userLatitude, userLongitude, userAddress, userName, userPassword);
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

    public void submitUserLocation(Double userLatitude, Double userLongitude, String userAddress, String userName, String userPassword)
    {
        Double longitude = userLongitude;
        Double latitude = userLatitude;
        String address = userAddress;
        String username = userName;
        String password = userPassword;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConstantLib.BASE_URL+""+USER_LOCATION+"longitude="+longitude+"&latitude="+latitude+"&address="+address+"&username="+username+"&password="+password;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
