package com.example.rscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//TODO Cropfunktion,
    RecyclerView recyclerView;
    CardViewAdapter adapter;
    List<Receipt> allReceipts;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonReader jr = new JsonReader(MainActivity.this);
        try {allReceipts = jr.fillListWithJson();}
        catch (JSONException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

      /*TextHandlingTest test = new TextHandlingTest(MainActivity.this);
        try {test.extractText();}
        catch (IOException e) {e.printStackTrace();}*/
        Button importReceipt = (Button) findViewById(R.id.impKvitto);
        Button showReceipt = (Button) findViewById(R.id.visa);
        Button scanReceipt = findViewById(R.id.skannaKvitto);
        Button calculateReceipt = findViewById(R.id.calcKvitto);
        importReceipt.setOnClickListener(menuChoice);
        scanReceipt.setOnClickListener(menuChoice);
        calculateReceipt.setOnClickListener(menuChoice);
        showReceipt.setOnClickListener(menuChoice);


    }

    private View.OnClickListener menuChoice = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.impKvitto:
                     startActivity(new Intent(MainActivity.this, ImportReceipts.class));
                     break;
                case R.id.skannaKvitto:
                    startActivity(new Intent(MainActivity.this, ScanReceipts.class));
                    break;
                case R.id.calcKvitto:
                    System.out.println("Not yet implemented");
                    break;
                case R.id.visa:
                    Intent show = new Intent(MainActivity.this, showReceipts.class);
                    show.putExtra("allReceipts",(Serializable) allReceipts);
                    startActivity(show);
                    System.out.println(allReceipts.toString());
                    break;

            }
        }
    };


    }
