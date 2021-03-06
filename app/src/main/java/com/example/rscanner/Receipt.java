package com.example.rscanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receipt implements Serializable {
    private List<ReceiptItem> items = new ArrayList<>();
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
            getItems().add(new ReceiptItem(key,val));
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
                    getItems().add(new ReceiptItem(i.getKey(), i.getValue()));
                }
                count++;
        }
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public class ReceiptItem implements Serializable {
        private String name;
        private Double price;

        //public transient int id;

        ReceiptItem(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return "    name=" + getName() +
                    " price= " + price + '\n';
        }

        public String getName() {
            return name;
        }
        public Double getprice() {
            return price;
        }
    }

    public JSONObject createJsonObject() throws JSONException {
        JSONObject receiptPost = new JSONObject();
        JSONArray itemArray = new JSONArray();
        receiptPost.put("id", id);
        receiptPost.put("user", usr);
        receiptPost.put("sum", sum);
        for (ReceiptItem r : getItems()){
            JSONObject itemsObject = new JSONObject();
            itemsObject.put(r.getName(), r.price);
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
                "items=" + '\n' + getItems() +'\n'+
                "sum=" + sum +'\n'+
                '}'+ '\n';
    }
    public void reCalculateSum(Double newSum)
    {
        System.out.println(newSum);
        Double newVal = 0.00;
        if(newSum < 0)
        {
             newVal = sum + newSum*(-1);
        }
        else {
             newVal = sum - newSum;
            String.format("%.2f", newVal);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        this.sum = Double.valueOf(df.format(newVal).replaceAll(",","."));
        System.out.println(this.sum);
    }
}
