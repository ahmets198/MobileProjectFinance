package com.example.finance.fragments; // Ahmet Sazan worked on this page

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.adapters.SavingsAdapter;
import com.example.finance.database.DatabaseHelper;
import com.example.finance.models.Expense;
import com.example.finance.models.Saving;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SavingsFragment extends Fragment {
    private TextView textViewTotalSavings;
    private TextView textViewNetAvailable;
    private RecyclerView recyclerViewSavings;
    private DatabaseHelper databaseHelper;
    private SavingsAdapter adapter;
    private List<Saving> savingsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savings, container, false);
        databaseHelper = new DatabaseHelper(requireContext());
        initializeViews(view);
        setupRecyclerView();
        loadSavings();
        updateSavingsInfo();
        return view;
    }

    private void initializeViews(View view) {
        textViewTotalSavings = view.findViewById(R.id.textViewTotalSavings);
        textViewNetAvailable = view.findViewById(R.id.textViewNetAvailable);
        recyclerViewSavings = view.findViewById(R.id.recyclerViewSavings);
    }

    private void setupRecyclerView() {
        recyclerViewSavings.setLayoutManager(new LinearLayoutManager(requireContext()));
        savingsList = new ArrayList<>();
        adapter = new SavingsAdapter(savingsList);
        recyclerViewSavings.setAdapter(adapter);
    }

    private void updateSavingsInfo() {
        // Calculate net available for saving
        double totalIncome = databaseHelper.getTotalIncome();
        double totalExpenses = calculateTotalExpenses();

        double netAvailable = totalIncome - totalExpenses;

        String formattedAmount;
        if (netAvailable <= 0) {
            formattedAmount = "$0.00";
        } else {
            formattedAmount = String.format(Locale.getDefault(), "$%.2f", netAvailable);
        }

        // Display the amounts
        textViewTotalSavings.setText("Total Savings: " + formattedAmount);
        textViewNetAvailable.setText("Net Available for Saving: " + formattedAmount);
    }

    private double calculateTotalExpenses() {
        double total = 0;
        List<Expense> expenses = databaseHelper.getAllExpenses();
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private void loadSavings() {
        savingsList.clear();
        savingsList.addAll(databaseHelper.getAllSavings());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSavings();
        updateSavingsInfo();
    }
}