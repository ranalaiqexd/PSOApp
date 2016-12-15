package com.inspections.pso.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inspections.pso.R;
import com.inspections.pso.adapter.SapMonthAdapter;
import com.inspections.pso.adapter.StationAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.SapDetailDTO;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.Constants;
import com.inspections.pso.utils.ItemOffsetDecoration;
import com.inspections.pso.utils.OnItemClickListener;
import com.inspections.pso.utils.Utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by mobiweb on 18/3/16.
 */
public class SapSalesActivity extends InspectionBaseActivity {


    private Toolbar toolBar;
    LinearLayoutManager layoutManager;
    Spinner sp_product_type,sp_stations;
    RecyclerView recycler_view_sales;
    Button btn_export,btn_mail;
    InspectionDTO inspectionDTO=null;
    private AppSession appSession;
    private  int stationPosition=0;
    SapMonthAdapter sapMonthAdapter;
    TextView tv_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sap_sales);

        appSession = new AppSession(SapSalesActivity.this);
        //initiated inspections
        inspectionDTO=appSession.getInspectionDTO();
        initView();
        getLocation();
        toolBar = (Toolbar) findViewById(R.id.toolBar_getStn);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStations();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }
    private void initView(){
        recycler_view_sales=(RecyclerView) findViewById(R.id.recycler_view_sales);
        layoutManager = new LinearLayoutManager(SapSalesActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(SapSalesActivity.this, R.dimen.item_offset_1);
        recycler_view_sales.addItemDecoration(itemDecoration);
        recycler_view_sales.setLayoutManager(layoutManager);
        recycler_view_sales.setNestedScrollingEnabled(false);
        recycler_view_sales.setVisibility(View.GONE);
        tv_error=(TextView) findViewById(R.id.tv_error);
        sp_stations=(Spinner) findViewById(R.id.sp_stations);
        sp_product_type=(Spinner) findViewById(R.id.sp_product_type);

        setCenterSpinner();



        btn_mail=(Button) findViewById(R.id.btn_mail);
        btn_export=(Button) findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && Build.VERSION.SDK_INT >= 23 && !TextUtils.isEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(SapSalesActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }else {
                    if ( recycler_view_sales.getVisibility()==View.VISIBLE){
                        new ExportDatabaseCSVTask().execute();
                    }else {
                        Toast toast = Toast.makeText(SapSalesActivity.this, "Please select product.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });
        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file!=null) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("image/jpeg");
                    emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } else {
                    Toast toast = Toast.makeText(SapSalesActivity.this, "Please Export file", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        sp_product_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                file=null;
                if (position>0){
                    recycler_view_sales.setVisibility(View.VISIBLE);
                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().size()<6){
                        List<InspectionDTO.Station.Purchase.Total> totals=new ArrayList<InspectionDTO.Station.Purchase.Total>();
                        inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().add(0,totals);
                    }
                    sapMonthAdapter = new SapMonthAdapter(SapSalesActivity.this, inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal(),sp_product_type.getSelectedItem().toString(),onItemClickCallback);
                    recycler_view_sales.setAdapter(sapMonthAdapter);
                }else {
                    recycler_view_sales.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setCenterSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nav_drawer_labels,R.layout.spinner_center_item);
        // set whatever dropdown resource you want
        adapter.setDropDownViewResource(R.layout.spinner_center_item);

        sp_product_type.setAdapter(adapter);
    }
    private void setStations(){

        if (inspectionDTO!=null && inspectionDTO.getStations()!= null && inspectionDTO.getStations().size() > 0) {
            for (int i=0;i< inspectionDTO.getStations().size();i++){
                try {
                    double lat = Double.parseDouble(inspectionDTO.getStations().get(i).getStationsDetalis().getStationLatitude());
                    double longg = Double.parseDouble(inspectionDTO.getStations().get(i).getStationsDetalis().getStationLongitude());
                    float[] distance = new float[2];
                    Location.distanceBetween(latitude,longitude, lat,longg, distance);

                    Log.i("distance[0]",inspectionDTO.getStations().get(i).getStationsDetalis().getStationName()+" distance "+distance[0]);

                    if (distance[0]>500) {
                        inspectionDTO.getStations().remove(i);
                       i--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (inspectionDTO!=null && inspectionDTO.getStations()!= null && inspectionDTO.getStations().size() > 0) {
            tv_error.setVisibility(View.GONE);
            StationAdapter ful = new StationAdapter(SapSalesActivity.this, inspectionDTO.getStations(),latitude, longitude);
            sp_stations.setAdapter(ful);
        } else {
            tv_error.setText("Stations not available in your range.");
            tv_error.setVisibility(View.VISIBLE);
        }

        sp_stations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recycler_view_sales.setVisibility(View.GONE);
                stationPosition =position;
                if (inspectionDTO!=null && inspectionDTO.getStations()!= null && inspectionDTO.getStations().size() > 0) {
                    sp_product_type.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {

            if (position==0)
                return;


            switch (position){
                case 1:
                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst()==null
                            ||inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().size()==0){
                        Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        SapDetailDTO dto=new SapDetailDTO();
                        for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().size();i++){
                            if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())){
                                SapDetailDTO.First first=dto.new First();
                                first.setInvoiceNo(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getInvoiceNo());
                                first.setMonth(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getMonth());
                                first.setQuantity(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getQuantity());
                                first.setProduct(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getProduct());
                                first.setDate(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getDate());
                                first.setTimestamp(inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().get(i).getTimestamp());
                                dto.getFirst().add(first);
                            }
                        }
                        if (dto.getFirst().size()==0){
                            Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        Intent intent1=new Intent(SapSalesActivity.this,SapSalesDetailActivity.class);
                        intent1.putExtra("data",new Gson().toJson(dto));
                        startActivity(intent1);
                    }

                    break;
                case 2:
                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond()==null
                            ||inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().size()==0){
                        Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        SapDetailDTO dto=new SapDetailDTO();
                        for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().size();i++){
                            if (inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())) {
                                SapDetailDTO.First first=dto.new First();
                                first.setInvoiceNo(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getInvoiceNo());
                                first.setMonth(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getMonth());
                                first.setQuantity(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getQuantity());
                                first.setProduct(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getProduct());
                                first.setDate(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getDate());
                                first.setTimestamp(inspectionDTO.getStations().get(stationPosition).getPurchase().getSecond().get(i).getTimestamp());
                                dto.getFirst().add(first);
                            }
                        }
                        if (dto.getFirst().size()==0){
                            Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        Intent intent1=new Intent(SapSalesActivity.this,SapSalesDetailActivity.class);
                        intent1.putExtra("data",new Gson().toJson(dto));
                        startActivity(intent1);
                    }

                    break;
                case 3:
                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getThird()==null
                            ||inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().size()==0){
                        Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        SapDetailDTO dto=new SapDetailDTO();
                        for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().size();i++){
                            if (inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())) {
                                SapDetailDTO.First first=dto.new First();
                                first.setInvoiceNo(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getInvoiceNo());
                                first.setMonth(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getMonth());
                                first.setQuantity(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getQuantity());
                                first.setProduct(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getProduct());
                                first.setDate(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getDate());
                                first.setTimestamp(inspectionDTO.getStations().get(stationPosition).getPurchase().getThird().get(i).getTimestamp());
                                dto.getFirst().add(first);

                            }
                        }
                        if (dto.getFirst().size()==0){
                            Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        Intent intent1=new Intent(SapSalesActivity.this,SapSalesDetailActivity.class);
                        intent1.putExtra("data",new Gson().toJson(dto));
                        startActivity(intent1);
                    }

                    break;
                case 4:
                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth()==null
                            ||inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().size()==0){
                        Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        SapDetailDTO dto=new SapDetailDTO();
                        for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().size();i++){
                            if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())) {
                                SapDetailDTO.First first=dto.new First();
                                first.setInvoiceNo(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getInvoiceNo());
                                first.setMonth(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getMonth());
                                first.setQuantity(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getQuantity());
                                first.setProduct(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getProduct());
                                first.setDate(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getDate());
                                first.setTimestamp(inspectionDTO.getStations().get(stationPosition).getPurchase().getFourth().get(i).getTimestamp());
                                dto.getFirst().add(first);

                            }
                        }
                        if (dto.getFirst().size()==0){
                            Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        Intent intent1=new Intent(SapSalesActivity.this,SapSalesDetailActivity.class);
                        intent1.putExtra("data",new Gson().toJson(dto));
                        startActivity(intent1);
                    }

                    break;
                case 5:

                    if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth()==null
                            ||inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().size()==0){
                        Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        SapDetailDTO dto=new SapDetailDTO();
                            for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getFirst().size();i++){
                                if (inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())) {
                                    SapDetailDTO.First first=dto.new First();
                                    first.setInvoiceNo(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getInvoiceNo());
                                    first.setMonth(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getMonth());
                                    first.setQuantity(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getQuantity());
                                    first.setProduct(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getProduct());
                                    first.setDate(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getDate());
                                    first.setTimestamp(inspectionDTO.getStations().get(stationPosition).getPurchase().getFifth().get(i).getTimestamp());
                                    dto.getFirst().add(first);
                                }
                        }

                        if (dto.getFirst().size()==0){
                            Toast toast = Toast.makeText(SapSalesActivity.this, "No records found.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        Intent intent1=new Intent(SapSalesActivity.this,SapSalesDetailActivity.class);
                        intent1.putExtra("data",new Gson().toJson(dto));
                        startActivity(intent1);
                    }
                    break;
                default:

                    break;
            }


        }
    };

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>{
        private final ProgressDialog dialog = new ProgressDialog(SapSalesActivity.this);
        @Override
        protected void onPreExecute(){
            this.dialog.setMessage("Exporting sap sales report...");
            this.dialog.show();
        }
        protected Boolean doInBackground(final String... args){
            return saveExcelFile();
        }
        protected void onPostExecute(final Boolean success){
            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(SapSalesActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(SapSalesActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    File file=null;
    private boolean saveCSVFile(){
        try
        {

            File folder = new File(Environment.getExternalStorageDirectory() +File.separator + "AbcFuel");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
           // deleteAllFiles(folder);//delete all old files
            if (success) {
                // Do something on success

                file = new File(folder, "FuelAbc"+ String.valueOf(System.currentTimeMillis())+".csv");

                if (file.createNewFile()) {
                    System.out.println("File is created!");
                    System.out.println("myfile.csv " + file.getAbsolutePath());
                } else {
                    System.out.println("File already exists.");
                }

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String arrStr1[] = {"No.","Month","Purchases"};
                csvWrite.writeNext(arrStr1);

                for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().size();i++){
                    List<InspectionDTO.Station.Purchase.Total> item=inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().get(i);
                    if (item!=null){
                        for (int j=0;j<item.size();j++){
                            if (item.get(j).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())){

                                String arrStr[] = {i+"",item.get(j).getMonth(),item.get(j).getQuantity()+""};
                                csvWrite.writeNext(arrStr);
                            }
                        }
                    }
                }
                csvWrite.close();
                return true;
            } else {
                // Do something else on failure
                System.out.println("Do something else on failure .");
            }
        } catch (SQLException sqlEx){
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            return false;
        } catch (IOException e){
            Log.e("MainActivity", e.getMessage(), e);
            return false;
        }
        return false;
    }


    private  boolean saveExcelFile() {

        File folder = new File(Environment.getExternalStorageDirectory() +File.separator + "AbcFuel");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
       // deleteAllFiles(folder);//delete all old files
        if (success) {
            // Do something on success
            file = new File(folder, inspectionDTO.getStations().get(stationPosition).getStationsDetalis().getStationName()+ " SapSales " + String.valueOf(System.currentTimeMillis()) + ".xls");
        }


        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setColor(HSSFColor.WHITE.index);
        cs.setFont(font);



        //Cell style for other row
        CellStyle cs1 = wb.createCellStyle();
        cs1.setFillForegroundColor(HSSFColor.WHITE.index);
        cs1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs1.setAlignment(CellStyle.ALIGN_CENTER);
        cs1.setBorderLeft((short)1);
        cs1.setBorderRight((short)1);
        cs1.setBorderTop((short)1);
        cs1.setBorderBottom((short)1);
        cs1.setTopBorderColor(HSSFColor.BLACK.index);
        cs1.setBottomBorderColor(HSSFColor.BLACK.index);
        cs1.setLeftBorderColor(HSSFColor.BLACK.index);
        cs1.setRightBorderColor(HSSFColor.BLACK.index);
        Font font1 = wb.createFont();
        font1.setColor(HSSFColor.BLACK.index);
        cs1.setFont(font1);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet(sapMonthAdapter.getType());
        sheet1.setColumnWidth(0, (15 * 100));
        sheet1.setColumnWidth(1, (15 * 220));
        sheet1.setColumnWidth(2, (15 * 220));
        // Generate column headings
        Row rowTop = sheet1.createRow(0);

        c = rowTop.createCell(0);
        c.setCellValue("No.");
        c.setCellStyle(cs);


        c = rowTop.createCell(1);
        c.setCellValue("Month");
        c.setCellStyle(cs);

        c = rowTop.createCell(2);
        c.setCellValue("Purchases");
        c.setCellStyle(cs);




        for (int i=0;i<inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().size();i++){
            List<InspectionDTO.Station.Purchase.Total> item=inspectionDTO.getStations().get(stationPosition).getPurchase().getTotal().get(i);
            if (item!=null){
                for (int j=0;j<item.size();j++){
                    if (item.get(j).getProduct().equalsIgnoreCase(sapMonthAdapter.getType())){

                        Row row = sheet1.createRow(i);

                        c = row.createCell(0);
                        c.setCellValue(i+"");
                        c.setCellStyle(cs1);


                        c = row.createCell(1);
                        c.setCellValue(item.get(j).getMonth());
                        c.setCellStyle(cs1);

                        c = row.createCell(2);
                        c.setCellValue(item.get(j).getQuantity()+"");
                        c.setCellStyle(cs1);
                    }
                }
            }
        }

        // Create a path where we will place our List of objects on external storage
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }
    private void deleteAllFiles(File dir){
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
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
}
