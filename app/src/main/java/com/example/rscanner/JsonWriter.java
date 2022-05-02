package com.example.rscanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonWriter {
    Map<String, Double> items;
    Context context;
    JsonWriter(Map<String, Double> m, Context c){
        this.items = m;
        this.context = c;
    }

    public void write() throws JSONException, IOException {
        JSONArray allReceipts = new JSONArray();
        Receipt rec = new Receipt(items, 1,1);
        Receipt rec2 = new Receipt(items, 2,2);

        allReceipts.put(rec.createJsonObject());
        allReceipts.put(rec2.createJsonObject());
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                File(context.getFilesDir()+File.separator+"JsonData.txt")));
        bufferedWriter.write(allReceipts.toString(2));
        bufferedWriter.close();

    }





}
