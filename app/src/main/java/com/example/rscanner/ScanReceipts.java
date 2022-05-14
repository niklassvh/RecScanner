package com.example.rscanner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScanReceipts extends AppCompatActivity {
    ImageView imgView;
    TextView textView;
    Button retake;
    Button savePic;
    Intent takePic;
    Map<String,Double> m = new LinkedHashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipts);
        imgView = findViewById(R.id.imgViewPhoto);
        textView = findViewById(R.id.scannedText);
        retake = findViewById(R.id.retakePic);
        savePic = findViewById(R.id.savePic);
       // TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
        takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicLauncher.launch(takePic);
        retake.setOnClickListener(buttons);
        savePic.setOnClickListener(buttons);


    }
    View.OnClickListener buttons = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.retakePic:
                    takePicLauncher.launch(takePic);
                    break;
                case R.id.savePic:
                    try {
                        JsonWriter write = new JsonWriter(m,getApplicationContext(), ReceiptListManager.allReceipts);
                        write.write();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    ActivityResultLauncher<Intent> takePicLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        //TODO fixa image rotation
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if(data == null)
            {return;}
            Bundle bundle = data.getExtras();
            Bitmap bitMap = (Bitmap) bundle.get("data");
            //Rotera bild som visas i imgview
            //TODO fixa image rotation för snea bilder också.
            //TODO kolla om det är värt att lägga in cropping för kvittona
            Bitmap rotatedBitMap = Bitmap.createBitmap(bitMap,0,0,bitMap.getWidth(),
                    bitMap.getHeight(), setImageRotation(imgView), true);
            imgView.setImageBitmap(rotatedBitMap);
            // läs text vertikal vinkel
            InputImage image = InputImage.fromBitmap(bitMap, 90);
            // finish();
            textRecognizer(image);


        }
    });

    public void textRecognizer(InputImage image){
        TextRecognizer recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text vText) {

                String resultText = vText.getText();
                TextHandling textHandler = new TextHandling(getApplicationContext());
                 m = textHandler.textHandling(vText);
                mapToString mps = new mapToString(m);
                System.out.println(mps.getSb());
                textView.setText(new mapToString(m).getSb().toString());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in recognizing text");
            }
        });

    }
    public Matrix setImageRotation(ImageView img){
        Matrix matrix = new Matrix();
        matrix.postRotate(90,img.getWidth(),img.getHeight());
        return matrix;
    }
    public void cropImage(){

    }

}