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
import java.util.List;
import java.util.Map;

public class JsonWriter {

    JSONArray allReceipts = new JSONArray();
    Context context;

    JsonWriter(Context c){
        this.context = c;

    }
    JsonWriter(Map<String, Double> m, Context c, List<Receipt> receipts) throws JSONException {
        if(!receipts.isEmpty()) {
            for (Receipt i : receipts) {
                allReceipts.put(i.createJsonObject());
            }
        }
        if (m.size() > 0)
        {
            allReceipts.put(new Receipt(m,MainActivity.currentUser,allReceipts.length()).createJsonObject());
        }

        this.context = c;

    }
    JsonWriter(Map<String, Double> m, Context c) throws JSONException {
        Receipt rec = new Receipt(m, 1,0);
        Receipt rec2 = new Receipt(m, 2,1);
        allReceipts.put(rec.createJsonObject());
        allReceipts.put(rec2.createJsonObject());
        this.context = c;
    }

    JsonWriter(List<Receipt> receipts, Context c) throws JSONException {
        for (Receipt i : receipts){
            allReceipts.put(i.createJsonObject());
        }
        this.context = c;
    }


    public void write() throws JSONException, IOException {
        File file = new File(context.getFilesDir()+File.separator+"JsonData.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(allReceipts.toString(2));
        bufferedWriter.close();


    }

    public static void tryWrite(List<Receipt> receiptList, Context c) {
        try {
            JsonWriter myWriter = new JsonWriter(receiptList, c);
            myWriter.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
