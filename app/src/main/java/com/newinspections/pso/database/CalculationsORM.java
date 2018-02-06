//package com.newinspections.pso.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.DatabaseUtils;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.newinspections.pso.model.InspectionsModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Exd on 10/19/2017.
// */
//
//public class CalculationsORM {
//
//    DatabaseWrapper databaseWrapper;
//    SQLiteDatabase database;
//    private static int calculationCounter = 1;
//
//    private static final String TAG = "CalculationsORM";
//
//    private static final String CALCULATION_TABLE_NAME = "calculations";
//
//    private static final String COMMA_SEP = ", ";
//
//    private static final String CALCULATION_ID_TYPE = "INTEGER PRIMARY KEY";
//    private static final String CALCULATION_ID = "calculation_id";
//
//    private static final String CALCULATION_PRODUCT_NAME_TYPE = "TEXT";
//    private static final String CALCULATION_PRODUCT_NAME = "calculation_product_name";
//
//    private static final String CALCULATION_A_TYPE = "REAL";
//    private static final String CALCULATION_A = "calculation_a_name";
//
//    private static final String CALCULATION_B_TYPE = "REAL";
//    private static final String CALCULATION_B = "calculation_b_name";
//
//    private static final String CALCULATION_C_TYPE = "REAL";
//    private static final String CALCULATION_C = "calculation_c_name";
//
//    private static final String CALCULATION_D_TYPE = "REAL";
//    private static final String CALCULATION_D = "calculation_d_name";
//
//    private static final String CALCULATION_E_TYPE = "REAL";
//    private static final String CALCULATION_E = "calculation_e_name";
//
//    private static final String CALCULATION_F_TYPE = "REAL";
//    private static final String CALCULATION_F = "calculation_f_name";
//
//    private static final String CALCULATION_RESULT_TYPE = "REAL";
//    private static final String CALCULATION_RESULT = "calculation_result_name";
//
//    private static final String STATION_ID_TYPE = "INTEGER";
//    private static final String STATION_ID = "station_id";
//
//
//    public static final String SQL_CREATE_CALCULATION_TABLE =
//            "CREATE TABLE " + CALCULATION_TABLE_NAME + " (" +
//                    CALCULATION_ID + " " + CALCULATION_ID_TYPE + COMMA_SEP +
//                    CALCULATION_PRODUCT_NAME  + " " + CALCULATION_PRODUCT_NAME_TYPE + COMMA_SEP +
//                    CALCULATION_A + " " + CALCULATION_A_TYPE + COMMA_SEP +
//                    CALCULATION_B + " " + CALCULATION_B_TYPE + COMMA_SEP +
//                    CALCULATION_C + " " + CALCULATION_C_TYPE + COMMA_SEP +
//                    CALCULATION_D + " " + CALCULATION_D_TYPE + COMMA_SEP +
//                    CALCULATION_E + " " + CALCULATION_E_TYPE + COMMA_SEP +
//                    CALCULATION_F + " " + CALCULATION_F_TYPE + COMMA_SEP +
//                    CALCULATION_RESULT + " " + CALCULATION_RESULT_TYPE + COMMA_SEP +
//                    STATION_ID + " " + STATION_ID_TYPE +
//                    ")";
//
//    public static final String SQL_DROP_CALCULATION_TABLE =
//            "DROP TABLE IF EXISTS " + CALCULATION_TABLE_NAME;
//
//    protected static void insertCalculationsToDb(Context context, SQLiteDatabase database, List<InspectionsModel.Stations.ProductCalculation> inspectionProductCalculations) {
//
//        for (int i=0; i<inspectionProductCalculations.size(); i++)
//        {
//            ContentValues values = ProductCalculationsToContentValues(inspectionProductCalculations,i);
//            long calculationTableId = database.insert(CalculationsORM.CALCULATION_TABLE_NAME, "null", values);
//        }
//    }
//
//    private static ContentValues ProductCalculationsToContentValues(List<InspectionsModel.Stations.ProductCalculation> inspectionProductCalculations, int index) {
//        ContentValues values = new ContentValues();
//        values.put(CalculationsORM.CALCULATION_ID, calculationCounter);
//        calculationCounter++;
//        values.put(CalculationsORM.CALCULATION_PRODUCT_NAME, inspectionProductCalculations.get(index).getProductName());
//        values.put(CalculationsORM.CALCULATION_A, inspectionProductCalculations.get(index).getA());
//        values.put(CalculationsORM.CALCULATION_B, inspectionProductCalculations.get(index).getB());
//        values.put(CalculationsORM.CALCULATION_C, inspectionProductCalculations.get(index).getC());
//        values.put(CalculationsORM.CALCULATION_D, inspectionProductCalculations.get(index).getD());
//        values.put(CalculationsORM.CALCULATION_E, inspectionProductCalculations.get(index).getE());
//        values.put(CalculationsORM.CALCULATION_F, inspectionProductCalculations.get(index).getF());
//        values.put(CalculationsORM.CALCULATION_RESULT, inspectionProductCalculations.get(index).getResult());
//        values.put(CalculationsORM.STATION_ID, inspectionProductCalculations.get(index).getStationId());
////        Log.i("CALCULATIONsSizeId", String.valueOf(inspectionStations.get(index).getStationId()));
//
//        return values;
//    }
//
//    public static void truncateCalculationTable(Context context)
//    {
//        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
//        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
//        if (database!=null)
//        {
//            database.delete(CALCULATION_TABLE_NAME, null, null);
//        }
//    }
//
//    public static List<InspectionsModel.Stations.ProductCalculation> getCalculations(Context context, String productName) {
//        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
//        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
//
//        Cursor cursor = database.rawQuery("SELECT * FROM " + CalculationsORM.CALCULATION_TABLE_NAME + " WHERE " + CalculationsORM.CALCULATION_PRODUCT_NAME + "="+productName
//                + " ORDER BY " + CALCULATION_ID + " DESC LIMIT 1", null);
//
//        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
//        List<InspectionsModel.Stations.ProductCalculation> calculationsList = new ArrayList<InspectionsModel.Stations.ProductCalculation>();
//
//        if(cursor.getCount() > 0) {
//            cursor.moveToFirst();
//
//            while (!cursor.isAfterLast()) {
//                InspectionsModel.Stations.ProductCalculation dataRow = cursorToCalculation(cursor);
//                calculationsList.add(dataRow);
//                cursor.moveToNext();
//            }
//            Log.i(TAG, "Inspections loaded successfully.");
//        }
//
//        database.close();
//
//        return calculationsList;
//    }
//
//    private static InspectionsModel.Stations.ProductCalculation cursorToCalculation(Cursor cursor)
//    {
//        InspectionsModel.Stations stationModel = new InspectionsModel.Stations();
//        InspectionsModel.Stations.ProductCalculation calculationsModel = stationModel.new ProductCalculation();
//        calculationsModel.setA(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_A))));
//        calculationsModel.setB(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_B))));
//        calculationsModel.setC(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_C))));
//        calculationsModel.setD(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_D))));
//        calculationsModel.setE(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_E))));
//        calculationsModel.setF(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_F))));
//        calculationsModel.setResult(Double.valueOf(cursor.getString(cursor.getColumnIndex(CALCULATION_RESULT))));
//
//        return calculationsModel;
//    }
//
//    public static long countCALCULATIONsTable(Context context)
//    {
//        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
//        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
//        long count = DatabaseUtils.queryNumEntries(database, CALCULATION_TABLE_NAME);
//
//        return count;
//    }
//
//    public static String getCALCULATIONId() {
//        return CALCULATION_ID;
//    }
//
//}
