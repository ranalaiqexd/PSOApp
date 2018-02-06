package com.newinspections.pso.newview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.AddNozzelsAdapter;
import com.newinspections.pso.database.NozzlesORM;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.newadapter.NewAddNozzleAdapter;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.view.AddNozzlesActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewAddNozzleActivity extends AppCompatActivity implements  View.OnClickListener{

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    Button btnAddNozzels, btnSubmit, btnRemove;
    View viewRemove;
    NewAddNozzleAdapter addNozzelsAdapter;
    List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList = null;
    InspectionsModel inspectionsModel = null;
    int stationPosition, tankPosition;
    private String tankId;
    private String nozzleCode = "NZ-";
    private int nozzleCodeNo = 0;
    private int nozzleTableId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        appSession = new AppSession(context);
        if (getIntent()!=null){
            stationPosition = getIntent().getExtras().getInt("stationPosition");
            tankPosition = getIntent().getExtras().getInt("tankPosition");
            tankId = getIntent().getExtras().getString("tankId");
            Log.d("TankId1", tankId);
        }
        setContentView(R.layout.activity_add_nozzles);
        toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        NozzlesORM.deleteNozzleTemporary(context);
        nozzleTableId = NozzlesORM.getLastNozzleRow(context);
        Log.d("NozzleTable1", ""+nozzleTableId);
        nozzleTableId++;

        btnAddNozzels = (Button) findViewById(R.id.btnAddNozzels);
        btnAddNozzels.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        viewRemove = (View) findViewById(R.id.viewRemove);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);

        recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        initializeNozzles();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initializeNozzles();

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

    private void initializeNozzles()
    {
        if (inspectionsModel == null)
        {
            inspectionsModel = appSession.getInspectionModel();
        }

        nozzlesList = NozzlesORM.getNozzles(context, tankId);
        if (nozzlesList==null)
        {
            nozzlesList = new ArrayList<>();
        }

        if (nozzlesList.size()==0)
        {
            Log.d("TankId2", tankId);
            viewRemove.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
            InspectionsModel.Stations.Tanks.Nozzles nozzle = inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).new Nozzles();
            nozzle.setNozzleName(getNozzleName());
            nozzle.setNozzleCode(getNozzleCode());
            nozzle.setNozzleProductType(inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).getTankProductType());
            nozzle.setNozzleNewlyCreated("1");
            nozzle.setTankId(tankId);
            nozzle.setNozzleCreatedBy(appSession.getUserName());
            nozzle.setNozzleCreationDate(getNozzleCreationTime());
            nozzlesList.add(nozzle);
        }
        else
        {
            viewRemove.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }

        if (nozzlesList!=null)
        {
            if (nozzlesList.size()>0)
            {
//                Log.d("Check", "I am here");
//                recycler_view=(RecyclerView) findViewById(R.id.recycler_view);
//                layoutManager = new LinearLayoutManager(context);
//                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
//                recycler_view.addItemDecoration(itemDecoration);
//                recycler_view.setLayoutManager(layoutManager);
                addNozzelsAdapter = new NewAddNozzleAdapter(context, nozzlesList);
                recycler_view.setAdapter(addNozzelsAdapter);

                for (int i=0; i<nozzlesList.size(); i++)
                {
                    if(nozzlesList.get(i).getNozzleNewlyCreated().equals("1"))
                    {
                        viewRemove.setVisibility(View.VISIBLE);
                        btnRemove.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    boolean containsName(List<InspectionsModel.Stations.Tanks.Nozzles> list, String name) {
        for (InspectionsModel.Stations.Tanks.Nozzles item : list) {
            if (!TextUtils.isEmpty(item.getNozzleName()) && item.getNozzleName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    boolean containsCode(List<InspectionsModel.Stations.Tanks.Nozzles> list, String code) {
        for (InspectionsModel.Stations.Tanks.Nozzles item : list) {
            if (!TextUtils.isEmpty(item.getNozzleCode()) && item.getNozzleCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    private String getNozzleName(){
        int i=1;
        while (true){
            String nozzleName = inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).getTankProductType()+" "+(nozzlesList.size()+i);
            if (containsName(nozzlesList,nozzleName)){
                i++;
                continue;
            }
            return nozzleName;
        }
    }

    private String getNozzleCode(){
        int i=1;
        while (true){
            String nozzleCode = inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).getTankCode()+"-NZ"+(nozzlesList.size()+i);
            if (containsCode(nozzlesList,nozzleCode)){
                i++;
                continue;
            }
            Log.d("NozzleCode", "NozzleCode: "+nozzleCode);
            return nozzleCode;
        }
    }

    public String getNozzleCreationTime() {
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            YYYY_MM_DD_HH_MM_SS.setTimeZone(TimeZone.getTimeZone("UTC"));
            return YYYY_MM_DD_HH_MM_SS.format(Calendar.getInstance(Locale.getDefault()).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "N/A";
        }
    }

    private void checkNewNozzles(List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList)
    {
//        Log.d("Checking", String.valueOf(tanksList.size()-1));
//        Log.d("Checking", String.valueOf(btnRemove.getVisibility())+" / "+tanksList.get(tanksList.size()-1).getTankNewlyCreated());
        try {
            if (nozzlesList.get(nozzlesList.size()-1).getNozzleNewlyCreated().equals("1"))
            {
                viewRemove.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.VISIBLE);
            }
            else
            {
                viewRemove.setVisibility(View.GONE);
                btnRemove.setVisibility(View.GONE);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            viewRemove.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddNozzels:
                InspectionsModel.Stations.Tanks.Nozzles nozzle = inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).new Nozzles();
                nozzle.setNozzleName(getNozzleName());
                nozzle.setNozzleCode(getNozzleCode());
                nozzle.setNozzleProductType(inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).getTankProductType());
                nozzle.setNozzleNewlyCreated("1");
                nozzle.setTankId(tankId);
                nozzle.setNozzleCreatedBy(appSession.getUserName());
                nozzle.setNozzleCreationDate(getNozzleCreationTime());
                nozzlesList.add(nozzle);
                addNozzelsAdapter.notifyDataSetChanged();
                recycler_view.scrollToPosition(addNozzelsAdapter.getItemCount() - 1);

                viewRemove.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.VISIBLE);

                if (nozzlesList!=null)
                {
                    if (nozzlesList.size()>0)
                    {
                        addNozzelsAdapter = new NewAddNozzleAdapter(context, nozzlesList);
                        recycler_view.setAdapter(addNozzelsAdapter);
                    }
                }

//                int nozzelsCount=0;
//                for (int i=0; i<inspectionsModel.getStations().get(stationPosition).getTanks().size(); i++)
//                {
//                    nozzelsCount = nozzelsCount+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size();
//                }
//                if (nozzelsCount < 24)
//                {
//                    InspectionsModel.Stations.Tanks.Nozzles nozzle = inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).new Nozzles();
//                    nozzle.setNozzleName(getNozzleName());
//                    nozzle.setNozzleCode(nozzleCode+nozzleCodeNo);
//                    nozzle.setNozzleProductType(inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).getTankProductType());
//                    nozzle.setNozzleNewlyCreated("1");
//                    nozzle.setTankId(tankId);
//                    nozzle.setNozzleCreatedBy(appSession.getUserName());
//                    nozzle.setNozzleCreationDate(getNozzleCreationTime());
//                    nozzlesList.add(nozzle);
//                    addNozzelsAdapter.notifyDataSetChanged();
//                    recycler_view.scrollToPosition(addNozzelsAdapter.getItemCount() - 1);
//                    viewRemove.setVisibility(View.VISIBLE);
//                    btnRemove.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    Toast toast = Toast.makeText(context, "You can't add more then 24 Nozzles.", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }

                break;
            case R.id.btnRemove:
                nozzlesList.remove((nozzlesList.size()-1));
                addNozzelsAdapter.notifyDataSetChanged();
                checkNewNozzles(nozzlesList);
                break;
            case R.id.btnSubmit:
                boolean isValid=false;
                for (int i=0;i<nozzlesList.size();i++){
                    isValid=isValid(i);
                    if (!isValid)
                        break;
                }
                if (isValid)
                {
                    for (int i=0; i<nozzlesList.size(); i++)
                    {
                        String nozzleId = nozzlesList.get(i).getNozzleId();
                        nozzlesList.get(i).setNozzleUpdatedBy(appSession.getUserName());
                        nozzlesList.get(i).setNozzleUpdationDate(getCurrentDateAndTime());
                        Log.d("Nozzleid", i+" / "+nozzleId);
                        Log.d("Database2", ""+tankId);
                        NozzlesORM.updateNozzleRowTemporary(context, nozzlesList, i, nozzleId, tankId);
//                        NozzlesORM.updateNozzleRow(context, nozzlesList, i, nozzleId, tankId);
                    }
                    inspectionsModel.getStations().get(stationPosition).getTanks().get(tankPosition).setNozzles(nozzlesList);
                    appSession.setInspectionModel(inspectionsModel);
                    Toast.makeText(context,"Nozzle saved successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    private boolean isValid(int position){
        if (TextUtils.isEmpty(nozzlesList.get(position).getNozzleMaximum())){
            nozzlesList.get(position).setNozzleMaximum("0");
        }
        if (TextUtils.isEmpty(nozzlesList.get(position).getNozzleCurrentReading())){
            nozzlesList.get(position).setNozzleCurrentReading("0");
//            Toast.makeText(context, "Please enter Nozzle "+(position+1)+" current reading", Toast.LENGTH_SHORT).show();
//            return false;

        }
        if (nozzlesList.get(position).getNozzleCurrentReading().equals("0")){
            Toast.makeText(NewAddNozzleActivity.this,"Please enter current reading of "+nozzlesList.get(position).getNozzleName(),Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(nozzlesList.get(position).getNozzlePreviousReading()) || nozzlesList.get(position).getNozzlePreviousReading().equals("0"))
        {
            if (TextUtils.isEmpty(nozzlesList.get(position).getNozzleOpeningBalance())|| nozzlesList.get(position).getNozzleOpeningBalance().equals("0")){
                Toast.makeText(NewAddNozzleActivity.this,"Please enter opening balance of "+nozzlesList.get(position).getNozzleName(),Toast.LENGTH_SHORT).show();
                return  false;
            }
        }
        if (Double.parseDouble(nozzlesList.get(position).getNozzleCurrentReading())>Double.parseDouble(nozzlesList.get(position).getNozzleMaximum())){
            Toast.makeText(NewAddNozzleActivity.this,"Current reading cannot be greater then maximum nozzles of "+nozzlesList.get(position).getNozzleName(),Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (TextUtils.isEmpty(nozzlesList.get(position).getNozzleId()) && (Double.parseDouble(nozzlesList.get(position).getNozzleOpeningBalance())>Double.parseDouble(nozzlesList.get(position).getNozzleMaximum()))){
            Toast.makeText(NewAddNozzleActivity.this,"Opening balance cannot be greater then maximum nozzles of "+nozzlesList.get(position).getNozzleName(),Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    private String getCurrentDateAndTime ()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }
}
