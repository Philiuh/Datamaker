package com.example.cameraapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import static java.lang.Math.sqrt;

public class DisplayImage extends AppCompatActivity implements Callback<Post> {

    private static final String TAG = "retrofit";
    PhotoView mPhotoView;
    Bitmap workingbitmap, mutablebitmap;
    TextView tvMenu;
    Paint paint = new Paint();
    Canvas canvas;
    FrameLayout frameLayout;

    ArrayList<Float> x_dots = new ArrayList<>();
    ArrayList<Float> y_dots = new ArrayList<>();
    ArrayList<String> obj_names = new ArrayList<>();

    private int i = 0, j = 0;
    private boolean new_object_flag;

    final int MENU_CLEAN_CANVAS = 0;
    final int MENU_ADD_TO_SERV = 1;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        frameLayout = findViewById(R.id.fml);
        mPhotoView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        workingbitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        mPhotoView.setImageBitmap(mutablebitmap);
        canvas = new Canvas(mutablebitmap);
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());

        tvMenu = findViewById(R.id.actionMenu);

        registerForContextMenu(tvMenu);

        paint.setColor(Color.rgb(0,255,0));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);
//        TakePhotoTask photoTask = new TakePhotoTask();
//        photoTask.onPostExecute(mutablebitmap  );
    }

//    class TakePhotoTask extends AsyncTask<byte[], String, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(byte[]... data) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data[0], 0, data.length);
//            if (bitmap == null) {
//                //Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
//                return null;
//            }
//
//            //capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 400, 400));
//            try {
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                workingbitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                byte[] array = bos.toByteArray();
//                final String tmp = Base64.encodeToString(array, Base64.NO_WRAP);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 400, 400));
//        }
//    }

    @Override
    public void onResponse(Call<Post> call, Response<Post> response) {

        if(response.isSuccessful()) {
            showResponse(response.body().toString());
            Log.e(TAG, "post submitted to API." + response.body().name);
        }
    }

    @Override
    public void onFailure(Call<Post> call, Throwable t) {
        Log.e(TAG, "Unable to submit post to API.");
    }

    public class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {

            x_dots.add(x * canvas.getWidth());
            y_dots.add(y * canvas.getHeight());
            onDrawForeground(canvas);
            mPhotoView.invalidate();
        }
    }

    public void onDrawForeground(Canvas canvas) {

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
                j = i;
                new_object_flag = false;
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
                    public void onClick(View v) {
                        obj_names.add(TempEditText.getText().toString());
                        Toast toast = Toast.makeText(DisplayImage.this, "Название объекта '" + TempEditText.getText().toString() + "' добавлено", Toast.LENGTH_LONG);
                        frameLayout.removeAllViews();
                        toast.show();
                    }
                });

                Resources res = getResources();
                TempEditText.setLinkTextColor(Color.BLACK);
                TempEditText.setText("Введите название объекта");
                TempEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TempEditText.getText().clear();
                    }
                });

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
                menu.add(0,MENU_ADD_TO_SERV,0,"Отправить изображение");
                break;
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
                paint.setColor(Color.rgb(0, 255, 0));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeWidth(10);
                i = 0;
                j = 0;
                break;
            case MENU_ADD_TO_SERV:
                String name = obj_names.get(0);
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.ENDPOINT)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                APIService apiInterface = retrofit.create(APIService.class);

                try {

                    JSONObject paramObject = new JSONObject();
                    paramObject.put("name", name);
                    Call<Post> Wut = apiInterface.sendPost(paramObject.toString());
                    Wut.enqueue(this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    public void showResponse(String response) {
        Toast toast = Toast.makeText(DisplayImage.this, "Ответ: " + response, Toast.LENGTH_LONG);
        toast.show();
    }

}
