package com.example.rscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
//TODO Cropfunktion,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       TextHandlingTest test = new TextHandlingTest(MainActivity.this);
        try {
            test.extractText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button importReceipt = (Button) findViewById(R.id.impKvitto);
        Button scanReceipt = findViewById(R.id.skannaKvitto);
        Button calculateReceipt = findViewById(R.id.calcKvitto);
        importReceipt.setOnClickListener(menuChoice);
        scanReceipt.setOnClickListener(menuChoice);
        calculateReceipt.setOnClickListener(menuChoice);

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

            }
        }
    };
}