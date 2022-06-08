package com.example.route4you;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.route4you.model.TipoGasolina;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *Clase dónde se define la estructura del formulario de tipo de gasolina
 *
 *@author Legions
 *@version 1.1
 */
public  class GasolinaActivity extends AppCompatActivity {
    private List<TipoGasolina> listTipoGasolina = new ArrayList<>();
    ArrayAdapter<TipoGasolina> arrayAdapterTipoGasolina;

    EditText nombreTipoGasolina;
    ListView listViewTipoGasolina;

    private final static String TIPO_GASOLINA="TipoGasolina";


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TipoGasolina selectedTipoGasolina;

    /**
     * Se crea el formulario
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina);

        nombreTipoGasolina = findViewById(R.id.txt_nombreTipoGasolina);
        listViewTipoGasolina = findViewById(R.id.lv_datosTipoGasolina);

        initFirebase();

        listarDatosTiposGasolina();

        listViewTipoGasolina.setOnItemClickListener((parent, view, position, l) -> {
            selectedTipoGasolina = (TipoGasolina) parent.getItemAtPosition(position);
            nombreTipoGasolina.setText(selectedTipoGasolina.getName());
        });
    }

    /**
     * Se crea el menu de la parte superior de la app
     * @param menu
     * @return la creacion del menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void listarDatosTiposGasolina() {
        databaseReference.child(TIPO_GASOLINA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTipoGasolina.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    TipoGasolina tg = objSnapshot.getValue(TipoGasolina.class);
                    listTipoGasolina.add(tg);

                    arrayAdapterTipoGasolina = new ArrayAdapter<>(GasolinaActivity.this, android.R.layout.simple_list_item_1, listTipoGasolina);
                    listViewTipoGasolina.setAdapter(arrayAdapterTipoGasolina);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    /**
     * Realiza la accion dependiendo del boton escogido en el menu superior
     * @param item boton escogido
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre = nombreTipoGasolina.getText().toString();
        switch (item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("")) {
                    validationNombre();
                }else {
                    TipoGasolina tg = new TipoGasolina();
                    tg.setUid(UUID.randomUUID().toString());
                    tg.setName(nombre);
                    databaseReference.child(TIPO_GASOLINA).child(tg.getUid()).setValue(tg);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                TipoGasolina tg = new TipoGasolina();
                tg.setUid(selectedTipoGasolina.getUid());
                databaseReference.child(TIPO_GASOLINA).child(tg.getUid()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_save: {
                TipoGasolina tg = new TipoGasolina();
                tg.setUid(selectedTipoGasolina.getUid());
                tg.setName(nombreTipoGasolina.getText().toString().trim());
                databaseReference.child(TIPO_GASOLINA).child(tg.getUid()).setValue(tg);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    /**
     * Resetea los campos del formulario
     */
    private void limpiarCajas() {
        nombreTipoGasolina.setText("");
    }

    private void validationNombre(){
        nombreTipoGasolina.setError("Required");
    }
}
