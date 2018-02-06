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

public class NewHSESurveyActivity extends AppCompatActivity implements  View.OnClickListener {

    Context context;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    NewHSESurveyAdapter hseSurveyAdapter;

    private Toolbar toolBar;
    private Button btnSbmt;
    ArrayList<SurveyDTO> surveyDTOs;
    AppSession appSession = null;
    InspectionsModel inspectionsModel = null;

    private String[] hseSurvey, hseSurveyKey;
    private int stationPosition;
    private String stationId;
    private String HSEData = "";
    private String hseDraftData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hse_survay);

        context = this;
        appSession = new AppSession(context);
        if (getIntent()!=null){
            stationPosition = getIntent().getExtras().getInt("StationPosition");
            stationId = getIntent().getExtras().getString("StationId");
            hseDraftData = getIntent().getExtras().getString("hseDraft");
//            Log.d("hseDraftData: ",hseDraftData+"");
        }
//        Log.d("hseDraftData3", hseDraftData);
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

        hseSurvey=getResources().getStringArray(R.array.hse_survey);
        hseSurveyKey=getResources().getStringArray(R.array.hse_survey_key);
        surveyDTOs=new ArrayList<>();

        String hseData[] = null;
        if (!TextUtils.isEmpty(hseDraftData))
        {
            hseData = hseDraftData.split(",");
        }

        for (int i=0; i<hseSurvey.length; i++)
        {
            SurveyDTO dto=new SurveyDTO();
            dto.setKey(hseSurveyKey[i]);
            dto.setMessage(hseSurvey[i]);
            if (hseData != null && hseData.length>i)
            {
                dto.setStatus(hseData[i]);
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
        if (hseData != null)
        {
            dto.setMessage(hseData[hseData.length-1]);
        }
        surveyDTOs.add(dto);

        hseSurveyAdapter = new NewHSESurveyAdapter(context, surveyDTOs);
        recycler_view.setAdapter(hseSurveyAdapter);

        toolBar = (Toolbar) findViewById(R.id.toolBar_rtlOut_inspctn);
        btnSbmt = (Button) findViewById(R.id.btnSbmtHse);

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSbmtHse:
                if (surveyDTOs!=null)
                {
                    for (int i=0;i<surveyDTOs.size()-1;i++)
                    {
                        if (surveyDTOs.get(i).getStatus().equals("-1")){
                            Toast.makeText(context, "Please select "+(i+1)+" option", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (TextUtils.isEmpty(surveyDTOs.get(surveyDTOs.size()-1).getMessage()))
                {
                    Toast.makeText(context, "Please Insert Comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculateHSERating();
                break;

        }

    }

    private void calculateHSERating()
    {
        HSEData="";
        Log.d("Check", String.valueOf(surveyDTOs.size()));
        int hseQuestions = surveyDTOs.size()-1;
        Log.d("Check", String.valueOf(hseQuestions));
        int hsePoints = 0;
        for (int i=0; i<surveyDTOs.size(); i++)
        {
            if (TextUtils.isEmpty(HSEData)){
                HSEData = surveyDTOs.get(i).getStatus();
            }
            else
            {
                if (i!=surveyDTOs.size()-1){
                    HSEData = HSEData+","+surveyDTOs.get(i).getStatus();
                }else {
                    HSEData = HSEData+","+surveyDTOs.get(i).getMessage();
                }
            }

            if (surveyDTOs.get(i).getStatus().equals("1"))
            {
                hsePoints++;
            }
            else if (surveyDTOs.get(i).getStatus().equals("2"))
            {
                hseQuestions--;
            }
        }

        Log.d("Check", hsePoints+" / "+hseQuestions +" / "+HSEData);
        inspectionsModel.getStations().get(stationPosition).setHseData(HSEData);
        inspectionsModel.getStations().get(stationPosition).setHseRating(String.valueOf(hsePoints));
        inspectionsModel.getStations().get(stationPosition).setHseQuestions(String.valueOf(hseQuestions));
        Intent intent=new Intent();
        appSession.setInspectionModel(inspectionsModel);
        setResult(RESULT_OK,intent);
        finish();
    }
}
