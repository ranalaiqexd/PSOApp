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
 * Created by Exd on 11/8/2017.
 */

public class NozzlesORM {

    private static int nozzleCounter = 10000;

    private static final String TAG = "NozzleORM";

    private static final String NOZZLE_TABLE_NAME = "nozzles";

    private static final String COMMA_SEP = ", ";

    private static final String NOZZLE_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
//    private static final String NOZZLE_ID_TYPE = "INTEGER";
    private static final String NOZZLE_ID = "Nozzle_id";

    private static final String NOZZLE_NAME_TYPE = "TEXT";
    private static final String NOZZLE_NAME = "Nozzle_name";

    private static final String NOZZLE_CODE_TYPE = "TEXT";
    private static final String NOZZLE_CODE = "Nozzle_code";

    private static final String NOZZLE_NUMBER_TYPE = "TEXT";
    private static final String NOZZLE_NUMBER = "Nozzle_number";

    private static final String NOZZLE_OPENING_BALANCE_TYPE = "TEXT";
    private static final String NOZZLE_OPENING_BALANCE = "Nozzle_opening_balance";

    private static final String NOZZLE_MAXIMUM_TYPE = "TEXT";
    private static final String NOZZLE_MAXIMUM = "Nozzle_maximum";

    private static final String NOZZLE_PREVIOUS_READING_TYPE = "TEXT";
    private static final String NOZZLE_PREVIOUS_READING = "Nozzle_previous_reading";

    private static final String NOZZLE_CURRENT_READING_TYPE = "TEXT";
    private static final String NOZZLE_CURRENT_READING = "Nozzle_current_reading";

    private static final String NOZZLE_DEFECTED_TYPE = "TEXT";
    private static final String NOZZLE_DEFECTED = "Nozzle_defected";

    private static final String NOZZLE_RESET_TYPE = "TEXT";
    private static final String NOZZLE_RESET = "Nozzle_reset";

    private static final String NOZZLE_SPECIAL_READING_TYPE = "TEXT";
    private static final String NOZZLE_SPECIAL_READING = "Nozzle_special_reading";

    private static final String NOZZLE_SPECIAL_REMARKS_TYPE = "TEXT";
    private static final String NOZZLE_SPECIAL_REMARKS = "Nozzle_special_remarks";

    private static final String NOZZLE_PRODUCT_NAME_TYPE = "TEXT";
    private static final String NOZZLE_PRODUCT_NAME = "Nozzle_product_name";

    private static final String NOZZLE_NEWLY_CREATED_TYPE = "TEXT";
    private static final String NOZZLE_NEWLY_CREATED = "Nozzle_newly_created";

    private static final String NOZZLE_CREATION_DATE_TYPE = "TEXT";
    private static final String NOZZLE_CREATION_DATE = "Tank_creation_date";

    private static final String NOZZLE_CREATED_BY_TYPE = "TEXT";
    private static final String NOZZLE_CREATED_BY = "Tank_created_by";

    private static final String NOZZLE_IS_TEMPORARY_TYPE = "INTEGER";
    private static final String NOZZLE_IS_TEMPORARY = "Nozzle_is_temporary";

    private static final String NOZZLE_IS_DRAFTED_TYPE = "INTEGER";
    private static final String NOZZLE_IS_DRAFTED = "Nozzle_is_drafted";

    private static final String TANK_ID_TYPE = "INTEGER";
    private static final String TANK_ID = "tank_id";


