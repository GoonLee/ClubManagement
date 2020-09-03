package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class PersonAdapter_short extends RecyclerView.Adapter<PersonAdapter_short.ViewHolder>{
    //회원 프로필 저장 배열
    ArrayList<Person> personItems=new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index = 0;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.item_person_summary,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Person item= personItems.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return personItems.size();
    }

    public void addItem(Person item){
        personItems.add(item);
    }

    public void setItems(ArrayList<Person> personItems){
        this.personItems=personItems;
    }
    public Person getItem(int position){
        return personItems.get(position);
    }
    public void setItem(int position, Person item){
        personItems.set(position,item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Name, textView_Number, textView_index;

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

            textView_index = itemView.findViewById(R.id.textView_index);
            textView_Name=itemView.findViewById(R.id.textView_name);
            textView_Number=itemView.findViewById(R.id.textView_number);
        }

        public void setItem(Person item) {
            textView_index.setText(Integer.toString(index++));
            textView_Name.setText(item.getName());
            textView_Number.setText(Integer.toString(item.getId_num()));
        }
    }
}

