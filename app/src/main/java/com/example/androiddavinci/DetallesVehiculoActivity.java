package com.example.androiddavinci;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DetallesVehiculoActivity extends AppCompatActivity {

    // Variables de vista
    private TextView txtFechaInicio, txtFechaFin, txtPrecio, txtDias;
    private Button btnSeleccionarFechaInicio, btnSeleccionarFechaFin;
    private String fechaInicio = "", fechaFin = "";
    private double precioBase;
    private static final String TXT_DIAS = "Días: ";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_vehiculo);

        // Recuperar los datos enviados //
        Intent intent = getIntent();
        String precioVehiculo = intent.getStringExtra("precio");
        int imagenVehiculo = intent.getIntExtra("imagen", 0);

        // elementos que debe mostrar //
        ImageView imagen = findViewById(R.id.imagen_vehiculo);
        txtPrecio = findViewById(R.id.precio_vehiculo);
        txtFechaInicio = findViewById(R.id.txt_fecha_inicio);
        txtFechaFin = findViewById(R.id.txt_fecha_fin);
        txtDias = findViewById(R.id.txt_dias);
        btnSeleccionarFechaInicio = findViewById(R.id.btn_fecha_inicio);
        btnSeleccionarFechaFin = findViewById(R.id.btn_fecha_fin);

        imagen.setImageResource(imagenVehiculo);
        txtPrecio.setText(precioVehiculo);

        // replace de precio//
        precioBase = Double.parseDouble(precioVehiculo.replace("$", "").replace(",", ""));

        // listener de fecha inicio //
        btnSeleccionarFechaInicio.setOnClickListener(view -> showDatePickerDialog(true));

        // listener de fecha fin//
        btnSeleccionarFechaFin.setOnClickListener(view -> showDatePickerDialog(false));
    }

    // datePicker //
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
                    String precioFinal = "$" + String.format("%.2f", precioTotal);
                    txtPrecio.setText(precioFinal);
                } else {
                    // Si la fecha de fin es antes de la fecha de inicio
                    Toast.makeText(DetallesVehiculoActivity.this, "La fecha de fin debe ser después de la fecha de inicio", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    // Método para comprobar si la fecha seleccionada es anterior a la fecha actual ///
    private boolean isDateBeforeToday(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        String[] parts = selectedDate.split("-");
        int selectedYear = Integer.parseInt(parts[0]);
        int selectedMonth = Integer.parseInt(parts[1]) - 1;
        int selectedDay = Integer.parseInt(parts[2]);

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

        // Comparar las fechas
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
                return 0; // Si la fecha de fin es anterior a la de inicio, no se calcula la diferencia
            }

            // Convertir a días
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));  // 1000 ms = 1 seg, 60 seg = 1 min, 60 min = 1 hr, 24 hrs = 1 día
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
