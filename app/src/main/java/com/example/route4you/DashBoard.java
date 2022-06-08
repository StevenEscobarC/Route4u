package com.example.route4you;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Muestra los botónes para redirigirse a una funcionalidad
 * de la aplicación en específico
 *
 * @author Legions
 * @version 1.1
 */
public class DashBoard extends AppCompatActivity {

    //Botones para mostrar en la vista
    private Button btnSalir;
    private Button btnRuta;
    private Button btnEmpresa;
    private Button btnMapa;
    private Button btnBuscarRuta;

    //Variable para cerrar sesión en firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        initEvents();
    }

    /**
     * Asigna los botones
     */
    private void initEvents() {
//        Button btnTipoVehiculo = findViewById(R.id.buttonTipoV);
//        btnTipoVehiculo.setOnClickListener(view -> onClickTipoVehiculo());
//        Button btnGasolina = findViewById(R.id.buttonTipoC);
//        btnGasolina.setOnClickListener(View -> onClickGasolina());

        //Asignación de los botones y acción a realizar si se oprimen
        btnRuta = findViewById(R.id.buttonRuta);
        btnRuta.setOnClickListener(View -> onClickRuta());

        btnEmpresa = findViewById(R.id.buttonEmpresa);
        btnEmpresa.setOnClickListener(View -> onClickEmpresa());

        btnMapa = findViewById(R.id.buttonMapa);
        btnMapa.setOnClickListener(View -> onClickMapa());

        btnBuscarRuta = findViewById(R.id.buttonBuscarRuta);
        btnBuscarRuta.setOnClickListener(View -> onClickBuscarRuta());

        btnSalir = findViewById(R.id.btnSalir);
        mAuth = FirebaseAuth.getInstance();

        /**
         *Si se oprime el botón se cierra la sesión
         */
        btnSalir.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));

        });

    }
    /**
     * Lanza la activity correspondiente cuando se presiona el botón
     */
    private void onClickTipoVehiculo() {
        Intent miIntent = new Intent(DashBoard.this,TipoVehiculoActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza la activity correspondiente cuando se presiona el botón
     */
    public void onClickGasolina(){
        Intent miIntent = new Intent(DashBoard.this,GasolinaActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza RutaActivity cuando se presiona el botón
     */
    public void onClickRuta(){
        Intent miIntent = new Intent(DashBoard.this,RutaActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza EmpresaActivity correspondiente cuando se presiona el botón
     */
    public void onClickEmpresa(){
        Intent miIntent = new Intent(DashBoard.this,EmpresaActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza MapsActivity cuando se presiona el botón
     */
    public void onClickMapa(){
        Intent miIntent = new Intent(DashBoard.this,MapsActivity.class);
        startActivity(miIntent);
    }

    /**
     * Lanza BuscarRutaActivity cuando se presiona el botón
     */
    public void onClickBuscarRuta(){
        Intent miIntent = new Intent(DashBoard.this,BuscarRutaActivity.class);
        startActivity(miIntent);
    }




}