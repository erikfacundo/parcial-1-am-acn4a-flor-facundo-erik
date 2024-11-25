package com.example.androiddavinci;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlquilerToken {

    private FirebaseFirestore db;

    public AlquilerToken() {
        db = FirebaseFirestore.getInstance();
    }

    public void crearAlquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados, FirebaseUser user) {
        if (user == null) {
            System.out.println("Usuario no autenticado.");
            return;
        }


        Alquiler alquiler = new Alquiler(fechaInicio, fechaFin, precioFinal, diasSeleccionados, user.getUid());


        db.collection("alquileres")
                .add(alquiler)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Alquiler creado con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error al crear el alquiler: " + e.getMessage());
                });
    }

    public void obtenerAlquileres(FirebaseUser user) {
        if (user == null) {
            System.out.println("Usuario no autenticado.");
            return;
        }

        String userId = user.getUid();

        db.collection("alquileres")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Alquiler alquiler = document.toObject(Alquiler.class);
                            System.out.println("Alquiler encontrado: " + alquiler.getFechaInicio());
                        }
                    } else {
                        Log.e("Firestore", "Error al obtener los alquileres", task.getException());
                    }
                });
    }

}

