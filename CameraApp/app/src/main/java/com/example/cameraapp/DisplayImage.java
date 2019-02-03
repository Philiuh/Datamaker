package com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DisplayImage extends AppCompatActivity {

    ImageView imageView;
    Bitmap workingbitmap;
    Bitmap mutablebitmap;
    float downx = 0, downy = 0;
    Canvas canvas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mutablebitmap);


    }


}
