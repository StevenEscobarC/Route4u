package com.example.route4you;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class BuscarRutaActivity extends AppCompatActivity {
    EditText filtrado;

    //Variables ruta
    private List<Ruta> listRuta = new ArrayList<>();
    ArrayAdapter<Ruta> arrayAdapterRuta;
    ListView listViewRuta;
    Ruta selectedRuta;

    //Variables Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_ruta);

        initFirebase();
        initViews();

    }

    private void initViews() {
        listViewRuta = findViewById(R.id.lv_rutas);
        filtrado = findViewById(R.id.txtFiltrarRuta);
        listarDatosRuta();

        filtrado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapterRuta.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatosRuta() {
        databaseReference.child("Ruta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listRuta.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Ruta ruta = objSnapshot.getValue(Ruta.class);
                    listRuta.add(ruta);

                    arrayAdapterRuta = new ArrayAdapter<>(BuscarRutaActivity.this, android.R.layout.simple_list_item_1, listRuta);
                    listViewRuta.setAdapter(arrayAdapterRuta);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
