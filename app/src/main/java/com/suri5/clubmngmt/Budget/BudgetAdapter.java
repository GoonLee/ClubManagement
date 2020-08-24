package com.suri5.clubmngmt.Budget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>{
    ArrayList<Budget> budgetItems=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.activity_person_show,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Budget item= budgetItems.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    public void addItem(Budget item){
        budgetItems.add(item);
    }

    public void setItems(ArrayList<Budget> budgetItems){
        this.budgetItems=budgetItems;
    }
    public Budget getItem(int position){
        return budgetItems.get(position);
    }
    public void setItem(int position, Budget item){
        budgetItems.set(position,item);
    }

    static  class ViewHolder extends RecyclerView.ViewHolder {
        // Todo : declare view variables

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Todo : assign views using findViewById()
        }

        public void setItem(Budget item) {
            //Todo : set value of views (ex) textView.setText())
        }
    }
}
