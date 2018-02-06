package com.newinspections.pso.newadapter;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.database.ProductsORM;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.model.ProductsModel;
import com.newinspections.pso.utils.OnItemClickListener;

import java.util.List;

/**
 * Created by Exd on 10/30/2017.
 */

public class NewAddTankAdapter extends RecyclerView.Adapter<NewAddTankAdapter.RecyclerViewHolder>
{

    private static final String TAG = "AddTankAdapter";
    Context context;
//    InspectionDTO.Station.A aData;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    OnItemClickListener.OnItemClickCallback onDateClickCallback;
    List<InspectionsModel.Stations.Tanks> list;

    public NewAddTankAdapter(Context context, List<InspectionsModel.Stations.Tanks> list, OnItemClickListener.OnItemClickCallback onDateClickCallback, OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        this.onDateClickCallback = onDateClickCallback;
        this.list = list;
    }

    @Override
    public NewAddTankAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tank, parent, false);
        NewAddTankAdapter.RecyclerViewHolder holder = new NewAddTankAdapter.RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewAddTankAdapter.RecyclerViewHolder holder, int position) {
        InspectionsModel.Stations.Tanks item = list.get(position);

        holder.setTitle(item.getTankName());
        holder.setPreviousReading(item.getTankPreviousReading());
        holder.setOpeningBalance(item.getTankOpeningBalance(),item.getTankId(),item.getTankFlush(),item.getTankNewlyCreated());
        holder.setReading(item.getTankCurrentReading());
        holder.setMaxTank(item.getTankMaximum(),item.getTankId(),item.getTankNewlyCreated());
//        holder.setSpinner2(item.getTankProductType(),item.getTankId(),item.getTankFlush(), item.getTankNewlyCreated());
        holder.setSpinnerText(item.getTankProductType(),item.getTankId(),item.getTankFlush(), item.getTankNewlyCreated());
        holder.setFlush(item.getTankId(),item.getTankFlush());
        holder.setComment(item.getTankRemarks());
        holder.setDate(item.getTankRemarksDate());
        holder.setClickOnItem();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        TextView spinner_text, tv_title,tv_date, et_max_tank, tv_previous_reading;
        EditText et_opening_balance,et_reading,et_comment;
        Spinner spinner_type;
        CheckBox cb_flush;
        Button btn_nozel;
        LinearLayout ll_comment_layout,ll_opening_bal,ll_tank_max,ll_previous_reading ;
        private boolean onBind;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            spinner_text = (TextView) itemView.findViewById(R.id.spinner_text);
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
            tv_previous_reading = (TextView) itemView.findViewById(R.id.tv_previous_reading);
            ll_previous_reading = (LinearLayout) itemView.findViewById(R.id.ll_previous_reading);

            cb_flush.setOnCheckedChangeListener(this);
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
                date="Select Date";

            if (null == tv_date) return;
            tv_date.setText(date);
            tv_date.setMovementMethod(LinkMovementMethod.getInstance());
            SpannableString content = new SpannableString(date);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv_date.setText(content);
            tv_date.setOnClickListener(new OnItemClickListener(getAdapterPosition(), onDateClickCallback));
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
                        list.get(getAdapterPosition()).setTankRemarks("");
                    } else{
                        list.get(getAdapterPosition()).setTankRemarks(s.toString());
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
            if (!TextUtils.isEmpty(flush) && flush.equals("1")){
                cb_flush.setChecked(true);
                ll_comment_layout.setVisibility(View.VISIBLE);
            }
            else{
                cb_flush.setChecked(false);
                ll_comment_layout.setVisibility(View.GONE);
            }
            onBind = false;
        }

        public void setOpeningBalance(String balance,String tankId,String flush, String newlyValue) {
            if (null == et_opening_balance) return;

            if (TextUtils.isEmpty(tankId)){
                ll_opening_bal.setVisibility(View.VISIBLE);
            }
            else
            {
                if (!TextUtils.isEmpty(flush) && flush.equals("1")){
                    ll_opening_bal.setVisibility(View.VISIBLE);
                }else {
                    ll_opening_bal.setVisibility(View.GONE);
                }
                if (newlyValue.equals("1"))
                {
                    ll_opening_bal.setVisibility(View.VISIBLE);
                }
                else if (newlyValue.equals("0"))
                {
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
                            list.get(getAdapterPosition()).setTankOpeningBalance("");
                        } else{
                            list.get(getAdapterPosition()).setTankOpeningBalance(s.toString());
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
                        list.get(getAdapterPosition()).setTankCurrentReading("");
                    } else{
                        list.get(getAdapterPosition()).setTankCurrentReading(s.toString());
                        Log.d("Testadapter",""+s.toString());
                        Log.d("Testadapget",""+list.get(getAdapterPosition()).getTankCurrentReading());
                    }
                }
            });
        }

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


        public void setMaxTank(String tank,String tankId, String newlyValue) {
            int isEditable = 0;
            if (null == et_max_tank || ll_tank_max==null) return;
            if (TextUtils.isEmpty(tankId)){
                ll_tank_max.setVisibility(View.VISIBLE);
                isEditable = 1;
            }
            else
            {
                Log.d("New", newlyValue);
                if (!TextUtils.isEmpty(tank) && Float.parseFloat(tank)>0)
                {
                    if (newlyValue.equals("1"))
                    {
                        ll_tank_max.setVisibility(View.VISIBLE);
                        isEditable = 1;
                    }
                    else if (newlyValue.equals("0"))
                    {
                        ll_tank_max.setVisibility(View.VISIBLE);
                        isEditable = 0;
                    }
                }
                else
                {
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
                                list.get(getAdapterPosition()).setTankMaximum("");
                            } else{
                                list.get(getAdapterPosition()).setTankMaximum(s.toString());
                            }
                        }
                    });
                }
            }
        }

        public void setSpinnerText(String status, String tankId, final String flush, String newlyValue)
        {
            if (status==null)
            {
                status = "";
                initializeProductsList(status);
            }
            if (!TextUtils.isEmpty(tankId))
            {
                if (!TextUtils.isEmpty(flush) && flush.equals("1"))
                {
                    if (TextUtils.isEmpty(list.get(getAdapterPosition()).getTankOldProductType()))
                    {
                        list.get(getAdapterPosition()).setTankOldProductType(status);
                    }
                    initializeProductsList(status);
                }
                else
                {
                    spinner_text.setText(status);
                }

                if (newlyValue.equals("1"))
                {
                    initializeProductsList(status);
                }
                else if (newlyValue.equals("0"))
                {
                    spinner_text.setText(status);
                }
            }
            else if (TextUtils.isEmpty(tankId))
            {
                initializeProductsList(status);
            }
            else
            {
                spinner_text.setText(status);
            }

        }

        private void initializeProductsList(String productType)
        {
            if(productType.equals(""))
            {
                spinner_text.setText("Select Product");
            }
            else
            {
                spinner_text.setText(productType);
            }
            spinner_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    spinner_text.setVisibility(View.GONE);
                    spinner_type.setVisibility(View.VISIBLE);
                    spinner_type.performClick();
                }
            });

            final List<ProductsModel.Products> productsList = ProductsORM.getProducts(context);
            Log.d("ProductName",productsList.size()+"");
            if (productsList!=null && productsList.size()>0)
            {
                spinner_text.setText("Select Product");
                spinner_text.setVisibility(View.VISIBLE);
                spinner_type.setVisibility(View.GONE);
                Log.d("ProductName2",productsList.size()+"");
                NewProductAdapter productAdapter = new NewProductAdapter(context, productsList);
                spinner_type.setAdapter(productAdapter);
            }
            else
            {
                spinner_text.setText("Update Products First");
                spinner_text.setVisibility(View.VISIBLE);
                spinner_type.setVisibility(View.GONE);
            }
            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {

                    if (productsList!=null && productsList.size()>0)
                    {
                        int productId = Integer.parseInt(productsList.get(position).getProductId());
                        String productName = productsList.get(position).getProductsName();

                        Log.d("ProductName",productId+" / "+productName);
                        spinner_text.setText(productName);
                        list.get(getAdapterPosition()).setTankProductType(productsList.get(position).getProductsName());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinner_text.setText("Select Product");
                }
            });
        }

