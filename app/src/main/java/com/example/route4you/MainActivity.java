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
    private Button btnGasolina = null;
    private Button btnTipoVehiculo = null;
    private Button btnRuta = null;
    private Button btnEmpresa = null;
    private Button btnMapa = null;
    
    

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

        btnRuta=findViewById(R.id.buttonRuta);
        btnRuta.setOnClickListener(View -> onClickRuta());

        btnEmpresa = findViewById(R.id.buttonEmpresa);
        btnEmpresa.setOnClickListener(View -> onClickEmpresa());

        btnMapa = findViewById(R.id.buttonMapa);
        btnMapa.setOnClickListener(View -> onClickMapa());

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
    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickRuta(){
        Intent miIntent = new Intent(MainActivity.this,RutaActivity.class);
        startActivity(miIntent);
    }


    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickEmpresa(){
        Intent miIntent = new Intent(MainActivity.this,EmpresaActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickMapa(){
        Intent miIntent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(miIntent);
    }


}