    public static final String SQL_CREATE_NOZZLE_TABLE =
            "CREATE TABLE " + NOZZLE_TABLE_NAME + " (" +
                    NOZZLE_ID + " " + NOZZLE_ID_TYPE + COMMA_SEP +
                    NOZZLE_NAME  + " " + NOZZLE_NAME_TYPE + COMMA_SEP +
                    NOZZLE_CODE + " " + NOZZLE_CODE_TYPE + COMMA_SEP +
                    NOZZLE_NUMBER + " " + NOZZLE_NUMBER_TYPE + COMMA_SEP +
                    NOZZLE_OPENING_BALANCE + " " + NOZZLE_OPENING_BALANCE_TYPE + COMMA_SEP +
                    NOZZLE_MAXIMUM + " " + NOZZLE_MAXIMUM_TYPE + COMMA_SEP +
                    NOZZLE_PREVIOUS_READING + " " + NOZZLE_PREVIOUS_READING_TYPE + COMMA_SEP +
                    NOZZLE_CURRENT_READING + " " + NOZZLE_CURRENT_READING_TYPE + COMMA_SEP +
                    NOZZLE_PRODUCT_NAME + " " + NOZZLE_PRODUCT_NAME_TYPE + COMMA_SEP +
                    NOZZLE_DEFECTED + " " + NOZZLE_DEFECTED_TYPE + COMMA_SEP +
                    NOZZLE_RESET + " " + NOZZLE_RESET_TYPE + COMMA_SEP +
                    NOZZLE_SPECIAL_READING + " " + NOZZLE_SPECIAL_READING_TYPE + COMMA_SEP +
                    NOZZLE_SPECIAL_REMARKS + " " + NOZZLE_SPECIAL_REMARKS_TYPE + COMMA_SEP +
                    NOZZLE_NEWLY_CREATED + " " + NOZZLE_NEWLY_CREATED_TYPE + COMMA_SEP +
                    NOZZLE_CREATION_DATE + " " + NOZZLE_CREATION_DATE_TYPE + COMMA_SEP +
                    NOZZLE_CREATED_BY + " " + NOZZLE_CREATED_BY_TYPE + COMMA_SEP +
                    NOZZLE_IS_TEMPORARY + " " + NOZZLE_IS_TEMPORARY_TYPE + COMMA_SEP +
                    NOZZLE_IS_DRAFTED + " " + NOZZLE_IS_DRAFTED_TYPE + COMMA_SEP +
                    TANK_ID + " " + TANK_ID_TYPE +
                    ")";

    public static final String SQL_DROP_NOZZLE_TABLE =
            "DROP TABLE IF EXISTS " + NOZZLE_TABLE_NAME;

    protected static void insertNozzlesToDb(Context context, SQLiteDatabase database, List<InspectionsModel.Stations.Tanks.Nozzles> inspectionStations) {

        for (int i=0; i<inspectionStations.size(); i++)
        {
            ContentValues values = nozzlesToContentValues(inspectionStations,i);
            long nozzleTableId = database.insert(NozzlesORM.NOZZLE_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Nozzle with ID: " + nozzleTableId);
        }
    }

    private static ContentValues nozzlesToContentValues(List<InspectionsModel.Stations.Tanks.Nozzles> inspections, int index) {
        ContentValues values = new ContentValues();
        values.put(NozzlesORM.NOZZLE_ID, inspections.get(index).getNozzleId());
        values.put(NozzlesORM.NOZZLE_NAME, inspections.get(index).getNozzleName());
        values.put(NozzlesORM.NOZZLE_CODE, inspections.get(index).getNozzleCode());
        values.put(NozzlesORM.NOZZLE_NUMBER, inspections.get(index).getNozzleNumber());
        values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, inspections.get(index).getNozzleOpeningBalance());
        values.put(NozzlesORM.NOZZLE_MAXIMUM, inspections.get(index).getNozzleMaximum());
        values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, inspections.get(index).getNozzlePreviousReading());
        inspections.get(index).setNozzleCurrentReading("");
        values.put(NozzlesORM.NOZZLE_CURRENT_READING, "");
        values.put(NozzlesORM.NOZZLE_PRODUCT_NAME, inspections.get(index).getNozzleProductType());
        values.put(NozzlesORM.NOZZLE_DEFECTED, inspections.get(index).getNozzleDefected());
        values.put(NozzlesORM.NOZZLE_RESET, inspections.get(index).getNozzleReset());
        values.put(NozzlesORM.NOZZLE_SPECIAL_READING, inspections.get(index).getNozzleSpecialReading());
        values.put(NozzlesORM.NOZZLE_SPECIAL_REMARKS, inspections.get(index).getNozzleSpecialRemarks());
        values.put(NozzlesORM.NOZZLE_NEWLY_CREATED, inspections.get(index).getNozzleNewlyCreated());
        values.put(NozzlesORM.NOZZLE_CREATION_DATE, inspections.get(index).getNozzleCreationDate());
        values.put(NozzlesORM.NOZZLE_CREATED_BY, inspections.get(index).getNozzleCreatedBy());
        values.put(NozzlesORM.NOZZLE_IS_TEMPORARY, 0);
        values.put(NozzlesORM.NOZZLE_IS_DRAFTED, 0);
        values.put(NozzlesORM.TANK_ID, inspections.get(index).getTankId());

