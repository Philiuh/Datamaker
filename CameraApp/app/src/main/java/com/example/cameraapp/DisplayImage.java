package com.example.cameraapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DisplayImage extends AppCompatActivity implements View.OnTouchListener{

    ImageView imageView;
    Bitmap workingbitmap;
    Bitmap mutablebitmap;
    float downx = 0, downy = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mutablebitmap);
        imageView.setOnTouchListener(this);

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                v.performClick();
                downx = event.getX();
                downy = event.getY();

                Canvas canvas = new Canvas(mutablebitmap);

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(100);


                canvas.drawCircle(downx, downy,300, paint);
                imageView.setImageBitmap(mutablebitmap);
                break;
        }
        return true;
    }
}
