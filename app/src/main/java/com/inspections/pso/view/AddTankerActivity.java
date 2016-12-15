package com.inspections.pso.view;

import android.content.Context;
import android.content.Intent;
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
import com.inspections.pso.adapter.AddTankAdapter;
import com.inspections.pso.adapter.HSESurvayAdapter;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.TankDTO;
import com.inspections.pso.utils.AppSession;
import com.inspections.pso.utils.ItemOffsetDecoration;
import com.inspections.pso.utils.OnItemClickListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mobiweb on 3/3/16.
 */
public class AddTankerActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, View.OnClickListener{

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    Button btnAddTank,btnSubmit;
    AddTankAdapter addTankAdapter;
    List<InspectionDTO.Station.Tank> alltank =null;
    InspectionDTO inspectionDTO=null;

    private int inspectionPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        if (getIntent()!=null){
            inspectionPosition=getIntent().getExtras().getInt("inspectionPosition",0);
        }
        setContentView(R.layout.activity_add_tank);
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

        btnAddTank = (Button) findViewById(R.id.btnAddTank);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnAddTank.setOnClickListener(this);

        setValue();
    }
    boolean contains(List<InspectionDTO.Station.Tank> list, String name) {
        for (InspectionDTO.Station.Tank item : list) {
            if (!TextUtils.isEmpty(item.getTankName()) && item.getTankName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String getTankName(){
        int i=1;
        while (true){
            String tankName="Tank "+(alltank.size()+i);
            if (contains(alltank,tankName)){
                i++;
                continue;
            }
            return tankName;
        }
    }
    private void setValue(){

        //get current inspection from Preferences
        inspectionDTO=appSession.getInspectionDTO();
        //if current inspection are empty then create a new inspection
        if (inspectionDTO==null){
            inspectionDTO=new InspectionDTO();
        }
        alltank=inspectionDTO.getStations().get(inspectionPosition).getTanks();
        if (alltank==null){
            alltank=new ArrayList<>();
        }
        if (alltank.size()==0){

            InspectionDTO.Station.Tank tank=inspectionDTO.getStations().get(inspectionPosition).new Tank();
            tank.setTankTimezone(getUTCTimeFormat());
            tank.setTankName(getTankName());
            alltank.add(tank);
        }


        if (alltank!=null){
            if (alltank.size()>0) {
                addTankAdapter = new AddTankAdapter(context,inspectionDTO.getStations().get(inspectionPosition).getA(), alltank, onItemClickCallback, onDateClickCallback);
                recycler_view.setAdapter(addTankAdapter);
            }else {
                Log.i("alltank "," tanks size zero");
            }
        }else {
            Log.i("alltank "," tanks size zero");
        }

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
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            if (isValid(position)){
                // save tanks
                inspectionDTO.getStations().get(inspectionPosition).setTanks(alltank);
                appSession.setInspectionDTO(inspectionDTO);
                Intent intent1 = new Intent(context, AddNozzlesActivity.class);
                intent1.putExtra("inspectionPosition",inspectionPosition);
                intent1.putExtra("position",position);
                startActivityForResult(intent1,1005);
            }
        }
    };
    private int selectPosition=0;
    private OnItemClickListener.OnItemClickCallback onDateClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            selectPosition=position;
            setDate();
        }
    };
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        alltank.get(selectPosition).setRemarkDate(dayOfMonth + "/" + (++monthOfYear) + "/" + year);
        addTankAdapter.notifyDataSetChanged();
    }
    private void setDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddTankerActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
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
            case R.id.btnAddTank:

                if (inspectionDTO.getStations().get(inspectionPosition).getTanks().size()<6){

                    int nozzelsCount=0;
                    for (int i=0;i<inspectionDTO.getStations().get(inspectionPosition).getTanks().size();i++){
                        nozzelsCount=nozzelsCount+inspectionDTO.getStations().get(inspectionPosition).getTanks().get(i).getNozzle().size();
                    }
                    if (nozzelsCount<24){
                        InspectionDTO.Station.Tank tank=inspectionDTO.getStations().get(inspectionPosition).new Tank();
                        tank.setTankTimezone(getUTCTimeFormat());
                        tank.setTankName(getTankName());
                        alltank.add(tank);
                        addTankAdapter.notifyDataSetChanged();
                        recycler_view.scrollToPosition(addTankAdapter.getItemCount() - 1);
                    }else {
                        Toast toast = Toast.makeText(context, "24 Nozzles already added. You can't add more Tanks.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }else {
                    Toast toast = Toast.makeText(context, "You can't add more then six Tanks.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                break;
            case R.id.btnSubmit:
               boolean isValid=false;
                for (int i=0;i<alltank.size();i++){
                    isValid=isValid(i);
                    if (!isValid)
                        break;
                }
                if (isValid){
                    inspectionDTO.getStations().get(inspectionPosition).setTanks(alltank);
                    appSession.setInspectionDTO(inspectionDTO);
                    Toast.makeText(AddTankerActivity.this,"Tanks saved successfully",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }
    private boolean isValid(int position){

        if(TextUtils.isEmpty(alltank.get(position).getTankCurrentData())){
            alltank.get(position).setTankCurrentData("0");
        }
        if(TextUtils.isEmpty(alltank.get(position).getTankMaximumValue())){
            alltank.get(position).setTankMaximumValue("0");
        }

        if (TextUtils.isEmpty(alltank.get(position).getTankType())||alltank.get(position).getTankType().equals("Select Product")){
            Toast.makeText(AddTankerActivity.this,"Please select product of "+"Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }else if (Double.parseDouble(alltank.get(position).getTankCurrentData())>Double.parseDouble(alltank.get(position).getTankMaximumValue())){
            Toast.makeText(AddTankerActivity.this,"Current reading can't greater then tank maximum of "+" Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }else if (TextUtils.isEmpty(alltank.get(position).getTankId()) && (Double.parseDouble(alltank.get(position).getOpeningBalance())>Double.parseDouble(alltank.get(position).getTankMaximumValue()))){
            Toast.makeText(AddTankerActivity.this,"Opening balance can't greater then tank maximum of "+" Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setValue();
        }
    }
}
