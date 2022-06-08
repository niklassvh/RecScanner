package com.example.rscanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class showReceipts extends AppCompatActivity {
    ViewAdapter adapter;
   // List<Receipt> receipts;
    RecyclerView recyclerView;
    LinearLayoutManager linman;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_receipts);
        recyclerView = findViewById(R.id.recycler);
     //   receipts = (List<Receipt>) getIntent().getSerializableExtra("allReceipts");
        adapter = new ViewAdapter(this);
        linman = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linman);
        recyclerView.setAdapter(adapter);

    }


}
