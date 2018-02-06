package com.newinspections.pso.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.CalculationAdapter;
import com.newinspections.pso.adapter.MyInspectionImagesAdapter;
import com.newinspections.pso.dto.CalculationDTO;
import com.newinspections.pso.dto.MyInspectionDTO;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.utils.OnItemClickListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by mobiweb on 1/3/16.
 */
public class MyInspectionDetailActivity extends InspectionBaseActivity implements  View.OnClickListener {

    Context context;
    private Toolbar toolBar;
    private TextView tv_time,tv_date,tv_comment,tv_purpose,tv_station,tv_sap_code;
    private Button btnTnkRdng;

    private Button btnHseSurv;
    private Button btnForSurv;
    private  TextView hseTxtRtng;
    private  TextView forcortTxtrtng;

    List<MyInspectionDTO.Response.Tank> tempList =null;

    Button btn_export,btn_mail;
    private AppSession appSession;


    private String[] calTitle,calDescription;

    LinearLayoutManager layoutManager;
    RecyclerView recycler_view,recycler_view_calculation;
    List<String> imagesDTOs=new ArrayList<>();
    MyInspectionImagesAdapter imagesAdapter;
    CalculationAdapter calculationAdapter;

    ArrayList<CalculationDTO> calculationDTOs=null;
    MyInspectionDTO.Response inspectionDTO=null;
//    edited by exd
//    InspectionDTO.Station newInspectionDto = null;

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
//        edited by exd
//        newInspectionDto = appSession.getInspectionDTO().getStations().get(inspectionPosition);

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

//            edited by exd
            String[] InspectionTimestamp = inspectionDTO.getStationsDetalis().getInspectionTimestamp().split(" ");
            tv_date.setText(InspectionTimestamp[0]);
            tv_time.setText(InspectionTimestamp[1]);

//            tv_date.setText(Utils.getDateLogByFormat(inspectionDTO.getStationsDetalis().getInspectionTimestamp()));
//            tv_time.setText(Utils.getTimeLogByFormat(inspectionDTO.getStationsDetalis().getInspectionTimestamp()));
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
                    imagesAdapter = new MyInspectionImagesAdapter(context, imagesDTOs,inspectionDTO.getCommonObject().getPath(),onItemClickCallback);
                    recycler_view.setAdapter(imagesAdapter);
                }
            }
        }

    }
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {


            Intent intent = new Intent(context, FullViewActivity.class);
            intent.putExtra("fileName",inspectionDTO.getCommonObject().getPath()+imagesDTOs.get(position));
            startActivity(intent);
        }
    };
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
            }
            else if (inspectionDTO.getCommonObject().getHseSurvey().contains("(")){
                hseTxtRtng.setText(inspectionDTO.getCommonObject().getHseSurvey());
            }
            else if (Integer.parseInt(inspectionDTO.getCommonObject().getHseSurvey())>10){
                hseTxtRtng.setText("SAFE" + "(" + inspectionDTO.getCommonObject().getHseSurvey()+ "/13)");
            }
            else{
                hseTxtRtng.setText("UNSAFE" + "(" + inspectionDTO.getCommonObject().getHseSurvey()+ "/13)");
            }
        }
    }

    private void setForeCordSurvey(){
        if (inspectionDTO.getCommonObject()!=null){
            if (TextUtils.isEmpty(inspectionDTO.getCommonObject().getForcastSurvey())){
                forcortTxtrtng.setText("N/A");
            }
            else if (inspectionDTO.getCommonObject().getForcastSurvey().contains("(")){
                forcortTxtrtng.setText(inspectionDTO.getCommonObject().getForcastSurvey());
            }
            else if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>9){
                forcortTxtrtng.setText("GOOD" + "(" +inspectionDTO.getCommonObject().getForcastSurvey()+ "/14)");
            }
            else if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>6){
                forcortTxtrtng.setText("AVERAGE" + "(" +inspectionDTO.getCommonObject().getForcastSurvey()+ "/14)");
            }
            else{
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


        btn_mail=(Button) findViewById(R.id.btn_mail);
        btn_export=(Button) findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && Build.VERSION.SDK_INT >= 23 && !TextUtils.isEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(MyInspectionDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }else {
                    new ExportDatabaseCSVTask().execute();
                }
            }
        });
        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file!=null) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    edited by exd
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inspection Report - "
                            +inspectionDTO.getStationsDetalis().getStationName()
                            +" ("+inspectionDTO.getStationsDetalis().getSapCode()+")");
                    emailIntent.setType("image/jpeg");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } else {
                    Toast toast = Toast.makeText(MyInspectionDetailActivity.this, "Please Export file", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(MyInspectionDetailActivity.this);
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
                Toast.makeText(MyInspectionDetailActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(MyInspectionDetailActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    File file=null;

//    edited by exd
    public String parseDate(String time) {
    String inputPattern = "yyyy-MM-dd HH:mm:ss";
    String outputPattern = "EEE, d MMM yyyy";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

    Date date = null;
    String str = null;

    try {
        date = inputFormat.parse(time);
        str = outputFormat.format(date);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return str;
}

    private  boolean saveExcelFile() {

        File folder = new File(Environment.getExternalStorageDirectory() +File.separator + "Fuel");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        // deleteAllFiles(folder);//delete all old files
        if (success) {
            // Do something on success
            String stationName = inspectionDTO.getStationsDetalis().getStationName().trim();
            String stationOutletCode = inspectionDTO.getStationsDetalis().getSapCode();
            String  result = stationName.replaceAll("[^\\p{L}\\p{Z}]","");
//            file = new File(folder, result+ "gainloss" + String.valueOf(System.currentTimeMillis()) + ".xls");
//            edited by exd
            file = new File(folder, stationName + " ("+stationOutletCode+")"+".xls");
        }


        //New Workbook
        Workbook wb = new HSSFWorkbook();
        Cell c = null;

//        edited by exd
//        Cell style for top Station Details
        CellStyle csstation = wb.createCellStyle();
        csstation.setFillForegroundColor(HSSFColor.WHITE.index);
        csstation.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csstation.setAlignment(CellStyle.ALIGN_LEFT);
        csstation.setBorderLeft((short)1);
        csstation.setBorderRight((short)1);
        csstation.setBorderTop((short)1);
        csstation.setBorderBottom((short)1);
        csstation.setTopBorderColor(HSSFColor.BLACK.index);
        csstation.setBottomBorderColor(HSSFColor.BLACK.index);
        csstation.setLeftBorderColor(HSSFColor.BLACK.index);
        csstation.setRightBorderColor(HSSFColor.BLACK.index);
        Font fontstation = wb.createFont();
        fontstation.setColor(HSSFColor.BLACK.index);
        csstation.setFont(fontstation);

//        Cell style for top header row
        CellStyle cshead = wb.createCellStyle();
        cshead.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        cshead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cshead.setAlignment(CellStyle.ALIGN_LEFT);
        cshead.setBorderLeft((short)1);
        cshead.setBorderRight((short)1);
        cshead.setBorderTop((short)1);
        cshead.setBorderBottom((short)1);
        cshead.setTopBorderColor(HSSFColor.BLACK.index);
        cshead.setBottomBorderColor(HSSFColor.BLACK.index);
        cshead.setLeftBorderColor(HSSFColor.BLACK.index);
        cshead.setRightBorderColor(HSSFColor.BLACK.index);
        Font fonthead = wb.createFont();
        fonthead.setColor(HSSFColor.WHITE.index);
        cshead.setFont(fonthead);

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        cs.setBorderLeft((short)1);
        cs.setBorderRight((short)1);
        cs.setBorderTop((short)1);
        cs.setBorderBottom((short)1);
        cs.setTopBorderColor(HSSFColor.BLACK.index);
        cs.setBottomBorderColor(HSSFColor.BLACK.index);
        cs.setLeftBorderColor(HSSFColor.BLACK.index);
        cs.setRightBorderColor(HSSFColor.BLACK.index);
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

//        Cell style for Signature
        CellStyle csSignature = wb.createCellStyle();
        csSignature.setFillForegroundColor(HSSFColor.WHITE.index);
        cs1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csSignature.setAlignment(CellStyle.ALIGN_CENTER);
        csSignature.setFont(font1);

        //New Sheet
//        edited by exd
        Sheet sheet1 = null;
//        sheet1 = wb.createSheet("GainLoss");
        sheet1 = wb.createSheet(inspectionDTO.getStationsDetalis().getStationName()
                + " ("+inspectionDTO.getStationsDetalis().getSapCode()+")");
        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 300));
        sheet1.setColumnWidth(4, (15 * 300));
        sheet1.setColumnWidth(5, (15 * 300));
        sheet1.setColumnWidth(6, (15 * 300));
        sheet1.setColumnWidth(7, (15 * 300));
        sheet1.setColumnWidth(8, (15 * 300));
        sheet1.setColumnWidth(9, (15 * 300));

        Row row;
        int count=0;
        int temp = 0;

        row = sheet1.createRow(count+2);
//        sheet1.addMergedRegion(new CellRangeAddress(count+2,count+2,3,6));
        c = row.createCell(3);
        c.setCellValue("RETAIL OUTLET INSPECTION REPORT");
        CellStyle csheader = wb.createCellStyle();
        csheader.setAlignment(CellStyle.ALIGN_CENTER);
        Font fontheader = wb.createFont();
        fontheader.setFontHeightInPoints((short)(12));
        fontheader.setColor(HSSFColor.BLACK.index);
        fontheader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        csheader.setFont(fontheader);
        c.setCellStyle(csheader);

        row = sheet1.createRow(count+4);
        c = row.createCell(3);
        c.setCellValue("Say No to Corruption");
        CellStyle csCorruption = wb.createCellStyle();
        csCorruption.setAlignment(CellStyle.ALIGN_CENTER);
        Font fontCorruption = wb.createFont();
        fontCorruption.setFontHeightInPoints((short)(10));
        fontCorruption.setColor(HSSFColor.BLACK.index);
        csCorruption.setFont(fontCorruption);
        c.setCellStyle(csCorruption);

//        Inserting Logo to the Workbook sheet
        CellStyle csImage = wb.createCellStyle();
        csImage.setAlignment(CellStyle.ALIGN_CENTER);
        //FileInputStream obtains input bytes from the image file
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pso_fuel_logo_square);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //Adds a picture to the workbook
        int pictureIdx = wb.addPicture(byteArray, Workbook.PICTURE_TYPE_JPEG);

        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = wb.getCreationHelper();
//
        //Creates the top-level drawing patriarch.
        Drawing drawing = sheet1.createDrawingPatriarch();
//        HSSFPatriarch drawing = (HSSFPatriarch) sheet1.createDrawingPatriarch();

        //Create an anchor that is attached to the worksheet
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType( ClientAnchor.MOVE_AND_RESIZE);
        //set top-left corner for the image
        anchor.setCol1(0);
        anchor.setRow1(0);
        anchor.setCol2(1);
        anchor.setRow2(5);

        //Creates a picture
        Picture picture = drawing.createPicture(anchor, pictureIdx);
//
//        short col1 = 1; short col2 = 1;
//        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, col1, 1, col2, 4);
//        anchor.setAnchorType(2);
//        int index = wb.addPicture(byteArray, HSSFWorkbook.PICTURE_TYPE_JPEG);
//        // Create the drawing patriarch. This is the top level container for all shapes.
//        Drawing patriarch = sheet1.createDrawingPatriarch();
//        try {
//            Picture picture = patriarch.createPicture(anchor, index);
//            // picture.resize();
//        } catch (Exception e) {
//            String err = e.getMessage();
//        }
//        picture.resize();
        count = count+5;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Pakistan State Oil");
        CellStyle csPso = wb.createCellStyle();
        csPso.setAlignment(CellStyle.ALIGN_CENTER);
        Font fontPso = wb.createFont();
        fontPso.setColor(HSSFColor.BLACK.index);
        fontPso.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        csPso.setFont(fontPso);
        c.setCellStyle(csPso);
        count++;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        count++;

        String currentDate = parseDate(inspectionDTO.getStationsDetalis().getInspectionTimestamp());
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,1));
        c = row.createCell(0);
        c.setCellValue("Date: "+currentDate);
        c.setCellStyle(csstation);
        count++;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,1));
        c = row.createCell(0);
        c.setCellValue("Division: "+inspectionDTO.getStationsDetalis().getDivisionName());
        c.setCellStyle(csstation);
        count++;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,1));
        c = row.createCell(0);
        c.setCellValue("Sales Area: "+inspectionDTO.getStationsDetalis().getSalesArea());
        c.setCellStyle(csstation);
        count++;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,1));
        c = row.createCell(0);
        c.setCellValue("Outlet Name: "+inspectionDTO.getStationsDetalis().getStationName().toUpperCase());
        c.setCellStyle(csstation);
        count++;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,1));
        c = row.createCell(0);
        c.setCellValue("Outlet Code: "+inspectionDTO.getStationsDetalis().getSapCode());
        c.setCellStyle(csstation);
        count++;

        count=count+2;
        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Survey Name");
        c.setCellStyle(cshead);
        c = row.createCell(1);
        c.setCellValue("Maximum");
        c.setCellStyle(cshead);
        c = row.createCell(2);
        c.setCellValue("Score");
        c.setCellStyle(cshead);
        c = row.createCell(3);
        c.setCellValue("%");
        c.setCellStyle(cshead);
        c = row.createCell(4);
        c.setCellValue("Survey Rating");
        c.setCellStyle(cshead);
        count++;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("HSE Survey");
        c.setCellStyle(cs);
        c = row.createCell(1);
        String HSEMaximum = "13";
        c.setCellValue(HSEMaximum);
        c.setCellStyle(cs1);
        c = row.createCell(2);
        String HSEScore = inspectionDTO.getCommonObject().getHseSurvey()+"/13";
        c.setCellValue(HSEScore);
        c.setCellStyle(cs1);
        c = row.createCell(3);
        double HSEpercentage = ((Double.parseDouble(inspectionDTO.getCommonObject().getHseSurvey()))/13)*100;
        Log.d("exd", (Double.parseDouble(inspectionDTO.getCommonObject().getHseSurvey()))+"/"+String.valueOf(HSEpercentage));
        int roundHSEpercentage = (int) Math.round(HSEpercentage);
        String HSEPercent = String.valueOf(roundHSEpercentage)+"%";
        c.setCellValue(HSEPercent);
        c.setCellStyle(cs1);
        c = row.createCell(4);
        String HSERating = "";
        if (Integer.parseInt(inspectionDTO.getCommonObject().getHseSurvey())>10)
        {
            HSERating = "SAFE";
        }
        else
        {
            HSERating = "UNSAFE";
        }
        c.setCellValue(HSERating);
        c.setCellStyle(cs1);
        count++;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("FORECOURT Survey");
        c.setCellStyle(cs);
        c = row.createCell(1);
        String FCMaximum = "14";
        c.setCellValue(FCMaximum);
        c.setCellStyle(cs1);
        c = row.createCell(2);
        String FCScore = inspectionDTO.getCommonObject().getForcastSurvey()+"/14";
        c.setCellValue(FCScore);
        c.setCellStyle(cs1);
        c = row.createCell(3);
        double FCpercentage = ((Double.parseDouble(inspectionDTO.getCommonObject().getForcastSurvey()))/14)*100;
        Log.d("exd", (Double.parseDouble(inspectionDTO.getCommonObject().getForcastSurvey()))+"/"+String.valueOf(FCpercentage));
        int roundFCpercentage = (int) Math.round(FCpercentage);
        String FCPercent = String.valueOf(roundFCpercentage)+"%";
        c.setCellValue(FCPercent);
        c.setCellStyle(cs1);
        c = row.createCell(4);
        String FCRating = "";
        if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>9)
        {
            FCRating = "GOOD";
        }
        else if (Integer.parseInt(inspectionDTO.getCommonObject().getForcastSurvey())>6)
        {
            FCRating = "AVERAGE";
        }
        else
        {
            FCRating = "BELOW AVERAGE";
        }
        c.setCellValue(FCRating);
        c.setCellStyle(cs1);
        count++;

        count=count+2;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,4));
        c = row.createCell(0);
        c.setCellValue("Comments");
        c.setCellStyle(cshead);
        count++;
        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Purpose");
        c.setCellStyle(cs);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,1,4));
        c = row.createCell(1);
        String commentPurpose = inspectionDTO.getCommonObject().getPurpose();
        c.setCellValue(commentPurpose);
        c.setCellStyle(cs1);
        count++;
        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Comment");
        c.setCellStyle(cs);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,1,4));
        c = row.createCell(1);
        String commentComment = inspectionDTO.getCommonObject().getComment();
        c.setCellValue(commentComment);
        c.setCellStyle(cs1);
        count++;

        count=count+2;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,7));
        c = row.createCell(0);
        c.setCellValue("TANK DIP READING");
        c.setCellStyle(cshead);
        count++;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Product");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("Tank");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("Previous Date");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("Previous Reading");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("Current Date");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("Current Reading");
        c.setCellStyle(cs);
        c = row.createCell(6);
        c.setCellValue("Tank Flush");
        c.setCellStyle(cs);
        c = row.createCell(7);
        c.setCellValue("Flush Comment");
        c.setCellStyle(cs);
        count++;

        temp = count;
        for (int i=0; i<inspectionDTO.getTanks().size(); i++) {

            row = sheet1.createRow(i + count);

            String tankProduct = inspectionDTO.getTanks().get(i).getTankType();
            c = row.createCell(0);
            c.setCellValue(tankProduct);
            c.setCellStyle(cs1);
            String tankName = inspectionDTO.getTanks().get(i).getTankName();
            c = row.createCell(1);
            c.setCellValue(tankName);
            c.setCellStyle(cs1);
            String tankPreviousDate = "";
            if(inspectionDTO.getStationsDetalis().getPreviousTimestamp().equals("empty"))
            {
                tankPreviousDate = "";
            }
            else
            {
                String tankPreviousTimezone[] = inspectionDTO.getStationsDetalis().getPreviousTimestamp().split(" ");
                tankPreviousDate = tankPreviousTimezone[0];
            }
            c = row.createCell(2);
            c.setCellValue(tankPreviousDate);
            c.setCellStyle(cs1);
            String tankPrevious = inspectionDTO.getTanks().get(i).getPreviousBalance();
            c = row.createCell(3);
            c.setCellValue(tankPrevious);
            c.setCellStyle(cs1);
            String tankCurrentDate = "";
            if(inspectionDTO.getStationsDetalis().getInspectionTimestamp().equals("empty"))
            {
                tankCurrentDate = "";
            }
            else
            {
                String tankCurrentTimezone[] = inspectionDTO.getStationsDetalis().getInspectionTimestamp().split(" ");
                tankCurrentDate = tankCurrentTimezone[0];
            }
            c = row.createCell(4);
            c.setCellValue(tankCurrentDate);
            c.setCellStyle(cs1);
            String tankCurrent = inspectionDTO.getTanks().get(i).getTankAllquantity();
            c = row.createCell(5);
            c.setCellValue(tankCurrent);
            c.setCellStyle(cs1);
            String tankFlushValue = inspectionDTO.getTanks().get(i).getTankFlush();
            String tankFlush;
            if(tankFlushValue.equals("1"))
            {
                String remarkDate = inspectionDTO.getTanks().get(i).getRemarkDate();
                tankFlush = "Yes"+"("+remarkDate+")";
            }
            else
            {
                tankFlush = "";
            }
            c = row.createCell(6);
            c.setCellValue(tankFlush);
            c.setCellStyle(cs1);
            String tankRemark = inspectionDTO.getTanks().get(i).getRemark();
            c = row.createCell(7);
            c.setCellValue(tankRemark);
            c.setCellStyle(cs1);
            temp++;
        }
        count = temp;

        count=count+2;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,9));
        c = row.createCell(0);
        c.setCellValue("DU METER READING");
        c.setCellStyle(cshead);
        count++;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Product");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("Tank");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("Nozzle");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("Previous Date");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("Previous Reading");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("Current Date");
        c.setCellStyle(cs);
        c = row.createCell(6);
        c.setCellValue("Current Reading");
        c.setCellStyle(cs);
        c = row.createCell(7);
        c.setCellValue("Nozzle Defected");
        c.setCellStyle(cs);
        c = row.createCell(8);
        c.setCellValue("Special Reading Reset");
        c.setCellStyle(cs);
        c = row.createCell(9);
        c.setCellValue("Comments Reset");
        c.setCellStyle(cs);
        count++;

        temp = count;
        for (int i=0; i<inspectionDTO.getTanks().size(); i++)
        {
            for (int j=0; j<inspectionDTO.getTanks().get(i).getNozzle().size(); j++)
            {
                row = sheet1.createRow(temp);
                String nozzleProduct = inspectionDTO.getTanks().get(i).getNozzle().get(j).getDumeterProduct();
                c = row.createCell(0);
                c.setCellValue(nozzleProduct);
                c.setCellStyle(cs1);
                String nozzleTank = inspectionDTO.getTanks().get(i).getTankName();
                c = row.createCell(1);
                c.setCellValue(nozzleTank);
                c.setCellStyle(cs1);
                String nozzleName = inspectionDTO.getTanks().get(i).getNozzle().get(j).getDumeterName();
                c = row.createCell(2);
                c.setCellValue(nozzleName);
                c.setCellStyle(cs1);
                String nozzlePreviousDate="";
                if(inspectionDTO.getStationsDetalis().getPreviousTimestamp().equals("empty"))
                {
                    nozzlePreviousDate = "";
                }
                else
                {
                    String tankPreviousTimezone[] = inspectionDTO.getStationsDetalis().getPreviousTimestamp().split(" ");
                    nozzlePreviousDate = tankPreviousTimezone[0];
                }
                c = row.createCell(3);
                c.setCellValue(nozzlePreviousDate);
                c.setCellStyle(cs1);
                String nozzlePrevious = inspectionDTO.getTanks().get(i).getNozzle().get(j).getDumeterPreviousVolume();
                c = row.createCell(4);
                c.setCellValue(nozzlePrevious);
                c.setCellStyle(cs1);
                String nozzleCurrentDate = "";
                if(inspectionDTO.getStationsDetalis().getInspectionTimestamp().equals("empty"))
                {
                    nozzleCurrentDate = "";
                }
                else
                {
                    String nozzleCurrentTimezone[] = inspectionDTO.getStationsDetalis().getInspectionTimestamp().split(" ");
                    nozzleCurrentDate = nozzleCurrentTimezone[0];
                }
                c = row.createCell(5);
                c.setCellValue(nozzleCurrentDate);
                c.setCellStyle(cs1);
                String nozzleCurrent = inspectionDTO.getTanks().get(i).getNozzle().get(j).getDumeterAllquantity();
                c = row.createCell(6);
                c.setCellValue(nozzleCurrent);
                c.setCellStyle(cs1);
                String nozzleDefectedValue = inspectionDTO.getTanks().get(i).getNozzle().get(j).getDumeterDefect();
                String nozzleDefected;
                if(nozzleDefectedValue.equals("1"))
                {
                    nozzleDefected = "Defected";
                }
                else
                {
                    nozzleDefected = "";
                }
                c = row.createCell(7);
                c.setCellValue(nozzleDefected);
                c.setCellStyle(cs1);
                String nozzleSpecial = inspectionDTO.getTanks().get(i).getNozzle().get(j).getSpecialReadingReset();
                c = row.createCell(8);
                c.setCellValue(nozzleSpecial);
                c.setCellStyle(cs1);
                String nozzleComment = inspectionDTO.getTanks().get(i).getNozzle().get(j).getCommentsReset();
                c = row.createCell(9);
                c.setCellValue(nozzleComment);
                c.setCellStyle(cs1);
                temp++;
            }
        }
        count = temp;

        count=count+2;
        row = sheet1.createRow(count);
        sheet1.addMergedRegion(new CellRangeAddress(count,count,0,4));
        c = row.createCell(0);
        c.setCellValue("STOCK VARIATION");
        c.setCellStyle(cshead);
        count++;

        temp = count;
        if (calculationDTOs!=null){
            for (int i=0;i<calculationDTOs.size();i++){

                row = sheet1.createRow(i+count);

                c = row.createCell(0);
                c.setCellValue(calculationDTOs.get(i).getTitle());
                if (i==0)
                    c.setCellStyle(cs);
                else
                    c.setCellStyle(cs1);

                c = row.createCell(1);
                c.setCellValue(calculationDTOs.get(i).getDescription());
                if (i==0)
                    c.setCellStyle(cs);
                else
                    c.setCellStyle(cs1);

                c = row.createCell(2);
                c.setCellValue(calculationDTOs.get(i).getHsd());
                if (i==0)
                    c.setCellStyle(cs);
                else
                    c.setCellStyle(cs1);

                c = row.createCell(3);
                c.setCellValue(calculationDTOs.get(i).getPmg());
                if (i==0)
                    c.setCellStyle(cs);
                else
                    c.setCellStyle(cs1);

                c = row.createCell(4);
                c.setCellValue(calculationDTOs.get(i).getHobc());
                if (i==0)
                    c.setCellStyle(cs);
                else
                    c.setCellStyle(cs1);
                temp++;
            }
        }
        count=temp;
        count=count+4;

        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Area Incharge");
        c.setCellStyle(csSignature);
        c = row.createCell(3);
        c.setCellValue("Retail Outlet Dealer/Manager");
        c.setCellStyle(csSignature);
        count = count+2;

        row = sheet1.createRow(count);
        HSSFPatriarch patriarch = (HSSFPatriarch) sheet1.createDrawingPatriarch();
        HSSFClientAnchor anchorLineOne = new HSSFClientAnchor(0, 255, 0, 255, (short) 0, count,(short) 1, count);
        HSSFSimpleShape shapeLineOne = patriarch.createSimpleShape(anchorLineOne);
        shapeLineOne.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shapeLineOne.setLineStyleColor(10, 10, 10);
        shapeLineOne.setFillColor(90, 10, 200);
        shapeLineOne.setLineWidth(HSSFShape.LINEWIDTH_ONE_PT);
        shapeLineOne.setLineStyle(HSSFShape.LINESTYLE_SOLID);
        HSSFClientAnchor anchorLineTwo = new HSSFClientAnchor(0, 255, 0, 255, (short) 3, count,(short) 4, count);
        HSSFSimpleShape shapeLineTwo = patriarch.createSimpleShape(anchorLineTwo);
        shapeLineTwo.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shapeLineTwo.setLineStyleColor(10, 10, 10);
        shapeLineTwo.setFillColor(90, 10, 200);
        shapeLineTwo.setLineWidth(HSSFShape.LINEWIDTH_ONE_PT);
        shapeLineTwo.setLineStyle(HSSFShape.LINESTYLE_SOLID);
        count++;
        row = sheet1.createRow(count);
        c = row.createCell(0);
        c.setCellValue("Sign and Stamp");
        c.setCellStyle(csSignature);
        c = row.createCell(3);
        c.setCellValue("Sign and Stamp");
        c.setCellStyle(csSignature);

//        For empty rows at the end of sheet
        row = sheet1.createRow(count+3);

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