//        public void setSpinner(String status, String tankId, final String flush, String newlyValue) {
//            if (status==null)
//            {
//                status="";
//            }
//            if (spinner_type == null)
//                return;
//            if (!TextUtils.isEmpty(tankId))
//            {
//                if (flush.equals("1"))
//                {
//                    spinner_type.setEnabled(true);
//                }
//                else
//                {
//                    spinner_type.setEnabled(false);
//                }
//
//                if (newlyValue.equals("1"))
//                {
//                    spinner_type.setEnabled(true);
//                }
//                else if (newlyValue.equals("0"))
//                {
//                    spinner_type.setEnabled(false);
//                }
//            }
//            else
//            {
//                spinner_type.setEnabled(true);
//            }
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
//                default:
//                    spinner_type.setSelection(0);
//                    break;
//            }
//            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//
////                    if (!TextUtils.isEmpty(flush) && flush.equals("1")){
////                        if (TextUtils.isEmpty(list.get(getAdapterPosition()).getOldTankType())){
////                            list.get(getAdapterPosition()).setOldTankType(list.get(getAdapterPosition()).getTankType());
////                        }
////                    }
//
//                    switch (i){
//                        case 0:
//                            list.get(getAdapterPosition()).setTankProductType("");
//                            break;
//                        case 1:
//                            list.get(getAdapterPosition()).setTankProductType(spinner_type.getSelectedItem().toString());
//                            break;
//                        case 2:
//                            list.get(getAdapterPosition()).setTankProductType(spinner_type.getSelectedItem().toString());
//                            break;
//                        case 3:
//                            list.get(getAdapterPosition()).setTankProductType(spinner_type.getSelectedItem().toString());
//                            break;
//                    }
////                    refreshNozzlesName(getAdapterPosition());
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//        }

        public void setClickOnItem(){
            if (btn_nozel==null)
                return;
            btn_nozel.setOnClickListener(new OnItemClickListener(getAdapterPosition(),onItemClickCallback));
        }
    }

}
