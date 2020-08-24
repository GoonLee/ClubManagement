package com.suri5.clubmngmt.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View itemView=inflater.inflate(R.layout.activity_add_person_info,parent,false);
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

    static  class ViewHolder extends RecyclerView.ViewHolder {
        // Todo : declare view variables

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Todo : assign views using findViewById()
        }

        public void setItem(Schedule item) {
            //Todo : set value of views (ex) textView.setText())
        }
    }
}
