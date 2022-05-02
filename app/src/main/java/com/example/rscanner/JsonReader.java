package com.example.rscanner;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    Context context;

    JsonReader(Context context){ this.context =context;}

    public List<Receipt> fillListWithJson() throws JSONException, IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new
                File(context.getFilesDir()+File.separator+"JsonData.txt")));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            sb.append(line);
    }
        JSONArray arr = new JSONArray(sb.toString());
        List<Receipt> allReceipts = new ArrayList<>();
        for(int i = 0; i < arr.length();i++) {

            allReceipts.add(new Receipt(arr.getJSONObject(i)));
        }

        for (Receipt i : allReceipts){
            System.out.println(i.toString());
        }
        return allReceipts;
    }

}
