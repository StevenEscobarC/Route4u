package com.example.route4you;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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

/**
 * Clase que contiene el formulario de ruta, y las acciones que se pueden realizar
 * con estos datos
 *
 * @author Legions
 * @version 1.1
 */
public class RutaActivity extends AppCompatActivity {

    //Variables para obtener los valores del formulario
    private ActivityResultLauncher<Intent> actResLauncherSelectPhoto;

    private List<Ruta> listRuta = new ArrayList<>();
    private ArrayAdapter<Ruta> arrayAdapterRuta;
    private ListView listViewRuta;
    private Ruta selectedRuta;

    private EditText numRuta, inicio, llegada, controles;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



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

        initFirebase();
        initViews();



    }

    /**
     * Inicia las vistas y los botónes, asigna los valores obtenidos de los campos
     * Ir a {@link #listarDatosRuta()} para más información sobre el listado
     */
    private void initViews() {
        numRuta = findViewById(R.id.txt_numRuta);
        inicio = findViewById(R.id.txt_inicio);
        llegada = findViewById(R.id.txt_llegada);
        controles = findViewById(R.id.txt_controles);
        foto = findViewById(R.id.imgPhoto);
        listViewRuta = findViewById(R.id.lv_datosRuta);
        listarDatosRuta();

        /**
         * De la lista se obtiene el objeto de acuerdo a la posición
         * seleccionada, se asignan los valores a los campos de texto, para
         * realizar cualquier acción CRUD
         */
        listViewRuta.setOnItemClickListener((parent, view, position, l) -> {
            selectedRuta = (Ruta) parent.getItemAtPosition(position);
            //Inyección datos ruta en popUp
            numRuta.setText(selectedRuta.getNumRuta());
            inicio.setText(selectedRuta.getInicio());
            llegada.setText(selectedRuta.getLlegada());
            controles.setText(selectedRuta.getControles());

            //Se decodifica la imagen que se obtiene de la base de datos en formato Base64
            byte[] data = Base64.decode(selectedRuta.getImagen(), Base64.DEFAULT);

            //se convierte el array de bytes a bitmap para asignarlo al ImageView de la imagen
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            foto.setImageBitmap(bitmap);

        });

        seleccionarFoto = findViewById(R.id.butSelectPhoto);

        /**
         * Si hay una imagen seleccionada se procede a asignarla a el imageView, y a
         * guardarla
         * Ir a {@link #guardarImagen()} para más información
         */
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

        /**
         * Al presionar el botón de seleccionar foto, si se tienen los permisos para
         * acceder al storage del celular se procede a llamar a
         * {@link #actResLauncherSelectPhoto}
         */
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

    /**
     * Lista las rutas obtenidas de la base de datos
     */
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

    /**
     * inicializa las variables de firebase
     */
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /**
     * Se crea el menuúde la parte superior de la app
     * @param menu
     * @return la creación del menú
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Realiza la acción dependiendo del botón escogido en el menu superior
     * @param item botón escogido
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
                else if(imagen.equals("")){
                    validationImagen();
                }

                else {
                    Ruta ruta = new Ruta();
                    ruta.setUid(UUID.randomUUID().toString());
                    ruta.setNumRuta(numRuta);
                    ruta.setInicio(inicio);
                    ruta.setLlegada(llegada);
                    ruta.setControles(controles);
                    ruta.setImagen(imagen);

                    //Agrega la ruta a la base de datos mandandole la informacion del nombre de la tabla, el id de la ruta y el valor que va a tomar
                    databaseReference.child(RUTA).child(ruta.getUid()).setValue(ruta);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                Ruta ruta = new Ruta();
                ruta.setUid(selectedRuta.getUid());

                //Elimina la ruta mediante el id obtenido del item seleccionado, y remueve el valor de esta
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

                //Actualiza los campos de la ruta mediante el id del item seleccionado
                databaseReference.child(RUTA).child(ruta.getUid()).setValue(ruta);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    /**
     * Construye el diálogo de alerta que se muestra al usuario dependiendo de la validación
     * Ir a {@link #validationImagen()} para más información
     */
    private void alertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor inserte una imagen")
                .setTitle("ALERTA");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * La imagen obtenida se codifica en Base64 para así almacenarla a la base de datos
     */
    private void guardarImagen(){
        foto.buildDrawingCache();
        Bitmap bitmap = foto.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imagen2 = stream.toByteArray();
        imagenString = Base64.encodeToString(imagen2, Base64.DEFAULT);
    }

    /**
     * Muestra el diálogo si no se cumplen las validaciones
     */
     private void validationImagen() {
        foto.setImageBitmap(null);
        alertDialog();
    }



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


