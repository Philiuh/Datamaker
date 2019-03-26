package com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class DisplayImage extends AppCompatActivity {

    PhotoView mPhotoView;
    Bitmap workingbitmap, mutablebitmap;
    TextView tvMenu;
    Paint paint = new Paint();
    Canvas canvas;
    FrameLayout frameLayout;

    ArrayList<Float> x_dots = new ArrayList<>();
    ArrayList<Float> y_dots = new ArrayList<>();
    ArrayList<String> obj_names = new ArrayList<>();

    Toast toast = Toast.makeText(this, "Сперва сделайте изображение",Toast.LENGTH_LONG);

    private int i = 0, j = 0;
    private boolean new_object_flag;

    final int MENU_CLEAN_CANVAS = 0;
    final int MENU_NEW_OBJECT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        frameLayout = findViewById(R.id.fml);
        mPhotoView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);

        mPhotoView.setImageBitmap(mutablebitmap);
        canvas = new Canvas(mutablebitmap);
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());

        tvMenu = findViewById(R.id.actionMenu);

        registerForContextMenu(tvMenu);

        paint.setColor(Color.rgb(0,255,0));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);


    }

    public class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {

            Log.wtf("TAG","Kd pressed: x [" + x * mPhotoView.getWidth() + "] y [" + y * mPhotoView.getHeight() + "]");
            Log.wtf("TAG","Width: " + mPhotoView.getWidth() + " Heigth: " + mPhotoView.getHeight());
            x_dots.add(x * canvas.getWidth());
            y_dots.add(y * canvas.getHeight());
            onDrawForeground(canvas);
            mPhotoView.invalidate();
        }
    }

    public void onDrawForeground(Canvas canvas) {

        Log.wtf("TAG", "start drawing");

        if (x_dots.size() > 1 && new_object_flag) {
            if(sqrt(Math.pow(x_dots.get(i+1) - x_dots.get(i),2) + Math.pow(y_dots.get(i+1) - y_dots.get(i),2))>100)
            {
                while (i < x_dots.size() - 1) {
                    canvas.drawCircle(x_dots.get(i + 1), y_dots.get(i + 1), 10, paint);
                    canvas.drawLine(x_dots.get(i), y_dots.get(i), x_dots.get(i + 1), y_dots.get(i + 1), paint);
                    i++;
                }
            }
            else {
                canvas.drawLine(x_dots.get(i), y_dots.get(i), x_dots.get(j), y_dots.get(j), paint);
                x_dots.remove(i+1);
                y_dots.remove(i+1);
                i++;
                FrameLayout.LayoutParams EditTextParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                FrameLayout.LayoutParams ButtonParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

                EditTextParams.gravity = Gravity.CENTER;

                ButtonParams.gravity = Gravity.CENTER;
                ButtonParams.topMargin = 150;

                final EditText TempEditText = new EditText(this);
                Button TempButton = new Button(this);

                TempButton.setText("Ок");
                TempButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void OnCLick(View v) {
                        obj_names.add(TempEditText.getText().toString());
                        frameLayout.removeAllViews();
                        toast.show();
                    }
                });

                TempEditText.setBackgroundColor(Color.WHITE);
                TempEditText.setLinkTextColor(Color.BLACK);
                TempEditText.setText("Введите название объекта");

                frameLayout.addView(TempEditText, EditTextParams);
                frameLayout.addView(TempButton,ButtonParams);
            }
        }
        else {
            canvas.drawCircle(x_dots.get(i), y_dots.get(i), 10, paint);
            new_object_flag = true;
        }
        mPhotoView.onDrawForeground(canvas);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case (R.id.actionMenu):
                menu.add(0,MENU_CLEAN_CANVAS,0,"Очистить изображение");
                menu.add(0,MENU_NEW_OBJECT,0,"Добавить объект");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // пункты меню
            case MENU_CLEAN_CANVAS:
                x_dots.clear();
                y_dots.clear();
                obj_names.clear();
                canvas = null;
                workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
                mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);
                mPhotoView.setImageBitmap(mutablebitmap);
                canvas = new Canvas(mutablebitmap);
                paint.setColor(Color.rgb(0,255,0));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeWidth(10);
                i = 0;
                break;
            case MENU_NEW_OBJECT:
                j = i;
                new_object_flag = false;
                break;
        }
        return super.onContextItemSelected(item);
    }
}
