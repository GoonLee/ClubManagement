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

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    //회원 프로필 저장 배열
    static ArrayList<Person> personItems=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.item_person,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Name, textView_Birth,textView_Sex,textView_Mobile,textView_Email,textView_Major;
        ImageView imageView_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(view.getContext(),PersonEditActivity.class);
                        intent.putExtra("pk",personItems.get(pos).getPk());
                        intent.putExtra("name",personItems.get(pos).getName());
                        intent.putExtra("id_num",personItems.get(pos).getId_num());
                        intent.putExtra("major",personItems.get(pos).getMajor());
                        //intent.putExtra("email",personItems.get(pos).getEmail());
                        //intent.putExtra("birthday",personItems.get(pos).getBirthday());
                        intent.putExtra("mobile",personItems.get(pos).getMobile());
                        intent.putExtra("gender",personItems.get(pos).getGender());
                        //intent.putExtra("picture",personItems.get(pos).getPicture());
                        view.getContext().startActivity(intent);
                    }
                }
            });

            textView_Name=itemView.findViewById(R.id.textView_name);
            //textView_Birth=itemView.findViewById(R.id.textView_birth);
            //textView_Email=itemView.findViewById(R.id.textView_email);
            textView_Major=itemView.findViewById(R.id.textView_major);
            textView_Mobile=itemView.findViewById(R.id.textView_mobile);
            textView_Sex=itemView.findViewById(R.id.textView_sex);
            imageView_profile = itemView.findViewById(R.id.imageView_profile);
        }

        public void setItem(Person item) {
            textView_Name.setText(item.getName());
            textView_Mobile.setText(item.getMobile());
            textView_Sex.setText(item.getGender());
            textView_Major.setText(item.getMajor());
            //textView_Email.setText(item.getEmail());
            //textView_Birth.setText(item.getBirthday());
            imageView_profile.setImageBitmap(item.getPicture());
        }
    }
}