package com.example.route4you;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView numRuta, inicio, llegada,controles;
    private ImageView foto = null;
    private Button popUp_Ok;
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
        listViewRuta.setOnItemClickListener((parent, view, position, l) -> {
            selectedRuta = (Ruta) parent.getItemAtPosition(position);
            //Inyeccion datos ruta en popUp
            createNewRutaDialog(selectedRuta);

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
    //Dialog de la ruta seleccionada
    public void createNewRutaDialog(Ruta selectedRuta){
        dialogBuilder=new AlertDialog.Builder(this);
        final View rutaPopupView= getLayoutInflater().inflate(R.layout.popup,null);
        numRuta = rutaPopupView.findViewById(R.id.numRuta);
        numRuta.setText(selectedRuta.getNumRuta());
        inicio = rutaPopupView.findViewById(R.id.inicio);
        inicio.setText(selectedRuta.getInicio());
        llegada = rutaPopupView.findViewById(R.id.llegada);
        llegada.setText(selectedRuta.getLlegada());
        controles = rutaPopupView.findViewById(R.id.controles);
        controles.setText(selectedRuta.getControles());
        foto = rutaPopupView.findViewById(R.id.imagenRuta);

        byte[] data = Base64.decode(selectedRuta.getImagen(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        foto.setImageBitmap(bitmap);
        popUp_Ok=(Button) rutaPopupView.findViewById(R.id.btnOk);
        dialogBuilder.setView(rutaPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        popUp_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
