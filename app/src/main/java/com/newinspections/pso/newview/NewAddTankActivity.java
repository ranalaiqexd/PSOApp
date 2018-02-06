package com.newinspections.pso.newview;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newinspections.pso.R;
import com.newinspections.pso.adapter.AddTankAdapter;
import com.newinspections.pso.database.NozzlesORM;
import com.newinspections.pso.database.ProductsORM;
import com.newinspections.pso.database.StationORM;
import com.newinspections.pso.database.TanksORM;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.model.ProductsModel;
import com.newinspections.pso.netcome.CheckNetworkStateClass;
import com.newinspections.pso.newadapter.NewAddTankAdapter;
import com.newinspections.pso.newadapter.NewProductAdapter;
import com.newinspections.pso.newadapter.NewStationAdapter;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ItemOffsetDecoration;
import com.newinspections.pso.utils.OnItemClickListener;
import com.newinspections.pso.view.AddNozzlesActivity;
import com.newinspections.pso.view.AddTankerActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewAddTankActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Context context;
    private Toolbar toolBar;
    private AppSession appSession;
    RecyclerView recycler_view;
    LinearLayoutManager layoutManager;
    View viewRemove;
    Button btnAddTank,btnSubmit, btnRemove;
    NewAddTankAdapter addTankAdapter;
    List<InspectionsModel.Stations.Tanks> tanksList = null;
    InspectionsModel inspectionsModel = null;
    private int stationPosition = 0;
    private String stationId;
    private int tankPosition = 0;
    private String tankPermanentId;
    private int tankTableId = 0;
    private String tankCode = "";
    private int tankCodeNo = 0;
    Spinner productSpinner;
    TextView productText;
    int productId = 0;
    String productName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        appSession = new AppSession(context);
        if (getIntent()!=null){
            stationPosition = getIntent().getExtras().getInt("StationPosition");
            stationId = getIntent().getExtras().getString("StationId");
            Log.d("StationId",stationId+"");
        }
        setContentView(R.layout.activity_add_tank);
        toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TanksORM.deleteTankTemporary(context);
        tankTableId = TanksORM.getLastTankRow(context);
        Log.d("TankTable1", ""+tankTableId);
        tankTableId++;

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recycler_view.addItemDecoration(itemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        btnAddTank = (Button) findViewById(R.id.btnAddTank);
        btnAddTank.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        viewRemove = (View) findViewById(R.id.viewRemove);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);

        initializeTanks();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        initializeTanks();

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

    boolean containsName(List<InspectionsModel.Stations.Tanks> list, String name) {
        for (InspectionsModel.Stations.Tanks item : list) {
            if (!TextUtils.isEmpty(item.getTankName()) && item.getTankName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    boolean containsCode(List<InspectionsModel.Stations.Tanks> list, String code) {
        for (InspectionsModel.Stations.Tanks item : list) {
            if (!TextUtils.isEmpty(item.getTankCode()) && item.getTankCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    private String getTankName(){
        int i=1;
        while (true){
            String tankName="Tank "+(tanksList.size()+i);
            if (containsName(tanksList,tankName)){
                i++;
                continue;
            }
            return tankName;
        }
    }

    private String getTankCode(){
        int i=1;
        while (true){
            String tankCode = inspectionsModel.getStations().get(stationPosition).getStationCode()+"-TA"+(tanksList.size()+i);
            if (containsCode(tanksList,tankCode)){
                i++;
                continue;
            }
            Log.d("TankCode", "TankCode: "+tankCode);
            return tankCode;
        }
    }

    public String getTankCreationTime() {
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            YYYY_MM_DD_HH_MM_SS.setTimeZone(TimeZone.getTimeZone("UTC"));
            return YYYY_MM_DD_HH_MM_SS.format(Calendar.getInstance(Locale.getDefault()).getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "N/A";
        }
    }

    private void initializeTanks()
    {

        if (inspectionsModel == null)
        {
            inspectionsModel = appSession.getInspectionModel();
        }
        Log.d("123",stationId+"/"+stationPosition+"/"+inspectionsModel.getStations().get(stationPosition).getStationName());
        tanksList = TanksORM.getTanks(context, stationId);
        Log.d("123", String.valueOf(tanksList.size()));
        if (tanksList==null)
        {
            tanksList = new ArrayList<>();
            Log.d("Tanks Size", String.valueOf(tanksList.size()));
        }
        if (tanksList.size()==0)
        {
//            Log.d("Check", "I am here1");
            viewRemove.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
            InspectionsModel.Stations.Tanks tank = inspectionsModel.getStations().get(stationPosition).new Tanks();
            tank.setTankName(getTankName());
            tank.setTankCode(getTankCode());
            tank.setTankNewlyCreated("1");
            tank.setTankCreatedBy(appSession.getUserName());
            tank.setTankCreationDate(getTankCreationTime());
            Log.d("TankTable2", ""+tankTableId);
            tank.tankIdTemporary = tankTableId;
//            tank.setTankId(String.valueOf(tankTableId));
            tankTableId++;
            tanksList.add(tank);
//            addTankAdapter.notifyDataSetChanged();
//            recycler_view.scrollToPosition(addTankAdapter.getItemCount() - 1);

        }
        else
        {
            viewRemove.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }
        if (tanksList!=null)
        {
            if (tanksList.size()>0)
            {
                addTankAdapter = new NewAddTankAdapter(context,tanksList, onDateClickCallback, onItemClickCallback);
                recycler_view.setAdapter(addTankAdapter);

                for (int i=0; i<tanksList.size(); i++)
                {
                    if(tanksList.get(i).getTankNewlyCreated().equals("1"))
                    {
                        viewRemove.setVisibility(View.VISIBLE);
                        btnRemove.setVisibility(View.VISIBLE);
                    }
                }

//                viewRemove.setVisibility(View.GONE);
//                btnRemove.setVisibility(View.GONE);
            }
        }
    }

    private void checkNewTanks(List<InspectionsModel.Stations.Tanks> tanksList)
    {
//        Log.d("Checking", String.valueOf(tanksList.size()-1));
//        Log.d("Checking", String.valueOf(btnRemove.getVisibility())+" / "+tanksList.get(tanksList.size()-1).getTankNewlyCreated());
        try {
            if (tanksList.get(tanksList.size()-1).getTankNewlyCreated().equals("1"))
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

        switch (v.getId())
        {
            case R.id.btnAddTank:
                InspectionsModel.Stations.Tanks tank = inspectionsModel.getStations().get(stationPosition).new Tanks();
                tank.setTankName(getTankName());
                tank.setTankCode(getTankCode());
                tank.setTankNewlyCreated("1");
                tank.setTankCreatedBy(appSession.getUserName());
                tank.setTankCreationDate(getTankCreationTime());
                tank.tankIdTemporary = tankTableId;
                tankTableId++;
                tanksList.add(tank);
                addTankAdapter.notifyDataSetChanged();
                recycler_view.scrollToPosition(addTankAdapter.getItemCount() - 1);

                viewRemove.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.VISIBLE);

                if (tanksList!=null)
                {
                    if (tanksList.size()>0)
                    {
                        addTankAdapter = new NewAddTankAdapter(context,tanksList, onDateClickCallback, onItemClickCallback);
                        recycler_view.setAdapter(addTankAdapter);
                    }
                }

//                if (tanksList.size() < 6)
//                {
//                    Log.d("Tanks Size", String.valueOf(tanksList.size()));
//                    Log.d("Tanks Size 2", String.valueOf(inspectionsModel.getStations().get(stationPosition).getTanks().size()));
//
//                    int nozzelsCount=0;
//                    for (int i=0;i<inspectionsModel.getStations().get(stationPosition).getTanks().size();i++)
//                    {
//                        nozzelsCount = nozzelsCount+inspectionsModel.getStations().get(stationPosition).getTanks().get(i).getNozzles().size();
//                    }
//                    if (nozzelsCount<24)
//                    {
//                        InspectionsModel.Stations.Tanks tank = inspectionsModel.getStations().get(stationPosition).new Tanks();
//                        tank.setTankName(getTankName());
//                        tank.setTankCode(tankCode+tankCodeNo);
//                        tank.setTankNewlyCreated("1");
//                        tank.setTankCreatedBy(appSession.getUserName());
//                        tank.setTankCreationDate(getTankCreationTime());
//                        tank.tankIdTemporary = tankTableId;
//                        tankTableId++;
//                        tanksList.add(tank);
//                        addTankAdapter.notifyDataSetChanged();
//                        recycler_view.scrollToPosition(addTankAdapter.getItemCount() - 1);
//
//                        if (tanksList!=null)
//                        {
//                            if (tanksList.size()>0)
//                            {
//                                addTankAdapter = new NewAddTankAdapter(context,tanksList, onDateClickCallback, onItemClickCallback);
//                                recycler_view.setAdapter(addTankAdapter);
//                            }
//                        }
//
//                        viewRemove.setVisibility(View.VISIBLE);
//                        btnRemove.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        Toast toast = Toast.makeText(context, "24 Nozzles already added. You can't add more Tanks.", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//                }
//                else
//                {
//                    Log.d("Tanks Size", String.valueOf(tanksList.size()));
//                    Toast toast = Toast.makeText(context, "You can't add more then six Tanks.", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
                break;
            case R.id.btnRemove:
                tanksList.remove((tanksList.size()-1));
                tankTableId--;
                addTankAdapter.notifyDataSetChanged();
                checkNewTanks(tanksList);
                break;
            case R.id.btnSubmit:
                boolean validity = false;
                inspectionsModel = appSession.getInspectionModel();
                tanksList = inspectionsModel.getStations().get(stationPosition).getTanks();
                for (int i=0; i<tanksList.size(); i++)
                {
                    Log.d("Check", tanksList.get(i).getNozzles().size()+"");
                    validity = isValid(i);
                    if (!validity)
                        break;
                    validity = checkNozzles(i);
                    if (!validity)
                        break;
                }
                if (validity)
                {
                    Log.d("CheckTanks", String.valueOf(tanksList.size()));
                    for (int i=0; i<tanksList.size(); i++)
                    {
                        Log.d("CheckTanks", String.valueOf(tanksList.get(i).getNozzles().size()));
                        String tankId = tanksList.get(i).getTankId();
                        tanksList.get(i).setTankUpdatedBy(appSession.getUserName());
                        tanksList.get(i).setTankUpdationDate(getCurrentDateAndTime());
                        Log.d("StationId", "Tank Id: "+tankId+" Station Id: "+stationId);
//                        TanksORM.updateTankRow(context, tanksList, i, tankId, stationId);
                        TanksORM.updateTankRowTemporary(context, tanksList, i, tankId, stationId);
//                        Log.d("TesttanksList"+i, i+" / "+tanksList.get(i).getTankPreviousReading()+" / "+tanksList.get(i).getTankCurrentReading());
//                        Log.d("TesttanksList"+i, i+" / "+tanksList.get(i).getTankOpeningBalance() +" / "+ tanksList.get(i).getTankCurrentReading());
                    }
                    inspectionsModel.getStations().get(stationPosition).setTanks(tanksList);
                    appSession.setInspectionModel(inspectionsModel);
                    Toast.makeText(NewAddTankActivity.this, "Tanks saved successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    private boolean checkNozzles(int position)
    {
        if (tanksList.get(position).getNozzles().size()<=0)
        {
            Toast.makeText(NewAddTankActivity.this, "Please add atleast one nozzle in"+" Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValid(int position){

        if(TextUtils.isEmpty(tanksList.get(position).getTankCurrentReading()))
        {
            tanksList.get(position).setTankCurrentReading("0");
//            Toast.makeText(context, "Please enter Tank "+(position+1)+" current reading", Toast.LENGTH_SHORT).show();
//            return false;
        }
        if(TextUtils.isEmpty(tanksList.get(position).getTankMaximum())){
            tanksList.get(position).setTankMaximum("0");
//            return false;
        }
        if (tanksList.get(position).getTankCurrentReading().equals("0")){
            Toast.makeText(NewAddTankActivity.this,"Please enter current reading of "+tanksList.get(position).getTankName(),Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(tanksList.get(position).getTankProductType()) || tanksList.get(position).getTankProductType().equals("Select Product")){
            Toast.makeText(NewAddTankActivity.this,"Please select product of "+"Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (Double.parseDouble(tanksList.get(position).getTankCurrentReading()) > Double.parseDouble(tanksList.get(position).getTankMaximum()))
        {
            Toast.makeText(NewAddTankActivity.this, "Current reading cannot be greater then tank maximum of "+" Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (TextUtils.isEmpty(tanksList.get(position).getTankId()) && (Double.parseDouble(tanksList.get(position).getTankOpeningBalance()) > Double.parseDouble(tanksList.get(position).getTankMaximum()))){
            Toast.makeText(NewAddTankActivity.this, "Opening balance cannot be greater then tank maximum of "+" Tank "+(position+1),Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            if (isValid(position))
            {
                tankPosition = position;
                tankPermanentId = tanksList.get(tankPosition).getTankId();
                if (tankPermanentId == null)
                {
                    tankPermanentId = String.valueOf(tanksList.get(tankPosition).tankIdTemporary);
                }
                inspectionsModel.getStations().get(stationPosition).setTanks(tanksList);
                appSession.setInspectionModel(inspectionsModel);
                Log.d("CheckNozzle", stationPosition+" / "+tankPosition+" / "+tankPermanentId);
                Log.d("TankTable3", ""+tanksList.get(tankPosition).tankIdTemporary+" / "+tankPermanentId);
                Intent intent = new Intent(context, NewAddNozzleActivity.class);
                intent.putExtra("stationPosition", stationPosition);
                intent.putExtra("tankPosition", tankPosition);
                intent.putExtra("tankId", String.valueOf(tankPermanentId));
                startActivityForResult(intent,1005);
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
        tanksList.get(selectPosition).setTankRemarksDate(dayOfMonth + "/" + (++monthOfYear) + "/" + year);
        addTankAdapter.notifyDataSetChanged();
    }
    private void setDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(NewAddTankActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            inspectionsModel = appSession.getInspectionModel();
            tanksList = inspectionsModel.getStations().get(stationPosition).getTanks();
        }
    }

    private String getCurrentDateAndTime ()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    private void initializeProductsList()
    {
        productSpinner = (Spinner) findViewById(R.id.spinner_type);
        productText = (TextView) findViewById(R.id.spinner_text);
        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productText.getText().toString().equalsIgnoreCase("Select Product")){
                    productText.setVisibility(View.GONE);
                    productSpinner.setVisibility(View.VISIBLE);
                    productSpinner.performClick();
                }
            }
        });

        final List<ProductsModel.Products> list = ProductsORM.getProducts(context);
        if (list!=null && list.size()>0)
        {
            productText.setText("Select Product");
            productText.setVisibility(View.VISIBLE);
            productSpinner.setVisibility(View.GONE);

            NewProductAdapter productAdapter = new NewProductAdapter(context, list);
            productSpinner.setAdapter(productAdapter);
        }
        else
        {
            productText.setText("Update Products First");
            productText.setVisibility(View.VISIBLE);
            productSpinner.setVisibility(View.GONE);
        }
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if (list!=null && list.size()>0)
                {
                    productId = Integer.parseInt(list.get(position).getProductId());
                    productName = list.get(position+1).getProductsName();

                    Log.d("ProductName",productId+" / "+productName);
                    productText.setText(productName);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
