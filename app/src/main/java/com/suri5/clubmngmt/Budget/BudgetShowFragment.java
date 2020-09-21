package com.suri5.clubmngmt.Budget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suri5.clubmngmt.R;

public class BudgetShowFragment extends Fragment {

    RecyclerView recyclerViewBudget;
    BudgetAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_budget_show, container, false);

        recyclerViewBudget=rootView.findViewById(R.id.recyclerviewBudget);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewBudget.setLayoutManager(layoutManager);
        adapter = new BudgetAdapter();

        //계좌 리사이클러뷰 test
        adapter.addItem(new Budget(0, "임시", "0920", true, 10000, "없음"));
        adapter.addItem(new Budget(0, "임시2", "0920", false, 10000, "없음"));
        adapter.addItem(new Budget(0, "임시3", "0920", true, 20000, "없음"));
        adapter.addItem(new Budget(0, "임시4", "0920", false, 10000, "없음"));
        adapter.addItem(new Budget(0, "임시5", "0922", true, 40000, "없음"));
        adapter.addItem(new Budget(0, "임시6", "0922", false, 30000, "없음"));
        adapter.addItem(new Budget(0, "임시7", "0921", true, 10000, "없음"));
        adapter.addItem(new Budget(0, "임시8", "0921", false, 20000, "없음"));
        recyclerViewBudget.setAdapter(adapter);

        return rootView;
    }
}