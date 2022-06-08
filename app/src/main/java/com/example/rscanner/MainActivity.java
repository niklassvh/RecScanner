package com.example.rscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button usrButton;
    static Integer currentUser = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        Button importReceipt = (Button) findViewById(R.id.impKvitto);
        Button showReceipt = (Button) findViewById(R.id.visa);
        usrButton = (Button) findViewById(R.id.userbutton);
        Button scanReceipt = findViewById(R.id.skannaKvitto);
        Button calculateReceipt = findViewById(R.id.calcKvitto);
        importReceipt.setOnClickListener(menuChoice);
        scanReceipt.setOnClickListener(menuChoice);
        calculateReceipt.setOnClickListener(menuChoice);
        showReceipt.setOnClickListener(menuChoice);
        usrButton.setOnClickListener(menuChoice);
        ReceiptListManager.loadList(this);
       /*
       JsonReader jr = new JsonReader(MainActivity.this);
        try {allReceipts = jr.fillListWithJson();}
        catch (JSONException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}*/


        // för rec1bilden
        TextHandler th = new TextHandler(MainActivity.this);
        try {
            th.extractText();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException i){

        }

    }
    @Override
   public void onResume() {
        super.onResume();


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
                    userOwesAlert();
                    break;
                case R.id.visa:
                    ReceiptListManager.loadList(MainActivity.this);
                    Intent show = new Intent(MainActivity.this, showReceipts.class);
                    show.putExtra("allReceipts",(Serializable) ReceiptListManager.allReceipts);
                    startActivity(show);

                   // System.out.println(allReceipts.toString());
                    break;
                case R.id.userbutton:
                    if(currentUser == 1)
                        currentUser =2;
                    else
                        currentUser = 1;
                    usrButton.setText("Användare " + currentUser);
            }
        }
    };
    public void userOwesAlert(){
            Double usr1 = 0.00;
            Double usr2 = 0.00;
            List<Receipt> allReceipts = ReceiptListManager.allReceipts;
            for (int i = 0; i < allReceipts.size(); i++){
                if (allReceipts.get(i).usr == 1){
                   usr1 =+ allReceipts.get(i).sum;
                    System.out.println(usr1);
                }
                if (allReceipts.get(i).usr == 2){
                    usr2 =+ allReceipts.get(i).sum;
                    System.out.println(usr2);
                }
            }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if ( usr1 < usr2){

                builder.setMessage("Användare 1 är skyldig Användare 2 (" + (usr2 - usr1)/2 + ":-)");
            }
            if ( usr2 < usr1){
                builder.setMessage("Användare 2 är skyldig Användare 1 (" + (usr1 - usr2)/2 + ":-)");
             }
            if(usr1 == usr2){
                 builder.setMessage("Ingen användare är skyldig pengar");
             }
            if(allReceipts.isEmpty()){
                return;
            }

            builder.setCancelable(true);
            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Ta bort beräknade kvitton?");
                            builder.setCancelable(true);
                            builder.setPositiveButton(
                                    "Ja",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            deleteAllReceipts();
                                            dialog.cancel();

                                        }
                                    });
                            builder.setNegativeButton(
                                    "Nej",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        public void deleteAllReceipts(){
            ReceiptListManager.allReceipts.clear();
            JsonWriter.tryWrite(ReceiptListManager.allReceipts, this);
        }
    }
