package com.newinspections.pso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.newinspections.pso.model.InspectionsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Exd on 10/18/2017.
 */

public class StationORM {

    private static final String TAG = "StationORM";

    private static final String STATION_TABLE_NAME = "stations";

    private static final String COMMA_SEP = ", ";

    private static final String STATION_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String STATION_ID = "station_id";

    private static final String STATION_NAME_TYPE = "TEXT";
    private static final String STATION_NAME = "station_name";

    private static final String STATION_CODE_TYPE = "TEXT";
    private static final String STATION_CODE = "station_code";

    private static final String STATION_LONGITUDE_TYPE = "TEXT";
    private static final String STATION_LONGITUDE = "station_longitude";

    private static final String STATION_LATITUDE_TYPE = "TEXT";
    private static final String STATION_LATITUDE = "station_latitude";

    public static final String SQL_CREATE_STATION_TABLE =
            "CREATE TABLE " + STATION_TABLE_NAME + " (" +
                    STATION_ID + " " + STATION_ID_TYPE + COMMA_SEP +
                    STATION_NAME  + " " + STATION_NAME_TYPE + COMMA_SEP +
                    STATION_CODE + " " + STATION_CODE_TYPE + COMMA_SEP +
                    STATION_LONGITUDE + " " + STATION_LONGITUDE_TYPE + COMMA_SEP +
                    STATION_LATITUDE + " " + STATION_LATITUDE_TYPE +
                    ")";

    public static final String COUNT_STATION_TABLE = "SELECT COUNT(" +STATION_ID+") FROM "+STATION_TABLE_NAME;

    public static final String SQL_DROP_STATION_TABLE =
            "DROP TABLE IF EXISTS " + STATION_TABLE_NAME;

    public static void insertStationsToDb(Context context, InspectionsModel inspectionsModel) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        Log.i("StationsSize", String.valueOf(inspectionsModel.getStations().size()));

        for (int i=0; i<inspectionsModel.getStations().size(); i++)
        {
            ContentValues values = stationsToContentValues(inspectionsModel,i);
            long stationTableId = database.insert(StationORM.STATION_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Station with ID: " + stationTableId);

            Log.i("TanksSize", String.valueOf(inspectionsModel.getStations().get(i).getStationName()));
            Log.i("TanksSize", String.valueOf(inspectionsModel.getStations().get(i).getTanks().size()));

            TanksORM.insertTanksToDb(context, database, inspectionsModel.getStations().get(i).getTanks());
            PurchaseORM.insertPurchaseToDb(context, database, inspectionsModel.getStations().get(i).getPurchases());
        }

        Log.d("Database", "Database is closing");
        database.close();
    }

    private static ContentValues stationsToContentValues(InspectionsModel inspectionsModel, int index) {
        ContentValues values = new ContentValues();
        values.put(StationORM.STATION_ID, inspectionsModel.getStations().get(index).getStationId());
        values.put(StationORM.STATION_NAME, inspectionsModel.getStations().get(index).getStationName());
        values.put(StationORM.STATION_CODE, inspectionsModel.getStations().get(index).getStationCode());
        values.put(StationORM.STATION_LONGITUDE, inspectionsModel.getStations().get(index).getStationLongitude());
        values.put(StationORM.STATION_LATITUDE, inspectionsModel.getStations().get(index).getStationLatitude());

        return values;
    }

    public static List<InspectionsModel.Stations> getStations(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + StationORM.STATION_TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<InspectionsModel.Stations> stationsList = new ArrayList<InspectionsModel.Stations>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                InspectionsModel.Stations dataRow = cursorToStation(cursor);
                stationsList.add(dataRow);
                cursor.moveToNext();
            }
            Log.i(TAG, "Inspections loaded successfully.");
        }

        database.close();

        return stationsList;
    }

    private static InspectionsModel.Stations cursorToStation(Cursor cursor)
    {
        InspectionsModel.Stations inspectionsModel = new InspectionsModel.Stations();
        inspectionsModel.setStationId(cursor.getString(cursor.getColumnIndex(STATION_ID)));
        inspectionsModel.setStationName(cursor.getString(cursor.getColumnIndex(STATION_NAME)));
        inspectionsModel.setStationCode(cursor.getString(cursor.getColumnIndex(STATION_CODE)));
        inspectionsModel.setStationLongitude(cursor.getString(cursor.getColumnIndex(STATION_LONGITUDE)));
        inspectionsModel.setStationLatitude(cursor.getString(cursor.getColumnIndex(STATION_LATITUDE)));

        return inspectionsModel;
    }

    public static void truncateStationTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        if (database!=null)
        {
            PurchaseORM.truncatePurchaseTable(context);
            TanksORM.truncateTanksTable(database);
            database.delete(STATION_TABLE_NAME, null, null);
        }
    }

    public static void dropStationTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase writeDatabase = databaseWrapper.getWritableDatabase();
        databaseWrapper.TableDrop(writeDatabase, STATION_TABLE_NAME);
    }

    public static long countStationTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, STATION_TABLE_NAME);

        return count;
    }

    public static String getStationId() {
        Log.d("StationId", STATION_ID.toString());
        return STATION_ID;
    }
}
