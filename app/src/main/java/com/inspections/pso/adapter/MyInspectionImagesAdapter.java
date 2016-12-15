package com.inspections.pso.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspections.pso.R;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.utils.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class MyInspectionImagesAdapter extends RecyclerView.Adapter<MyInspectionImagesAdapter.ViewHolder> {
    private List<String> arrayList;
    private Context context;
    private String path="";
    float dpWidth;
    public MyInspectionImagesAdapter(Context context, List<String> arrayList,String path) {
        this.arrayList = arrayList;
        this.context = context;
        this.path=path;
        Display display =((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = ((Activity)context).getResources().getDisplayMetrics().density;
        dpWidth= Math.round(outMetrics.widthPixels / density);
       // dpWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,outMetrics.widthPixels, ((Activity)context).getResources().getDisplayMetrics());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_inspection_images, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String result = arrayList.get(position);
        holder.setImage(result);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem1;
        TextView tv_title;
        FrameLayout panel_content;
        public ViewHolder(View v) {
            super(v);
            this.imgItem1 = (ImageView) v.findViewById(R.id.imgItem1);
            this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            panel_content = (FrameLayout)itemView.findViewById(R.id.panel_content);
            panel_content.post(new Runnable() {
                @Override
                public void run() {
                    int width = (int)(dpWidth/3);
                    ViewGroup.LayoutParams lp = panel_content.getLayoutParams();
                    lp.width = width;
                    panel_content.setLayoutParams(lp);
                }

            });
        }

        public void setImage(String image) {
            if (null == imgItem1) return;
            Picasso.with(context)
                    .load(path+image)
                    .into(imgItem1);
        }
    }
}

