package com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.chrisbanes.photoview.PhotoView;

public class DisplayImage extends AppCompatActivity {

    PhotoView imageView;
    Bitmap workingbitmap, mutablebitmap;
    TextView tvMark, tvModel, tvPosition;

    final int MENU_POSITION_FRONT = 1;
    final int MENU_POSITION_BACK = 2;
    final int MENU_POSITION_FRONT_LEFT = 3;
    final int MENU_POSITION_FRONT_RIGHT = 4;

    final int MENU_MARK_LADA = 5;
    final int MENU_MARK_VAZ = 6;
    final int MENU_MARK_MARUSSIA = 7;
    final int MENU_MARK_VOLGA = 8;
    final int MENU_MARK_ZIL = 9;
    final int MENU_MARK_MOSKVICH = 10;
    final int MENU_MARK_GAZ = 11;
    final int MENU_MARK_UAZ = 12;

    final int MENU_MARK_LADA_KALINA = 13;
    final int MENU_MARK_LADA_SAMARA = 14;
    final int MENU_MARK_LADA_PRIORA = 15;
    final int MENU_MARK_LADA_VESTA = 16;
    final int MENU_MARK_LADA_XRAY = 17;

    final int TEST = 18;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.mimageView);
        workingbitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        mutablebitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mutablebitmap);

        tvMark = findViewById(R.id.markView);
        tvModel = findViewById(R.id.modelView);
        tvPosition = findViewById(R.id.positionView);

        registerForContextMenu(tvMark);
        registerForContextMenu(tvModel);
        registerForContextMenu(tvPosition);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case (R.id.markView):
                menu.add(0, MENU_MARK_LADA, 0, "Лада");
                menu.add(0, MENU_MARK_VAZ, 0, "ВАЗ");
                menu.add(0, MENU_MARK_MARUSSIA, 0, "Marussia");
                menu.add(0, MENU_MARK_VOLGA, 0, "Волга");
                menu.add(0, MENU_MARK_ZIL, 0, "ЗИЛ");
                menu.add(0, MENU_MARK_MOSKVICH, 0, "Москвич");
                menu.add(0, MENU_MARK_GAZ, 0, "ГАЗ");
                menu.add(0, MENU_MARK_UAZ, 0, "УАЗ");
                break;
            case (R.id.modelView):
                if (tvMark.getText().toString() == "Лада")
                    menu.add(0,MENU_MARK_LADA_KALINA,0,"Калина");
                if (tvMark.getText().toString() == "Лада")
                    menu.add(0,MENU_MARK_LADA_SAMARA,0,"Самара");
                if (tvMark.getText().toString() == "Лада")
                    menu.add(0,MENU_MARK_LADA_PRIORA,0,"Приора");
                if (tvMark.getText().toString() == "Лада")
                    menu.add(0,MENU_MARK_LADA_VESTA,0,"Веста");
                if (tvMark.getText().toString() == "Лада")
                    menu.add(0,MENU_MARK_LADA_XRAY,0,"XRay");
                else
                    menu.add(0,TEST,0,"Сперва выберите марку");

                break;
            case (R.id.positionView):
                menu.add(0,MENU_POSITION_FRONT,0,"Спереди");
                menu.add(0,MENU_POSITION_BACK,0,"Сзади");
                menu.add(0,MENU_POSITION_FRONT_LEFT,0,"Перед налево");
                menu.add(0,MENU_POSITION_FRONT_RIGHT,0,"Перед направо");
                break;
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
            case MENU_MARK_LADA:
                tvMark.setText("Лада");
                break;
            case MENU_MARK_VAZ:
                tvMark.setText("ВАЗ");
                break;
            case MENU_MARK_MARUSSIA:
                tvMark.setText("Marussia");
                break;
            case MENU_MARK_VOLGA:
                tvMark.setText("Волга");
                break;
            case MENU_MARK_ZIL:
                tvMark.setText("ЗИЛ");
                break;
            case MENU_MARK_MOSKVICH:
                tvMark.setText("Моквич");
                break;
            case MENU_MARK_GAZ:
                tvMark.setText("ГАЗ");
                break;
            case MENU_MARK_UAZ:
                tvMark.setText("УАЗ");
                break;
            case MENU_MARK_LADA_KALINA:
                tvModel.setText("Калина");
                break;
            case MENU_MARK_LADA_PRIORA:
                tvModel.setText("Приора");
                break;
            case MENU_MARK_LADA_VESTA:
                tvModel.setText("Веста");
                break;
            case MENU_MARK_LADA_SAMARA:
                tvModel.setText("Самара");
                break;
            case MENU_MARK_LADA_XRAY:
                tvModel.setText("XRay");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
