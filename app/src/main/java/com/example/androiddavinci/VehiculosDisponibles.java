package com.example.androiddavinci;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class VehiculosDisponibles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos_disponibles);

        //// ListView ////
        ListView listView = findViewById(R.id.vehicle_list_view);

        ////Ejemplo de autos////
        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add("Toyota Corolla");
        vehicles.add("Honda Civic");
        vehicles.add("Ford Mustang");
        vehicles.add("Chevrolet Camaro");
        vehicles.add("BMW Serie 3");
        vehicles.add("Audi A4");

        ////como mostrar los vehiculos////
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicles);
        listView.setAdapter(adapter);
    }
}

/* * //TODO rearmar visual de vehiculos con foto, precio y select estilo boostrap */