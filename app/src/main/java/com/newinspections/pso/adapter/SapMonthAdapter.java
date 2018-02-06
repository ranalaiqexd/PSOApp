package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.utils.OnItemClickListener;

import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class SapMonthAdapter extends RecyclerView.Adapter<SapMonthAdapter.ViewHolder> {
    private List<List<InspectionDTO.Station.Purchase.Total>> arrayList;
    private Context context;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    public  String type;
    public SapMonthAdapter(Context context, List<List<InspectionDTO.Station.Purchase.Total>> arrayList,String type, OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.arrayList = arrayList;
        this.context = context;
        this.type=type;
        this.onItemClickCallback=onItemClickCallback;
    }

    public  String getType(){
        return type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sales, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position==0){
            holder.setNo("No.");
            holder.setMonth("Month");
            holder.setPurchases("Purchases");
        }else {
            holder.setNo(position+"");
            holder.setMonth("");
            holder.setPurchases("");
            holder.setClickOnItem();
            List<InspectionDTO.Station.Purchase.Total> item=arrayList.get(position);
            if (item!=null){
                for (int i=0;i<item.size();i++){
                    if (item.get(i).getProduct().equalsIgnoreCase(type)){
                        holder.setMonth(item.get(i).getMonth()+"");
                        holder.setPurchases(item.get(i).getQuantity()+"");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_no,tv_month,tv_purchases;
        RelativeLayout rl_out;
        public ViewHolder(View v) {
            super(v);
            this.tv_no = (TextView) v.findViewById(R.id.tv_no);
            this.tv_month = (TextView) v.findViewById(R.id.tv_month);
            this.tv_purchases = (TextView) v.findViewById(R.id.tv_purchases);
            this.rl_out = (RelativeLayout) v.findViewById(R.id.rl_out);
        }
        public void setNo(String title) {
            if (null == tv_no) return;
            tv_no.setText(title);
        }
        public void setMonth(String description) {
            if (null == tv_month) return;
            tv_month.setText(description);
        }
        public void setPurchases(String hsd) {
            if (null == tv_purchases) return;
            tv_purchases.setText(hsd);
        }
        public void setClickOnItem(){
            if (rl_out==null)
                return;
            rl_out.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onItemClickCallback));
        }
    }
}

