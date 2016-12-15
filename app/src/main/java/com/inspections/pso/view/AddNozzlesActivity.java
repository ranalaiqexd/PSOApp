package com.inspections.pso.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inspections.pso.R;
import com.inspections.pso.adapter.AddNozzelsAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ItemOffsetDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mobiweb on 3/3/16.
 */
public class AddNozzlesActivity extends AppCompatActivity implements  View.OnClickListener{

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    Button btnAddNozzels,btnSubmit;
    AddNozzelsAdapter addNozzelsAdapter;
    InspectionDTO inspectionDTO=null;
    List<InspectionDTO.Station.Tank.Nozzle> nozzles =null;
    int position=0;

    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_add_nozzles);
        toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnAddNozzels = (Button) findViewById(R.id.btnAddNozzels);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnAddNozzels.setOnClickListener(this);

        appSession = new AppSession(context);

        if (getIntent()!=null){
            position=getIntent().getExtras().getInt("position",0);
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }

        //get current inspection from Preferences
        inspectionDTO=appSession.getInspectionDTO();


        //if current inspection are empty then create a new inspection
        if (inspectionDTO!=null){
            if (inspectionDTO.getStations().get(inspectionPosition).getTanks()!=null){
                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().size()>position){
                    nozzles=inspectionDTO.getStations().get(inspectionPosition).getTanks().get(position).getNozzle();
                    if (nozzles==null){
                        nozzles=new ArrayList<>();

                        Log.i("nozzles "," nozzles null");
                    }

                    recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
                    layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                    recycler_view.addItemDecoration(itemDecoration);
                    recycler_view.setLayoutManager(layoutManager);
                    addNozzelsAdapter = new AddNozzelsAdapter(context,inspectionDTO.getStations().get(inspectionPosition).getTanks().get(position), nozzles);
                    recycler_view.setAdapter(addNozzelsAdapter);


                    if (nozzles.size()==0){
                        btnAddNozzels.performClick();
                        Log.i("nozzles "," nozzles zero");
                    }
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
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAddNozzels:

                int nozzelsCount=0;
                for (int i=0;i<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();i++){
                    nozzelsCount=nozzelsCount+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(i).getNozzle().size();
                }
                if (nozzelsCount<24){
                    InspectionDTO.Station.Tank.Nozzle nozzle=inspectionDTO.getStations().get(inspectionPosition).getTanks().get(position).new Nozzle();
                    nozzle.setDumeterTimezone(getUTCTimeFormat());
                    nozzle.setDumeterName(getNozzleName());
                    nozzles.add(nozzle);
                    addNozzelsAdapter.notifyDataSetChanged();
                    recycler_view.scrollToPosition(addNozzelsAdapter.getItemCount() - 1);
                }else {
                    Toast toast = Toast.makeText(context, "You can't add more then 24 Nozzles.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                break;

            case R.id.btnSubmit:

                boolean isValid=false;
                for (int i=0;i<nozzles.size();i++){
                    isValid=isValid(i);
                    if (!isValid)
                        break;
                }
                if (isValid){
                    inspectionDTO.getStations().get(inspectionPosition).getTanks().get(position).setNozzle(nozzles);
                    appSession.setInspectionDTO(inspectionDTO);
                    Toast.makeText(AddNozzlesActivity.this,"Nozzle saved successfully",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }

    }

    private String getNozzleName(){
        int i=1;
        while (true){
            String nozzleName=inspectionDTO.getStations().get(inspectionPosition).getTanks().get(position).getTankType()+" "+(nozzles.size()+i);
            if (contains(nozzles,nozzleName)){
                i++;
               continue;
            }
            return nozzleName;
        }
    }


    boolean contains(List<InspectionDTO.Station.Tank.Nozzle> list, String name) {
        for (InspectionDTO.Station.Tank.Nozzle item : list) {
            if (!TextUtils.isEmpty(item.getDumeterName()) && item.getDumeterName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValid(int position){
        if (TextUtils.isEmpty(nozzles.get(position).getDumeterMaxValue())){
            nozzles.get(position).setDumeterMaxValue("0");
        }
        if (TextUtils.isEmpty(nozzles.get(position).getDumeterCurrentVolume())){
            nozzles.get(position).setDumeterCurrentVolume("0");
        }
        if (nozzles.get(position).getDumeterCurrentVolume().equals("0")){
            Toast.makeText(AddNozzlesActivity.this,"Please enter current reading of "+nozzles.get(position).getDumeterName(),Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(nozzles.get(position).getDumeterPreviousVolume()) || nozzles.get(position).getDumeterPreviousVolume().equals("0")){
            if (TextUtils.isEmpty(nozzles.get(position).getOpeningBalanceD())|| nozzles.get(position).getOpeningBalanceD().equals("0")){
                Toast.makeText(AddNozzlesActivity.this,"Please enter opening balance of "+nozzles.get(position).getDumeterName(),Toast.LENGTH_SHORT).show();
                return  false;
            }
        }
        if (Double.parseDouble(nozzles.get(position).getDumeterCurrentVolume())>Double.parseDouble(nozzles.get(position).getDumeterMaxValue())){
            Toast.makeText(AddNozzlesActivity.this,"Current reading can't greater then maximum nozzles of "+nozzles.get(position).getDumeterName(),Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (TextUtils.isEmpty(nozzles.get(position).getDumeterId()) && (Double.parseDouble(nozzles.get(position).getOpeningBalanceD())>Double.parseDouble(nozzles.get(position).getDumeterMaxValue()))){
            Toast.makeText(AddNozzlesActivity.this,"Opening balance can't greater then maximum nozzles of "+nozzles.get(position).getDumeterName(),Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }
    public String getUTCTimeFormat() {
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            YYYY_MM_DD_HH_MM_SS.setTimeZone(TimeZone.getTimeZone("UTC"));
            return YYYY_MM_DD_HH_MM_SS.format(Calendar.getInstance(Locale.getDefault()).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "N/A";
        }
    }

}
