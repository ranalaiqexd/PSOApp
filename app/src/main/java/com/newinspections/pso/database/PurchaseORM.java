package com.newinspections.pso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newinspections.pso.model.InspectionsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Exd on 10/19/2017.
 */

public class PurchaseORM {

    DatabaseWrapper databaseWrapper;
    SQLiteDatabase database;
    private static int tankCounter = 1000;

    private static final String TAG = "TanksORM";

    private static final String PURCHASE_TABLE_NAME = "purchase";

    private static final String COMMA_SEP = ", ";

    private static final String ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String ID = "Purchase_id";

    private static final String PRODUCT_TYPE = "TEXT";
    private static final String PRODUCT = "Purchase_product";

    private static final String SAPPREVIOUSDATA_TYPE = "REAL";
    private static final String SAPPREVIOUSDATA = "Purchase_sapPreviousData";

    private static final String AMOUNT_TYPE = "REAL";
    private static final String AMOUNT = "Purchase_amount";

    private static final String STATION_ID_TYPE = "INTEGER";
    private static final String STATION_ID = "station_id";


    public static final String SQL_CREATE_PURCHASE_TABLE =
            "CREATE TABLE " + PURCHASE_TABLE_NAME + " (" +
                    ID + " " + ID_TYPE + COMMA_SEP +
                    PRODUCT  + " " + PRODUCT_TYPE + COMMA_SEP +
                    SAPPREVIOUSDATA + " " + SAPPREVIOUSDATA_TYPE + COMMA_SEP +
                    AMOUNT + " " + AMOUNT_TYPE + COMMA_SEP +
                    STATION_ID + " " + STATION_ID_TYPE +
                    ")";

    public static final String SQL_DROP_PURCHASE_TABLE =
            "DROP TABLE IF EXISTS " + PURCHASE_TABLE_NAME;

    protected static void insertPurchaseToDb(Context context, SQLiteDatabase database, List<InspectionsModel.Stations.Purchase> purchaseStations) {

        for (int i=0; i<purchaseStations.size(); i++)
        {
            ContentValues values = tanksToContentValues(purchaseStations,i);
            long tankTableId = database.insert(PurchaseORM.PURCHASE_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Tank with ID: " + tankTableId);

        }
    }

    private static ContentValues tanksToContentValues(List<InspectionsModel.Stations.Purchase> purchaseStations, int index) {
        ContentValues values = new ContentValues();
        values.put(PurchaseORM.PRODUCT, purchaseStations.get(index).getProductName());
        values.put(PurchaseORM.SAPPREVIOUSDATA, purchaseStations.get(index).getSapPrevious());
        values.put(PurchaseORM.AMOUNT, purchaseStations.get(index).getAmount());
        values.put(PurchaseORM.STATION_ID, purchaseStations.get(index).getStationId());

        return values;
    }

    public static List<InspectionsModel.Stations.Purchase> getPurchases(Context context, String StationId) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + PurchaseORM.PURCHASE_TABLE_NAME + " WHERE " +PurchaseORM.STATION_ID+"="+StationId, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<InspectionsModel.Stations.Purchase> purchaseList = new ArrayList<InspectionsModel.Stations.Purchase>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                InspectionsModel.Stations.Purchase dataRow = cursorToPurchase(cursor);
                purchaseList.add(dataRow);
                cursor.moveToNext();
            }
            Log.i(TAG, "Inspections loaded successfully.");
        }

        database.close();

        return purchaseList;
    }

    private static InspectionsModel.Stations.Purchase cursorToPurchase(Cursor cursor)
    {
        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
        InspectionsModel.Stations.Purchase purchaseModel = stationModel.new Purchase();
        purchaseModel.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT)));
        purchaseModel.setSapPrevious(Double.valueOf(cursor.getString(cursor.getColumnIndex(SAPPREVIOUSDATA))));
        purchaseModel.setAmount(Double.valueOf(cursor.getString(cursor.getColumnIndex(AMOUNT))));

        return purchaseModel;
    }

    protected static void truncatePurchaseTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        if (database!=null)
        {
            TanksORM.truncateTanksTable(database);
            database.delete(PURCHASE_TABLE_NAME, null, null);
        }
    }

    public static void dropPurchaseTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase writeDatabase = databaseWrapper.getWritableDatabase();
        databaseWrapper.TableDrop(writeDatabase, PURCHASE_TABLE_NAME);
    }

}
