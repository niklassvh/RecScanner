package com.example.rscanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TextHandlingTest {
    Context context;
    public TextHandlingTest(Context context){
        this.context = context;
    }

    public void extractText () throws IOException {


        AssetManager amng = context.getAssets();
        InputStream i = amng.open("rec1.jpg");
        Bitmap b = BitmapFactory.decodeStream(i);
        InputImage inputImage = InputImage.fromBitmap(b, 0);
        TextRecognizer recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text vText) {
                textHandling(vText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in recognizing text");
            }
        });

    }

    public void textHandling(Text text){

        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        String resultText = text.getText();
        for(Text.TextBlock block: text.getTextBlocks()){
            //String blockText = block.getText();
           // System.out.println(blockText);
            for(Text.Line line : block.getLines()){
                String lineText = line.getText();
              // System.out.println(lineText);
                if (isPrice(lineText)) {
                    price.add(lineText);
                }
                else if (isNotUsed(lineText)){

                }
                else{
                    items.add(lineText);
                }

                for(Text.Element element : line.getElements()) {
                    //String elementText = element.getText();
                   // System.out.println(elementText);
                }
            }

        }
        System.out.println();
        for (String prices : price){
            System.out.println("priser:" + prices);
        }
        for(String item : items){
            System.out.println("varor: " + item);
        }
        Map<String,String> m = new TreeMap<>();
        for (int i = 0; i < price.size();i++){
            m.put(items.get(i), price.get(i));

        }
        for (String s : m.keySet()){
            String v = m.get(s);
            System.out.println(s + "    " + v);

        }

    }

    public Boolean isPrice(String line)
    {
        return line.matches("\\d+(,|\\.)\\d+");
    }

    //TODO gör nya metoder för varje regexcheck
    public Boolean isNotUsed(String line){
        if (line.matches("\\d+,\\d+ ?.g.*")){
            return true;
        }
        if(line.matches("\\d+ st.*")){
            return true;
        }

       return false;
    }
}
