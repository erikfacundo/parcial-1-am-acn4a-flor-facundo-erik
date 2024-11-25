package com.example.androiddavinci;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DetallesVehiculoActivity extends AppCompatActivity {

    // Variables de vista
    private TextView txtFechaInicio, txtFechaFin, txtPrecio, txtDias;
    private String fechaInicio = "", fechaFin = "";
    private double precioBase;
    private static final String TXT_DIAS = "Días: ";
    private ProgressBar progressBar; // Spinner

    // Firebase
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_vehiculo);

        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("alquileres");

        // Recuperar los datos enviados
        Intent intent = getIntent();
        String precioVehiculo = intent.getStringExtra("precio");
        int imagenVehiculo = intent.getIntExtra("imagen", 0);

        // Elementos que debe mostrar
        ImageView imagen = findViewById(R.id.imagen_vehiculo);
        txtPrecio = findViewById(R.id.precio_vehiculo);
        txtFechaInicio = findViewById(R.id.txt_fecha_inicio);
        txtFechaFin = findViewById(R.id.txt_fecha_fin);
        txtDias = findViewById(R.id.txt_dias);
        Button btnSeleccionarFechaInicio = findViewById(R.id.btn_fecha_inicio);
        Button btnSeleccionarFechaFin = findViewById(R.id.btn_fecha_fin);
        Button btnAtras = findViewById(R.id.btn_atras); // Inicializamos el botón "Continuar"
        progressBar = findViewById(R.id.progressBar); // Referencia al spinner (ProgressBar)

        imagen.setImageResource(imagenVehiculo);
        txtPrecio.setText(precioVehiculo);

        // Reemplazar el precio por la versión numérica sin signos de pesos y comas
        assert precioVehiculo != null;
        precioBase = Double.parseDouble(precioVehiculo.replace("$", "").replace(",", ""));

        // Listener de fecha inicio
        btnSeleccionarFechaInicio.setOnClickListener(view -> showDatePickerDialog(true));

        // Listener de fecha fin
        btnSeleccionarFechaFin.setOnClickListener(view -> showDatePickerDialog(false));

        // Configurar el listener del botón "Continuar" (btnAtras)
        btnAtras.setOnClickListener(view -> {
            // Verificar si las fechas han sido seleccionadas
            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                showAlert("Error", "Por favor, seleccione ambas fechas", "OK", false);
                return; // No continuar si alguna fecha está vacía
            }

            // Verificar que la fecha de fin sea posterior a la fecha de inicio
            if (fechaInicio.compareTo(fechaFin) >= 0) {
                showAlert("Error", "La fecha de fin debe ser posterior a la de inicio", "OK", false);
                return;
            }

            // Mostrar Spinner (ProgressBar)
            progressBar.setVisibility(View.VISIBLE);

            // Calcular el precio total basado en los días seleccionados
            int diasSeleccionados = calculateDaysDifference(fechaInicio, fechaFin);

            if (diasSeleccionados > 0) {
                double precioTotal = precioBase * diasSeleccionados;
                @SuppressLint("DefaultLocale") String precioFinal = "$" + String.format("%.2f", precioTotal);
                txtPrecio.setText(precioFinal);

                // Guardar en Firebase
                saveAlquilerToFirebase(fechaInicio, fechaFin, precioFinal, diasSeleccionados);

                // Mostrar un Alert de Confirmación
                showAlert("Confirmación", "El alquiler ha sido confirmado con éxito. Precio total: " + precioFinal, "OK", true);
            } else {
                // Si la fecha de fin es antes de la fecha de inicio
                showAlert("Error", "La fecha de fin debe ser después de la fecha de inicio", "OK", false);
            }

            // Ocultar Spinner después de la acción
            progressBar.setVisibility(View.GONE);
        });
    }

    // Método para mostrar el DatePicker
    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

            // Validar si la fecha seleccionada es anterior a la fecha actual
            if (isDateBeforeToday(date)) {
                Toast.makeText(DetallesVehiculoActivity.this, "La fecha seleccionada no puede ser anterior a la fecha actual.", Toast.LENGTH_SHORT).show();
                return; // Salir si la fecha es inválida
            }

            // Actualizar la fecha según si es fecha de inicio o fin
            if (isStartDate) {
                fechaInicio = date;
                txtFechaInicio.setText(fechaInicio);
            } else {
                fechaFin = date;
                txtFechaFin.setText(fechaFin);
            }

            // Actualizar el precio si ambas fechas están seleccionadas
            if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                int diasSeleccionados = calculateDaysDifference(fechaInicio, fechaFin);

                txtDias.setText(TXT_DIAS + diasSeleccionados);

                if (diasSeleccionados > 0) {
                    // Calcular el precio total basado en los días seleccionados
                    double precioTotal = precioBase * diasSeleccionados;
                    @SuppressLint("DefaultLocale") String precioFinal = "$" + String.format("%.2f", precioTotal);
                    txtPrecio.setText(precioFinal);
                } else {
                    // Si la fecha de fin es antes de la fecha de inicio
                    Toast.makeText(DetallesVehiculoActivity.this, "La fecha de fin debe ser después de la fecha de inicio", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    // Método para comprobar si la fecha seleccionada es anterior a la fecha actual
    private boolean isDateBeforeToday(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        String[] parts = selectedDate.split("-");
        int selectedYear = Integer.parseInt(parts[0]);
        int selectedMonth = Integer.parseInt(parts[1]) - 1;
        int selectedDay = Integer.parseInt(parts[2]);

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

        // Comparar las fechas
        return selectedCalendar.before(calendar);
    }

    // Método para calcular la diferencia en días entre dos fechas
    private int calculateDaysDifference(String startDate, String endDate) {
        try {
            String[] startParts = startDate.split("-");
            String[] endParts = endDate.split("-");

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Integer.parseInt(startParts[0]), Integer.parseInt(startParts[1]) - 1, Integer.parseInt(startParts[2]));

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Integer.parseInt(endParts[0]), Integer.parseInt(endParts[1]) - 1, Integer.parseInt(endParts[2]));

            long diffInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();

            if (diffInMillis < 0) {
                return 0;
            }

            // Convertir la diferencia de milisegundos a días
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Guardar los datos del alquiler en Firebase
    private void saveAlquilerToFirebase(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados) {
        String alquilerId = mDatabase.push().getKey(); // Generar un ID único para el alquiler

        Alquiler alquiler = new Alquiler(fechaInicio, fechaFin, precioFinal, diasSeleccionados);

        if (alquilerId != null) {
            mDatabase.child(alquilerId).setValue(alquiler);
        }
    }

    // Mostrar el AlertDialog
    private void showAlert(String title, String message, String positiveButtonText, boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    if (isSuccess) {
                        // Redirigir a la pantalla de confirmación (por ejemplo)
                        Intent intent = new Intent(DetallesVehiculoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
