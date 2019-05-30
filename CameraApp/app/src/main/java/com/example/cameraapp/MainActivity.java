package com.example.cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<Task> {
    private static String TAG = "retro";
    String currentImagePath = null;
    private static final int IMAGE_REQUEST = 1;
    TextView textView;
    String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_news);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIService apiInterface = retrofit.create(APIService.class);
        Call<Task> Wut = apiInterface.recieveGet();
        Wut.enqueue(this);
    }

    public void captureImage(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;

            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,IMAGE_REQUEST);
            }
        }
    }

    public void displayImage(View view) {

        if (currentImagePath != null) {

            Intent intent = new Intent(this, DisplayImage.class);
            intent.putExtra("image_path", currentImagePath);
            startActivity(intent);

        } else {

            Toast toast = Toast.makeText(this, "Сперва сделайте изображение",Toast.LENGTH_LONG);
            toast.show();

        }
    }

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onResponse(Call<Task> call, Response<Task> response) {
        String task = response.body().task;
        textView.setText(task);
    }

    @Override
    public void onFailure(Call<Task> call, Throwable t) {
        Log.e(TAG, "Unable to submit post to API with [" + t.getMessage() + "]");
    }
}
