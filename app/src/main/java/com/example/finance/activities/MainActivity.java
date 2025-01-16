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

        // İlk olarak LoginFragment'i yükle
        loadFragment(new LoginFragment());

        // Düğmeleri gizle
        toggleMenuButtons(false);

        setupButtonSelection();
    }

    private void initializeViews() {
        btnAssets = findViewById(R.id.btnAssets);
        btnIncome = findViewById(R.id.btnIncome);
        btnExpenses = findViewById(R.id.btnExpenses);
        btnSaving = findViewById(R.id.btnSaving);
        btnGoal = findViewById(R.id.btnGoal);
        btnLogout = findViewById(R.id.btnLogout); // Logout butonu
    }

    private void setupButtonSelection() {
        Button[] buttons = {btnAssets, btnIncome, btnExpenses, btnSaving, btnGoal};

        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                if (!isLoggedIn) {
                    // Kullanıcı giriş yapmadıysa Login ekranına yönlendir
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

        // Logout butonuna tıklama işlemi
        btnLogout.setOnClickListener(v -> onLogout());
    }

    private void handleNavigation(String buttonText) {
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
        // LoginFragment'ten başarılı giriş sonrası çağrılır
        isLoggedIn = true;

        // Menü düğmelerini göster
        toggleMenuButtons(true);

        // Varsayılan olarak AssetsFragment yüklenir
        btnAssets.performClick();
    }

    private void onLogout() {
        // Giriş yapmamış gibi ayarlama
        isLoggedIn = false;

        // Menü düğmelerini gizle
        toggleMenuButtons(false);

        // LoginFragment'i yükle
        loadFragment(new LoginFragment());
    }

    private void toggleMenuButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;

        btnAssets.setVisibility(visibility);
        btnIncome.setVisibility(visibility);
        btnExpenses.setVisibility(visibility);
        btnSaving.setVisibility(visibility);
        btnGoal.setVisibility(visibility);
        btnLogout.setVisibility(visibility); // Logout butonunu da kontrol et
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
