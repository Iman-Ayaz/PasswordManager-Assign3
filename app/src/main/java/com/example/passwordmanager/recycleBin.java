package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class recycleBin extends AppCompatActivity {
    RecyclerView rvPasswordEntries;
    LinearLayoutManager manager;
    recyclebinAdapter adapter;
    ArrayList<passwordEntryModel> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);
        init();

    }
    private void init()
    {
        rvPasswordEntries= findViewById(R.id.rvRecycleBin);
        rvPasswordEntries.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        rvPasswordEntries.setLayoutManager(manager);

        DatabaseHelper database = new DatabaseHelper(this);
        contacts = database.readRecyclebin();
        database.close();

        adapter = new recyclebinAdapter(this, contacts);
        rvPasswordEntries.setAdapter(adapter);
    }
}