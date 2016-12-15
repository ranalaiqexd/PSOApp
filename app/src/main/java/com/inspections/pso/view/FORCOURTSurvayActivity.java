package com.inspections.pso.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inspections.pso.R;
import com.inspections.pso.adapter.HSESurvayAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.SurveyDTO;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by mobiweb on 9/3/16.
 */
public class FORCOURTSurvayActivity extends AppCompatActivity implements  View.OnClickListener {

    RecyclerView recycler_view;
    Context context;
    private Toolbar toolBar;
    private Button btnSbmt;
    com.inspections.pso.adapter.HSESurvayAdapter HSESurvayAdapter;
    LinearLayoutManager layoutManager;
    private String[] forcourtSurvey,forcourtSurveyKey;
    ArrayList<SurveyDTO> surveyDTOs;
    AppSession appSession=null;
    InspectionDTO inspectionDTO=null;
    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecourt_survay);
        context = this;
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }
        appSession = new AppSession(context);
        //get current inspection from Preferences
        inspectionDTO=appSession.getInspectionDTO();

        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }


        //if current inspection are empty then create a new inspection
        if (inspectionDTO==null){
            inspectionDTO=new InspectionDTO();
        }
        forcourtSurvey=getResources().getStringArray(R.array.for_court_survey);
        forcourtSurveyKey=getResources().getStringArray(R.array.for_court_survey_key);
        surveyDTOs=new ArrayList<>();

        FORCOURTSurvayDATA=inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().getFORCOURTSurvayDATA();
        String[] FORCOURTData=null;
        if (!TextUtils.isEmpty(FORCOURTSurvayDATA)){
            FORCOURTData=FORCOURTSurvayDATA.split(",");
        }

        for (int i=0;i<forcourtSurvey.length;i++){
            SurveyDTO dto=new SurveyDTO();
            dto.setKey(forcourtSurveyKey[i]);
            dto.setMessage(forcourtSurvey[i]);
            dto.setStatus("-1");
            if (FORCOURTData!=null && FORCOURTData.length>i){
                dto.setStatus(FORCOURTData[i]);
            }
            surveyDTOs.add(dto);
        }

        //add item for comment
        SurveyDTO dto=new SurveyDTO();
        dto.setKey("");
        dto.setMessage("");
        dto.setStatus("");
        if (FORCOURTData!=null){
            dto.setMessage(FORCOURTData[FORCOURTData.length-1]);
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

        toolBar = (Toolbar) findViewById(R.id.toolBar_rtlOut_inspctn_for);
        btnSbmt = (Button) findViewById(R.id.btnForSbmt);


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

            case R.id.btnForSbmt:
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

    String FORCOURTSurvayDATA="";

    public void rtngCalCulated() {
        FORCOURTSurvayDATA="";
        int mainVl=0;
        for (int i=0;i<surveyDTOs.size();i++){
            if (TextUtils.isEmpty(FORCOURTSurvayDATA)){
                FORCOURTSurvayDATA=surveyDTOs.get(i).getStatus();
            }else {
                if (i!=surveyDTOs.size()-1){
                    FORCOURTSurvayDATA=FORCOURTSurvayDATA+","+surveyDTOs.get(i).getStatus();
                }else {
                    FORCOURTSurvayDATA=FORCOURTSurvayDATA+","+surveyDTOs.get(i).getMessage();
                }
            }
            if (surveyDTOs.get(i).getStatus().equals("1")){
                mainVl++;
            }
        }
        inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setForeCordSurveyRating(mainVl+"");
        inspectionDTO.getStations().get(inspectionPosition).getStationsDetalis().setFORCOURTSurvayDATA(FORCOURTSurvayDATA);
        Intent intent=new Intent();
        appSession.setInspectionDTO(inspectionDTO);
        setResult(RESULT_OK,intent);
        finish();
    }
}
