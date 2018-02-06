package com.newinspections.pso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Exd on 10/19/2017.
 */

public class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";

    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");
        db.execSQL(ProductsORM.SQL_CREATE_PRODUCT_TABLE);
        db.execSQL(StationORM.SQL_CREATE_STATION_TABLE);
        db.execSQL(PurchaseORM.SQL_CREATE_PURCHASE_TABLE);
        db.execSQL(TanksORM.SQL_CREATE_TANK_TABLE);
        db.execSQL(NozzlesORM.SQL_CREATE_NOZZLE_TABLE);
    }

//    public void TableUpdate(SQLiteDatabase db, String tablename, ContentValues data, String whereClause)
//    {
//        db.update(tablename, data, whereClause, null);
//    }

    public void TableUpdate(SQLiteDatabase db, String tablename, ContentValues data, String whereClause, String[] whereArgs)
    {
        int result = db.update(tablename, data, whereClause, whereArgs);
        Log.d("dbupdate", result+"");
    }

    public void TableDelete(SQLiteDatabase db, String tablename, String whereClause, String[] whereArgs)
    {
        int result = db.delete(tablename, whereClause, whereArgs);
        Log.d("dbdelete", result+"");
    }

    public void TableDrop(SQLiteDatabase db, String tableName)
    {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");

        db.execSQL(StationORM.SQL_DROP_STATION_TABLE);
        db.execSQL(TanksORM.SQL_DROP_TANK_TABLE);
        db.execSQL(NozzlesORM.SQL_DROP_NOZZLE_TABLE);
        db.execSQL(ProductsORM.SQL_DROP_PRODUCT_TABLE);
        db.execSQL(PurchaseORM.SQL_DROP_PURCHASE_TABLE);
        onCreate(db);
    }
}
