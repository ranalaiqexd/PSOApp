package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.CalculationDTO;

import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.ViewHolder> {
    private List<CalculationDTO> arrayList;
    private Context context;
    public CalculationAdapter(Context context, List<CalculationDTO> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calculation, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CalculationDTO item=arrayList.get(position);
        holder.setTitle(item.getTitle());
        holder.setDescription(item.getDescription());
        holder.setHSD(item.getHsd());
        holder.setPMG(item.getPmg());
        holder.setHDBC(item.getHobc());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_description,tv_hsd,tv_pmg,tv_hdbc;
        public ViewHolder(View v) {
            super(v);
            this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            this.tv_description = (TextView) v.findViewById(R.id.tv_description);
            this.tv_hsd = (TextView) v.findViewById(R.id.tv_hsd);
            this.tv_pmg = (TextView) v.findViewById(R.id.tv_pmg);
            this.tv_hdbc = (TextView) v.findViewById(R.id.tv_hdbc);
        }
        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title);
        }
        public void setDescription(String description) {
            if (null == tv_description) return;
            tv_description.setText(description);
        }
        public void setHSD(String hsd) {
            if (null == tv_hsd) return;
            tv_hsd.setText(hsd);
        }
        public void setPMG(String pmg) {
            if (null == tv_pmg) return;
            tv_pmg.setText(pmg);
        }
        public void setHDBC(String hdbc) {
            if (null == tv_hdbc) return;
            tv_hdbc.setText(hdbc);
        }
    }
}

