package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.SapDetailDTO;

import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class SapMonthDetailAdapter extends RecyclerView.Adapter<SapMonthDetailAdapter.ViewHolder> {
    private List<SapDetailDTO.First> arrayList;
    private Context context;
    public SapMonthDetailAdapter(Context context, List<SapDetailDTO.First> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sales_detail, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position==0){
            holder.setNo("Date");
            holder.setInvoice("Invoice");
            holder.setPurchases("Purchases");
        }else {
            holder.setNo("");
            holder.setInvoice("");
            holder.setPurchases("");
            SapDetailDTO.First item=arrayList.get(position);
            if (item!=null){
                holder.setInvoice(item.getInvoiceNo()+"");
                holder.setPurchases(item.getQuantity()+"");
                holder.setNo(item.getDate()+"");
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_no,tv_invoice,tv_purchases;
        public ViewHolder(View v) {
            super(v);
            this.tv_no = (TextView) v.findViewById(R.id.tv_no);
            this.tv_invoice = (TextView) v.findViewById(R.id.tv_invoice);
            this.tv_purchases = (TextView) v.findViewById(R.id.tv_purchases);
        }
        public void setNo(String title) {
            if (null == tv_no) return;
            tv_no.setText(title);
        }
        public void setInvoice(String description) {
            if (null == tv_invoice) return;
            tv_invoice.setText(description);
        }
        public void setPurchases(String hsd) {
            if (null == tv_purchases) return;
            tv_purchases.setText(hsd);
        }
    }
}

