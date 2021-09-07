package com.group17.teddysbrochure.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group17.teddysbrochure.R;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    List<WeatherModel> weatherModelList;
    Context context;

    public WeatherAdapter(Context context, List<WeatherModel> weatherModelList) {
        this.context = context;
        this.weatherModelList = weatherModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dayTextView.setText(weatherModelList.get(position).getDay());
        holder.highTextView.setText(weatherModelList.get(position).getHigh());
        holder.lowTextView.setText(weatherModelList.get(position).getLow());
    }

    @Override
    public int getItemCount() {
        return weatherModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        TextView highTextView;
        TextView lowTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.weatherDay);
            highTextView = itemView.findViewById(R.id.weatherHigh);
            lowTextView = itemView.findViewById(R.id.weatherLow);
        }
    }
}
