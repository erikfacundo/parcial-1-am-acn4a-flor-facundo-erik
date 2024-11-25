package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progressBar);
        Button registerButton = findViewById(R.id.registerButton);
        Button backButton = findViewById(R.id.backButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("RegisterActivity", "Registro exitoso: " + user.getEmail());

                        String userId = user.getUid();

                        saveUserIdToFirestore(userId);

                        saveAlquilerToFirestore(userId, "Alquiler inicial");

                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Log.e("RegisterActivity", "Error al registrar usuario", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserIdToFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId); // Guardar el UID en Firestore

        db.collection("usuarios").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User ID guardado con éxito"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar User ID", e));
    }

    private void saveAlquilerToFirestore(String userId, String descripcion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> alquilerMap = new HashMap<>();
        alquilerMap.put("descripcion", descripcion);
        alquilerMap.put("userId", userId);  // Asociar el alquiler con el usuario

        db.collection("alquileres").add(alquilerMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Alquiler guardado con éxito");
                    Toast.makeText(RegisterActivity.this, "Alquiler guardado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al guardar alquiler", e);
                    Toast.makeText(RegisterActivity.this, "Error al guardar alquiler", Toast.LENGTH_SHORT).show();
                });
    }
}
