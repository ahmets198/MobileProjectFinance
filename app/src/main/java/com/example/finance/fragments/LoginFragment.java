package com.example.finance.fragments; // Ahmet Sazan worked on this page

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finance.R;
import com.example.finance.activities.MainActivity;

public class LoginFragment extends Fragment {
    private EditText editUsername, editPassword;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editUsername = view.findViewById(R.id.editUsername);
        editPassword = view.findViewById(R.id.editPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> handleLogin());

        return view;
    }

    private void handleLogin() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simple user name and password to confirm log in
        if (username.equals("admin") && password.equals("1234")) {
            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).onLoginSuccess();
        } else {
            Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
