import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlquilerToken {

    private FirebaseFirestore db;  // Instancia de FirebaseFirestore

    public AlquilerToken() {
        db = FirebaseFirestore.getInstance();  // Inicializar Firestore
    }

    // Método para crear un alquiler
    public void crearAlquiler(String descripcion, FirebaseUser user) {
        // Crear un nuevo documento en la colección de alquileres
        Alquiler alquiler = new Alquiler(descripcion, user.getUid());

        // Referencia a la colección "alquileres"
        db.collection("alquileres")
                .add(alquiler)
                .addOnSuccessListener(documentReference -> {
                    // Si el alquiler se crea con éxito
                    System.out.println("Alquiler creado con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Si ocurre un error al crear el alquiler
                    System.out.println("Error al crear el alquiler: " + e.getMessage());
                });
    }

    // Clase Alquiler (puedes definirla como un POJO)
    public static class Alquiler {
        private String descripcion;
        private String userId;

        public Alquiler(String descripcion, String userId) {
            this.descripcion = descripcion;
            this.userId = userId;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getUserId() {
            return userId;
        }
    }
}
