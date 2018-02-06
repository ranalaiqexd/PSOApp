package com.newinspections.pso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newinspections.pso.model.ProductsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Exd on 1/24/2018.
 */

public class ProductsORM {

    private static final String TAG = "ProductsORM";

    private static final String PRODUCT_TABLE_NAME = "products";

    private static final String COMMA_SEP = ", ";

    private static final String PRODUCT_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String PRODUCT_ID = "product_id";

    private static final String PRODUCT_NAME_TYPE = "TEXT";
    private static final String PRODUCT_NAME = "product_name";

    public static final String SQL_CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + PRODUCT_TABLE_NAME + " (" +
                    PRODUCT_ID + " " + PRODUCT_ID_TYPE + COMMA_SEP +
                    PRODUCT_NAME  + " " + PRODUCT_NAME_TYPE +
                    ")";

    public static final String SQL_DROP_PRODUCT_TABLE =
            "DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME;

    public static void insertProductsToDb(Context context, ProductsModel productsModel) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        Log.i("ProductsSize", String.valueOf(productsModel.getProducts().size()));

        for (int i=0; i<productsModel.getProducts().size(); i++)
        {
            ContentValues values = productsToContentValues(productsModel, i);
            long productTableId = database.insert(ProductsORM.PRODUCT_TABLE_NAME, "null", values);
            Log.i("Database", "Inserted new Product with ID: " + productTableId);

            Log.i("ProductId", String.valueOf(productsModel.getProducts().get(i).getProductId()));
            Log.i("ProductsName", String.valueOf(productsModel.getProducts().get(i).getProductsName()));
        }

        Log.d("Database", "Database is closing");
        database.close();
    }

    private static ContentValues productsToContentValues(ProductsModel productsModel, int index) {
        ContentValues values = new ContentValues();
        values.put(ProductsORM.PRODUCT_ID, productsModel.getProducts().get(index).getProductId());
        values.put(ProductsORM.PRODUCT_NAME, productsModel.getProducts().get(index).getProductsName());

        return values;
    }

    public static void truncateProductsTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        if (database!=null)
        {
            database.delete(PRODUCT_TABLE_NAME, null, null);
        }
    }

    public static void dropProductTable(Context context)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase writeDatabase = databaseWrapper.getWritableDatabase();
        databaseWrapper.TableDrop(writeDatabase, PRODUCT_TABLE_NAME);
    }

    public static List<ProductsModel.Products> getProducts(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + ProductsORM.PRODUCT_TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<ProductsModel.Products> productsList = new ArrayList<ProductsModel.Products>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                ProductsModel.Products dataRow = cursorToProduct(cursor);
                productsList.add(dataRow);
                cursor.moveToNext();
            }
            Log.i(TAG, "Inspections loaded successfully.");
        }

        database.close();

        return productsList;
    }

    private static ProductsModel.Products cursorToProduct(Cursor cursor)
    {
        ProductsModel.Products productsModel = new ProductsModel.Products();
        productsModel.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
        productsModel.setProductsName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));

        return productsModel;
    }
}
