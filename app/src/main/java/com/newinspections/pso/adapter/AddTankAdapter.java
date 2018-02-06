package com.newinspections.pso.adapter;

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

import com.newinspections.pso.R;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.utils.OnItemClickListener;

import java.util.List;

public class AddTankAdapter extends RecyclerView.Adapter<AddTankAdapter.RecyclerViewHolder> {


    private static final String TAG = "AddTankAdapter";
    Context c;
    InspectionDTO.Station.A aData;
    OnItemClickListener.OnItemClickCallback onItemClickCallback,onDateClickCallback;
    List<InspectionDTO.Station.Tank>  list;
    public AddTankAdapter(Context c,InspectionDTO.Station.A aData, List<InspectionDTO.Station.Tank>  list, OnItemClickListener.OnItemClickCallback onItemClickCallback, OnItemClickListener.OnItemClickCallback onDateClickCallback)
    {
        this.c = c;
        this.list = list;
        this.onItemClickCallback=onItemClickCallback;
        this.onDateClickCallback=onDateClickCallback;
        this.aData=aData;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tank, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        InspectionDTO.Station.Tank item = list.get(position);
        holder.setTitle(item.getTankName());
        holder.setDate(item.getRemarkDate());
        holder.setFlush(item.getTankId(),item.getFlush());
        holder.setOpeningBalance(item.getOpeningBalance(),item.getTankId(),item.getFlush());
        holder.setReading(item.getTankCurrentData());
        holder.setMaxTank(item.getTankMaximumValue(),item.getTankId());
        holder.setSpinner(item.getTankType(),item.getTankId(),item.getFlush());
        holder.setComment(item.getRemark());
        holder.setClickOnItem();
//        edited by exd
        holder.setPreviousReading(item.getPreviousBalance());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView tv_title,tv_date, et_max_tank, tv_previous_reading;
        EditText et_opening_balance,et_reading,et_comment;
        Spinner spinner_type;
        CheckBox cb_flush;
        Button btn_nozel;
        LinearLayout ll_comment_layout,ll_opening_bal,ll_tank_max,ll_previous_reading ;
        private boolean onBind;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            spinner_type = (Spinner) itemView.findViewById(R.id.spinner_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            et_opening_balance = (EditText) itemView.findViewById(R.id.et_opening_balance);
            et_reading = (EditText) itemView.findViewById(R.id.et_reading);
            et_max_tank = (EditText) itemView.findViewById(R.id.et_max_tank);
            et_comment = (EditText) itemView.findViewById(R.id.et_comment);
            cb_flush = (CheckBox) itemView.findViewById(R.id.cb_flush);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            btn_nozel = (Button) itemView.findViewById(R.id.btn_nozel);
            ll_comment_layout = (LinearLayout) itemView.findViewById(R.id.ll_comment_layout);
            ll_opening_bal = (LinearLayout) itemView.findViewById(R.id.ll_opening_bal);
            ll_tank_max = (LinearLayout) itemView.findViewById(R.id.ll_tank_max);
//            edited by exd
            tv_previous_reading = (TextView) itemView.findViewById(R.id.tv_previous_reading);
            ll_previous_reading = (LinearLayout) itemView.findViewById(R.id.ll_previous_reading);

            cb_flush.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!onBind) {
                if (isChecked)
                    list.get(getAdapterPosition()).setFlush("1");
                else
                    list.get(getAdapterPosition()).setFlush("0");
                notifyDataSetChanged();
            }
        }


        public void setTitle(String tankName) {
            if (null == tv_title) return;
            tv_title.setText(tankName);
        }
        public void setDate(String date) {
            if (TextUtils.isEmpty(date))
                date="Select Date";

            if (null == tv_date) return;
            tv_date.setText(date);
            tv_date.setMovementMethod(LinkMovementMethod.getInstance());
            SpannableString content = new SpannableString(date);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv_date.setText(content);
            tv_date.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onDateClickCallback));
        }
        public void setComment(String comment) {
            if (null == et_comment) return;
            et_comment.setText(comment);
            et_comment.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        list.get(getAdapterPosition()).setRemark("");
                    } else{
                        list.get(getAdapterPosition()).setRemark(s.toString());
                    }
                }
            });
        }
        public void setFlush(String tankId, String flush) {

            if (null == cb_flush)
                return;

            if (TextUtils.isEmpty(tankId)){
                cb_flush.setVisibility(View.GONE);
            }else {
                cb_flush.setVisibility(View.VISIBLE);
            }
            onBind = true;
            Log.d("Flush Value", flush);
            if (!TextUtils.isEmpty(flush) && flush.equals("1")){
                cb_flush.setChecked(true);
                ll_comment_layout.setVisibility(View.VISIBLE);
            }else{
                cb_flush.setChecked(false);
                ll_comment_layout.setVisibility(View.GONE);
            }
            onBind = false;
        }

        public void setOpeningBalance(String balance,String tankId,String flush) {
            if (null == et_opening_balance) return;

            if (TextUtils.isEmpty(tankId)){
                ll_opening_bal.setVisibility(View.VISIBLE);
            }else {
               if (!TextUtils.isEmpty(flush) && flush.equals("1")){
                   ll_opening_bal.setVisibility(View.VISIBLE);
               }else {
                   ll_opening_bal.setVisibility(View.GONE);
               }
            }

            if (ll_opening_bal.getVisibility()==View.VISIBLE){
                et_opening_balance.setText(balance);
                et_opening_balance.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            list.get(getAdapterPosition()).setOpeningBalance("");
                        } else{
                            list.get(getAdapterPosition()).setOpeningBalance(s.toString());
                        }
                    }
                });
            }
        }

        public void setReading(String reading) {
            if (null == et_reading) return;
            et_reading.setText(reading);
            et_reading.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        list.get(getAdapterPosition()).setTankCurrentData("");
                    } else{
                        list.get(getAdapterPosition()).setTankCurrentData(s.toString());
                    }
                }
            });
        }

