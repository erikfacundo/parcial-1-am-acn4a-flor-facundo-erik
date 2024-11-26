package com.example.androiddavinci;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
        Button btnAtras = findViewById(R.id.btn_atras); // Botón para ir atrás
        progressBar = findViewById(R.id.progressBar); // Referencia al spinner (ProgressBar)

        imagen.setImageResource(imagenVehiculo);
        txtPrecio.setText(precioVehiculo);

        assert precioVehiculo != null;
        precioBase = Double.parseDouble(precioVehiculo.replace("$", "").replace(",", ""));

        btnSeleccionarFechaInicio.setOnClickListener(view -> showDatePickerDialog(true));

        btnSeleccionarFechaFin.setOnClickListener(view -> showDatePickerDialog(false));

        btnAtras.setOnClickListener(view -> {
            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                showAlert("Error", "Por favor, seleccione ambas fechas", "OK", false);
                return; // No continuar si alguna fecha está vacía
            }

            if (fechaInicio.compareTo(fechaFin) >= 0) {
                showAlert("Error", "La fecha de fin debe ser posterior a la de inicio", "OK", false);
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Calcular el precio total basado en los días seleccionados
            int diasSeleccionados = calculateDaysDifference(fechaInicio, fechaFin);

            if (diasSeleccionados > 0) {
                double precioTotal = precioBase * diasSeleccionados;
                @SuppressLint("DefaultLocale") String precioFinal = "$" + String.format("%.2f", precioTotal);
                txtPrecio.setText(precioFinal);

                saveAlquilerToFirebase(fechaInicio, fechaFin, precioFinal, diasSeleccionados);

                showAlert("Confirmación", "El alquiler ha sido confirmado con éxito. Precio total: " + precioFinal, "OK", true);
            } else {
                showAlert("Error", "La fecha de fin debe ser después de la fecha de inicio", "OK", false);
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

            if (isDateBeforeToday(date)) {
                Toast.makeText(DetallesVehiculoActivity.this, "La fecha seleccionada no puede ser anterior a la fecha actual.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isStartDate) {
                fechaInicio = date;
                txtFechaInicio.setText(fechaInicio);
            } else {
                fechaFin = date;
                txtFechaFin.setText(fechaFin);
            }

            if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                int diasSeleccionados = calculateDaysDifference(fechaInicio, fechaFin);

                txtDias.setText(TXT_DIAS + diasSeleccionados);

                if (diasSeleccionados > 0) {
                    double precioTotal = precioBase * diasSeleccionados;
                    @SuppressLint("DefaultLocale") String precioFinal = "$" + String.format("%.2f", precioTotal);
                    txtPrecio.setText(precioFinal);
                } else {
                    Toast.makeText(DetallesVehiculoActivity.this, "La fecha de fin debe ser después de la fecha de inicio", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private boolean isDateBeforeToday(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        String[] parts = selectedDate.split("-");
        int selectedYear = Integer.parseInt(parts[0]);
        int selectedMonth = Integer.parseInt(parts[1]) - 1;
        int selectedDay = Integer.parseInt(parts[2]);

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

        return selectedCalendar.before(calendar);
    }

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

            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void saveAlquilerToFirebase(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados) {
        String alquilerId = mDatabase.push().getKey();

        Alquiler alquiler = new Alquiler(fechaInicio, fechaFin, precioFinal, diasSeleccionados);

        if (alquilerId != null) {
            mDatabase.child(alquilerId).setValue(alquiler);
        }
    }

    private void showAlert(String title, String message, String positiveButtonText, boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    if (isSuccess) {
                        Intent intent = new Intent(DetallesVehiculoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
