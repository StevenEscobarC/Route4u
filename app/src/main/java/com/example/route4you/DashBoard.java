package com.example.route4you;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {


    private Button btnSalir;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        initEvents();

        btnSalir.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));

        });



    }

    /**
     * Inicializa los botones
     */
    private void initEvents() {
        Button btnTipoVehiculo = findViewById(R.id.buttonTipoV);
        btnTipoVehiculo.setOnClickListener(view -> onClickTipoVehiculo());
        Button btnGasolina = findViewById(R.id.buttonTipoC);
        btnGasolina.setOnClickListener(View -> onClickGasolina());

        Button btnRuta = findViewById(R.id.buttonRuta);
        btnRuta.setOnClickListener(View -> onClickRuta());

        Button btnEmpresa = findViewById(R.id.buttonEmpresa);
        btnEmpresa.setOnClickListener(View -> onClickEmpresa());

        Button btnMapa = findViewById(R.id.buttonMapa);
        btnMapa.setOnClickListener(View -> onClickMapa());

        btnSalir = findViewById(R.id.btnSalir);

        mAuth = FirebaseAuth.getInstance();

    }
    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    private void onClickTipoVehiculo() {
        Intent miIntent = new Intent(DashBoard.this,TipoVehiculoActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickGasolina(){
        Intent miIntent = new Intent(DashBoard.this,GasolinaActivity.class);
        startActivity(miIntent);
    }
    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickRuta(){
        Intent miIntent = new Intent(DashBoard.this,RutaActivity.class);
        startActivity(miIntent);
    }


    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickEmpresa(){
        Intent miIntent = new Intent(DashBoard.this,EmpresaActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza la activity correspondiente cuando se presiona el boton
     */
    public void onClickMapa(){
        Intent miIntent = new Intent(DashBoard.this,MapsActivity.class);
        startActivity(miIntent);
    }




}