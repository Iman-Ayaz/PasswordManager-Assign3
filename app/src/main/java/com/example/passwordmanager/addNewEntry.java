package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addNewEntry extends AppCompatActivity {

    EditText  etUsername, etPassword,etURL;
    Button btnAdd, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id. etPassword);
        etURL=findViewById(R.id.etURL);
        btnAdd= findViewById(R.id.btnAdd);
        btnCancel= findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString();
                String url =etURL.getText().toString();

                DatabaseHelper myDatabaseHelper = new DatabaseHelper(addNewEntry.this);

                myDatabaseHelper.addEntry(name, password,url);

                startActivity(new Intent(addNewEntry.this, VaultActivity.class));
                finish();
            }
        });
    }


}