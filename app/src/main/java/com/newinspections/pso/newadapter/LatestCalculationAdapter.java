package com.newinspections.pso.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.model.CalculationModel;
import com.newinspections.pso.model.InspectionsModel;

import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class LatestCalculationAdapter extends RecyclerView.Adapter<LatestCalculationAdapter.ViewHolder> {
    private List<InspectionsModel.Stations.Calculation> arrayList;
    private Context context;
    public LatestCalculationAdapter(Context context, List<InspectionsModel.Stations.Calculation> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calculateproducts, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        InspectionsModel.Stations.Calculation item = arrayList.get(position);
        holder.setTitle(item.getTitle());
        holder.setDescription(item.getDescription());
        holder.setProduct(item.getProduct());
        holder.setValue(item.getValues());
        //holder.setHDBC(item.getHobc());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_description,tv_products,tv_values,tv_hdbc;
        public ViewHolder(View v) {
            super(v);
            this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            this.tv_description = (TextView) v.findViewById(R.id.tv_description);
            this.tv_products = (TextView) v.findViewById(R.id.tv_products);
            this.tv_values = (TextView) v.findViewById(R.id.tv_values);
        }

        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title);
        }
        public void setDescription(String description) {
            if (null == tv_description) return;
            tv_description.setText(description);
        }

        public void setProduct(String product) {
            if (null == tv_products) return;
            tv_products.setText(product);
        }
        public void setValue(String value) {
            if (null == tv_values) return;
            tv_values.setText(value);
        }
    }
}