        return values;
    }

    public static void updateNozzleRow(Context context, List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList, int index, String nozzleId, String tankId)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseReadable = databaseWrapper.getReadableDatabase();
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        Cursor cursor = databaseReadable.rawQuery("SELECT * FROM " + NozzlesORM.NOZZLE_TABLE_NAME + " WHERE " + NozzlesORM.NOZZLE_ID + "="+nozzleId, null);
        int rowCount = cursor.getCount();
        Log.d("Checkrow", String.valueOf(rowCount));
        ContentValues values = new ContentValues();
        if (rowCount>0)
        {
            Log.d("Checkrow", "Updating...");
            values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, nozzlesList.get(index).getNozzleOpeningBalance());
            values.put(NozzlesORM.NOZZLE_MAXIMUM, nozzlesList.get(index).getNozzleMaximum());
            values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, nozzlesList.get(index).getNozzleCurrentReading());
            values.put(NozzlesORM.NOZZLE_DEFECTED, nozzlesList.get(index).getNozzleDefected());
            values.put(NozzlesORM.NOZZLE_RESET, nozzlesList.get(index).getNozzleReset());
            values.put(NozzlesORM.NOZZLE_SPECIAL_READING, nozzlesList.get(index).getNozzleSpecialReading());
            values.put(NozzlesORM.NOZZLE_SPECIAL_REMARKS, nozzlesList.get(index).getNozzleSpecialRemarks());

            String[] args = new String[]{""+nozzleId};
            databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_ID+"=?", args);

        }
        else
        {
//            if (nozzlesList.get(index).getNozzleDefected().equals(null))
//            {
//                nozzlesList.get(index).setNozzleDefected("0");
//            }
//            if (nozzlesList.get(index).getNozzleReset().equals(null))
//            {
//                nozzlesList.get(index).setNozzleReset("0");
//                nozzlesList.get(index).setNozzleSpecialReading("");
//                nozzlesList.get(index).setNozzleSpecialRemarks("");
//            }

            values.put(NozzlesORM.NOZZLE_ID, nozzleCounter);
            nozzleCounter++;
            values.put(NozzlesORM.NOZZLE_NAME, nozzlesList.get(index).getNozzleName());
            values.put(NozzlesORM.NOZZLE_CODE, nozzlesList.get(index).getNozzleCode());
            values.put(NozzlesORM.NOZZLE_NUMBER, nozzlesList.get(index).getNozzleNumber());
            values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, nozzlesList.get(index).getNozzleOpeningBalance());
            values.put(NozzlesORM.NOZZLE_MAXIMUM, nozzlesList.get(index).getNozzleMaximum());
            values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, nozzlesList.get(index).getNozzleCurrentReading());
            values.put(NozzlesORM.NOZZLE_PRODUCT_NAME, nozzlesList.get(index).getNozzleProductType());
            values.put(NozzlesORM.NOZZLE_DEFECTED, "0");
            values.put(NozzlesORM.NOZZLE_RESET, "0");
            values.put(NozzlesORM.NOZZLE_SPECIAL_READING, "");
            values.put(NozzlesORM.NOZZLE_SPECIAL_REMARKS, "");
            values.put(NozzlesORM.NOZZLE_NEWLY_CREATED, 1);
            values.put(NozzlesORM.NOZZLE_CREATION_DATE, nozzlesList.get(index).getNozzleCreationDate());
            values.put(NozzlesORM.NOZZLE_CREATED_BY, nozzlesList.get(index).getNozzleCreatedBy());
            values.put(NozzlesORM.NOZZLE_IS_TEMPORARY, 1);
