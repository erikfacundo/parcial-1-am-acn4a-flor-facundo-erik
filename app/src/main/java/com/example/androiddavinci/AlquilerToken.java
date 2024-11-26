package com.example.androiddavinci;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AlquilerToken {

    private FirebaseFirestore db;

    public AlquilerToken() {
        db = FirebaseFirestore.getInstance();
    }

    public void crearAlquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados, FirebaseUser user) {
        if (user == null) {
            return;
        }

        String userId = user.getUid();
        Alquiler alquiler = new Alquiler(fechaInicio, fechaFin, precioFinal, diasSeleccionados, userId);

        db.collection("alquileres")
                .add(alquiler)
                .addOnSuccessListener(documentReference -> {
                    String idAlquiler = documentReference.getId();
                    alquiler.setIdAlquiler(idAlquiler);
                    db.collection("alquileres").document(idAlquiler)
                            .set(alquiler);
                })
                .addOnFailureListener(e -> {});
    }

    public void obtenerAlquileres(FirebaseUser user) {
        if (user == null) {
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
                        }
                    }
                });
    }
}
