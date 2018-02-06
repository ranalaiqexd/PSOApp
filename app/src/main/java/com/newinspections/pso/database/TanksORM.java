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

public class TanksORM {

    DatabaseWrapper databaseWrapper;
    SQLiteDatabase database;
    private static int tankCounter = 1000;

    private static final String TAG = "TanksORM";

    private static final String TANK_TABLE_NAME = "tanks";

    private static final String COMMA_SEP = ", ";

    private static final String TANK_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
//    private static final String TANK_ID_TYPE = "INTEGER";
    private static final String TANK_ID = "Tank_id";

    private static final String TANK_NAME_TYPE = "TEXT";
    private static final String TANK_NAME = "Tank_name";

    private static final String TANK_CODE_TYPE = "TEXT";
    private static final String TANK_CODE = "Tank_code";

    private static final String TANK_OPENING_BALANCE_TYPE = "TEXT";
    private static final String TANK_OPENING_BALANCE = "Tank_opening_balance";

    private static final String TANK_MAXIMUM_TYPE = "TEXT";
    private static final String TANK_MAXIMUM = "Tank_maximum";

    private static final String TANK_PREVIOUS_READING_TYPE = "TEXT";
    private static final String TANK_PREVIOUS_READING = "Tank_previous_reading";

    private static final String TANK_CURRENT_READING_TYPE = "TEXT";
    private static final String TANK_CURRENT_READING = "Tank_current_reading";

    private static final String TANK_FLUSH_TYPE = "TEXT";
    private static final String TANK_FlUSH = "Tank_flush";

    private static final String TANK_REMARKS_TYPE = "TEXT";
    private static final String TANK_REMARKS = "Tank_remarks";

    private static final String TANK_REMARKS_DATE_TYPE = "TEXT";
    private static final String TANK_REMARKS_DATE = "Tank_remarks_date";

    private static final String TANK_PRODUCT_NAME_TYPE = "TEXT";
    private static final String TANK_PRODUCT_NAME = "Tank_product_name";

    private static final String TANK_NEWLY_CREATED_TYPE = "TEXT";
    private static final String TANK_NEWLY_CREATED = "Tank_newly_created";

    private static final String TANK_CREATION_DATE_TYPE = "TEXT";
    private static final String TANK_CREATION_DATE = "Tank_creation_date";

    private static final String TANK_CREATED_BY_TYPE = "TEXT";
    private static final String TANK_CREATED_BY = "Tank_created_by";

    private static final String TANK_IS_TEMPORARY_TYPE = "INTEGER";
    private static final String TANK_IS_TEMPORARY = "Tank_is_temporary";

    private static final String TANK_IS_DRAFTED_TYPE = "INTEGER";
    private static final String TANK_IS_DRAFTED = "Tank_is_drafted";

//    private static String FOREIGN_TABLE_NAME = "stations";
    private static final String STATION_ID_TYPE = "INTEGER";
    private static final String STATION_ID = "station_id";


    public static final String SQL_CREATE_TANK_TABLE =
            "CREATE TABLE " + TANK_TABLE_NAME + " (" +
                    TANK_ID + " " + TANK_ID_TYPE + COMMA_SEP +
                    TANK_NAME  + " " + TANK_NAME_TYPE + COMMA_SEP +
                    TANK_CODE + " " + TANK_CODE_TYPE + COMMA_SEP +
                    TANK_OPENING_BALANCE + " " + TANK_OPENING_BALANCE_TYPE + COMMA_SEP +
                    TANK_MAXIMUM + " " + TANK_MAXIMUM_TYPE + COMMA_SEP +
                    TANK_PREVIOUS_READING + " " + TANK_PREVIOUS_READING_TYPE + COMMA_SEP +
                    TANK_CURRENT_READING + " " + TANK_CURRENT_READING_TYPE + COMMA_SEP +
                    TANK_FlUSH + " " + TANK_FLUSH_TYPE + COMMA_SEP +
                    TANK_REMARKS + " " + TANK_REMARKS_TYPE + COMMA_SEP +
                    TANK_REMARKS_DATE + " " + TANK_REMARKS_DATE_TYPE + COMMA_SEP +
                    TANK_PRODUCT_NAME + " " + TANK_PRODUCT_NAME_TYPE + COMMA_SEP +
                    TANK_NEWLY_CREATED + " " + TANK_NEWLY_CREATED_TYPE + COMMA_SEP +
                    TANK_CREATION_DATE + " " + TANK_CREATION_DATE_TYPE + COMMA_SEP +
                    TANK_CREATED_BY + " " + TANK_CREATED_BY_TYPE + COMMA_SEP +
                    TANK_IS_TEMPORARY + " " + TANK_IS_TEMPORARY_TYPE + COMMA_SEP +
                    TANK_IS_DRAFTED + " " + TANK_IS_DRAFTED_TYPE + COMMA_SEP +
                    STATION_ID + " " + STATION_ID_TYPE + //COMMA_SEP +
                    //" FOREIGN KEY ("+STATION_ID+") REFERENCES "+ FOREIGN_TABLE_NAME +"("+STATION_ID+")" +
                    ")";

