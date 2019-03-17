package com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import java.util.ArrayList;

public class DisplayImage extends AppCompatActivity {

    PhotoView mPhotoView;
    Bitmap workingbitmap, mutablebitmap;
    TextView tvMark, tvModel, tvPosition, tvMenu;
    Paint paint = new Paint();
    Path path;
    Canvas canvas;

    private float x_start, y_start;
    ArrayList<Float> x_dots = new ArrayList<>();
    ArrayList<Float> y_dots = new ArrayList<>();
    private int i = 0;

    final int MENU_POSITION_FRONT = 3;
    final int MENU_POSITION_BACK = 4;
    final int MENU_POSITION_FRONT_LEFT = 5;
    final int MENU_POSITION_FRONT_RIGHT = 6;

    final int MENU_CLEAN_CANVAS = 1;
    final int MENU_NEW_OBJECT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        mPhotoView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);

        mPhotoView.setImageBitmap(mutablebitmap);
        canvas = new Canvas(mutablebitmap);
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());

        tvMark = findViewById(R.id.markView);
        tvModel = findViewById(R.id.modelView);
        tvPosition = findViewById(R.id.positionView);
        tvMenu = findViewById(R.id.actionMenu);

        registerForContextMenu(tvMark);
        registerForContextMenu(tvModel);
        registerForContextMenu(tvPosition);
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
        canvas.drawCircle(x_dots.get(i),y_dots.get(i),10,paint);

        if (x_dots.size() > 1) {
            while (i < x_dots.size() - 1) {
                canvas.drawCircle(x_dots.get(i + 1),y_dots.get(i + 1),10,paint);
                canvas.drawLine(x_dots.get(i), y_dots.get(i), x_dots.get(i + 1), y_dots.get(i + 1), paint);
                i++;
            }
        }
        else canvas.drawCircle(x_dots.get(i),y_dots.get(i),10,paint);
        mPhotoView.onDrawForeground(canvas);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case (R.id.positionView):
                menu.add(0,MENU_POSITION_FRONT,0,"Спереди");
                menu.add(0,MENU_POSITION_BACK,0,"Сзади");
                menu.add(0,MENU_POSITION_FRONT_LEFT,0,"Перед налево");
                menu.add(0,MENU_POSITION_FRONT_RIGHT,0,"Перед направо");
                break;
            case (R.id.actionMenu):
                menu.add(0,MENU_CLEAN_CANVAS,0,"Очистить изображение");
                menu.add(0,MENU_NEW_OBJECT,0,"Добавить объект");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // пункты меню для tvColor
            case MENU_POSITION_FRONT:
                tvPosition.setText("Спереди");
                break;
            case MENU_POSITION_BACK:
                tvPosition.setText("Сзади");
                break;
            case MENU_POSITION_FRONT_LEFT:
                tvPosition.setText("Перед налево");
                break;
            case MENU_POSITION_FRONT_RIGHT:
                tvPosition.setText("Перед направо");
                break;
            case MENU_CLEAN_CANVAS:
                x_dots.clear();
                y_dots.clear();
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
                i++;
                break;
        }
        return super.onContextItemSelected(item);
    }
}