//        edited by exd
        public void setPreviousReading(String prvReading)
        {
            if (null == tv_previous_reading)
                return;
            if (!TextUtils.isEmpty(prvReading) && Float.parseFloat(prvReading)>0){
                tv_previous_reading.setText(prvReading);
                ll_previous_reading.setVisibility(View.VISIBLE);
            }else {
                tv_previous_reading.setText("");
                ll_previous_reading.setVisibility(View.GONE);
            }
        }

//        edited by exd
        public void setMaxTank(String tank,String tankId) {
            int isEditable = 0;
//            Log.d("exd123", "Tank value "+tank);
            if (null == et_max_tank || ll_tank_max==null) return;
            if (TextUtils.isEmpty(tankId)){
                ll_tank_max.setVisibility(View.VISIBLE);
                isEditable = 1;
            }
            else
            {
                Log.d("exd", "Tank value "+tank);
                if (!TextUtils.isEmpty(tank) && Float.parseFloat(tank)>0){
                    ll_tank_max.setVisibility(View.VISIBLE);
                    isEditable = 0;

                }else {
                    ll_tank_max.setVisibility(View.VISIBLE);
                    isEditable = 1;
                }
            }
            if (ll_tank_max.getVisibility()==View.VISIBLE)
            {
                Log.d("exd", ""+isEditable);
                if (isEditable==0)
                {
                    et_max_tank.setText(tank);
                    et_max_tank.setEnabled(false);
                }
                else
                {
                    et_max_tank.setText(tank);
                    et_max_tank.setEnabled(true);
                    et_max_tank.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }
                        public void afterTextChanged(Editable s) {
                            if (s.length() == 0) {
                                list.get(getAdapterPosition()).setTankMaximumValue("");
                            } else{
                                list.get(getAdapterPosition()).setTankMaximumValue(s.toString());
                            }
                        }
                    });
                }
            }
        }

        public void setSpinner(String status, String tankId, final String flush) {
            if (null == spinner_type) return;
            if (!TextUtils.isEmpty(tankId) && flush.equals("0")){
                spinner_type.setEnabled(false);
            }else {
                spinner_type.setEnabled(true);
            }

            switch (status){
                case "HSD":
                    spinner_type.setSelection(1);
                    break;
                case "PMG":
                    spinner_type.setSelection(2);
                    break;
                case "HOBC":
                    spinner_type.setSelection(3);
                    break;
                default: spinner_type.setSelection(0);
            }
            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if (!TextUtils.isEmpty(flush) && flush.equals("1")){
                        if (TextUtils.isEmpty(list.get(getAdapterPosition()).getOldTankType())){
                            list.get(getAdapterPosition()).setOldTankType(list.get(getAdapterPosition()).getTankType());
                        }
                    }

                    Log.i("onItemSelected","onItemSelected "+i);
                    switch (i){
                        case 0:
                            list.get(getAdapterPosition()).setTankType("");
                            break;
                        case 1:
                            list.get(getAdapterPosition()).setTankType(spinner_type.getSelectedItem().toString());
                            break;
                        case 2:
                            list.get(getAdapterPosition()).setTankType(spinner_type.getSelectedItem().toString());
                            break;
                        case 3:
                            list.get(getAdapterPosition()).setTankType(spinner_type.getSelectedItem().toString());
                            break;
                    }
                    refreshNozzlesName(getAdapterPosition());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        public void setClickOnItem(){
            if (btn_nozel==null)
                return;
            btn_nozel.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onItemClickCallback));
        }
    }


    private void refreshNozzlesName(int position){

        if (list.get(position).getNozzle()!=null){
            if (list.get(position).getNozzle().size()>0){
                for (int i=0;i<list.get(position).getNozzle().size();i++){
                    list.get(position).getNozzle().get(i).setDumeterName(list.get(position).getTankType()+" "+(i+1));
                    list.get(position).getNozzle().get(i).setDumeterType(list.get(position).getTankType());
                }
            }
        }

        list.get(position).getTankType();

    }
}
