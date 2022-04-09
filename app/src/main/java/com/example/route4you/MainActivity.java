package com.example.route4you;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Clase main, pagina principal de la app
 *
 * @author Legions
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {
    private Button btnGasolina=null;
    private Button btnTipoVehiculo = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initEvents();

    }

    /**
     * Inicializa los botones
     */
    private void initEvents() {
        btnTipoVehiculo = findViewById(R.id.buttonTipoV);
        btnTipoVehiculo.setOnClickListener(view -> onClickTipoVehiculo());
        btnGasolina=findViewById(R.id.buttonTipoC);
        btnGasolina.setOnClickListener(View -> onClickGasolina());
    }
    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    private void onClickTipoVehiculo() {
        Intent miIntent = new Intent(MainActivity.this,TipoVehiculoActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickGasolina(){
        Intent miIntent = new Intent(MainActivity.this,GasolinaActivity.class);
        startActivity(miIntent);
    }


}