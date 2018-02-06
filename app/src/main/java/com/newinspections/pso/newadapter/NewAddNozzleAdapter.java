package com.newinspections.pso.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.newinspections.pso.model.InspectionsModel;

import java.util.List;

/**
 * Created by Exd on 11/8/2017.
 */

public class NewAddNozzleAdapter  extends RecyclerView.Adapter<NewAddNozzleAdapter.RecyclerViewHolder> {

    private static final String TAG = "NewAddNozzelsAdapter";
    Context context;
    List<InspectionsModel.Stations.Tanks.Nozzles> nozzleslist;

    public NewAddNozzleAdapter(Context context, List<InspectionsModel.Stations.Tanks.Nozzles> nozzleslist) {
        this.context = context;
        this.nozzleslist = nozzleslist;
    }

    @Override
    public NewAddNozzleAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nozzels, parent, false);
        NewAddNozzleAdapter.RecyclerViewHolder holder = new NewAddNozzleAdapter.RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewAddNozzleAdapter.RecyclerViewHolder holder, int position)
    {
        InspectionsModel.Stations.Tanks.Nozzles nozzle = nozzleslist.get(position);
        holder.setTitle(nozzle.getNozzleName());
        holder.setOpeningBalance(nozzle.getNozzleOpeningBalance(), nozzle.getNozzleId(), nozzle.getNozzleNewlyCreated());
        holder.setCurrentReading(nozzle.getNozzleCurrentReading());
        holder.setNumber(nozzle.getNozzleNumber(), nozzle.getNozzleNewlyCreated());
        holder.setPreviousReading(nozzle.getNozzlePreviousReading());
        holder.setMaxNozzle(nozzle.getNozzleMaximum(), nozzle.getNozzleId(), nozzle.getNozzleNewlyCreated());
        holder.setSpinner(nozzle.getNozzleProductType(),nozzle.getNozzleNewlyCreated());
        holder.setDefected(nozzle.getNozzleDefected());
        holder.setSpecialReading(nozzle.getNozzleSpecialReading());
        holder.setReset(nozzle.getNozzleReset(), nozzle.getNozzleId());
        holder.setCommentsReset(nozzle.getNozzleSpecialRemarks());
    }

    @Override
    public int getItemCount() {
        return nozzleslist.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView tv_title,tv_previous_reading,tv_spinner;
        EditText et_opening_balance,et_number_nozzels,et_current_reading,et_max_nozzels,et_special_reading,et_special_comment;
        Spinner spinner_type;
        CheckBox cb_defected,cb_reset;
        LinearLayout llOpening, llOpeningReading, ll_DuMax,ll_reset_layout;
        private boolean onBind;
        private boolean onBindReset;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            spinner_type = (Spinner) itemView.findViewById(R.id.spinner_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_previous_reading = (TextView) itemView.findViewById(R.id.tv_previous_reading);
            et_opening_balance = (EditText) itemView.findViewById(R.id.et_opening_balance);
            et_number_nozzels = (EditText) itemView.findViewById(R.id.et_number_nozzels);
            et_current_reading = (EditText) itemView.findViewById(R.id.et_current_reading);
            et_max_nozzels = (EditText) itemView.findViewById(R.id.et_max_nozzels);
            et_special_reading = (EditText) itemView.findViewById(R.id.et_special_reading);
            et_special_comment = (EditText) itemView.findViewById(R.id.et_special_comment);
            cb_defected = (CheckBox) itemView.findViewById(R.id.cb_defected);
            cb_reset = (CheckBox) itemView.findViewById(R.id.cb_reset);
            ll_reset_layout = (LinearLayout) itemView.findViewById(R.id.ll_reset_layout);
            llOpeningReading = (LinearLayout) itemView.findViewById(R.id.llOpeningReading);
            llOpening = (LinearLayout) itemView.findViewById(R.id.llOpening);
            ll_DuMax = (LinearLayout) itemView.findViewById(R.id.ll_DuMax);
            tv_spinner = (TextView) itemView.findViewById(R.id.spinner_text);
            cb_defected.setOnCheckedChangeListener(this);
            cb_reset.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch ( buttonView.getId()){
                case R.id.cb_defected:
                    if(!onBind) {
                        if (isChecked)
                            nozzleslist.get(getAdapterPosition()).setNozzleDefected("1");
                        else
                            nozzleslist.get(getAdapterPosition()).setNozzleDefected("0");
                        notifyDataSetChanged();
                    }
                    break;

                case R.id.cb_reset:

                    if(!onBindReset) {
                        if (isChecked){
                            ll_reset_layout.setVisibility(View.VISIBLE);
                            nozzleslist.get(getAdapterPosition()).setNozzleReset("1");
                        }else{
                            ll_reset_layout.setVisibility(View.GONE);
                            nozzleslist.get(getAdapterPosition()).setNozzleReset("0");
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
            if (!TextUtils.isEmpty(reading) && Float.parseFloat(reading)>0)
            {
                llOpeningReading.setVisibility(View.VISIBLE);
                tv_previous_reading.setText(reading);
            }
            else
            {
                llOpeningReading.setVisibility(View.GONE);
                tv_previous_reading.setText("");
            }

        }

        public void setNumber(String nozzleNumber, String newlyValue)
        {
            if (null == et_number_nozzels)
                return;
            if (!TextUtils.isEmpty(nozzleNumber) && Float.parseFloat(nozzleNumber)>0)
            {
                if (newlyValue.equals("1"))
                {
                    et_number_nozzels.setText(nozzleNumber);
                    et_number_nozzels.setEnabled(true);
                }
                else if (newlyValue.equals("0"))
                {
                    et_number_nozzels.setText(nozzleNumber);
                    et_number_nozzels.setEnabled(false);
                }
            }
            else
            {
                et_number_nozzels.setText("");
                et_number_nozzels.setEnabled(true);
            }

            et_number_nozzels.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        nozzleslist.get(getAdapterPosition()).setNozzleNumber("");
                    } else{
                        nozzleslist.get(getAdapterPosition()).setNozzleNumber(s.toString());
                    }
                }
            });
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
                        nozzleslist.get(getAdapterPosition()).setNozzleSpecialReading("");
                    } else{
                        nozzleslist.get(getAdapterPosition()).setNozzleSpecialReading(s.toString());
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
                        nozzleslist.get(getAdapterPosition()).setNozzleSpecialRemarks("");
                    } else{
                        nozzleslist.get(getAdapterPosition()).setNozzleSpecialRemarks(s.toString());
                    }
                }
            });
        }

        public void setDefected(String defected) {

            if (null == cb_defected || llOpening==null)
                return;

            onBind = true;
            if (defected!=null)
            {
                if (defected.equals("1"))
                {
                    cb_defected.setChecked(true);
                }
                else
                {
                    cb_defected.setChecked(false);
                }
            }
            else
            {
                cb_defected.setChecked(false);
            }
            onBind = false;
        }

        public void setReset(String reset, String nozzleId) {

            if (null == cb_reset || ll_reset_layout==null)
                return;

            if (TextUtils.isEmpty(nozzleId)){
                cb_reset.setVisibility(View.GONE);
            }else {
                cb_reset.setVisibility(View.VISIBLE);
            }
            onBindReset = true;
            if (reset!=null)
            {
                if (reset.equals("1"))
                {
                    ll_reset_layout.setVisibility(View.VISIBLE);
                    cb_reset.setChecked(true);
                }
                else
                    {
                    ll_reset_layout.setVisibility(View.GONE);
                    cb_reset.setChecked(false);
                }
            }
            else
            {
                ll_reset_layout.setVisibility(View.GONE);
                cb_reset.setChecked(false);
            }
            onBindReset = false;
        }

        public void setOpeningBalance(String balance,String nozzelId, String newlyValue) {
            if (null == et_opening_balance ||llOpening==null) return;

            if (TextUtils.isEmpty(nozzelId)){
                llOpening.setVisibility(View.VISIBLE);
            }else {
                if (!TextUtils.isEmpty(balance) && Float.parseFloat(balance)>0)
                {
                    if (newlyValue.equals("1"))
                    {
                        llOpening.setVisibility(View.VISIBLE);
                    }
                    else if (newlyValue.equals("0"))
                    {
                        llOpening.setVisibility(View.GONE);
                    }
                }
                else
                {
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
                            nozzleslist.get(getAdapterPosition()).setNozzleOpeningBalance("");
                        } else{
                            nozzleslist.get(getAdapterPosition()).setNozzleOpeningBalance(s.toString());
                        }
                    }
                });
            }
        }

        public void setCurrentReading(String reading) {
            if (null == et_current_reading)
                return;
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
                        nozzleslist.get(getAdapterPosition()).setNozzleCurrentReading("");
                    } else{
                        nozzleslist.get(getAdapterPosition()).setNozzleCurrentReading(s.toString());
                    }
                }
            });
        }

        public void setMaxNozzle(String nozzle, String nozzleId, String newlyValue) {
            if (null == et_max_nozzels || ll_DuMax==null) return;

            if (TextUtils.isEmpty(nozzleId)){
                ll_DuMax.setVisibility(View.VISIBLE);
            }
            else
            {
                if (newlyValue.equals("1"))
                {
                    ll_DuMax.setVisibility(View.VISIBLE);
                }
                else if (newlyValue.equals("0"))
                {
                    ll_DuMax.setVisibility(View.GONE);
                }
            }

            if (ll_DuMax.getVisibility()==View.VISIBLE){
                et_max_nozzels.setText(nozzle);
                et_max_nozzels.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            nozzleslist.get(getAdapterPosition()).setNozzleMaximum("");
                        } else{
                            nozzleslist.get(getAdapterPosition()).setNozzleMaximum(s.toString());
                        }
                    }
                });
            }
        }

        public void setSpinner(String status, String newlyValue)
        {

            if (status == null)
            {
                tv_spinner.setText("Select Product");
            }
            else
            {
                tv_spinner.setText(status);
            }

//            if (null == spinner_type)
//                return;
//            spinner_type.setEnabled(false);
//            switch (status){
//                case "HSD":
//                    spinner_type.setSelection(1);
//                    break;
//                case "PMG":
//                    spinner_type.setSelection(2);
//                    break;
//                case "HOBC":
//                    spinner_type.setSelection(3);
//                    break;
//                default: spinner_type.setSelection(0);
//            }
        }
    }
}
