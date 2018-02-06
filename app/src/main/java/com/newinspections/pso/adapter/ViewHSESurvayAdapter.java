package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.SurveyDTO;

import java.util.ArrayList;

public class ViewHSESurvayAdapter extends RecyclerView.Adapter<ViewHSESurvayAdapter.RecyclerViewHolder> {

    Context c;
    ArrayList<SurveyDTO> list;
    public ViewHSESurvayAdapter(Context c, ArrayList<SurveyDTO> list)
    {
        this.c = c;
        this.list = list;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = -1;
        switch (viewType) {
            case 0:
                layout = R.layout.list_item_view_survay;
                break;
            case 1:
                layout = R.layout.view_comment_view;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        SurveyDTO item = list.get(position);
        holder.setTitle(item.getMessage());
        holder.setSpinner(item.getStatus());
        holder.setComment(item.getMessage());

    }

    @Override
    public int getItemViewType(int position) {

        if (list.size()-1==position){
            return 1;
        }else
            return 0;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_status,tv_comment;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
        }


        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title);
        }
        public void setComment(String comment) {
            if (null == tv_comment) return;
            tv_comment.setText(comment);
        }

        public void setSpinner(String status) {
            if (null == tv_status) return;
            switch (status){
                case "1":
                    tv_status.setText("Yes");
                    break;
                case "2":
                    tv_status.setText("N/A");
                    break;
                default:
                    tv_status.setText("No");
            }
        }
    }
}
