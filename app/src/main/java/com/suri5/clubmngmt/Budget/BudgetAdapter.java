package com.suri5.clubmngmt.Budget;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>{
    ArrayList<Budget> budgetItems=new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBudgetDate,textViewBudgetAmount, textViewBudgetTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBudgetDate=itemView.findViewById(R.id.textViewBudgetDate);
            textViewBudgetAmount=itemView.findViewById(R.id.textViewBudgetAmount);
            textViewBudgetTitle=itemView.findViewById(R.id.textViewBudgetTitle);

        }

        public void setItem(Budget item) {
            textViewBudgetDate.setText(item.date);

            if(item.isIncome==true){
                textViewBudgetAmount.setText(""+item.amount+"원");
                textViewBudgetAmount.setTextColor(Color.BLUE);
            }else{
                textViewBudgetAmount.setText("-"+item.amount+"원");
                textViewBudgetAmount.setTextColor(Color.RED);
            }

            textViewBudgetTitle.setText(item.title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.budget_item,parent,false);

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


}