    public static final String SQL_DROP_TANK_TABLE =
            "DROP TABLE IF EXISTS " + TANK_TABLE_NAME;

    protected static void insertTanksToDb(Context context, SQLiteDatabase database, List<InspectionsModel.Stations.Tanks> inspectionStations) {

        for (int i=0; i<inspectionStations.size(); i++)
        {
            ContentValues values = tanksToContentValues(inspectionStations,i);
            long tankTableId = database.insert(TanksORM.TANK_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Tank with ID: " + tankTableId);
//            Log.i("TanksSize", String.valueOf(inspectionStations.get(i).getStationId()));
//            Log.i("TanksSize", String.valueOf(inspectionStations.get(i).getTankCode()));
//            Log.i("TanksSize", String.valueOf(inspectionStations.get(i).getNozzles().size()));
            NozzlesORM.insertNozzlesToDb(context, database, inspectionStations.get(i).getNozzles());
        }
    }

    private static ContentValues tanksToContentValues(List<InspectionsModel.Stations.Tanks> inspectionStations, int index) {
        ContentValues values = new ContentValues();
        Log.i("TanksSizeId", String.valueOf(inspectionStations.get(index).getTankId()));
        Log.i("TanksSizeId", String.valueOf(inspectionStations.get(index).getTankId()));
        values.put(TanksORM.TANK_ID, inspectionStations.get(index).getTankId());
        values.put(TanksORM.TANK_NAME, inspectionStations.get(index).getTankName());
        values.put(TanksORM.TANK_CODE, inspectionStations.get(index).getTankCode());
        values.put(TanksORM.TANK_OPENING_BALANCE, inspectionStations.get(index).getTankOpeningBalance());
        values.put(TanksORM.TANK_MAXIMUM, inspectionStations.get(index).getTankMaximum());
        values.put(TanksORM.TANK_PREVIOUS_READING, inspectionStations.get(index).getTankPreviousReading());
        inspectionStations.get(index).setTankCurrentReading("");
        values.put(TanksORM.TANK_CURRENT_READING, "");
        values.put(TanksORM.TANK_FlUSH, inspectionStations.get(index).getTankFlush());
        values.put(TanksORM.TANK_REMARKS, inspectionStations.get(index).getTankRemarks());
        values.put(TanksORM.TANK_REMARKS_DATE, inspectionStations.get(index).getTankRemarksDate());
        values.put(TanksORM.TANK_PRODUCT_NAME, inspectionStations.get(index).getTankProductType());
        values.put(TanksORM.TANK_NEWLY_CREATED, inspectionStations.get(index).getTankNewlyCreated());
        values.put(TanksORM.TANK_CREATION_DATE, inspectionStations.get(index).getTankCreationDate());
        values.put(TanksORM.TANK_CREATED_BY, inspectionStations.get(index).getTankCreatedBy());
        values.put(TanksORM.TANK_IS_TEMPORARY, 0);
        values.put(TanksORM.TANK_IS_DRAFTED, 0);
        values.put(TanksORM.STATION_ID, inspectionStations.get(index).getStationId());
        Log.i("TanksSizeId", String.valueOf(inspectionStations.get(index).getStationId()));

        return values;
    }

    public static void updateTankRow(Context context, List<InspectionsModel.Stations.Tanks> tanksList, int index, String tankId, String stationId)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseReadable = databaseWrapper.getReadableDatabase();
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = databaseReadable.rawQuery("SELECT * FROM " + TanksORM.TANK_TABLE_NAME + " WHERE " + TanksORM.TANK_ID + "="+tankId, null);
        int rowCount = cursor.getCount();

        if (rowCount>0)
        {
            values.put(TanksORM.TANK_OPENING_BALANCE, tanksList.get(index).getTankOpeningBalance());
            values.put(TanksORM.TANK_MAXIMUM, tanksList.get(index).getTankMaximum());
            values.put(TanksORM.TANK_PREVIOUS_READING, tanksList.get(index).getTankCurrentReading());
            values.put(TanksORM.TANK_FlUSH, tanksList.get(index).getTankFlush());
            values.put(TanksORM.TANK_REMARKS, tanksList.get(index).getTankRemarks());
            values.put(TanksORM.TANK_REMARKS_DATE, tanksList.get(index).getTankRemarksDate());
            values.put(TanksORM.TANK_NEWLY_CREATED, 0);
            values.put(TanksORM.TANK_IS_TEMPORARY, 0);
            values.put(TanksORM.TANK_IS_DRAFTED, 0);

            String[] args = new String[]{""+tankId};
            databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_ID+"=?", args);
        }
        else
        {
//            if (tanksList.get(index).getTankFlush().equals(null))
//            {
//                tanksList.get(index).setTankFlush("0");
//            }
//            if (tanksList.get(index).getTankRemarks().equals(null))
//            {
//                tanksList.get(index).setTankRemarks("");
//                tanksList.get(index).setTankRemarksDate("");
//            }

//            values.put(TanksORM.TANK_ID, tanksList.get(index).getTankId());
            tankCounter++;
            values.put(TanksORM.TANK_ID, tankCounter);
            values.put(TanksORM.TANK_FlUSH, "0");
            values.put(TanksORM.TANK_REMARKS, "");
            values.put(TanksORM.TANK_REMARKS_DATE, "");
            values.put(TanksORM.TANK_NAME, tanksList.get(index).getTankName());
            values.put(TanksORM.TANK_CODE, tanksList.get(index).getTankCode());
            values.put(TanksORM.TANK_OPENING_BALANCE, tanksList.get(index).getTankOpeningBalance());
            values.put(TanksORM.TANK_MAXIMUM, tanksList.get(index).getTankMaximum());
            values.put(TanksORM.TANK_PREVIOUS_READING, tanksList.get(index).getTankCurrentReading());
            values.put(TanksORM.TANK_PRODUCT_NAME, tanksList.get(index).getTankProductType());
            values.put(TanksORM.TANK_NEWLY_CREATED, 1);
            values.put(TanksORM.TANK_CREATION_DATE, tanksList.get(index).getTankCreationDate());
            values.put(TanksORM.TANK_CREATED_BY, tanksList.get(index).getTankCreatedBy());
            values.put(TanksORM.TANK_IS_TEMPORARY, 0);
            values.put(TanksORM.TANK_IS_DRAFTED, 0);
            values.put(TanksORM.STATION_ID, Integer.parseInt(stationId));
            Log.d("StationId1", "Station Id: "+stationId);

            long tankTableId = databaseWriteable.insert(TanksORM.TANK_TABLE_NAME, "null", values);
            Log.d("Database", "Inserted new Tank with ID: " + tankTableId);
        }
    }

    public static void updateTankRowTemporary(Context context, List<InspectionsModel.Stations.Tanks> tanksList, int index, String tankId, String stationId)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseReadable = databaseWrapper.getReadableDatabase();
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = databaseReadable.rawQuery("SELECT * FROM " + TanksORM.TANK_TABLE_NAME + " WHERE " + TanksORM.TANK_ID + "="+tankId, null);
        int rowCount = cursor.getCount();

        Log.d("Testtemp"+index, tanksList.get(index).getTankPreviousReading() +" / "+ tanksList.get(index).getTankCurrentReading());
        Log.d("Testtemp"+index, tanksList.get(index).getTankOpeningBalance() +" / "+ tanksList.get(index).getTankMaximum());

        if (rowCount>0)
        {
            values.put(TanksORM.TANK_OPENING_BALANCE, tanksList.get(index).getTankOpeningBalance());
            values.put(TanksORM.TANK_MAXIMUM, tanksList.get(index).getTankMaximum());
            values.put(TanksORM.TANK_PREVIOUS_READING, tanksList.get(index).getTankPreviousReading());
            values.put(TanksORM.TANK_CURRENT_READING, tanksList.get(index).getTankCurrentReading());
            values.put(TanksORM.TANK_NEWLY_CREATED, tanksList.get(index).getTankNewlyCreated());
            values.put(TanksORM.TANK_FlUSH, tanksList.get(index).getTankFlush());
            values.put(TanksORM.TANK_REMARKS, tanksList.get(index).getTankRemarks());
            values.put(TanksORM.TANK_REMARKS_DATE, tanksList.get(index).getTankRemarksDate());

            String[] args = new String[]{""+tankId};
            databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_ID+"=?", args);
        }
        else
        {
//            if (tanksList.get(index).getTankFlush().equals(null))
//            {
//                tanksList.get(index).setTankFlush("0");
//            }
//            if (tanksList.get(index).getTankRemarks().equals(null))
//            {
//                tanksList.get(index).setTankRemarks("");
//                tanksList.get(index).setTankRemarksDate("");
//            }

//            values.put(TanksORM.TANK_ID, tanksList.get(index).getTankId());
            tankCounter++;
            values.put(TanksORM.TANK_ID, tankCounter);
            values.put(TanksORM.TANK_FlUSH, "0");
            values.put(TanksORM.TANK_REMARKS, "");
            values.put(TanksORM.TANK_REMARKS_DATE, "");
            values.put(TanksORM.TANK_NAME, tanksList.get(index).getTankName());
            values.put(TanksORM.TANK_CODE, tanksList.get(index).getTankCode());
            values.put(TanksORM.TANK_OPENING_BALANCE, tanksList.get(index).getTankOpeningBalance());
            values.put(TanksORM.TANK_MAXIMUM, tanksList.get(index).getTankMaximum());
            values.put(TanksORM.TANK_PREVIOUS_READING, tanksList.get(index).getTankPreviousReading());
            values.put(TanksORM.TANK_CURRENT_READING, tanksList.get(index).getTankCurrentReading());
            values.put(TanksORM.TANK_PRODUCT_NAME, tanksList.get(index).getTankProductType());
            values.put(TanksORM.TANK_NEWLY_CREATED, 1);
            values.put(TanksORM.TANK_IS_TEMPORARY, 1);
            values.put(TanksORM.TANK_IS_DRAFTED, 0);
            values.put(TanksORM.TANK_CREATION_DATE, tanksList.get(index).getTankCreationDate());
            values.put(TanksORM.TANK_CREATED_BY, tanksList.get(index).getTankCreatedBy());
            values.put(TanksORM.STATION_ID, Integer.parseInt(stationId));
            Log.d("StationId1", "Station Id: "+stationId);

            long tankTableId = databaseWriteable.insert(TanksORM.TANK_TABLE_NAME, "null", values);
            Log.d("Database", "Inserted new Tank with ID: " + tankTableId);
        }
    }

    public static void makeTankTemporay(Context context, String whereStatement)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TanksORM.TANK_IS_TEMPORARY, 1);

        String[] args = new String[]{""+whereStatement};
        databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_ID+"=?", args);
    }

    public static void saveTanksDrafted(Context context, List<InspectionsModel.Stations.Tanks> tanksList, int index, String whereCode)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        for (int i=0; i<tanksList.get(index).getNozzles().size(); i++)
        {
            NozzlesORM.saveNozzlesDrafted(context, tanksList.get(index).getNozzles(), i, tanksList.get(index).getNozzles().get(i).getNozzleCode());
        }

        ContentValues values = new ContentValues();
        values.put(TanksORM.TANK_IS_DRAFTED, 1);

        String[] args = new String[]{""+0, whereCode};
        databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_IS_DRAFTED+"=? AND "+TanksORM.TANK_CODE+"=?", args);
    }

    public static void makeTanksPermanent(Context context, List<InspectionsModel.Stations.Tanks> tanksList, int index, String whereCode)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TanksORM.TANK_OPENING_BALANCE, tanksList.get(index).getTankOpeningBalance());
        values.put(TanksORM.TANK_MAXIMUM, tanksList.get(index).getTankMaximum());
        values.put(TanksORM.TANK_PREVIOUS_READING, tanksList.get(index).getTankCurrentReading());
        values.put(TanksORM.TANK_CURRENT_READING, "");
        values.put(TanksORM.TANK_NEWLY_CREATED, 0);
        values.put(TanksORM.TANK_IS_TEMPORARY, 0);
        values.put(TanksORM.TANK_IS_DRAFTED, 0);

        String[] args = new String[]{""+1, whereCode};
        databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_IS_TEMPORARY+"=? AND "+TanksORM.TANK_CODE+"=?", args);
    }

    public static void updateTankCurrentReading(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TanksORM.TANK_CURRENT_READING, "");
        String[] args = new String[]{""+0, ""+0};
        databaseWrapper.TableUpdate(databaseWriteable, TANK_TABLE_NAME, values, TanksORM.TANK_NEWLY_CREATED+"=? AND "+TanksORM.TANK_IS_DRAFTED+"=?", args);
    }

    public static void deleteTemporaryTanks(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        String[] args = new String[]{""+1, ""+1, ""+0};
        databaseWrapper.TableDelete(databaseWriteable, TANK_TABLE_NAME, TanksORM.TANK_IS_TEMPORARY+"=? AND "+TanksORM.TANK_NEWLY_CREATED+"=? AND "+TanksORM.TANK_IS_DRAFTED+"=?", args);
    }

    public static int getLastTankRow(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getReadableDatabase();
        Cursor cursor = databaseWriteable.rawQuery("SELECT * FROM " + TanksORM.TANK_TABLE_NAME + " ORDER BY "+ TANK_ID + " DESC LIMIT 1", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int tempId = cursor.getInt(0);
            tankCounter = tempId;
            tankCounter++;
        }

        return tankCounter;
    }


    public static List<InspectionsModel.Stations.Tanks> getTanks(Context context, String StationId) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TanksORM.TANK_TABLE_NAME + " WHERE " +TanksORM.STATION_ID+"="+StationId+
                " AND " +TanksORM.TANK_IS_TEMPORARY+"="+0, null);

        Log.d("Database", "Tanks Loaded " + cursor.getCount() + " Posts...");

        List<InspectionsModel.Stations.Tanks> tanksList = new ArrayList<InspectionsModel.Stations.Tanks>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                InspectionsModel.Stations.Tanks dataRow = cursorToTanks(cursor);
                String tankWhereClause = cursor.getString(0);
                makeTankTemporay(context, tankWhereClause);
                tanksList.add(dataRow);
                cursor.moveToNext();
            }
            Log.i(TAG, "Inspections loaded successfully.");
        }
        else
        {
            Cursor cursor2 = database.rawQuery("SELECT * FROM " + TanksORM.TANK_TABLE_NAME + " WHERE " +TanksORM.STATION_ID+"="+StationId+
                    " AND " +TanksORM.TANK_IS_TEMPORARY+"="+1, null);

            if(cursor2.getCount() > 0)
            {
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    InspectionsModel.Stations.Tanks dataRow1 = cursorToTanksTemporary(cursor2);
                    tanksList.add(dataRow1);
                    cursor2.moveToNext();
                }
            }
        }

        database.close();

        return tanksList;
    }

    private static InspectionsModel.Stations.Tanks cursorToTanks(Cursor cursor)
    {
        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
        InspectionsModel.Stations.Tanks tanksModel = stationModel.new Tanks();
        tanksModel.setTankId(cursor.getString(cursor.getColumnIndex(TANK_ID)));
        tanksModel.setTankName(cursor.getString(cursor.getColumnIndex(TANK_NAME)));
        tanksModel.setTankCode(cursor.getString(cursor.getColumnIndex(TANK_CODE)));
        tanksModel.setTankOpeningBalance(cursor.getString(cursor.getColumnIndex(TANK_OPENING_BALANCE)));
        tanksModel.setTankMaximum(cursor.getString(cursor.getColumnIndex(TANK_MAXIMUM)));
        tanksModel.setTankPreviousReading(cursor.getString(cursor.getColumnIndex(TANK_PREVIOUS_READING)));
        tanksModel.setTankCurrentReading("");
        tanksModel.setTankProductType(cursor.getString(cursor.getColumnIndex(TANK_PRODUCT_NAME)));
        tanksModel.setTankNewlyCreated(cursor.getString(cursor.getColumnIndex(TANK_NEWLY_CREATED)));
        tanksModel.setTankFlush(cursor.getString(cursor.getColumnIndex(TANK_FlUSH)));
        tanksModel.setTankRemarks(cursor.getString(cursor.getColumnIndex(TANK_REMARKS)));
        tanksModel.setTankRemarksDate(cursor.getString(cursor.getColumnIndex(TANK_REMARKS_DATE)));
        tanksModel.setTankCreationDate(cursor.getString(cursor.getColumnIndex(TANK_CREATION_DATE)));
        tanksModel.setTankCreatedBy(cursor.getString(cursor.getColumnIndex(TANK_CREATED_BY)));

        return tanksModel;
    }

    private static InspectionsModel.Stations.Tanks cursorToTanksTemporary(Cursor cursor)
    {
        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
        InspectionsModel.Stations.Tanks tanksModel = stationModel.new Tanks();
        tanksModel.setTankId(cursor.getString(cursor.getColumnIndex(TANK_ID)));
        tanksModel.setTankName(cursor.getString(cursor.getColumnIndex(TANK_NAME)));
        tanksModel.setTankCode(cursor.getString(cursor.getColumnIndex(TANK_CODE)));
        tanksModel.setTankOpeningBalance(cursor.getString(cursor.getColumnIndex(TANK_OPENING_BALANCE)));
        tanksModel.setTankMaximum(cursor.getString(cursor.getColumnIndex(TANK_MAXIMUM)));
        tanksModel.setTankPreviousReading(cursor.getString(cursor.getColumnIndex(TANK_PREVIOUS_READING)));
        tanksModel.setTankCurrentReading(cursor.getString(cursor.getColumnIndex(TANK_CURRENT_READING)));
        tanksModel.setTankProductType(cursor.getString(cursor.getColumnIndex(TANK_PRODUCT_NAME)));
        tanksModel.setTankNewlyCreated(cursor.getString(cursor.getColumnIndex(TANK_NEWLY_CREATED)));
        tanksModel.setTankFlush(cursor.getString(cursor.getColumnIndex(TANK_FlUSH)));
        tanksModel.setTankRemarks(cursor.getString(cursor.getColumnIndex(TANK_REMARKS)));
        tanksModel.setTankRemarksDate(cursor.getString(cursor.getColumnIndex(TANK_REMARKS_DATE)));
        tanksModel.setTankCreationDate(cursor.getString(cursor.getColumnIndex(TANK_CREATION_DATE)));
        tanksModel.setTankCreatedBy(cursor.getString(cursor.getColumnIndex(TANK_CREATED_BY)));

        return tanksModel;
    }

    protected static void truncateTanksTable(SQLiteDatabase database)
    {
        database.delete(TANK_TABLE_NAME, null, null);
    }

    public static void dropTanksTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase writeDatabase = databaseWrapper.getWritableDatabase();
        databaseWrapper.TableDrop(writeDatabase, TANK_TABLE_NAME);
    }

    public static long countTanksTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, TANK_TABLE_NAME);

        return count;
    }

    public static String getTankId() {
        return TANK_ID;
    }

}
