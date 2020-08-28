package com.suri5.clubmngmt.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    ArrayList<Schedule> scheduleItems=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.schedule_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule item= scheduleItems.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public void addItem(Schedule item){
        scheduleItems.add(item);
    }

    public void setItems(ArrayList<Schedule> scheduleItems){
        this.scheduleItems=scheduleItems;
    }
    public Schedule getItem(int position){
        return scheduleItems.get(position);
    }
    public void setItem(int position, Schedule item){
        scheduleItems.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScheduleTitle, textViewScheduleStartTime;
        TextView textViewScheduleEnd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewScheduleTitle=itemView.findViewById(R.id.textViewScheduleTitle);
            textViewScheduleStartTime=itemView.findViewById(R.id.textViewScheduleStartTime);
            textViewScheduleEnd=itemView.findViewById(R.id.textViewScheduleEnd);

        }

        public void setItem(Schedule item) {
            textViewScheduleTitle.setText(item.title);
            textViewScheduleStartTime.setText(item.startTime);
            textViewScheduleEnd.setText("~"+item.endDate+" "+item.endTime);
        }
    }
}
