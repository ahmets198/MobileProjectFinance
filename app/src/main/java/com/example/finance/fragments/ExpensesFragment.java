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
import com.example.finance.adapters.ExpenseAdapter;
import com.example.finance.database.DatabaseHelper;
import com.example.finance.models.Expense;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpensesFragment extends Fragment {
    private RecyclerView recyclerViewExpenses;
    private AutoCompleteTextView spinnerExpenseCategory;
    private EditText editTextAmount;
    private Button btnAddExpense;
    private TextView textViewTotalExpense;
    private List<Expense> expenseList;
    private DatabaseHelper databaseHelper;
    private ExpenseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        initializeViews(view);
        setupExpenseCategoryDropdown();
        loadExpenses();
        updateTotalExpense();

        return view;
    }

    private void initializeViews(View view) {
        textViewTotalExpense = view.findViewById(R.id.textViewTotalExpense);

        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);
        recyclerViewExpenses.setAdapter(adapter);

        spinnerExpenseCategory = view.findViewById(R.id.spinnerExpenseCategory);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> addNewExpense());
    }

    private void setupExpenseCategoryDropdown() {
        String[] expenseCategories = {"Bills", "Rent", "Interest", "Shopping"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_dropdown,
                expenseCategories
        );
        spinnerExpenseCategory.setAdapter(dropdownAdapter);
    }

    private void loadExpenses() {
        expenseList.clear();
        expenseList.addAll(databaseHelper.getAllExpenses());
        adapter.notifyDataSetChanged();
    }

    private void updateTotalExpense() {
        double totalExpense = calculateTotalExpense();
        String formattedTotal = String.format(Locale.getDefault(),
                "Total Expenses\n$%.2f", totalExpense);
        textViewTotalExpense.setText(formattedTotal);
    }

    private double calculateTotalExpense() {
        double total = 0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    private void addNewExpense() {
        String category = spinnerExpenseCategory.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(requireContext(), "Please select an expense category", Toast.LENGTH_SHORT).show();
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

            Expense expense = new Expense(category, amount, currentDate);
            databaseHelper.addExpense(expense);

            loadExpenses();
            updateTotalExpense();
            clearInputs();

            Toast.makeText(requireContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        spinnerExpenseCategory.setText("");
        editTextAmount.setText("");
        spinnerExpenseCategory.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExpenses();
        updateTotalExpense();
    }
}