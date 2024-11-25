package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button registerVehicleButton;
    private Button alquilarMotoButton;
    private Button usuarioButton;
    private Button loginButton; // Botón de Iniciar sesión
    private Button registerButton; // Botón de Registrarse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Inicialización de los botones
        registerVehicleButton = findViewById(R.id.registerVehicleButton);
        alquilarMotoButton = findViewById(R.id.alquilarMotoButton);
        usuarioButton = findViewById(R.id.usuarioButton);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);

            // Y ocultamos los botones relacionados con el alquiler
            registerVehicleButton.setVisibility(View.GONE);
            alquilarMotoButton.setVisibility(View.GONE);
            usuarioButton.setVisibility(View.GONE);
        } else {

            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);

            registerVehicleButton.setVisibility(View.VISIBLE);
            alquilarMotoButton.setVisibility(View.VISIBLE);
            usuarioButton.setVisibility(View.VISIBLE);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        registerVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VehiculosDisponibles.class);
                startActivity(intent);
            }
        });


        alquilarMotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Próximamente", Toast.LENGTH_SHORT).show();
            }
        });


        usuarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
