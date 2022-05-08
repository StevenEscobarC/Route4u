package com.example.route4you;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.example.route4you.model.Ruta;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RutaActivity extends AppCompatActivity {
    private List<Ruta> listRuta = new ArrayList<>();
    ArrayAdapter<Ruta> arrayAdapterRuta;

    EditText numRuta, inicio, llegada,controles,imagen;
    ListView listViewRuta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Ruta selectedRuta;


    /**
     * Se crea el formulario
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        numRuta = findViewById(R.id.txt_numRuta);
        inicio = findViewById(R.id.txt_inicio);
        llegada = findViewById(R.id.txt_llegada);
        controles = findViewById(R.id.txt_controles);
        imagen = findViewById(R.id.txt_imagen);

        listViewRuta = findViewById(R.id.lv_datosRuta);

        initFirebase();

        listarDatosRuta();

        listViewRuta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                selectedRuta = (Ruta) parent.getItemAtPosition(position);
                numRuta.setText(selectedRuta.getNumRuta());
                inicio.setText(selectedRuta.getInicio());
                llegada.setText(selectedRuta.getLlegada());
                controles.setText(selectedRuta.getControles());
                imagen.setText(selectedRuta.getImagen());

            }
        });

    }

    private void listarDatosRuta() {
        databaseReference.child("Ruta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listRuta.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Ruta ruta = objSnapshot.getValue(Ruta.class);
                    listRuta.add(ruta);

                    arrayAdapterRuta = new ArrayAdapter<>(RutaActivity.this, android.R.layout.simple_list_item_1, listRuta);
                    listViewRuta.setAdapter(arrayAdapterRuta);
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
        //firebaseDatabase.serutaersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
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

    /**
     * Realiza la accion dependiendo del boton escogido en el menu superior
     * @param item boton escogido
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String numRuta = this.numRuta.getText().toString();
        String inicio= this.inicio.getText().toString();
        String llegada = this.llegada.getText().toString();
        String controles = this.controles.getText().toString();
        String imagen = this.imagen.getText().toString();
        
        switch (item.getItemId()){
            case R.id.icon_add: {
                if (numRuta.equals("")) {
                    validationNumRuta();
                }else if(inicio.equals("")){
                    validationInicio();
                }else if(llegada.equals("")){
                    validationLlegada();
                }else if(controles.equals("")){
                    validationControles();
                }else if(imagen.equals("")){
                    validationImagen();
                }else {
                    Ruta ruta = new Ruta();
                    ruta.setUid(UUID.randomUUID().toString());
                    ruta.setNumRuta(numRuta);
                    ruta.setInicio(inicio);
                    ruta.setLlegada(llegada);
                    ruta.setControles(controles);
                    ruta.setImagen(imagen);

                    databaseReference.child("Ruta").child(ruta.getUid()).setValue(ruta);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                Ruta ruta = new Ruta();
                ruta.setUid(selectedRuta.getUid());
                databaseReference.child("Ruta").child(ruta.getUid()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_save: {
                Ruta ruta = new Ruta();
                ruta.setUid(selectedRuta.getUid());
                ruta.setNumRuta(this.numRuta.getText().toString().trim());
                ruta.setInicio(this.inicio.getText().toString().trim());
                ruta.setLlegada(this.llegada.getText().toString().trim());
                ruta.setControles(this.controles.getText().toString().trim());
                ruta.setImagen(this.imagen.getText().toString().trim());
                databaseReference.child("Ruta").child(ruta.getUid()).setValue(ruta);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    private void validationImagen() {
        imagen.setError("Required");
    }

    private void validationControles() {
        controles.setError("Required");
    }

    private void validationLlegada() {
        llegada.setError("Required");
    }

    private void validationInicio() {
        inicio.setError("Required");
    }

    private void validationNumRuta() {
        numRuta.setError("Required");
    }

    /**
     * Resetea los campos del formulario
     */
    private void limpiarCajas() {
        numRuta.setText("");
        llegada.setText("");
        imagen.setText("");
        inicio.setText("");
        controles.setText("");

    }


}


