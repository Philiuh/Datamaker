package com.example.cameraapp;

import android.content.Context;
import android.content.Entity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class DisplayImage extends AppCompatActivity {

    private static final String TAG = "retrofit";
    public static String name = "";
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
    private APIService mAPIService;

    final int MENU_CLEAN_CANVAS = 0;
    final int MENU_ADD_TO_SERV = 1;

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

        mAPIService = ApiUtils.getAPIService();

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
                paint.setColor(Color.rgb(0,255,0));
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeWidth(10);
                i = 0;
                j = 0;
                break;
            case MENU_ADD_TO_SERV:
                try {
                    name = obj_names.get(0);
//                    BackgroundWorker backgroundWorker= new BackgroundWorker(this);
//                    backgroundWorker.execute(name);
                    String body = name;
                    sendPost(body);

                } catch (Exception e) {

                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void sendPost(String body) {
        mAPIService.savePost(body).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(String response) {
        Toast toast = Toast.makeText(DisplayImage.this, "Ответ: " + response, Toast.LENGTH_LONG);
    }

    class BackgroundWorker extends AsyncTask<String, String, String> {

        public BackgroundWorker(Context ctx){
            Context context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String myurl = "http://10.0.2.2:3000/route1";
            try{
                Log.wtf("TAG","u post this:" + name);
                URL url = new URL(myurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null)
                    result += line;
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.wtf("TAG","result:" + result);
                return result;
            } catch (MalformedURLException e) {
                Log.wtf("TAG", "Ошибка с подключением");
                e.printStackTrace();
            } catch (IOException e) {
                Log.wtf("TAG", "Ошибка с подключением");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


//    class SendData extends AsyncTask<Void, Void, Void> {
//
//        String resultString = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//
//                String myURL = "http://localhost:5000/route1";
//
//                String parammetrs = name;
//                byte[] data = null;
//                InputStream is = null;
//
//                try {
//
//                    Log.wtf("TAG", "dannie peredani?");
//                    URL url = new URL(myURL);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setReadTimeout(10000);
//                    conn.setConnectTimeout(15000);
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Connection", "Keep-Alive");
//                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                    conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//
//                    // конвертируем передаваемую строку в UTF-8
//                    data = parammetrs.getBytes("UTF-8");
//
//                    OutputStream os = conn.getOutputStream();
//
//                    // передаем данные на сервер
//                    os.write(data);
//                    os.flush();
//                    os.close();
//                    data = null;
//                    conn.connect();
//                    int responseCode= conn.getResponseCode();
//                    Log.wtf("TAG", "dannie peredani?");
//
//
//                    // передаем ответ сервер
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//                    if (responseCode == 200) {    // Если все ОК (ответ 200)
//                        is = conn.getInputStream();
//
//                        byte[] buffer = new byte[8192]; // размер буфера
//
//                        // Далее так читаем ответ
//                        int bytesRead;
//
//                        while ((bytesRead = is.read(buffer)) != -1) {
//                            baos.write(buffer, 0, bytesRead);
//                        }
//
//                        data = baos.toByteArray();
//                        resultString = new String(data, StandardCharsets.UTF_8);  // сохраняем в переменную ответ сервера, у нас "OK"
//
//                    }
//
//                    conn.disconnect();
//
//                } catch (MalformedURLException e) {
//
//                    //resultString = "MalformedURLException:" + e.getMessage();
//                } catch (IOException e) {
//
//                    //resultString = "IOException:" + e.getMessage();
//                } catch (Exception e) {
//
//                    //resultString = "Exception:" + e.getMessage();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            Toast toast = Toast.makeText(getApplicationContext(), "Данные переданы!", Toast.LENGTH_SHORT);
//            toast.show();
//
//        }
//    }
}
