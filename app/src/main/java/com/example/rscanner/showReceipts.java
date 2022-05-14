package com.example.rscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class showReceipts extends AppCompatActivity {
    CardViewAdapter adapter;
   // List<Receipt> receipts;
    RecyclerView recyclerView;
    LinearLayoutManager linman;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_receipts);
        recyclerView = findViewById(R.id.recycler);
     //   receipts = (List<Receipt>) getIntent().getSerializableExtra("allReceipts");
        adapter = new CardViewAdapter(this);
        linman = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linman);
        recyclerView.setAdapter(adapter);

    }


}
