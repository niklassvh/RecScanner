package com.example.rscanner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.oginotihiro.cropview.Crop;
import com.oginotihiro.cropview.CropView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImportReceipts extends AppCompatActivity {
    ImageView imgView;
    TextView textView;
    Button importPic;
    Button savePic;
    Intent picPhoto;
    Context cont = this;
    Map<String,Double> m = new LinkedHashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_receipts);
        imgView = findViewById(R.id.impImgViewPhoto);
        textView = findViewById(R.id.importedText);
        importPic = findViewById(R.id.importPic);
        savePic = findViewById(R.id.saveImp);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},102);
        }
         picPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        picPhotoLauncher.launch(picPhoto);
        importPic.setOnClickListener(buttons);
        savePic.setOnClickListener(buttons);
    }
    View.OnClickListener buttons = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.importPic:
                    picPhotoLauncher.launch(picPhoto);
                    break;
                case R.id.saveImp:
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
    ActivityResultLauncher<Intent> picPhotoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if(data == null)
                    {return;}

                    Uri selectedPhoto = data.getData();
                    Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

                    imgView.setImageURI(selectedPhoto);
                    //
                    try {
                        InputImage image = InputImage.fromFilePath(ImportReceipts.this,
                                selectedPhoto);
                       textRecognizer(image);

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    });

    public void textRecognizer(InputImage image) {
        TextRecognizer recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text vText) {
                String resultText = vText.getText();
                TextHandler textHandler = new TextHandler(getApplicationContext());
                m = textHandler.textHandling(vText);
                textView.setText(new mapToString(m).getSb().toString());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in recognizing text");
            }
        });


    }




    }
