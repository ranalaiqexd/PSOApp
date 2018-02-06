package com.newinspections.pso.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.model.InspectionsModel;
import com.newinspections.pso.utils.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Created by linux102 on 1/3/16.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private List<InspectionsModel.Stations.Images> arrayList;
    private Context context;
   // AQuery aQuery;
    int count = 0;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    float dpWidth;
    public ImagesAdapter(Context context, List<InspectionsModel.Stations.Images> arrayList, OnItemClickListener.OnItemClickCallback onItemClickCallback)
    {
        this.arrayList = arrayList;
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;

        Display display =((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = ((Activity)context).getResources().getDisplayMetrics().density;
        dpWidth = Math.round(outMetrics.widthPixels / density);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_inspection_images, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);
        return holder;

    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        InspectionsModel.Stations.Images result = arrayList.get(position);
        holder.setImageName(result.getImageName());
        holder.setImage(result.getImagePath());
        holder.setTitle(result.getImageText());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem1;
        TextView tv_title;
        TextView tv_imagename;
        FrameLayout panel_content;
        public ViewHolder(View v) {
            super(v);
            this.imgItem1 = (ImageView) v.findViewById(R.id.imgItem1);
            this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            this.tv_imagename = (TextView) v.findViewById(R.id.imageName);
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

        public void setImageName(String name) {
            if (null == tv_imagename)
                return;
            Log.d("Picasso", "Namw Here: "+name);
            tv_imagename.setText(name);
        }

        public void setImage(String image) {
            if (null == imgItem1)
                return;
            Log.d("Picasso", "Image Here: "+image);
            Picasso.with(context).load(new File(image)).into(imgItem1);
        }

        public void setTitle(String title) {
            if (null == tv_title)
                return;
            tv_title.setText("");
            tv_title.setBackgroundColor(Color.parseColor("#60111111"));
        }
    }
}

