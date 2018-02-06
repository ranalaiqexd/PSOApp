package com.newinspections.pso.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.ViewNozzelsAdapter;
import com.newinspections.pso.dto.MyInspectionDTO;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiweb on 3/3/16.
 */
public class ViewNozzlesActivity extends AppCompatActivity{

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    ViewNozzelsAdapter viewNozzelsAdapter;
    MyInspectionDTO inspectionDTO=null;
    List<MyInspectionDTO.Response.Tank.Nozzle> nozzles =null;
    int position=0;

    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.view_nozzles);
        toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        appSession = new AppSession(context);

        if (getIntent()!=null){
            position=getIntent().getExtras().getInt("position",0);
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }

        //get current inspection from Preferences
        inspectionDTO=appSession.getMyInspectionDTO();
        inspectionDTO.toString();


        //if current inspection are empty then create a new inspection
        if (inspectionDTO!=null)
         {
            if (inspectionDTO.getResponse().get(inspectionPosition).getTanks()!=null)
            {
                if (inspectionDTO.getResponse().get(inspectionPosition).getTanks().size()>position)
                {
                    nozzles=inspectionDTO.getResponse().get(inspectionPosition).getTanks().get(position).getNozzle();
                    if (nozzles==null){
                        nozzles=new ArrayList<>();
                    }
                    recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
                    layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    recycler_view.addItemDecoration(itemDecoration);
                    recycler_view.setLayoutManager(layoutManager);
                    viewNozzelsAdapter = new ViewNozzelsAdapter(context, nozzles);
                    recycler_view.setAdapter(viewNozzelsAdapter);
                }else {
                    Log.i("Nozzels "," less position zero");
                }
            }else {
                Log.i("Nozzels "," getAlltank zero");
            }
        }else {
            Log.i("Nozzels "," inspectionDTO zero");
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        super.onBackPressed();
        return true;
    }
}
