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

    private FirebaseAuth mAuth;  // Instancia de FirebaseAuth
    private Button registerVehicleButton;
    private Button alquilarMotoButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Si no está autenticado, redirigir a la pantalla de login
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Finaliza esta actividad para evitar volver sin haber iniciado sesión
        }

        // Botón para registrar vehículo
        registerVehicleButton = findViewById(R.id.registerVehicleButton);
        registerVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar la actividad VehiculosDisponibles
                Intent intent = new Intent(MainActivity.this, VehiculosDisponibles.class);
                startActivity(intent);
            }
        });

        // Botón para alquilar moto
        alquilarMotoButton = findViewById(R.id.alquilarMotoButton);
        alquilarMotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para cerrar sesión
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                // Redirigir al login
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    // Manejo del botón de atrás (opcional)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void logout(View view) {
        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut();

        // Mostrar un mensaje de confirmación
        Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Redirigir al LoginActivity
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        // Finalizar esta actividad para evitar regresar a la pantalla principal
        finish();
    }

}
