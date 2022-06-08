package com.example.route4you;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.route4you.adapter.TitleListingArrayAdapter;
import com.example.route4you.model.Ruta;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtra la ruta seleccionada de acuerdo a una
 * cadena, además muestra su informacion detallada.
 *
 * @author Legions
 * @version 1.1
 */
public class BuscarRutaActivity extends AppCompatActivity {

    //Variables de componentes visuales
    private EditText filtrado;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView numRuta, inicio, llegada,controles;
    private ImageView foto = null;
    private Button popUp_Ok;

    //Variables ruta
    private List<Ruta> listRuta = new ArrayList<>();
    private TitleListingArrayAdapter arrayAdapterRuta;
    private ListView listViewRuta;
    private Ruta selectedRuta;

    //Variables Firebase (base de datos)
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_ruta);

        initFirebase();
        initViews();

    }

    /**
     * Se crea el textWatcher para que cuando cambie el texto en el campo de búsqueda
     * este pendiente a verificar si contiene la cadena especificada por el usuario
     * en alguna de la información mostrada en el listado de rutas
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals("")) {
                List<Ruta> filteredRutas = new ArrayList<Ruta>();
                for (int i=0; i<listRuta.size(); i++) {
                    if (listRuta.get(i).toString().toLowerCase().contains(s)) {
                        filteredRutas.add(listRuta.get(i));
                    }
                }
                arrayAdapterRuta = new TitleListingArrayAdapter(BuscarRutaActivity.this, R.id.lv_rutas, filteredRutas);
                listViewRuta.setAdapter(arrayAdapterRuta);
            }
            else {
                arrayAdapterRuta = new TitleListingArrayAdapter(BuscarRutaActivity.this, R.id.lv_rutas, listRuta);
                listViewRuta.setAdapter(arrayAdapterRuta);
            }
        }
    };

    /**
     * Inicia las vistas y los eventos de los botónes
     */
    private void initViews() {
        listViewRuta = findViewById(R.id.lv_rutas);
        filtrado = findViewById(R.id.txtFiltrarRuta);
        filtrado.addTextChangedListener(filterTextWatcher);
        listarDatosRuta();

        /**
         *Este evento esta pendiente a cualquier cambio que haya en el campo de texto
         * para asi filtrar la ruta correspondiente
         */
        /*
        filtrado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapterRuta.filtrar(filtrado.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        /**
         * En el listener del listado de las rutas.
         *
         * {@link #createNewRutaDialog(Ruta)} createNewRutaDialog} envía el objeto de tipo ruta hacia el metodo de la ventana emergente
         */
        listViewRuta.setOnItemClickListener((parent, view, position, l) -> {
            selectedRuta = (Ruta) parent.getItemAtPosition(position);


            //Inyeccion datos ruta en popUp
            createNewRutaDialog(selectedRuta);

        });

    }

    /**
     * Método que inicializa la base de datos
     */
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /**
     * Método que lista las rutas, mapeando los objetos desde la base de datos
     */
    private void listarDatosRuta() {
        databaseReference.child("Ruta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listRuta.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Ruta ruta = objSnapshot.getValue(Ruta.class);
                    listRuta.add(ruta);

                    arrayAdapterRuta = new TitleListingArrayAdapter(BuscarRutaActivity.this, R.id.lv_rutas, listRuta);
                    listViewRuta.setAdapter(arrayAdapterRuta);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Vista emergente de la ruta seleccionada
     * con todos los detalles correspondientes
     * a la clase ruta.
     *
     * @param selectedRuta ruta seleccionada desde el ListView del layout
     */
    public void createNewRutaDialog(Ruta selectedRuta){

        //Inicialización de la vista emergente
        dialogBuilder=new AlertDialog.Builder(this);
        final View rutaPopupView= getLayoutInflater().inflate(R.layout.popup,null);

        //Asignación de los campos de la ruta seleccionada
        numRuta = rutaPopupView.findViewById(R.id.numRuta);
        numRuta.setText(selectedRuta.getNumRuta());
        inicio = rutaPopupView.findViewById(R.id.inicio);
        inicio.setText(selectedRuta.getInicio());
        llegada = rutaPopupView.findViewById(R.id.llegada);
        llegada.setText(selectedRuta.getLlegada());
        controles = rutaPopupView.findViewById(R.id.controles);
        controles.setText(selectedRuta.getControles());
        foto = rutaPopupView.findViewById(R.id.imagenRuta);

        //Decocificación de la imagen de Base64 hacia Bitmap
        byte[] data = Base64.decode(selectedRuta.getImagen(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        //Asignación foto ruta con el bitmap obtenido
        foto.setImageBitmap(bitmap);

        //Creación de los componentes de la vista emergente
        popUp_Ok=(Button) rutaPopupView.findViewById(R.id.btnOk);
        dialogBuilder.setView(rutaPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        /**
         * Cierra la ventana emergente
         */
        popUp_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
