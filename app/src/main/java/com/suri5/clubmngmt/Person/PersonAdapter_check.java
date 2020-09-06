package com.suri5.clubmngmt.Person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class PersonAdapter_check extends RecyclerView.Adapter<PersonAdapter_check.ViewHolder>{
    //회원 프로필 저장 배열
    ArrayList<Person> personItems;
    ArrayList<Person> checkedlist = new ArrayList<Person>();



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.item_person_checkbox,parent,false);
        return new PersonAdapter_check.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final Person item= personItems.get(position);

        holder.setItem(item);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.getChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setChecked(b);
                println(item.getName() + item.getChecked());


                //새로 체크된 애
                if(item.getChecked() == true){
                    checkedlist.add(item);
                }
                //체크 취소된 애
                else{
                    checkedlist.remove(item);
                }

            }
        });
    }

    public ArrayList<Person> getCheckedlist() {
        return checkedlist;
    }
    public void setCheckedlist(){
        checkedlist.clear();
    }

    @Override
    public int getItemCount() {
        return personItems.size();
    }
    public ArrayList<Person> getItems() { return personItems; }
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
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_index = itemView.findViewById(R.id.textView_index);
            textView_Name=itemView.findViewById(R.id.textView_name);
            textView_Number=itemView.findViewById(R.id.textView_number);
            checkBox = itemView.findViewById(R.id.checkbox_person_summary);
        }

        public void setItem(Person item) {
            textView_Name.setText(item.getName());
            textView_Number.setText(Integer.toString(item.getId_num()));
            checkBox.setChecked(item.getChecked());
        }
    }
}