//            Log.d("Database3", ""+tankId);
            values.put(NozzlesORM.TANK_ID, tankId);

            long nozzleTableId = databaseWriteable.insert(NozzlesORM.NOZZLE_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Nozzle with ID: " + nozzleTableId);
        }
    }

    public static void updateNozzleRowTemporary(Context context, List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList, int index, String nozzleId, String tankId)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseReadable = databaseWrapper.getReadableDatabase();
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        Cursor cursor = databaseReadable.rawQuery("SELECT * FROM " + NozzlesORM.NOZZLE_TABLE_NAME + " WHERE " + NozzlesORM.NOZZLE_ID + "="+nozzleId, null);
        int rowCount = cursor.getCount();
        Log.d("Checkrow", String.valueOf(rowCount));
        ContentValues values = new ContentValues();
        if (rowCount>0)
        {
            Log.d("Checkrow", "Updating...");
            values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, nozzlesList.get(index).getNozzleOpeningBalance());
            values.put(NozzlesORM.NOZZLE_MAXIMUM, nozzlesList.get(index).getNozzleMaximum());
            values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, nozzlesList.get(index).getNozzlePreviousReading());
            values.put(NozzlesORM.NOZZLE_CURRENT_READING, nozzlesList.get(index).getNozzleCurrentReading());
            values.put(NozzlesORM.NOZZLE_DEFECTED, nozzlesList.get(index).getNozzleDefected());
            values.put(NozzlesORM.NOZZLE_RESET, nozzlesList.get(index).getNozzleReset());
            values.put(NozzlesORM.NOZZLE_SPECIAL_READING, nozzlesList.get(index).getNozzleSpecialReading());
            values.put(NozzlesORM.NOZZLE_SPECIAL_REMARKS, nozzlesList.get(index).getNozzleSpecialRemarks());

            String[] args = new String[]{""+nozzleId};
            databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_ID+"=?", args);

        }
        else
        {
//            if (nozzlesList.get(index).getNozzleDefected().equals(null))
//            {
//                nozzlesList.get(index).setNozzleDefected("0");
//            }
//            if (nozzlesList.get(index).getNozzleReset().equals(null))
//            {
//                nozzlesList.get(index).setNozzleReset("0");
//                nozzlesList.get(index).setNozzleSpecialReading("");
//                nozzlesList.get(index).setNozzleSpecialRemarks("");
//            }

            values.put(NozzlesORM.NOZZLE_ID, nozzleCounter);
            nozzleCounter++;
            values.put(NozzlesORM.NOZZLE_NAME, nozzlesList.get(index).getNozzleName());
            values.put(NozzlesORM.NOZZLE_CODE, nozzlesList.get(index).getNozzleCode());
            values.put(NozzlesORM.NOZZLE_NUMBER, nozzlesList.get(index).getNozzleNumber());
            values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, nozzlesList.get(index).getNozzleOpeningBalance());
            values.put(NozzlesORM.NOZZLE_MAXIMUM, nozzlesList.get(index).getNozzleMaximum());
            values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, nozzlesList.get(index).getNozzlePreviousReading());
            values.put(NozzlesORM.NOZZLE_CURRENT_READING, nozzlesList.get(index).getNozzleCurrentReading());
            values.put(NozzlesORM.NOZZLE_PRODUCT_NAME, nozzlesList.get(index).getNozzleProductType());
            values.put(NozzlesORM.NOZZLE_DEFECTED, "0");
            values.put(NozzlesORM.NOZZLE_RESET, "0");
            values.put(NozzlesORM.NOZZLE_SPECIAL_READING, "");
            values.put(NozzlesORM.NOZZLE_SPECIAL_REMARKS, "");
            values.put(NozzlesORM.NOZZLE_NEWLY_CREATED, 1);
            values.put(NozzlesORM.NOZZLE_CREATION_DATE, nozzlesList.get(index).getNozzleCreationDate());
            values.put(NozzlesORM.NOZZLE_CREATED_BY, nozzlesList.get(index).getNozzleCreatedBy());
            values.put(NozzlesORM.NOZZLE_IS_TEMPORARY, 1);
            values.put(NozzlesORM.NOZZLE_IS_DRAFTED, 0);
            values.put(NozzlesORM.TANK_ID, tankId);

            long nozzleTableId = databaseWriteable.insert(NozzlesORM.NOZZLE_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Nozzle with ID: " + nozzleTableId);
        }
    }

    public static void makeNozzleTemporary(Context context, String whereStatement)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NozzlesORM.NOZZLE_IS_TEMPORARY, 1);

        String[] args = new String[]{""+whereStatement};
        databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_ID+"=?", args);
    }

    public static void saveNozzlesDrafted(Context context, List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList, int index, String whereCode)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NozzlesORM.NOZZLE_IS_DRAFTED, 1);

        String[] args = new String[]{""+0, whereCode};
        databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_IS_DRAFTED+"=? AND "+NozzlesORM.NOZZLE_CODE+"=?", args);
    }

    public static void makeNozzlesPermanent(Context context, List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList, int index, String whereCode)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        Log.d("TestNozzle", index+" / ");
