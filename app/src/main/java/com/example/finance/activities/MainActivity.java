package com.example.finance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import androidx.core.content.ContextCompat;

import com.example.finance.R;
import com.example.finance.fragments.*;

public class MainActivity extends AppCompatActivity {
    private Button btnAssets, btnIncome, btnExpenses, btnSaving, btnGoal, btnLogout;
    private boolean isLoggedIn = false; // Kullanıcı giriş durumu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        // first load login screen
        loadFragment(new LoginFragment());

        // this is for hiding buttons
        toggleMenuButtons(false);

        setupButtonSelection();
    }

    private void initializeViews() {
        btnAssets = findViewById(R.id.btnAssets); // our pages we used in app
        btnIncome = findViewById(R.id.btnIncome);
        btnExpenses = findViewById(R.id.btnExpenses);
        btnSaving = findViewById(R.id.btnSaving);
        btnGoal = findViewById(R.id.btnGoal);
        btnLogout = findViewById(R.id.btnLogout); // Logout button
    }

    private void setupButtonSelection() {
        Button[] buttons = {btnAssets, btnIncome, btnExpenses, btnSaving, btnGoal};

        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                if (!isLoggedIn) {
                    // If user didn`t login yet. Shows login screen
                    loadFragment(new LoginFragment());
                    return;
                }

                for (Button b : buttons) {
                    b.setBackgroundColor(ContextCompat.getColor(this, R.color.button_normal));
                }
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.button_selected));
                handleNavigation(button.getText().toString());
            });
        }

        // Logout button click
        btnLogout.setOnClickListener(v -> onLogout());
    }

    private void handleNavigation(String buttonText) {   // all chosen fragment cases in our app
        Fragment selectedFragment = null;

        switch (buttonText.toUpperCase()) {
            case "ASSETS":
                selectedFragment = new AssetsFragment();
                break;
            case "INCOME":
                selectedFragment = new IncomeFragment();
                break;
            case "EXPENSE":
                selectedFragment = new ExpensesFragment();
                break;
            case "SAVING":
                selectedFragment = new SavingsFragment();
                break;
            case "GOAL":
                selectedFragment = new GoalsFragment();
                break;
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }
    }

    public void onLoginSuccess() {
        // ıf log in is succes we call this function
        isLoggedIn = true;

        // Menü düğmelerini göster
        toggleMenuButtons(true);

        // Varsayılan olarak AssetsFragment yüklenir
        btnAssets.performClick();
    }

    private void onLogout() {
        // this is for logour function
        isLoggedIn = false;

        // hide menu button
        toggleMenuButtons(false);

        // after logout load login screen again
        loadFragment(new LoginFragment());
    }

    private void toggleMenuButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;

        btnAssets.setVisibility(visibility);
        btnIncome.setVisibility(visibility);
        btnExpenses.setVisibility(visibility);
        btnSaving.setVisibility(visibility);
        btnGoal.setVisibility(visibility);
        btnLogout.setVisibility(visibility); // our menu buttons
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
