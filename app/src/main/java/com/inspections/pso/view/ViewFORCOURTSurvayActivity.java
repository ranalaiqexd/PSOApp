package com.inspections.pso.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.inspections.pso.R;
import com.inspections.pso.adapter.ViewHSESurvayAdapter;
import com.inspections.pso.dto.MyInspectionDTO;
import com.inspections.pso.dto.SurveyDTO;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by mobiweb on 8/3/16.
 */
public class ViewFORCOURTSurvayActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    Context context;
    private Toolbar toolBar;
    ViewHSESurvayAdapter HSESurvayAdapter;
    LinearLayoutManager layoutManager;
    private String[] forCourtSurvey;
    ArrayList<SurveyDTO> surveyDTOs;
    AppSession appSession=null;

    MyInspectionDTO inspectionDTO=null;
    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_forecourt_survay);
        context = this;
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
            Log.i("inspectionPosition","inspectionPosition "+inspectionPosition+"");
        }
        appSession = new AppSession(context);
        //get current inspection from Preferences
        inspectionDTO=appSession.getMyInspectionDTO();
        //if current inspection are empty then create a new inspection
        if (inspectionDTO!=null
                && inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey()!=null
                && inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().size()>0){

            forCourtSurvey=getResources().getStringArray(R.array.for_court_survey);
            surveyDTOs=new ArrayList<>();

            for (int i=0;i<=forCourtSurvey.length;i++){

                SurveyDTO dto=new SurveyDTO();
                switch (i){
                    case 0:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getForecourtareaClearity());
                        break;
                    case 1:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getDispensersFunctionalityProperly());
                        break;
                    case 2:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getSignageLightingProperOrder());
                        break;
                    case 3:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getLubricantsProperAvailable());
                        break;
                    case 4:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getMeterReadingCustmer());
                        break;
                    case 5:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getWindScreenCleaning());
                        break;
                    case 6:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getQuickOilChange());
                        break;
                    case 7:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getOnlyPsoLubricantSold());
                        break;
                    case 8:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getAllTheFuelPricesUpdateVisible());
                        break;
                    case 9:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getDustbinsAvailable());
                        break;
                    case 10:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getStaffMembersCleanProper());
                        break;
                    case 11:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getSolganWallPresentable());
                        break;
                    case 12:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getCleanDrinkingWaterAvaialble());
                        break;
                    case 13:
                        dto.setKey("");
                        dto.setMessage(forCourtSurvey[i]);
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getExplosiveLicenseValidPaidChallan());
                        break;
                     case 14:
                        dto.setKey("");
                        dto.setMessage(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getCustmerComment());
                        dto.setStatus(inspectionDTO.getResponse().get(inspectionPosition).getForcastSurvey().get(0).getCustmerComment());
                        break;
                }
                surveyDTOs.add(dto);
            }

            recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
            recycler_view.addItemDecoration(itemDecoration);
            recycler_view.setLayoutManager(layoutManager);

            HSESurvayAdapter = new ViewHSESurvayAdapter(context, surveyDTOs);
            recycler_view.setAdapter(HSESurvayAdapter);

        }

        toolBar = (Toolbar) findViewById(R.id.toolBar_rtlOut_inspctn_for);


        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();
        return true;
    }

}