//        Log.d("TestPermanent", index+" / "+nozzlesList.get(index).getNozzleCurrentReading()+" / "+nozzlesList.get(index).getNozzleMaximum());

        ContentValues values = new ContentValues();
        values.put(NozzlesORM.NOZZLE_OPENING_BALANCE, nozzlesList.get(index).getNozzleOpeningBalance());
        values.put(NozzlesORM.NOZZLE_MAXIMUM, nozzlesList.get(index).getNozzleMaximum());
        values.put(NozzlesORM.NOZZLE_PREVIOUS_READING, nozzlesList.get(index).getNozzleCurrentReading());
        values.put(NozzlesORM.NOZZLE_CURRENT_READING, "");
        values.put(NozzlesORM.NOZZLE_NEWLY_CREATED, 0);
        values.put(NozzlesORM.NOZZLE_IS_TEMPORARY, 0);

        String[] args = new String[]{""+1, whereCode};
        databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_IS_TEMPORARY+"=? AND "+NozzlesORM.NOZZLE_CODE+"=?", args);
    }

    public static void updateNozzleCurrentReading(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NozzlesORM.NOZZLE_CURRENT_READING, "");
        String[] args = new String[]{""+0, ""+0};
        databaseWrapper.TableUpdate(databaseWriteable, NOZZLE_TABLE_NAME, values, NozzlesORM.NOZZLE_NEWLY_CREATED+"=? AND "+NozzlesORM.NOZZLE_IS_DRAFTED+"=?", args);
    }

    public static void deleteTemporaryNozzles(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getWritableDatabase();

        String[] args = new String[]{""+1, ""+1, ""+0};
        databaseWrapper.TableDelete(databaseWriteable, NOZZLE_TABLE_NAME, NozzlesORM.NOZZLE_IS_TEMPORARY+"=? AND "+NozzlesORM.NOZZLE_NEWLY_CREATED+"=? AND "+NozzlesORM.NOZZLE_IS_DRAFTED+"=?", args);
    }

    public static int getLastNozzleRow(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase databaseWriteable = databaseWrapper.getReadableDatabase();
        Cursor cursor = databaseWriteable.rawQuery("SELECT * FROM " + NozzlesORM.NOZZLE_TABLE_NAME + " ORDER BY "+ NOZZLE_ID + " DESC LIMIT 1", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int tempId = cursor.getInt(0);
            nozzleCounter = tempId;
            nozzleCounter++;
        }

        return nozzleCounter;
    }

    public static List<InspectionsModel.Stations.Tanks.Nozzles> getNozzles(Context context, String TankId) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Log.d("Error", TankId);
        Cursor cursor = database.rawQuery("SELECT * FROM " + NozzlesORM.NOZZLE_TABLE_NAME + " WHERE " + NozzlesORM.TANK_ID + "="+TankId+
                " AND " +NozzlesORM.NOZZLE_IS_TEMPORARY+"="+0, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<InspectionsModel.Stations.Tanks.Nozzles> nozzlesList = new ArrayList<InspectionsModel.Stations.Tanks.Nozzles>();

        Log.d("Checkrow", String.valueOf(cursor.getCount()));
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                InspectionsModel.Stations.Tanks.Nozzles dataRow = cursorToNozzles(cursor);
                String nozzleWhereClause = cursor.getString(0);
                makeNozzleTemporary(context, nozzleWhereClause);
                nozzlesList.add(dataRow);
                cursor.moveToNext();
            }
            Log.i(TAG, "Inspections loaded successfully.");
        }
        else
        {
            Cursor cursor2 = database.rawQuery("SELECT * FROM " + NozzlesORM.NOZZLE_TABLE_NAME + " WHERE " + NozzlesORM.TANK_ID + "="+TankId+
                    " AND " +NozzlesORM.NOZZLE_IS_TEMPORARY+"="+1, null);

            Log.d("Checkrow2", cursor2.getCount()+"");
            if(cursor2.getCount() > 0)
            {
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    InspectionsModel.Stations.Tanks.Nozzles dataRow1 = cursorToNozzlesTemporary(cursor2);
                    nozzlesList.add(dataRow1);
                    cursor2.moveToNext();
                }
            }
        }

        database.close();

        return nozzlesList;
    }

    private static InspectionsModel.Stations.Tanks.Nozzles cursorToNozzles(Cursor cursor)
    {
        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
        InspectionsModel.Stations.Tanks tanksModel = stationModel.new Tanks();
        InspectionsModel.Stations.Tanks.Nozzles nozzlesModel = tanksModel.new Nozzles();
        Log.d("Checknozzle1", cursor.getString(cursor.getColumnIndex(NOZZLE_ID)));
        nozzlesModel.setNozzleId(cursor.getString(cursor.getColumnIndex(NOZZLE_ID)));
        nozzlesModel.setNozzleName(cursor.getString(cursor.getColumnIndex(NOZZLE_NAME)));
        nozzlesModel.setNozzleCode(cursor.getString(cursor.getColumnIndex(NOZZLE_CODE)));
        nozzlesModel.setNozzleNumber(cursor.getString(cursor.getColumnIndex(NOZZLE_NUMBER)));
        nozzlesModel.setNozzleOpeningBalance(cursor.getString(cursor.getColumnIndex(NOZZLE_OPENING_BALANCE)));
        nozzlesModel.setNozzleMaximum(cursor.getString(cursor.getColumnIndex(NOZZLE_MAXIMUM)));
        nozzlesModel.setNozzlePreviousReading(cursor.getString(cursor.getColumnIndex(NOZZLE_PREVIOUS_READING)));
        nozzlesModel.setNozzleCurrentReading("");
        nozzlesModel.setNozzleProductType(cursor.getString(cursor.getColumnIndex(NOZZLE_PRODUCT_NAME)));
        nozzlesModel.setNozzleDefected(cursor.getString(cursor.getColumnIndex(NOZZLE_DEFECTED)));
        nozzlesModel.setNozzleReset(cursor.getString(cursor.getColumnIndex(NOZZLE_RESET)));
        nozzlesModel.setNozzleSpecialReading(cursor.getString(cursor.getColumnIndex(NOZZLE_SPECIAL_READING)));
        nozzlesModel.setNozzleSpecialRemarks(cursor.getString(cursor.getColumnIndex(NOZZLE_SPECIAL_REMARKS)));
        nozzlesModel.setNozzleNewlyCreated(cursor.getString(cursor.getColumnIndex(NOZZLE_NEWLY_CREATED)));
        nozzlesModel.setNozzleCreationDate(cursor.getString(cursor.getColumnIndex(NOZZLE_CREATION_DATE)));
        nozzlesModel.setNozzleCreatedBy(cursor.getString(cursor.getColumnIndex(NOZZLE_CREATED_BY)));

        return nozzlesModel;
    }

    private static InspectionsModel.Stations.Tanks.Nozzles cursorToNozzlesTemporary(Cursor cursor)
    {
        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
        InspectionsModel.Stations.Tanks tanksModel = stationModel.new Tanks();
        InspectionsModel.Stations.Tanks.Nozzles nozzlesModel = tanksModel.new Nozzles();
        Log.d("Checknozzle2", cursor.getString(cursor.getColumnIndex(NOZZLE_ID)));
        nozzlesModel.setNozzleId(cursor.getString(cursor.getColumnIndex(NOZZLE_ID)));
        nozzlesModel.setNozzleName(cursor.getString(cursor.getColumnIndex(NOZZLE_NAME)));
        nozzlesModel.setNozzleCode(cursor.getString(cursor.getColumnIndex(NOZZLE_CODE)));
        nozzlesModel.setNozzleNumber(cursor.getString(cursor.getColumnIndex(NOZZLE_NUMBER)));
        nozzlesModel.setNozzleOpeningBalance(cursor.getString(cursor.getColumnIndex(NOZZLE_OPENING_BALANCE)));
        nozzlesModel.setNozzleMaximum(cursor.getString(cursor.getColumnIndex(NOZZLE_MAXIMUM)));
        nozzlesModel.setNozzlePreviousReading(cursor.getString(cursor.getColumnIndex(NOZZLE_PREVIOUS_READING)));
        nozzlesModel.setNozzleCurrentReading(cursor.getString(cursor.getColumnIndex(NOZZLE_CURRENT_READING)));
        nozzlesModel.setNozzleProductType(cursor.getString(cursor.getColumnIndex(NOZZLE_PRODUCT_NAME)));
        nozzlesModel.setNozzleDefected(cursor.getString(cursor.getColumnIndex(NOZZLE_DEFECTED)));
        nozzlesModel.setNozzleReset(cursor.getString(cursor.getColumnIndex(NOZZLE_RESET)));
        nozzlesModel.setNozzleSpecialReading(cursor.getString(cursor.getColumnIndex(NOZZLE_SPECIAL_READING)));
        nozzlesModel.setNozzleSpecialRemarks(cursor.getString(cursor.getColumnIndex(NOZZLE_SPECIAL_REMARKS)));
        nozzlesModel.setNozzleNewlyCreated(cursor.getString(cursor.getColumnIndex(NOZZLE_NEWLY_CREATED)));
        nozzlesModel.setNozzleCreationDate(cursor.getString(cursor.getColumnIndex(NOZZLE_CREATION_DATE)));
        nozzlesModel.setNozzleCreatedBy(cursor.getString(cursor.getColumnIndex(NOZZLE_CREATED_BY)));

        return nozzlesModel;
    }

    protected static void truncateNozzlesTable(SQLiteDatabase database)
    {
        database.delete(NOZZLE_TABLE_NAME, null, null);
    }

    public static void dropNozzlesTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase writeDatabase = databaseWrapper.getWritableDatabase();
        databaseWrapper.TableDrop(writeDatabase, NOZZLE_TABLE_NAME);
    }

    public static long countNozzlesTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, NOZZLE_TABLE_NAME);

        return count;
    }
}
