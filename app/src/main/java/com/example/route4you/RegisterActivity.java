package com.example.route4you;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que contiene el formulario de registro del usuario
 *
 * @author Legions
 * @version 1.1
 */
public class RegisterActivity extends AppCompatActivity {

    //Variables necesarias para capturar la información de los campos
    private EditText txtUser;
    private EditText txtMail;
    private EditText txtPhone;
    private TextInputLayout txtPassword;
    private Button btnRegister;
    private TextView lblLogin;

    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initViews();

    }//End onCreate

    /**
     * Inicia las vistas y dependiendo de cual botón se presione
     * realiza una serie de acciones
     */
    private void initViews() {
        txtUser = findViewById(R.id.txtUser);
        txtMail = findViewById(R.id.txtMail);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        lblLogin = findViewById(R.id.lblLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        /**
         * {@link #createuser()}
         */
        btnRegister.setOnClickListener(view ->  createuser() );

        /**
         * {@link #openLoginActivity()}
         */
        lblLogin.setOnClickListener(view -> openLoginActivity());
    }//end initViews


    /**
     * Abre la actividad de login
     */
    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }// End openLoginActivity

    /**
     * Obtiene los valores de los campos, procede a validarlos y si esta bien
     * se llama al servicio de Firebase Authenticator, se suben los datos del usuario
     * a la base de datos de firebase y muestra una alerta de que ha sido registrado con éxito
     */
    public void createuser(){

        String name = txtUser.getText().toString();
        String mail = txtMail.getText().toString();
        String phone = txtPhone.getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)){
            txtUser.setError("Ingrese un Nombre");
            txtUser.requestFocus();
        }else if (TextUtils.isEmpty(mail)){
            txtMail.setError("Ingrese un Correo");
            txtMail.requestFocus();
        }else if (TextUtils.isEmpty(phone)){
            txtPhone.setError("Ingrese un Teléfono");
            txtPhone.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            txtPassword.setError("Ingrese una Contraseña");
            txtPassword.requestFocus();
        }else {

            /**
             * Se le dice al autenticador que el usuario se va a registrar con el correo y la contraseña
             */
            mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String,Object> user=new HashMap<>();
                        user.put("Nombre", name);
                        user.put("Correo", mail);
                        user.put("Teléfono", phone);
                        user.put("Contraseña", password);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Datos registrados"+userID);
                            }
                        });
                        Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this, "Usuario no registrado "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

}// End RegisterActivity