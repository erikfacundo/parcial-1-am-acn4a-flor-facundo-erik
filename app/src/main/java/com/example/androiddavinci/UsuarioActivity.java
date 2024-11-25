package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsuarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListView alquileresListView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        alquileresListView = findViewById(R.id.alquileresListView);
        logoutButton = findViewById(R.id.logoutButton);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            mostrarAlquileres(currentUser.getUid());
        } else {

            Toast.makeText(UsuarioActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(UsuarioActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }


        logoutButton.setOnClickListener(view -> cerrarSesion());
    }


    private void mostrarAlquileres(String userId) {
        CollectionReference alquileresRef = db.collection("alquileres");

        alquileresRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Lista para almacenar los alquileres
                            List<String> alquileres = new ArrayList<>();

                            // Iterar sobre los documentos de alquileres
                            for (DocumentSnapshot document : querySnapshot) {
                                String fechaInicio = document.getString("fechaInicio");
                                String fechaFin = document.getString("fechaFin");
                                String precioFinal = document.getString("precioFinal");

                                // Verificar que los campos necesarios existen en el documento
                                if (fechaInicio != null && fechaFin != null && precioFinal != null) {
                                    String alquiler = "Alquiler desde: " + fechaInicio + " hasta: " + fechaFin + "\nPrecio: " + precioFinal;
                                    alquileres.add(alquiler);
                                } else {
                                    alquileres.add("Datos incompletos para este alquiler");
                                }
                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<>(UsuarioActivity.this, android.R.layout.simple_list_item_1, alquileres);
                            alquileresListView.setAdapter(adapter);
                        } else {
                            Toast.makeText(UsuarioActivity.this, "No tienes alquileres registrados", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UsuarioActivity.this, "Error al obtener los alquileres: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void cerrarSesion() {
        mAuth.signOut(); // Cierra sesión de Firebase


        Intent loginIntent = new Intent(UsuarioActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish(); // Cierra la actividad actual
    }
}
