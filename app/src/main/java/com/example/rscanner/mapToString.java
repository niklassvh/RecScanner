package com.example.rscanner;

import java.util.Map;

public class mapToString {
    private StringBuilder sb = new StringBuilder();
    mapToString(Map<String,Double> m) {
        String line;
        for (String s : m.keySet()) {
            Double v = m.get(s);
            sb.append(s + " " + v +":- \n");
        }
    }
    public StringBuilder getSb() {
        return sb;
    }
}
