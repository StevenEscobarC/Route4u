package com.example.route4you;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity {
    private Button btnGasolina=null;
    private Button btnTipoVehiculo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnTipoVehiculo = findViewById(R.id.buttonTipoV);
        btnTipoVehiculo.setOnClickListener(view -> onClickTipoVehiculo());
        btnGasolina=findViewById(R.id.buttonTipoC);
        btnGasolina.setOnClickListener(View -> onClickGasolina());
        
    }

    private void onClickTipoVehiculo() {
        Intent miIntent = new Intent(MainActivity.this,TipoVehiculoActivity.class);
        startActivity(miIntent);

    }
    public void onClickGasolina(){
        Intent miIntent = new Intent(MainActivity.this,GasolinaActivity.class);
        startActivity(miIntent);
    }


}