package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VaultActivity extends AppCompatActivity {

    FloatingActionButton fabAdd, fabRecycle;
    RecyclerView rvPasswordEntries;
    LinearLayoutManager manager;
    passwordEntryAdapter adapter;
    ArrayList<passwordEntryModel> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);
        init();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaultActivity.this, addNewEntry.class);
                startActivity(intent);
            }
        });

        fabRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaultActivity.this, recycleBin.class);
                startActivity(intent);
            }
        });
    }
    private void init()
    {
        fabAdd = findViewById(R.id.fabAdd);
        fabRecycle = findViewById(R.id.fabRecycle);
        rvPasswordEntries= findViewById(R.id.rvContacts);
        rvPasswordEntries.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        rvPasswordEntries.setLayoutManager(manager);

        DatabaseHelper database = new DatabaseHelper(this);
        contacts = database.readAllEntries();
        database.close();

        adapter = new passwordEntryAdapter(this, contacts);
        rvPasswordEntries.setAdapter(adapter);
    }
}