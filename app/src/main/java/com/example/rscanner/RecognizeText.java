package com.example.rscanner;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class RecognizeText {
InputImage image;
Context context;
String resultText = "Very bad stuff";
    public RecognizeText(InputImage image) {
        this.image = image;
    }
    public RecognizeText(InputImage image, Context context){
        this.image = image;
        this.context =context;
    }



    public String textRecognizer() {

        com.google.mlkit.vision.text.TextRecognizer recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text vText) {
                String text = vText.getText();
                resultText = text;
                System.out.println(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in recognizing text");
            }
        });
        while(!result.isComplete()) {
            continue;
        }
        try {
            result.wait();
        } catch(InterruptedException e) {

        }

        return result.getResult().getText();
        //return resultText;
    }
}
