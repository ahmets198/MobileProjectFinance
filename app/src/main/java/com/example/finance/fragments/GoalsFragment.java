package com.example.finance.fragments; // Ahmet Sazan worked on this page

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.finance.R;
import com.example.finance.database.DatabaseHelper;
import com.example.finance.models.Goal;
import com.example.finance.models.Expense;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoalsFragment extends Fragment {
    private EditText editTextGoalAmount;
    private EditText editTextGoalDescription;
    private Button btnSetGoal;
    private ProgressBar progressBarGoal;
    private TextView textViewProgress;
    private TextView textViewTimeEstimate;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        databaseHelper = new DatabaseHelper(requireContext());
        initializeViews(view);
        updateGoalProgress();
        return view;
    }

    private void initializeViews(View view) {
        editTextGoalAmount = view.findViewById(R.id.editTextGoalAmount);
        editTextGoalDescription = view.findViewById(R.id.editTextGoalDescription);
        btnSetGoal = view.findViewById(R.id.btnSetGoal);
        progressBarGoal = view.findViewById(R.id.progressBarGoal);
        textViewProgress = view.findViewById(R.id.textViewProgress);
        textViewTimeEstimate = view.findViewById(R.id.textViewTimeEstimate);

        btnSetGoal.setOnClickListener(v -> setNewGoal());

        Button btnResetData = view.findViewById(R.id.btnResetData);
        btnResetData.setOnClickListener(v -> showResetConfirmationDialog());
    }

    private void setNewGoal() {
        String amountStr = editTextGoalAmount.getText().toString();
        String description = editTextGoalDescription.getText().toString();

        if (!amountStr.isEmpty() && !description.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                double currentSavings = calculateCurrentSavings();
                Goal goal = new Goal(description, amount, currentSavings);
                databaseHelper.setGoal(goal);
                updateGoalProgress();
                clearInputs();
                Toast.makeText(requireContext(), "Goal set successfully", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Reset All Data")
                .setMessage("Are you sure you want to delete all data? This cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    databaseHelper.clearAllData();
                    Toast.makeText(requireContext(), "All data has been reset", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    updateGoalProgress();
                    requireActivity().recreate();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private double calculateCurrentSavings() {
        double totalAssets = databaseHelper.getTotalWealth();
        double totalIncome = databaseHelper.getTotalIncome();
        double totalExpenses = calculateTotalExpenses();

        double netSavings = (totalAssets + totalIncome) - totalExpenses;
        return Math.max(0, netSavings);
    }

    private double calculateTotalExpenses() {
        double total = 0;
        List<Expense> expenses = databaseHelper.getAllExpenses();
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private double calculateMonthlyIncome() {
        String currentYearMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
                .format(new Date());
        return databaseHelper.getMonthlyIncome(currentYearMonth);
    }

    private double calculateMonthlyExpenses() {
        String currentYearMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
                .format(new Date());
        return databaseHelper.getMonthlyExpensesByCategory("all", currentYearMonth);
    }

    private String calculateTimeToGoal(Goal goal) {
        double remainingAmount = goal.getTargetAmount() - goal.getCurrentAmount();
        if (remainingAmount <= 0) {
            return "Goal achieved!";
        }

        double monthlyIncome = calculateMonthlyIncome();
        double monthlyExpenses = calculateMonthlyExpenses();
        double monthlySavings = monthlyIncome - monthlyExpenses;

        if (monthlySavings <= 0) {
            return "Unable to calculate - no monthly savings";
        }

        double monthsNeeded = remainingAmount / monthlySavings;

        int years = (int) (monthsNeeded / 12);
        int months = (int) (monthsNeeded % 12);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, (int) monthsNeeded);
        String estimatedDate = new SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                .format(calendar.getTime());

        if (years > 0) {
            return String.format("Estimated time: %d years, %d months\nEstimated completion: %s",
                    years, months, estimatedDate);
        } else {
            return String.format("Estimated time: %d months\nEstimated completion: %s",
                    months, estimatedDate);
        }
    }

    private void updateGoalProgress() {
        Goal currentGoal = databaseHelper.getCurrentGoal();
        if (currentGoal != null) {
            double currentSavings = calculateCurrentSavings();
            currentGoal.setCurrentAmount(currentSavings);

            double progress = (currentSavings / currentGoal.getTargetAmount()) * 100;
            progress = Math.min(100, progress);

            progressBarGoal.setProgress((int) progress);
            textViewProgress.setText(String.format("%.1f%% of %s ($%.2f/$%.2f)",
                    progress,
                    currentGoal.getDescription(),
                    currentSavings,
                    currentGoal.getTargetAmount()));

            textViewTimeEstimate.setText(calculateTimeToGoal(currentGoal));

            databaseHelper.addOrUpdateGoal(currentGoal);
        } else {
            textViewTimeEstimate.setText("");
        }
    }

    private void clearInputs() {
        editTextGoalAmount.setText("");
        editTextGoalDescription.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGoalProgress();
    }
}