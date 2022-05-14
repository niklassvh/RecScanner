package com.example.rscanner;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptListManager {
   public static List<Receipt> allReceipts = new ArrayList<>();
    public static void loadList(Context c){
        JsonReader jr = new JsonReader(c);
        try {allReceipts = jr.fillListWithJson();}
        catch (JSONException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

    }
}
