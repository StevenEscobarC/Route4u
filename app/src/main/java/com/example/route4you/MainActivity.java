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

import com.example.route4you.model.TipoVehiculo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<TipoVehiculo> listTipoVehiculo = new ArrayList<TipoVehiculo>();
    ArrayAdapter<TipoVehiculo> arrayAdapterTipoVehiculo;

    EditText nombreTipoVehiculo, añoTipoVehiculo;
    ListView list_tipo_vehiculo;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TipoVehiculo selectedTipoVehiculo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreTipoVehiculo = findViewById(R.id.txt_nombreTipoVehiculo);
        añoTipoVehiculo = findViewById(R.id.txt_añoTipoVehiculo);
        list_tipo_vehiculo = findViewById(R.id.lv_datosTipoVehiculo);
        
        initFirebase();

        listarDatosTiposvehiculo();

        list_tipo_vehiculo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                selectedTipoVehiculo = (TipoVehiculo) parent.getItemAtPosition(position);
                nombreTipoVehiculo.setText(selectedTipoVehiculo.getName());
                añoTipoVehiculo.setText(selectedTipoVehiculo.getYear());
            }
        });

    }

    private void listarDatosTiposvehiculo() {
        databaseReference.child("TipoVehiculo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTipoVehiculo.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    TipoVehiculo tp = objSnapshot.getValue(TipoVehiculo.class);
                    listTipoVehiculo.add(tp);

                    arrayAdapterTipoVehiculo = new ArrayAdapter<TipoVehiculo>(MainActivity.this, android.R.layout.simple_list_item_1, listTipoVehiculo);
                    list_tipo_vehiculo.setAdapter(arrayAdapterTipoVehiculo);
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
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre = nombreTipoVehiculo.getText().toString();
        String año = añoTipoVehiculo.getText().toString();
        switch (item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("")) {
                    validationNombre();
                }else if(año.equals("")){
                    validationAño();
                }else {
                    TipoVehiculo tp = new TipoVehiculo();
                    tp.setUid(UUID.randomUUID().toString());
                    tp.setName(nombre);
                    tp.setYear(año);
                    databaseReference.child("TipoVehiculo").child(tp.getUid()).setValue(tp);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                TipoVehiculo tp = new TipoVehiculo();
                tp.setUid(selectedTipoVehiculo.getUid());
                databaseReference.child("TipoVehiculo").child(tp.getUid()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_save: {
                TipoVehiculo tp = new TipoVehiculo();
                tp.setUid(selectedTipoVehiculo.getUid());
                tp.setName(nombreTipoVehiculo.getText().toString().trim());
                tp.setYear(añoTipoVehiculo.getText().toString().trim());
                databaseReference.child("TipoVehiculo").child(tp.getUid()).setValue(tp);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    private void limpiarCajas() {
        nombreTipoVehiculo.setText("");
        añoTipoVehiculo.setText("");
    }

    private void validationNombre(){
        nombreTipoVehiculo.setError("Required");
    }

    private void validationAño(){
        añoTipoVehiculo.setError("Required");
    }
}