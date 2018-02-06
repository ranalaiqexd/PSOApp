package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.MyInspectionDTO;

import java.util.List;

public class ViewNozzelsAdapter extends RecyclerView.Adapter<ViewNozzelsAdapter.RecyclerViewHolder> {


    private static final String TAG = "ViewNozzelsAdapter";
    Context c;
    List<MyInspectionDTO.Response.Tank.Nozzle> list;

    public ViewNozzelsAdapter(Context c, List<MyInspectionDTO.Response.Tank.Nozzle> list)
    {
        this.c = c;
        this.list = list;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_nozzels, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MyInspectionDTO.Response.Tank.Nozzle item = list.get(position);
        holder.setTitle(item.getDumeterName());
        holder.setDefected(item.getDumeterDefect());
        holder.setOpeningBalance(item.getOpeningBalanceD());
        holder.setCurrentReading(item.getDumeterAllquantity());
        holder.setPreviousReading(item.getDumeterPreviousVolume());
        holder.setMaxTank(item.getDumeterMaxValue());
        holder.setSpinner(item.getDumeterType());
        holder.setSpecialReading(item.getSpecialReadingReset());
        holder.setCommentsReset(item.getCommentsReset());
        holder.setReset(item.getResetStatus());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView tv_title,tv_previous_reading;
        TextView tv_opening_balance,tv_current_reading,tv_max_nozzels,tv_product_type,tv_special_reading,tv_special_comment;
        CheckBox cb_defected,cb_reset;
        LinearLayout llOpening,ll_DuMax,ll_reset_layout;
        private boolean onBind,onBindReset;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tv_product_type = (TextView) itemView.findViewById(R.id.tv_product_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_previous_reading = (TextView) itemView.findViewById(R.id.tv_previous_reading);
            tv_opening_balance = (TextView) itemView.findViewById(R.id.tv_opening_balance);
            tv_special_reading = (TextView) itemView.findViewById(R.id.tv_special_reading);
            tv_special_comment = (TextView) itemView.findViewById(R.id.tv_special_comment);
            tv_current_reading = (TextView) itemView.findViewById(R.id.tv_current_reading);
            tv_max_nozzels = (TextView) itemView.findViewById(R.id.tv_max_nozzels);
            cb_defected = (CheckBox) itemView.findViewById(R.id.cb_defected);
            cb_reset = (CheckBox) itemView.findViewById(R.id.cb_reset);
            llOpening = (LinearLayout) itemView.findViewById(R.id.llOpening);
            ll_DuMax = (LinearLayout) itemView.findViewById(R.id.ll_DuMax);
            cb_defected.setOnCheckedChangeListener(this);
            ll_reset_layout=(LinearLayout) itemView.findViewById(R.id.ll_reset_layout);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()){
                case R.id.cb_defected:
                        if(!onBind) {
                            if (isChecked)
                                list.get(getAdapterPosition()).setDumeterDefect("1");
                            else
                                list.get(getAdapterPosition()).setDumeterDefect("0");
                            notifyDataSetChanged();
                        }
                    break;
                case R.id.cb_reset:
                    if(!onBindReset) {
                        if (isChecked)
                            list.get(getAdapterPosition()).setResetStatus("1");
                        else
                            list.get(getAdapterPosition()).setResetStatus("0");
                        notifyDataSetChanged();
                    }
                    break;
            }

        }


        public void setTitle(String nozzleName) {
            if (null == tv_title) return;
            tv_title.setText(nozzleName);
        }
        public void setPreviousReading(String reading) {
            if (null == tv_previous_reading) return;
            if (!TextUtils.isEmpty(reading) && Float.parseFloat(reading)>0){
                tv_previous_reading.setText(reading);
            }else {
                tv_previous_reading.setText("");
            }
        }
        public void setDefected(String defected) {

            if (null == cb_defected ) return;

            onBind = true;
            if (defected.equals("1")){
                cb_defected.setChecked(true);
            }else{
                cb_defected.setChecked(false);
            }
            cb_defected.setClickable(false);
            onBind = false;
        }
        public void setReset(String reset) {

            if (null == cb_reset ) return;
            onBindReset = true;
            if (reset.equals("1")){
                ll_reset_layout.setVisibility(View.VISIBLE);
                cb_reset.setChecked(true);
            }else{
                ll_reset_layout.setVisibility(View.GONE);
                cb_reset.setChecked(false);
            }
            cb_reset.setClickable(false);
            onBindReset = false;
        }
        public void setOpeningBalance(String balance) {
            if (null == tv_opening_balance) return;

            if (TextUtils.isEmpty(balance)){
                tv_opening_balance.setText("");
            }else {
                tv_opening_balance.setText(balance);
            }
        }
        public void setSpecialReading(String reading) {
            if (null == tv_special_reading || ll_reset_layout==null) return;
            if (TextUtils.isEmpty(reading)){
                tv_special_reading.setText("");
            }else {
                tv_special_reading.setText(""+reading);
            }
        }
        public void setCommentsReset(String comment) {
            if (null == tv_special_comment) return;
            if (TextUtils.isEmpty(comment)){
                tv_special_comment.setText("");
            }else {
                tv_special_comment.setText(comment);
            }
        }
        public void setCurrentReading(String reading) {
            if (null == tv_current_reading) return;

            if (!TextUtils.isEmpty(reading))
                tv_current_reading.setText(reading);
        }

        public void setMaxTank(String tank) {
            if (null == tv_max_nozzels) return;

            if (TextUtils.isEmpty(tank)){
                tv_max_nozzels.setText("");
            }else {
                tv_max_nozzels.setText(tank);
            }
        }

        public void setSpinner(String status) {
            if (null == tv_product_type) return;
            tv_product_type.setText(status);
        }
    }
}
