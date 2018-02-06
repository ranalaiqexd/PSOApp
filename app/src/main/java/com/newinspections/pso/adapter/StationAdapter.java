package com.newinspections.pso.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newinspections.pso.R;
import com.newinspections.pso.dto.InspectionDTO;

import java.util.List;

/**
 * Created by mobiweb on 8/4/16.
 */
public class StationAdapter extends BaseAdapter {
    Context context;
    List<InspectionDTO.Station> stations;

    public StationAdapter(Context context, List<InspectionDTO.Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cstm_sppnr_layout, parent, false);
        }
        TextView tv = (TextView) v.findViewById(R.id.txtSppnr);
        tv.setText(stations.get(position).getStationsDetalis().getStationName());
        Log.d("checkexd2", tv.getText().toString());
        return v;
    }
}
