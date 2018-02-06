package com.newinspections.pso.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.ViewTankAdapter;
import com.newinspections.pso.dto.MyInspectionDTO;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.utils.OnItemClickListener;

import java.util.List;

/**
 * Created by mobiweb on 3/3/16.
 */
public class ViewTankActivity extends AppCompatActivity{

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    ViewTankAdapter viewTankAdapter;
    List<MyInspectionDTO.Response.Tank> alltank =null;
    MyInspectionDTO inspectionDTO=null;

    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }
        setContentView(R.layout.view_tank);
        toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        appSession = new AppSession(context);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        setValue();
    }

    private void setValue(){

        //get current inspection from Preferences
        inspectionDTO=appSession.getMyInspectionDTO();
        //if current inspection are empty then create a new inspection
        if (inspectionDTO!=null) {
            alltank = inspectionDTO.getResponse().get(inspectionPosition).getTanks();
        }
        if (alltank!=null){
            if (alltank.size()>0) {
                viewTankAdapter = new ViewTankAdapter(context, alltank, onItemClickCallback);
                recycler_view.setAdapter(viewTankAdapter);
            }else {
                Log.i("alltank "," tanks size zero");
            }
        }else {
            Log.i("alltank "," tanks size zero");
        }
    }
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            Intent intent1 = new Intent(context, ViewNozzlesActivity.class);
            intent1.putExtra("inspectionPosition",inspectionPosition);
            intent1.putExtra("position",position);
            startActivity(intent1);
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        super.onBackPressed();
        return true;
    }
}
