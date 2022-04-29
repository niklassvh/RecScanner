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
        JSONObject receiptPost = new JSONObject();
        JSONObject itemsObject = new JSONObject();
        receiptPost.put("id", 0);
        receiptPost.put("user", 1);
        int count = 1;
        for(Map.Entry<String, Double> i : items.entrySet()){

            if (count == items.size()){
                receiptPost.put("sum",i.getValue());
            }
            else {
                itemsObject.put(i.getKey(), i.getValue());
            }
            count++;

        }
        receiptPost.put("items", itemsObject);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                File(context.getFilesDir()+File.separator+"JsonData.txt"),true));
        bufferedWriter.write(receiptPost.toString(2));
        bufferedWriter.close();
    }





}
