package com.newinspections.pso.newview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.newinspections.pso.dto.SurveyDTO;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.newadapter.NewHSESurveyAdapter;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;

import java.util.ArrayList;

public class NewForecourtActivity extends AppCompatActivity implements  View.OnClickListener {

    Context context;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    NewHSESurveyAdapter hseSurveyAdapter;

    private Toolbar toolBar;
    private Button btnSbmt;
    ArrayList<SurveyDTO> surveyDTOs;
    AppSession appSession = null;
    InspectionsModel inspectionsModel = null;

    private String[] forecourtSurvey, forecourtSurveyKey;
    private int stationPosition;
    private String stationId;
    private String forecourtData = "";
    private String forecourtDraftData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecourt_survay);

        context = this;
        appSession = new AppSession(context);
        if (getIntent()!=null){
            stationPosition = getIntent().getExtras().getInt("StationPosition");
            stationId = getIntent().getExtras().getString("StationId");
            forecourtDraftData = getIntent().getExtras().getString("forecourtDraft");
            Log.d("StationId",stationId+"");
        }

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);

        inspectionsModel = appSession.getInspectionModel();
        if (inspectionsModel==null)
        {
            inspectionsModel = new InspectionsModel();
        }

        forecourtSurvey=getResources().getStringArray(R.array.for_court_survey);
        forecourtSurveyKey=getResources().getStringArray(R.array.for_court_survey_key);
        surveyDTOs=new ArrayList<>();

        String forecourtData[] = null;
        if (!TextUtils.isEmpty(forecourtDraftData))
        {
            forecourtData = forecourtDraftData.split(",");
        }

        for (int i=0; i<forecourtSurvey.length; i++)
        {
            SurveyDTO dto=new SurveyDTO();
            dto.setKey(forecourtSurveyKey[i]);
            dto.setMessage(forecourtSurvey[i]);
            dto.setStatus("-1");
            if (forecourtData != null && forecourtData.length>i)
            {
                dto.setStatus(forecourtData[i]);
            }
            else
            {
                dto.setStatus("-1");
            }
            surveyDTOs.add(dto);
        }

        SurveyDTO dto=new SurveyDTO();
        dto.setKey("");
        dto.setMessage("");
        dto.setStatus("");
        if (forecourtData != null)
        {
            dto.setMessage(forecourtData[forecourtData.length-1]);
        }
        surveyDTOs.add(dto);

        hseSurveyAdapter = new NewHSESurveyAdapter(context, surveyDTOs);
        recycler_view.setAdapter(hseSurveyAdapter);

        toolBar = (Toolbar) findViewById(R.id.toolBar_rtlOut_inspctn_for);
        btnSbmt = (Button) findViewById(R.id.btnForSbmt);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSbmt.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnForSbmt:
                if (surveyDTOs!=null){
                    for (int i=0;i<surveyDTOs.size()-1;i++){
                        if (surveyDTOs.get(i).getStatus().equals("-1")){
                            Toast.makeText(context, "Please select "+(i+1)+" option", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (TextUtils.isEmpty(surveyDTOs.get(surveyDTOs.size()-1).getMessage())){
                    Toast.makeText(context, "Please Insert Comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculateForecourtRating();
                break;
        }
    }

    private void calculateForecourtRating()
    {
        forecourtData="";
        Log.d("Check", String.valueOf(surveyDTOs.size()));
        int forecourtQuestions = surveyDTOs.size()-1;
        Log.d("Check", String.valueOf(forecourtQuestions));
        int forecourtPoints = 0;
        for (int i=0; i<surveyDTOs.size(); i++)
        {
            if (TextUtils.isEmpty(forecourtData)){
                forecourtData = surveyDTOs.get(i).getStatus();
            }
            else
            {
                if (i!=surveyDTOs.size()-1){
                    forecourtData = forecourtData+","+surveyDTOs.get(i).getStatus();
                }else {
                    forecourtData = forecourtData+","+surveyDTOs.get(i).getMessage();
                }
            }

            if (surveyDTOs.get(i).getStatus().equals("1"))
            {
                forecourtPoints++;
            }
            else if (surveyDTOs.get(i).getStatus().equals("2"))
            {
                forecourtQuestions--;
            }
        }

        Log.d("Check", forecourtPoints+" / "+forecourtQuestions +" / "+forecourtData);
        inspectionsModel.getStations().get(stationPosition).setForecourtData(forecourtData);
        inspectionsModel.getStations().get(stationPosition).setForecourtRating(String.valueOf(forecourtPoints));
        inspectionsModel.getStations().get(stationPosition).setForecourtQuestions(String.valueOf(forecourtQuestions));
        Intent intent=new Intent();
        appSession.setInspectionModel(inspectionsModel);
        setResult(RESULT_OK,intent);
        finish();
    }
}
