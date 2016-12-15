package com.inspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.inspections.pso.R;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.MyInspectionDTO;
import com.inspections.pso.utils.OnItemClickListener;

import java.util.List;

public class ViewTankAdapter extends RecyclerView.Adapter<ViewTankAdapter.RecyclerViewHolder> {


    private static final String TAG = "AddTankAdapter";
    Context c;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    List<MyInspectionDTO.Response.Tank>  list;
    public ViewTankAdapter(Context c, List<MyInspectionDTO.Response.Tank>  list, OnItemClickListener.OnItemClickCallback onItemClickCallback)
    {
        this.c = c;
        this.list = list;
        this.onItemClickCallback=onItemClickCallback;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_tank, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MyInspectionDTO.Response.Tank item = list.get(position);
        holder.setTitle(item.getTankName());
        holder.setDate(item.getRemarkDate());
        holder.setFlush(item.getTankFlush());
        holder.setOpeningBalance(item.getOpeningBalance());
        holder.setReading(item.getTankAllquantity());
        holder.setMaxTank(item.getTankMax());
        holder.setSpinner(item.getTankType());
        holder.setComment(item.getRemark());
        holder.setClickOnItem();

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView tv_title,tv_date,tv_product_type;
        TextView tv_opening_balance,tv_reading,tv_max_tank,tv_comment;
        CheckBox cb_flush;
        Button btn_nozel;
        private boolean onBind;
        LinearLayout ll_comment_layout,ll_opening_bal,ll_tank_max;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tv_product_type = (TextView) itemView.findViewById(R.id.tv_product_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_opening_balance = (TextView) itemView.findViewById(R.id.tv_opening_balance);
            tv_reading = (TextView) itemView.findViewById(R.id.tv_reading);
            tv_max_tank = (TextView) itemView.findViewById(R.id.tv_max_tank);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            cb_flush = (CheckBox) itemView.findViewById(R.id.cb_flush);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            btn_nozel = (Button) itemView.findViewById(R.id.btn_nozel);
            ll_comment_layout = (LinearLayout) itemView.findViewById(R.id.ll_comment_layout);
            ll_opening_bal = (LinearLayout) itemView.findViewById(R.id.ll_opening_bal);
            ll_tank_max = (LinearLayout) itemView.findViewById(R.id.ll_tank_max);

        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!onBind) {
                if (isChecked)
                    list.get(getAdapterPosition()).setTankFlush("1");
                else
                    list.get(getAdapterPosition()).setTankFlush("0");
                notifyDataSetChanged();
            }
        }

        public void setTitle(String tankName) {
            if (null == tv_title) return;
            tv_title.setText(tankName);
        }
        public void setDate(String date) {
            if (TextUtils.isEmpty(date))
                date="N/A";

            if (null == tv_date) return;
            tv_date.setText(date);
        }
        public void setComment(String comment) {
            if (null == tv_comment) return;
            tv_comment.setText(comment);
        }
        public void setFlush(String flush) {

            if (null == cb_flush ||ll_comment_layout==null) return;
            ll_comment_layout.setVisibility(View.GONE);
            onBind = true;
            if (flush.equals("1")){
                ll_comment_layout.setVisibility(View.VISIBLE);
                cb_flush.setChecked(true);
            }else{
                cb_flush.setChecked(false);
            }
            cb_flush.setClickable(false);
            onBind = false;

        }
        public void setOpeningBalance(String balance) {
            if (null == tv_opening_balance) return;

            if (!TextUtils.isEmpty(balance)){
                tv_opening_balance.setText(balance);
            }else {
                tv_opening_balance.setText("");
            }
        }

        public void setReading(String reading) {
            if (null == tv_reading) return;
            tv_reading.setText(reading);
        }

        public void setMaxTank(String tank) {
            if (null == tv_max_tank) return;
            if (!TextUtils.isEmpty(tank)){
                tv_max_tank.setText(tank);
            }else{
                tv_max_tank.setText("");
            }
        }

        public void setSpinner(String type) {
            if (null == tv_product_type) return;
            if (!TextUtils.isEmpty(type)){
                tv_product_type.setText(type);
            }else {
                tv_product_type.setText("");
            }
        }

        public void setClickOnItem(){
            if (btn_nozel==null)
                return;
            btn_nozel.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onItemClickCallback));
        }
    }
}
