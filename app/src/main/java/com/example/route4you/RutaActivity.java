package com.example.route4you;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.route4you.model.Ruta;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class RutaActivity extends AppCompatActivity {
    private List<Ruta> listRuta = new ArrayList<>();
    ArrayAdapter<Ruta> arrayAdapterRuta;

    EditText numRuta, inicio, llegada,controles,imagen;
    ListView listViewRuta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Ruta selectedRuta;

    //Subir imagen firebase
    ImageView foto;
    Button seleccionar;
    DatabaseReference imgRef;
    StorageReference storageReference;
    ProgressDialog cargando;

    Bitmap thumb_bitmap = null;

    private final static String RUTA = "Ruta";
    private String requerido = "Requerido";
    private  byte [] thumb_byte=null;

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

        foto = findViewById(R.id.img_ruta);
        seleccionar = findViewById(R.id.btn_seleccionar_foto);
        storageReference = FirebaseStorage.getInstance().getReference().child("img_comprimidas");
        cargando = new ProgressDialog(this);

        seleccionar.setOnClickListener(view -> {
            CropImage.startPickImageActivity(RutaActivity.this);
        });

        listViewRuta = findViewById(R.id.lv_datosRuta);

        initFirebase();

        listarDatosRuta();

        listViewRuta.setOnItemClickListener((parent, view, position, l) -> {
            selectedRuta = (Ruta) parent.getItemAtPosition(position);
            numRuta.setText(selectedRuta.getNumRuta());
            inicio.setText(selectedRuta.getInicio());
            llegada.setText(selectedRuta.getLlegada());
            controles.setText(selectedRuta.getControles());
            imagen.setText(selectedRuta.getImagen());

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            CropImage.activity(imageUri).start(RutaActivity.this);

        }
    if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if(resultCode== RESULT_OK){
            Uri resultUri=result.getUri();
            File url = new File(resultUri.getPath());
            Picasso.with(this).load(url).into(foto);
            try{
                thumb_bitmap= new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(90).compressToBitmap(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
             thumb_byte = byteArrayOutputStream.toByteArray();


        }
    }

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
                int p= (int)(Math.random()*25+1);int s= (int)(Math.random()*25+1);
                int t= (int)(Math.random()*25+1);int c= (int)(Math.random()*25+1);
                int numero1=(int)(Math.random()*1012+2111);
                int numero2=(int)(Math.random()*1012+2111);
                String[] elementos={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
                final String aleatorio = elementos[p]+elementos[s]+numero1+elementos[t]+elementos[c]+numero2+"comprimido.jpg";
                Ruta ruta = new Ruta();
                ruta.setUid(selectedRuta.getUid());
                ruta.setNumRuta(this.numRuta.getText().toString().trim());
                ruta.setInicio(this.inicio.getText().toString().trim());
                ruta.setLlegada(this.llegada.getText().toString().trim());
                ruta.setControles(this.controles.getText().toString().trim());



                StorageReference ref = storageReference.child(aleatorio);
                UploadTask uploadTask= ref.putBytes(thumb_byte);
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloaduri=task.getResult();
                        ruta.setImagen(downloaduri.toString());
                    }
                });
                databaseReference.child(RUTA).child(ruta.getUid()).setValue(ruta);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();

                limpiarCajas();
                break;
            }
            default: break;
        }
        return true;
    }

    private void validationImagen() {
        imagen.setError(requerido);
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
        imagen.setText("");
        inicio.setText("");
        controles.setText("");

    }


}


