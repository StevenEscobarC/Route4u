package com.example.route4you;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
private Button btnGasolina=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        btnGasolina=findViewById(R.id.buttonTipoC);
        btnGasolina.setOnClickListener(View -> onClick());
    }
    public void onClick(){
        Intent miIntent = new Intent(MainActivity.this,GasolinaActivity.class);
        startActivity(miIntent);
    }


}