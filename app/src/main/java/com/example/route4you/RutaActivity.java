package com.example.route4you;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.route4you.model.Ruta;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RutaActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> actResLauncherSelectPhoto;

    private List<Ruta> listRuta = new ArrayList<>();
    ArrayAdapter<Ruta> arrayAdapterRuta;

    EditText numRuta, inicio, llegada,controles;
    ListView listViewRuta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Ruta selectedRuta;

    private final static String RUTA = "Ruta";
    private String requerido = "Requerido";
    private Button seleccionarFoto = null;
    private ImageView foto = null;



    private String imagenString = "";

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


        foto = findViewById(R.id.imgPhoto);





        listViewRuta = findViewById(R.id.lv_datosRuta);

        initFirebase();

        listarDatosRuta();

        listViewRuta.setOnItemClickListener((parent, view, position, l) -> {
            selectedRuta = (Ruta) parent.getItemAtPosition(position);
            numRuta.setText(selectedRuta.getNumRuta());
            inicio.setText(selectedRuta.getInicio());
            llegada.setText(selectedRuta.getLlegada());
            controles.setText(selectedRuta.getControles());

            byte[] data = Base64.decode(selectedRuta.getImagen(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            foto.setImageBitmap(bitmap);

        });

        seleccionarFoto = findViewById(R.id.butSelectPhoto);

        actResLauncherSelectPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            switch (result.getResultCode()){
                case RESULT_OK:
                    Log.d("Take Photo", "Select Photo");
                    Uri selectImageUri = result.getData().getData();
                    if(null != selectImageUri){

                        runOnUiThread(() -> {
                            foto.setImageBitmap(null);
                            foto.setImageURI(selectImageUri);
                            guardarImagen();


                        });
                    }
                    break;

            }
        });

        seleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);

            if(intent.resolveActivity(getPackageManager()) != null){
                try {
                    actResLauncherSelectPhoto.launch(intent);
                }catch (Exception e){
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
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
        String imagen = imagenString;
        
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
                }
                /*
                else if(imagen.equals("")){
                    validationImagen();
                }

                 */
                else {
                    Ruta ruta = new Ruta();
                    ruta.setUid(UUID.randomUUID().toString());
                    ruta.setNumRuta(numRuta);
                    ruta.setInicio(inicio);
                    ruta.setLlegada(llegada);
                    ruta.setControles(controles);
                    ruta.setImagen(imagen);

                    databaseReference.child(RUTA).child(ruta.getUid()).setValue(ruta);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                Ruta ruta = new Ruta();
                ruta.setUid(selectedRuta.getUid());
                databaseReference.child(RUTA).child(ruta.getUid()).removeValue();
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
                ruta.setImagen(imagen);
                databaseReference.child(RUTA).child(ruta.getUid()).setValue(ruta);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    private void guardarImagen(){
        foto.buildDrawingCache();
        Bitmap bitmap = foto.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imagen2 = stream.toByteArray();
        imagenString = Base64.encodeToString(imagen2, Base64.DEFAULT);
    }

    /*
     private void validationImagen() {
        foto.setError(requerido);
    }
     */


    private void validationControles() {
        controles.setError(requerido);
    }

    private void validationLlegada() {
        llegada.setError(requerido);
    }

    private void validationInicio() {
        inicio.setError(requerido);
    }

    private void validationNumRuta() {
        numRuta.setError(requerido);
    }

    /**
     * Resetea los campos del formulario
     */
    private void limpiarCajas() {
        numRuta.setText("");
        llegada.setText("");
        foto.setImageBitmap(null);
        inicio.setText("");
        controles.setText("");

    }


}


