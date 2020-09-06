package com.suri5.clubmngmt.Group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Person.Person;
import com.suri5.clubmngmt.Person.PersonAdapter_short;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;


public class GroupAdapter_short extends RecyclerView.Adapter<GroupAdapter_short.ViewHolder>{
    ArrayList<Group> groupItems = new ArrayList<Group>();

    @NonNull
    @Override
    public GroupAdapter_short.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.item_group_summary,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Group item= groupItems.get(position);
        holder.setItem(item);
        holder.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groupItems.isEmpty()){
                    groupItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }

    public void addItem(Group item){
        groupItems.add(item);
    }

    public void setItems(ArrayList<Group> personItems){
        this.groupItems=personItems;
    }
    public Group getItem(int position){
        return groupItems.get(position);
    }
    public void setItem(int position, Group item){
        groupItems.set(position,item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Name;
        ImageButton button_cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                    }
                }
            });
            textView_Name=itemView.findViewById(R.id.textView_name);
            button_cancel = itemView.findViewById(R.id.button_delete);
        }

        public void setItem(Group item) {
            textView_Name.setText(item.getName());
        }
    }
}
