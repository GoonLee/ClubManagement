package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    ArrayList<Schedule> scheduleItems=new ArrayList<Schedule>();
    private OnItemClickListener mListener = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScheduleTitle, textViewScheduleStartTime;
        TextView textViewScheduleEnd;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewScheduleTitle=itemView.findViewById(R.id.textViewScheduleTitle);
            textViewScheduleStartTime=itemView.findViewById(R.id.textViewScheduleStartTime);
            textViewScheduleEnd=itemView.findViewById(R.id.textViewScheduleEnd);

            //아이템 클릭 리스너
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Schedule item = scheduleItems.get(pos);
                        Intent intent = new Intent(itemView.getContext(), ScheduleEditActivity.class);
                        intent.putExtra("schedule",item);
                        view.getContext().startActivity(intent);
                    }
                }
            });

        }

        public void setItem(Schedule item) {
            textViewScheduleTitle.setText(item.title);
            textViewScheduleStartTime.setText(item.startDate.substring(4,8)+" "+item.startTime.substring(0,2)+":"+item.startTime.substring(2,4));
            textViewScheduleEnd.setText("~"+item.endDate.substring(4,8)+" "+item.endTime.substring(0,2)+":"+item.endTime.substring(2,4));
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

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

}
