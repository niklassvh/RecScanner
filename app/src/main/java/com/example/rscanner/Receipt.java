package com.example.rscanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Receipt {
    List<ReceiptItem> items = new ArrayList<>();
    Integer id;
    Integer usr;
    Double sum = 0.00;
    Receipt(JSONObject jo) throws JSONException {
        this.id= jo.getInt("id");
        this.usr=jo.getInt("user");
        this.sum = jo.getDouble("sum");
        JSONArray arr = jo.getJSONArray("items");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject lineKeyVal = arr.getJSONObject(i);
            String key = lineKeyVal.keys().next();
            Double val = lineKeyVal.getDouble(key);
            items.add(new ReceiptItem(key,val));
        }
    }
    Receipt(Map<String, Double> map, Integer usr, Integer id) throws JSONException {
        this.id = id;
        this.usr = usr;
        Integer count = 1;

            for (Map.Entry<String, Double> i : map.entrySet()) {
                if (count == map.size()) {
                    sum = i.getValue();
                } else {
                    items.add(new ReceiptItem(i.getKey(), i.getValue()));
                }
                count++;
        }
    }
    public class ReceiptItem {
        String name;
        Double price;

        ReceiptItem(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return "    name=" + name  +
                    " price= " + price + '\n';
        }
    }

    public JSONObject createJsonObject() throws JSONException {
        JSONObject receiptPost = new JSONObject();
        JSONArray itemArray = new JSONArray();
        receiptPost.put("id", id);
        receiptPost.put("user", usr);
        receiptPost.put("sum", sum);
        for (ReceiptItem r : items){
            JSONObject itemsObject = new JSONObject();
            itemsObject.put(r.name, r.price);
            itemArray.put(itemsObject);
        }
        receiptPost.put("items", itemArray);

    return receiptPost;
    }

    @Override
    public String toString() {
        return "{" + '\n' +
                "id=" + id + '\n' +
                "usr=" + usr +'\n'+
                "items=" + '\n' +items +'\n'+
                "sum=" + sum +'\n'+
                '}'+ '\n';
    }
}
