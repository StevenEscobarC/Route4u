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

import com.example.route4you.model.Empresa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmpresaActivity extends AppCompatActivity {
    private List<Empresa> listEmpresa = new ArrayList<>();
    ArrayAdapter<Empresa> arrayAdapterEmpresa;

    EditText nombreEmpresa, direccionEmpresa, numeroControlesEmpresa, emailEmpresa, telefonoEmpresa, contraseñaEmpresa;
    ListView listViewEmpresa;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Empresa selectedEmpresa;

    private String mensaje = "Requerido";
    private final static String EMPRESA = "Empresa";


    /**
     * Se crea el formulario
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        nombreEmpresa = findViewById(R.id.txt_nombreEmpresa);
        direccionEmpresa = findViewById(R.id.txt_direccionEmpresa);
        emailEmpresa = findViewById(R.id.txt_emailEmpresa);
        telefonoEmpresa = findViewById(R.id.txt_telefonoEmpresa);
        contraseñaEmpresa = findViewById(R.id.txt_contraseñaEmpresa);
        numeroControlesEmpresa = findViewById(R.id.txt_numeroControlesEmpresa);
        listViewEmpresa = findViewById(R.id.lv_datosEmpresas);

        initFirebase();

        listarDatosEmpresa();

        listViewEmpresa.setOnItemClickListener((parent, view, position, l) -> {
            selectedEmpresa = (Empresa) parent.getItemAtPosition(position);
            nombreEmpresa.setText(selectedEmpresa.getNombre());
            direccionEmpresa.setText(selectedEmpresa.getDireccion());
            emailEmpresa.setText(selectedEmpresa.getEmail());
            telefonoEmpresa.setText(selectedEmpresa.getTelefono());
            contraseñaEmpresa.setText(selectedEmpresa.getContraseña());
            numeroControlesEmpresa.setText(selectedEmpresa.getNumeroControles());
        });

    }

    private void listarDatosEmpresa() {
        databaseReference.child(EMPRESA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listEmpresa.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Empresa em = objSnapshot.getValue(Empresa.class);
                    listEmpresa.add(em);

                    arrayAdapterEmpresa = new ArrayAdapter<>(EmpresaActivity.this, android.R.layout.simple_list_item_1, listEmpresa);
                    listViewEmpresa.setAdapter(arrayAdapterEmpresa);
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
        String nombre = nombreEmpresa.getText().toString();
        String direccion = direccionEmpresa.getText().toString();
        String telefono = telefonoEmpresa.getText().toString();
        String email = emailEmpresa.getText().toString();
        String contraseña = contraseñaEmpresa.getText().toString();
        String numeroControl = numeroControlesEmpresa.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("")) {
                    validationNombre();
                }else if(email.equals("")){
                    validationEmail();
                }else if(contraseña.equals("")){
                    validationContraseña();
                }else if(direccion.equals("")){
                    validationDireccion();
                }else if(numeroControl.equals("")){
                    validationNumeroControl();
                }else if(telefono.equals("")){
                    validationTelefono();
                }else {
                    Empresa em = new Empresa();
                    em.setUid(UUID.randomUUID().toString());
                    em.setNombre(nombre);
                    em.setDireccion(direccion);
                    em.setEmail(email);
                    em.setNumeroControles(numeroControl);
                    em.setContraseña(contraseña);
                    em.setTelefono(telefono);
                    databaseReference.child(EMPRESA).child(em.getUid()).setValue(em);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_delete: {
                Empresa em = new Empresa();
                em.setUid(selectedEmpresa.getUid());
                databaseReference.child(EMPRESA).child(em.getUid()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_save: {
                Empresa em = new Empresa();
                em.setUid(selectedEmpresa.getUid());
                em.setNombre(nombreEmpresa.getText().toString().trim());
                em.setDireccion(direccionEmpresa.getText().toString().trim());
                em.setEmail(emailEmpresa.getText().toString().trim());
                em.setNumeroControles(numeroControlesEmpresa.getText().toString().trim());
                em.setContraseña(contraseñaEmpresa.getText().toString().trim());
                em.setTelefono(telefonoEmpresa.getText().toString().trim());
                databaseReference.child(EMPRESA).child(em.getUid()).setValue(em);
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
        nombreEmpresa.setText("");
        direccionEmpresa.setText("");
        telefonoEmpresa.setText("");
        numeroControlesEmpresa.setText("");
        direccionEmpresa.setText("");
        contraseñaEmpresa.setText("");
        emailEmpresa.setText("");
    }

    private void validationNombre(){
        nombreEmpresa.setError(mensaje);
    }

    private void validationTelefono() {
        telefonoEmpresa.setError(mensaje);
    }

    private void validationNumeroControl() {
        numeroControlesEmpresa.setError(mensaje);
    }

    private void validationDireccion() {
        direccionEmpresa.setError(mensaje);
    }

    private void validationContraseña() {
        contraseñaEmpresa.setError(mensaje);
    }

    private void validationEmail() {
        emailEmpresa.setError(mensaje);
    }
}
