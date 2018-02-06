package com.newinspections.pso.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.HSESurvayAdapter;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.dto.SurveyDTO;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by mobiweb on 8/3/16.
 */
public class HSESurvayActivity extends AppCompatActivity implements  View.OnClickListener {

    RecyclerView recycler_view;
    Context context;
    private Toolbar toolBar;
    private Button btnSbmt;
    HSESurvayAdapter HSESurvayAdapter;
    LinearLayoutManager layoutManager;
    private String[] hseSurvey,hseSurveyKey;
    ArrayList<SurveyDTO> surveyDTOs;
    AppSession appSession=null;

    InspectionDTO inspectionDTO=null;
    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hse_survay);
        context = this;
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
            Log.i("inspectionPosition","inspectionPosition "+inspectionPosition+"");
        }
        appSession = new AppSession(context);
        //get current inspection from Preferences
        inspectionDTO=appSession.getInspectionDTO();
        //if current inspection are empty then create a new inspection
        if (inspectionDTO==null){
            inspectionDTO=new InspectionDTO();
        }


        hseSurvey=getResources().getStringArray(R.array.hse_survey);
        hseSurveyKey=getResources().getStringArray(R.array.hse_survey_key);
        surveyDTOs=new ArrayList<>();

        HSESurvayDATA=inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getHSESurvayDATA();
        String[] HSEData=null;
        if (!TextUtils.isEmpty(HSESurvayDATA)){
            HSEData=HSESurvayDATA.split(",");
        }

        for (int i=0;i<hseSurvey.length;i++){
            SurveyDTO dto=new SurveyDTO();
            dto.setKey(hseSurveyKey[i]);
            dto.setMessage(hseSurvey[i]);
            dto.setStatus("-1");
            if (HSEData!=null && HSEData.length>i){
                dto.setStatus(HSEData[i]);
            }
            surveyDTOs.add(dto);
        }

        //add item for comment
        SurveyDTO dto=new SurveyDTO();
        dto.setKey("");
        dto.setMessage("");
        dto.setStatus("");
        if (HSEData!=null){
            dto.setMessage(HSEData[HSEData.length-1]);
        }

        surveyDTOs.add(dto);

        recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        HSESurvayAdapter = new HSESurvayAdapter(context, surveyDTOs);
        recycler_view.setAdapter(HSESurvayAdapter);

        toolBar = (Toolbar) findViewById(R.id.toolBar_rtlOut_inspctn);
        btnSbmt = (Button) findViewById(R.id.btnSbmtHse);


        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSbmt.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();
        return true;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnSbmtHse:
                if (surveyDTOs!=null){
                    for (int i=0;i<surveyDTOs.size()-1;i++){
                        if (surveyDTOs.get(i).getStatus().equals("-1")){
                            Toast.makeText(context, "please select "+(i+1)+" option", Toast.LENGTH_SHORT).show();
                           return;
                        }
                    }
                }
                if (TextUtils.isEmpty(surveyDTOs.get(surveyDTOs.size()-1).getMessage())){
                    Toast.makeText(context, "Please Insert Comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                rtngCalCulated();
                break;
        }
    }

    String HSESurvayDATA="";

    public void rtngCalCulated() {
        HSESurvayDATA="";
        int mainVl=0;
        for (int i=0;i<surveyDTOs.size();i++){
            if (TextUtils.isEmpty(HSESurvayDATA)){
                HSESurvayDATA=surveyDTOs.get(i).getStatus();
            }
            else
            {
                if (i!=surveyDTOs.size()-1){
                    HSESurvayDATA=HSESurvayDATA+","+surveyDTOs.get(i).getStatus();
                }else {
                    HSESurvayDATA=HSESurvayDATA+","+surveyDTOs.get(i).getMessage();
                }
            }
           if (surveyDTOs.get(i).getStatus().equals("1")||surveyDTOs.get(i).getStatus().equals("2")){
               mainVl++;
           }
        }
        inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setHSESurvayDATA(HSESurvayDATA);
        inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setHseSurveyRating( String.valueOf(mainVl));
        Intent intent=new Intent();
        appSession.setInspectionDTO(inspectionDTO);
        setResult(RESULT_OK,intent);
        finish();
    }
}
