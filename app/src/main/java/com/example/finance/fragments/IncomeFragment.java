package com.example.finance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.adapters.IncomeAdapter;
import com.example.finance.database.DatabaseHelper;
import com.example.finance.models.Income;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeFragment extends Fragment {
    private RecyclerView recyclerViewIncome;
    private AutoCompleteTextView spinnerIncomeSource;
    private EditText editTextAmount;
    private Button btnAddIncome;
    private TextView textViewTotalIncome;
    private List<Income> incomeList;
    private DatabaseHelper databaseHelper;
    private IncomeAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        initializeViews(view);
        setupIncomeSourceDropdown();
        loadIncome();
        updateTotalIncome();

        return view;
    }

    private void initializeViews(View view) {
        textViewTotalIncome = view.findViewById(R.id.textViewTotalIncome);

        recyclerViewIncome = view.findViewById(R.id.recyclerViewIncome);
        recyclerViewIncome.setLayoutManager(new LinearLayoutManager(requireContext()));
        incomeList = new ArrayList<>();
        adapter = new IncomeAdapter(incomeList);
        recyclerViewIncome.setAdapter(adapter);

        spinnerIncomeSource = view.findViewById(R.id.spinnerIncomeSource);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        btnAddIncome.setOnClickListener(v -> addNewIncome());
    }

    private void setupIncomeSourceDropdown() {
        String[] incomeSources = {"Salary", "Rental Income", "Interest", "Financial return from Assets"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_dropdown,
                incomeSources
        );
        spinnerIncomeSource.setAdapter(dropdownAdapter);
    }

    private void loadIncome() {
        incomeList.clear();
        incomeList.addAll(databaseHelper.getAllIncome());
        adapter.notifyDataSetChanged();
    }

    private void updateTotalIncome() {
        double totalIncome = calculateTotalIncome();
        String formattedTotal = String.format(Locale.getDefault(),
                "Total Income\n$%.2f", totalIncome);
        textViewTotalIncome.setText(formattedTotal);
    }

    private double calculateTotalIncome() {
        double total = 0;
        for (Income income : incomeList) {
            total += income.getAmount();
        }
        return total;
    }

    private void addNewIncome() {
        String source = spinnerIncomeSource.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();

        if (source.isEmpty()) {
            Toast.makeText(requireContext(), "Please select an income source", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            Income income = new Income(source, amount, currentDate);
            databaseHelper.addIncome(income);

            loadIncome();
            updateTotalIncome();
            clearInputs();

            Toast.makeText(requireContext(), "Income added successfully", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        spinnerIncomeSource.setText("");
        editTextAmount.setText("");
        spinnerIncomeSource.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIncome();
        updateTotalIncome();
    }
}