package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.InspectionDTO;

import java.util.List;

public class AddNozzelsAdapter extends RecyclerView.Adapter<AddNozzelsAdapter.RecyclerViewHolder> {


    private static final String TAG = "AddNozzelsAdapter";
    Context c;
    List<InspectionDTO.Station.Tank.Nozzle> list;
    InspectionDTO.Station.Tank tank;
    public AddNozzelsAdapter(Context c, InspectionDTO.Station.Tank tank, List<InspectionDTO.Station.Tank.Nozzle> list)
    {
        this.c = c;
        this.list = list;
        this.tank=tank;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nozzels, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        InspectionDTO.Station.Tank.Nozzle item = list.get(position);
        item.setDumeterType(tank.getTankType());
        holder.setTitle(item.getDumeterName());
        holder.setDefected(item.getDumeterDefect());
        holder.setOpeningBalance(item.getOpeningBalanceD(),item.getDumeterId());
        holder.setCurrentReading(item.getDumeterCurrentVolume());
        holder.setPreviousReading(item.getDumeterPreviousVolume());
        holder.setMaxTank(item.getDumeterMaxValue(),item.getDumeterId());
        holder.setSpinner(item.getDumeterType());
        holder.setSpecialReading(item.getSpecialReading());
        holder.setCommentsReset(item.getCommentsReset());
        holder.setReset(item.getResetStatus(),item.getDumeterId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView tv_title,tv_previous_reading;
        EditText et_opening_balance,et_current_reading,et_max_nozzels,et_special_reading,et_special_comment;
        Spinner spinner_type;
        CheckBox cb_defected,cb_reset;
        LinearLayout llOpening,ll_DuMax,ll_reset_layout;
        private boolean onBind;
        private boolean onBindReset;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            spinner_type = (Spinner) itemView.findViewById(R.id.spinner_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_previous_reading = (TextView) itemView.findViewById(R.id.tv_previous_reading);
            et_opening_balance = (EditText) itemView.findViewById(R.id.et_opening_balance);
            et_current_reading = (EditText) itemView.findViewById(R.id.et_current_reading);
            et_max_nozzels = (EditText) itemView.findViewById(R.id.et_max_nozzels);
            et_special_reading = (EditText) itemView.findViewById(R.id.et_special_reading);
            et_special_comment = (EditText) itemView.findViewById(R.id.et_special_comment);
            cb_defected = (CheckBox) itemView.findViewById(R.id.cb_defected);
            cb_reset = (CheckBox) itemView.findViewById(R.id.cb_reset);
            ll_reset_layout = (LinearLayout) itemView.findViewById(R.id.ll_reset_layout);
            llOpening = (LinearLayout) itemView.findViewById(R.id.llOpening);
            ll_DuMax = (LinearLayout) itemView.findViewById(R.id.ll_DuMax);
            cb_defected.setOnCheckedChangeListener(this);
            cb_reset.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

           switch ( buttonView.getId()){
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
                       if (isChecked){
                           ll_reset_layout.setVisibility(View.VISIBLE);
                           list.get(getAdapterPosition()).setResetStatus("1");
                       }else{
                           ll_reset_layout.setVisibility(View.GONE);
                           list.get(getAdapterPosition()).setResetStatus("0");
                       }
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
        public void setSpecialReading(String reading) {
            if (null == et_special_reading) return;
            et_special_reading.setText(reading);
            et_special_reading.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        list.get(getAdapterPosition()).setSpecialReading("");
                    } else{
                        list.get(getAdapterPosition()).setSpecialReading(s.toString());
                    }
                }
            });
        }
        public void setCommentsReset(String comment) {
            if (null == et_special_comment) return;
            et_special_comment.setText(comment);
            et_special_comment.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        list.get(getAdapterPosition()).setCommentsReset("");
                    } else{
                        list.get(getAdapterPosition()).setCommentsReset(s.toString());
                    }
                }
            });
        }
        public void setDefected(String defected) {

            if (null == cb_defected ||llOpening==null) return;

            onBind = true;
            if (defected.equals("1")){
                cb_defected.setChecked(true);
            }else{
                cb_defected.setChecked(false);
            }
            onBind = false;
        }
        public void setReset(String reset,String nozzleId) {

            if (null == cb_reset ||ll_reset_layout==null) return;

            if (TextUtils.isEmpty(nozzleId)){
                cb_reset.setVisibility(View.GONE);
            }else {
                cb_reset.setVisibility(View.VISIBLE);
            }
            onBindReset = true;
            if (reset.equals("1")){
                ll_reset_layout.setVisibility(View.VISIBLE);
                cb_reset.setChecked(true);
            }else{
                ll_reset_layout.setVisibility(View.GONE);
                cb_reset.setChecked(false);
            }
            onBindReset = false;
        }
        public void setOpeningBalance(String balance,String nozzelId) {
            if (null == et_opening_balance ||llOpening==null) return;

            if (TextUtils.isEmpty(nozzelId)){
                llOpening.setVisibility(View.VISIBLE);
            }else {
                if (!TextUtils.isEmpty(balance) && Float.parseFloat(balance)>0){
                    llOpening.setVisibility(View.GONE);
                }else {
                    llOpening.setVisibility(View.VISIBLE);
                }
            }

            if (llOpening.getVisibility()==View.VISIBLE){
                et_opening_balance.setText(balance);
                et_opening_balance.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            list.get(getAdapterPosition()).setOpeningBalanceD("");
                        } else{
                            list.get(getAdapterPosition()).setOpeningBalanceD(s.toString());
                        }
                    }
                });
            }
        }

        public void setCurrentReading(String reading) {
            if (null == et_current_reading) return;
            et_current_reading.setText(reading);
            if (!TextUtils.isEmpty(reading) && reading.equals("0"))
                et_current_reading.setText("");

            et_current_reading.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        list.get(getAdapterPosition()).setDumeterCurrentVolume("");
                    } else{
                        list.get(getAdapterPosition()).setDumeterCurrentVolume(s.toString());
                    }
                }
            });
        }

        public void setMaxTank(String tank,String nozzleId) {
            if (null == et_max_nozzels || ll_DuMax==null) return;

            if (TextUtils.isEmpty(nozzleId)){
                ll_DuMax.setVisibility(View.VISIBLE);
            }else {
                ll_DuMax.setVisibility(View.GONE);
            }

            if (ll_DuMax.getVisibility()==View.VISIBLE){
                et_max_nozzels.setText(tank);
                et_max_nozzels.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            list.get(getAdapterPosition()).setDumeterMaxValue("");
                        } else{
                            list.get(getAdapterPosition()).setDumeterMaxValue(s.toString());
                        }
                    }
                });
            }
        }

        public void setSpinner(String status) {
            if (null == spinner_type) return;
            spinner_type.setEnabled(false);
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
        }
    }
}
