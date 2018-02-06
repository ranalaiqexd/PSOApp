package com.newinspections.pso.newview;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.R;
import com.newinspections.pso.database.DatabaseWrapper;
import com.newinspections.pso.database.NozzlesORM;
import com.newinspections.pso.database.ProductsORM;
import com.newinspections.pso.database.PurchaseORM;
import com.newinspections.pso.database.StationORM;
import com.newinspections.pso.database.TanksORM;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.model.ProductsModel;
import com.newinspections.pso.utils.AppSession;
import com.newinspections.pso.utils.ConstantLib;
import com.newinspections.pso.view.NewInspectionActivity;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

import static com.newinspections.pso.utils.ConstantLib.GET_PRODUCTS;
import static com.newinspections.pso.utils.ConstantLib.GET_USER_INSPECTIONS;

public class NewDashboard extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "NewDashboard";

    Context context;
    private AppSession appSession;
    private LinearLayout myInspectionLayout;
    private LinearLayout newInspection;
    private LinearLayout updateData;
    private LinearLayout getStationLayout;
    private LinearLayout dashBoard;

    private Gson gson;
    GetInspectionsFromServer getInspectionsFromServer = null;
    GetProductsFromServer getProductsFromServer = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context= this;
        appSession= new AppSession(context);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        newInspection=(LinearLayout)findViewById(R.id.llayoutNwInspctn);
        updateData=(LinearLayout)findViewById(R.id.llayoutUpdate);
//        myInspectionLayout=(LinearLayout)findViewById(R.id.llayoutMyInspection);
//        getStationLayout=(LinearLayout)findViewById(R.id.llayoutGetStation);
//        dashBoard=(LinearLayout)findViewById(R.id.lLayoutDashBoard);
    }

    @Override
    protected void onStart() {
        super.onStart();

        newInspection.setOnClickListener(this);
        updateData.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.llayoutNwInspctn:
                Intent intentNewInspection = new Intent(NewDashboard.this, LatestNewInspectionActivity.class);
                startActivity(intentNewInspection);
                break;

            case R.id.llayoutUpdate:
                getInspectionsFromServer = new GetInspectionsFromServer();
                getInspectionsFromServer.execute();
                break;
        }
    }

    public class GetInspectionsFromServer extends AsyncTask<Void, Void, String>
    {

        String urlInspection = ConstantLib.BASE_URL+""+GET_USER_INSPECTIONS+"username="+appSession.getUserName();
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(NewDashboard.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Updating Inspections from Server");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("UrlTest", urlInspection);
            Builder b = new Builder();
            b.connectTimeout(20, TimeUnit.SECONDS);
            b.readTimeout(20, TimeUnit.SECONDS);
            b.writeTimeout(20, TimeUnit.SECONDS);

            OkHttpClient client = b.build();
            Request request = new Request.Builder()
                    .url(urlInspection)
                    .build();



            Response response = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
            Log.d("Answer", result);
            return result;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            Log.d("checkhere", "Hello "+result);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("result.txt"), "utf-8"));
                writer.write(""+result);
            } catch (IOException ex) {
                // report
            } finally {
                try {writer.close();} catch (Exception ex) {/*ignore*/}
            }

            if(result!=null)
            {
                List<InspectionsModel> inspections = Arrays.asList(gson.fromJson(result, InspectionsModel.class));

                StationORM.truncateStationTable(context);

//                StationORM.dropStationTable(context);
//                TanksORM.dropTanksTable(context);
//                NozzlesORM.dropNozzlesTable(context);
//                ProductsORM.dropProductTable(context);
//                PurchaseORM.dropPurchaseTable(context);

                for (InspectionsModel inspection : inspections) {
                    StationORM.insertStationsToDb(context, inspection);
                }

                InspectionsModel inspectionsModel = new Gson().fromJson(result, new TypeToken<InspectionsModel>() {}.getType());
                appSession.setInspectionModel(inspectionsModel);

                Toast.makeText(getApplicationContext(), "Inspections updated Successfully", Toast.LENGTH_SHORT).show();
                getProductsFromServer = new GetProductsFromServer();
                getProductsFromServer.execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void initializeProductsSpinner()
//    {
//        final List<ProductsModel.Products> list = ProductsORM.getProducts(context);
//        if (list!=null && list.size()>0)
//        {
//            for (int i=0; i<list.size(); i++)
//            {
//                Log.d("ProductsList", list.get(i).getProductId()+" / "+list.get(i).getProductsName());
//            }
//        }
//    }

    public class GetProductsFromServer extends AsyncTask<Void, Void, String>
    {

        String urlProduct = ConstantLib.BASE_URL+""+GET_PRODUCTS;
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog=new ProgressDialog(NewDashboard.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Updating Products from Server");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("UrlTest", urlProduct);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlProduct)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
            }
            Log.d("Answer", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            Log.d("checkhere", "Hello "+result);
            if(result!=null)
            {
                List<ProductsModel> products = Arrays.asList(gson.fromJson(result, ProductsModel.class));

                ProductsORM.truncateProductsTable(context);
                for (ProductsModel product : products)
                {
                    Log.d("Product", ""+products.get(0).getProducts().size());
                    ProductsORM.insertProductsToDb(context, product);
                }

                ProductsModel productsModel = new Gson().fromJson(result, new TypeToken<ProductsModel>() {}.getType());
//                appSession.setInspectionModel(productsModel);

                Toast.makeText(getApplicationContext(), "Products updated Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
