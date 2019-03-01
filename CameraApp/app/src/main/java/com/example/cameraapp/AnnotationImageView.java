package com.example.cameraapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class AnnotationImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "AnnotationImageView";

    float downx = 0;
    float downy = 0;
    Paint paint = new Paint();
    ArrayList<Float> x_dots = new ArrayList<>();
    ArrayList<Float> y_dots = new ArrayList<>();
    Path path;
    com.example.cameraapp.AnnotationImageView AIV;      //NullPointerException (null object reference)


    public AnnotationImageView(Context context) {
        super(context);
        paint.setColor(Color.rgb(255,255,255));
        AIV = findViewById(R.id.mimageView);
    }

    public AnnotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AnnotationImageView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }


    @Override
    public void onDrawForeground(Canvas canvas) {
        Log.d(TAG, "ondrawforeground");

        if (x_dots.size()>1) {
            path = new Path();
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawCircle(x_dots.get(0),y_dots.get(0),5,paint);
            path.moveTo(x_dots.get(0), y_dots.get(0));

            for (int j = 1; j < x_dots.size(); j++) {

                canvas.drawCircle(x_dots.get(j),y_dots.get(j),5,paint);
                path.lineTo(x_dots.get(j), y_dots.get(j));

            }
            path.close();
            canvas.drawPath(path, paint);
        }

        else {

            canvas.drawCircle(downx,downy,5,paint);
        }

        super.onDrawForeground(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                x_dots.add(downx);
                y_dots.add(downy);
                
                AIV.getLayoutParams().width = 100;  //NullPointerException (null object reference)
                AIV.getLayoutParams().height = 100;
                super.getLayoutParams().width = 100;    //It doesn't work at all idk why
                super.getLayoutParams().height = 100;

                invalidate();
        }

        return super.onTouchEvent(event);
    }
}

