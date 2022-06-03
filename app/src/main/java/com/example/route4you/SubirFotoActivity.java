package com.example.route4you;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class SubirFotoActivity extends AppCompatActivity {

    ImageView foto;
    Button seleccionar;
    DatabaseReference imgRef;
    StorageReference storageReference;
    ProgressDialog cargando;

    Bitmap thumb_bitmap = null;
}
