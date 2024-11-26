package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

    private TextView userEmailTextView, userNameTextView, userLastNameTextView, userDniTextView;  // Agregar nuevos TextViews
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListView alquileresListView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Inicializa las vistas
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userNameTextView = findViewById(R.id.userNameTextView);  // Nueva vista para el nombre
        userLastNameTextView = findViewById(R.id.userLastNameTextView);  // Nueva vista para el apellido
        userDniTextView = findViewById(R.id.userDniTextView);  // Nueva vista para el DNI
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        alquileresListView = findViewById(R.id.alquileresListView);
        logoutButton = findViewById(R.id.logoutButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Mostrar el correo del usuario en el TextView
            userEmailTextView.setText(currentUser.getEmail());
            mostrarInformacionUsuario(currentUser.getUid());  // Cambiar el método para mostrar más información
            mostrarAlquileres(currentUser.getUid());
        } else {
            Toast.makeText(UsuarioActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(UsuarioActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        logoutButton.setOnClickListener(view -> cerrarSesion());
    }

    // Método para obtener y mostrar la información adicional del usuario (name, lastName, dni)
    private void mostrarInformacionUsuario(String userId) {
        db.collection("usuarios")  // Asegúrate de que la colección se llama "usuarios"
                .document(userId)  // Usamos el userId para obtener el documento correspondiente
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Recuperar los datos del documento
                            String name = document.getString("name");
                            String lastName = document.getString("lastName");
                            String email = document.getString("email");
                            String dni = document.getString("dni");

                            // Mostrar la información en los TextViews
                            userNameTextView.setText(name);
                            userLastNameTextView.setText(lastName);
                            userDniTextView.setText(dni);
                        } else {
                            Toast.makeText(UsuarioActivity.this, "No se encontraron datos para este usuario", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UsuarioActivity.this, "Error al obtener los datos del usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                                // Obtener los datos de cada alquiler
                                String alquilerId = document.getId(); // ID del alquiler
                                String fechaInicio = document.getString("fechaInicio");
                                String fechaFin = document.getString("fechaFin");
                                String precioFinal = document.getString("precioFinal");
                                String alquilerUserId = document.getString("userId"); // userId del alquiler

                                if (fechaInicio != null && fechaFin != null && precioFinal != null) {
                                    // Crear el texto para mostrar el alquiler
                                    String alquiler = "Alquiler ID: " + alquilerId + "\n" +
                                            "User ID: " + alquilerUserId + "\n" +
                                            "Desde: " + fechaInicio + " hasta: " + fechaFin + "\n" +
                                            "Precio: " + precioFinal;
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
        mAuth.signOut();

        Intent loginIntent = new Intent(UsuarioActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
