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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Formulario para que el usuario pueda ingresar a la aplicación y acceder a sus funcionalidades,
 * con autenticación por email y contraseña
 *
 * @author Legions
 * @version 1.1
 */
public class LoginActivity extends AppCompatActivity {

    //Variables del formulario
    private EditText txtMail;
    private TextInputLayout txtPassword;
    private Button btnLogin;
    private TextView lblRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();


    }//End onCreate

    /**
     * Inicia las vistas y los eventos de los botónes
     */
    private void initViews(){
        txtMail = findViewById(R.id.txtMail);
        txtPassword = findViewById(R.id.txtPassword);
        lblRegister = findViewById(R.id.lblRegister);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        /**
         * {@link #userLogin()}
         */
        btnLogin.setOnClickListener(view -> {
            userLogin();
        });

        lblRegister.setOnClickListener(view -> openRegisterActivity());
    }

    /**
     * Abre el RegisterActivity
     */
    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }// End openRegisterActivity

    /**
     * Se obtienen los datos de los campos de texto para luego validarlos
     * y si esta correcto realiza la autenticacion de firebase por medio
     * del medio escogido en la configuración de este servicio,
     * en este caso ingresar por email y contraseña
     */
    public void userLogin(){
        String mail = txtMail.getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(mail)){
            txtMail.setError("Ingrese un correo");
            txtMail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
        }else{

            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Bienvenid@", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        Log.w("TAG", "Error:", task.getException());
                    }
                }
            });

        }

    }


}// End LoginActivity