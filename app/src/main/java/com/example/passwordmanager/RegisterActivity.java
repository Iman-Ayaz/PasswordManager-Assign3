package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText,confirmPasswordEditText;
    Button registerButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        databaseHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                long result=-1;
                // Validate username and password fields
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Validate username and password fields
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and confirm password match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    result = databaseHelper.addUser(username, password);
                    finish();
                }


                if (result != -1) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    // Navigate to login activity or perform other actions
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed - User Exists Already", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
