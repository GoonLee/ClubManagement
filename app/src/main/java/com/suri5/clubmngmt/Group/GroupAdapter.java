package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suri5.clubmngmt.R;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    ArrayList<Group> groupItems = new ArrayList<Group>();
    public static final int RESULT_SAVE = 102;


    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.group_item,parent,false);
        return new GroupAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group item= groupItems.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }
    public void addGroup(Group item){groupItems.add(item);}
    public Group getItem(int position){return groupItems.get(position);}
    public void setItem(int position, Group item){ groupItems.set(position,item);}
    public void setItems(ArrayList<Group> groupItems){
        this.groupItems=groupItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_name, textView_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_number = itemView.findViewById(R.id.textView_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(view.getContext(), GroupEditActivity.class);
                        intent.putExtra("group", groupItems.get(pos)); //그룹 객체를 아예 넘겨줌
                        ((Activity)view.getContext()).startActivityForResult(intent, RESULT_SAVE);
                    }
                }
            });
        }
        public void setItem(Group item){
            textView_number.setText("인원 수 : " + item.getTotalNum());
            textView_name.setText("그룹명 : " + item.getName());
        }
    }
}
