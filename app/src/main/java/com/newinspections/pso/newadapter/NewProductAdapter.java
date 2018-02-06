package com.newinspections.pso.newadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.model.ProductsModel;

import java.util.List;

/**
 * Created by Exd on 1/24/2018.
 */

public class NewProductAdapter extends BaseAdapter {
    Context context;
    List<ProductsModel.Products> products;

    public NewProductAdapter(Context context, List<ProductsModel.Products> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cstm_sppnr_layout, parent, false);
        }
        TextView tv = (TextView) v.findViewById(R.id.txtSppnr);
//        tv.setVisibility(View.VISIBLE);
        tv.setText(products.get(position).getProductsName());

        return v;
    }
}
