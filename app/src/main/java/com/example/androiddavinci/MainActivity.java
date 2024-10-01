package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

////barradesistema////
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

////boton////
        Button registerVehicleButton = findViewById(R.id.registerVehicleButton);

        registerVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar la actividad VehicleListActivity
                Intent intent = new Intent(MainActivity.this, VehiculosDisponibles.class);
                startActivity(intent);
            }
        });

        Button alquilarMotoButton = findViewById(R.id.alquilarMotoButton);

        alquilarMotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Próximamente", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /// TODO no funciona boton de atras chequeando///
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}


////fin boton lleva a lista de vehiculos////