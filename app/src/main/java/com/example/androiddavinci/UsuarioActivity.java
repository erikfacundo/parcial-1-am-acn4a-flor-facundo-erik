package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        alquileresListView = findViewById(R.id.alquileresListView);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            mostrarAlquileres(currentUser.getUid());
        } else {

            Toast.makeText(UsuarioActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();

            Intent loginIntent = new Intent(UsuarioActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
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


                            for (DocumentSnapshot document : querySnapshot) {

                                String descripcion = document.getString("descripcion");
                                if (descripcion != null) {
                                    alquileres.add(descripcion);
                                } else {
                                    alquileres.add("Descripción no disponible");
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(UsuarioActivity.this, android.R.layout.simple_list_item_1, alquileres);
                            alquileresListView.setAdapter(adapter);
                        } else {
                            Toast.makeText(UsuarioActivity.this, "No tienes alquileres registrados", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UsuarioActivity.this, "Error al obtener los alquileres", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
