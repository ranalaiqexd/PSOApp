package com.newinspections.pso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.SurveyDTO;

import java.util.ArrayList;

public class HSESurvayAdapter extends RecyclerView.Adapter<HSESurvayAdapter.RecyclerViewHolder> {


    private static final String TAG = "HSESurvayAdapter";
    Context c;
    ArrayList<SurveyDTO> list;
    public HSESurvayAdapter(Context c, ArrayList<SurveyDTO> list)
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
                layout = R.layout.list_item_survay;
                break;
            case 1:
                layout = R.layout.comment_view;
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
        holder.setSpinner(item.getStatus(),position);
        holder.setComment(item.getMessage());
        holder.setClickOnItem(position);

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

        TextView tv_title;
        EditText et_comment;
        Spinner hseSppnr1;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            hseSppnr1 = (Spinner) itemView.findViewById(R.id.hseSppnr1);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            et_comment = (EditText) itemView.findViewById(R.id.et_comment);
        }


        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title);
        }
        public void setComment(String comment) {
            if (null == et_comment) return;
            et_comment.setText(comment);
            et_comment.addTextChangedListener(commentWatcher);
        }
        private final TextWatcher commentWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    list.get(list.size()-1).setMessage("");
                } else{
                    list.get(list.size()-1).setMessage(s.toString());
                }
            }
        };
        public void setSpinner(String status,final  int position) {
            if (null == hseSppnr1) return;
            switch (status){
                case "-1":
                    hseSppnr1.setSelection(0);
                    break;
                case "1":
                    hseSppnr1.setSelection(1);
                    break;
                case "0":
                    hseSppnr1.setSelection(2);
                    break;
                case "2":
                    hseSppnr1.setSelection(3);
                    break;
            }
            hseSppnr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    Log.i("onItemSelected","onItemSelected "+i);

                        switch (i){
                            case 0:
                                list.get(position).setStatus("-1");
                                break;
                            case 1:
                                list.get(position).setStatus("1");
                                break;
                            case 2:
                                list.get(position).setStatus("0");
                                break;
                            case 3:
                                list.get(position).setStatus("2");
                                break;
                        }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        public void setClickOnItem(int position){
            if (hseSppnr1==null)
                return;
          //  hseSppnr1.setOnClickListener(new OnItemClickListener(position,onItemClickCallback));
        }
    }
}
