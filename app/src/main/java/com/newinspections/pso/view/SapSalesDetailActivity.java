package com.newinspections.pso.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.adapter.SapMonthDetailAdapter;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.dto.SapDetailDTO;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;

/**
 * Created by mobiweb on 18/3/16.
 */
public class  SapSalesDetailActivity extends InspectionBaseActivity {


    private Toolbar toolBar;
    LinearLayoutManager layoutManager;
    RecyclerView recycler_view_sales;
    InspectionDTO inspectionDTO=null;
    private AppSession appSession;
    SapMonthDetailAdapter sapMonthAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sap_sales_detail);

        appSession = new AppSession(SapSalesDetailActivity.this);
        //initiated inspections
        inspectionDTO=appSession.getInspectionDTO();
        initView();

        toolBar = (Toolbar) findViewById(R.id.toolBar_getStn);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            SapDetailDTO dto=new Gson().fromJson(getIntent().getExtras().getString("data"), new TypeToken<SapDetailDTO>() {}.getType());
            SapDetailDTO.First first=dto.new First();
            dto.getFirst().add(0,first);
            sapMonthAdapter = new SapMonthDetailAdapter(SapSalesDetailActivity.this,dto.getFirst());
            recycler_view_sales.setAdapter(sapMonthAdapter);
        }else {
            Log.i("SapSalesDetailActivity ","SapSalesDetailActivity null");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }

    private void initView() {
        recycler_view_sales = (RecyclerView) findViewById(R.id.recycler_view_sales);
        layoutManager = new LinearLayoutManager(SapSalesDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(SapSalesDetailActivity.this, R.dimen.item_offset_1);
        recycler_view_sales.addItemDecoration(itemDecoration);
        recycler_view_sales.setLayoutManager(layoutManager);
        recycler_view_sales.setNestedScrollingEnabled(false);
    }
}
