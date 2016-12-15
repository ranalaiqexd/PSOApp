package com.inspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inspections.pso.R;
import com.inspections.pso.dto.CalculationDTO;
import com.inspections.pso.dto.MyInspectionDTO;
import com.inspections.pso.utils.OnItemClickListener;
import com.inspections.pso.utils.Utils;

import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class MyInspectionAdapter extends RecyclerView.Adapter<MyInspectionAdapter.ViewHolder> {
    private List<MyInspectionDTO.Response> arrayList;
    private Context context;

    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    public MyInspectionAdapter(Context context, List<MyInspectionDTO.Response> arrayList,OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.arrayList = arrayList;
        this.context = context;
        this.onItemClickCallback=onItemClickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_inspection, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MyInspectionDTO.Response item=arrayList.get(position);
        holder.setTitle(item.getStationsDetalis().getStationName());
        holder.setDate(Utils.getDateLogByFormat(item.getStationsDetalis().getInspectionTimestamp()));
        holder.setTime(Utils.getTimeLogByFormat(item.getStationsDetalis().getInspectionTimestamp()));
        holder.setSapCode(item.getStationsDetalis().getSapCode());
        holder.setClick();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_date,tv_time,tv_sap_code;
        RelativeLayout rl_main;

        public ViewHolder(View v) {
            super(v);
            this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            this.tv_date = (TextView) v.findViewById(R.id.tv_date);
            this.tv_time = (TextView) v.findViewById(R.id.tv_time);
            this.tv_sap_code = (TextView) v.findViewById(R.id.tv_sap_code);
            this.rl_main = (RelativeLayout) v.findViewById(R.id.rl_main);
        }
        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title);
        }
        public void setDate(String date) {
            if (null == tv_date) return;
            tv_date.setText(date);
        }
        public void setTime(String time) {
            if (null == tv_time) return;
            tv_time.setText(time);
        }
        public void setSapCode(String sapCode) {
            if (null == tv_sap_code) return;
            tv_sap_code.setText(sapCode);
        }
        public void setClick() {
            if (null == rl_main) return;
            rl_main.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onItemClickCallback));
        }
    }
}

