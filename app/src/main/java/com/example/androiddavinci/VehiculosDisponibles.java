package com.example.androiddavinci;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// Clase de vehículo //
class Vehicle {
    String name;
    String price;
    int imageResource;

    Vehicle(String name, String price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }
}

///// mostrar vehículos ////
class VehicleAdapter extends ArrayAdapter<Vehicle> {
    private final ArrayList<Vehicle> vehicles;
    private final AppCompatActivity context;

    VehicleAdapter(AppCompatActivity context, ArrayList<Vehicle> vehicles) {
        super(context, 0, vehicles);
        this.context = context;
        this.vehicles = vehicles;
    }

    @Override //lista de vehiculos//
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_vehicle, parent, false);
        }

        Vehicle vehicle = vehicles.get(position);

        // nombre precio y foto//
        TextView nameTextView = convertView.findViewById(R.id.vehicle_name);
        TextView priceTextView = convertView.findViewById(R.id.vehicle_price);
        ImageView vehicleImageView = convertView.findViewById(R.id.vehicle_image);

        nameTextView.setText(vehicle.name);
        priceTextView.setText(vehicle.price);
        vehicleImageView.setImageResource(vehicle.imageResource);

        return convertView;
    }
}

public class VehiculosDisponibles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos_disponibles);

        ListView listView = findViewById(R.id.vista_vehiculos);

        // arraylist de vehiculos//
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("Toyota Corolla", "$20,000", R.drawable.toyota_corolla));
        vehicles.add(new Vehicle("Honda Civic", "$22,000", R.drawable.honda_civic));
        vehicles.add(new Vehicle("Ford Mustang", "$30,000", R.drawable.ford_mustang));
        vehicles.add(new Vehicle("Chevrolet Camaro", "$28,000", R.drawable.chevrolet_camaro));
        vehicles.add(new Vehicle("BMW Serie 3", "$35,000", R.drawable.bmw_serie_3));
        vehicles.add(new Vehicle("Audi A4", "$33,000", R.drawable.audi_a4));
        vehicles.add(new Vehicle("Toyota Corolla", "$20,000", R.drawable.toyota_corolla));
        vehicles.add(new Vehicle("Honda Civic", "$22,000", R.drawable.honda_civic));
        vehicles.add(new Vehicle("Ford Mustang", "$30,000", R.drawable.ford_mustang));
        vehicles.add(new Vehicle("Chevrolet Camaro", "$28,000", R.drawable.chevrolet_camaro));
        vehicles.add(new Vehicle("BMW Serie 3", "$35,000", R.drawable.bmw_serie_3));
        vehicles.add(new Vehicle("Audi A4", "$33,000", R.drawable.audi_a4));
        vehicles.add(new Vehicle("Toyota Corolla", "$20,000", R.drawable.toyota_corolla));
        vehicles.add(new Vehicle("Honda Civic", "$22,000", R.drawable.honda_civic));
        vehicles.add(new Vehicle("Ford Mustang", "$30,000", R.drawable.ford_mustang));
        vehicles.add(new Vehicle("Chevrolet Camaro", "$28,000", R.drawable.chevrolet_camaro));
        vehicles.add(new Vehicle("BMW Serie 3", "$35,000", R.drawable.bmw_serie_3));
        vehicles.add(new Vehicle("Audi A4", "$33,000", R.drawable.audi_a4));

        // nombre, imagen, precio del auto
        VehicleAdapter adapter = new VehicleAdapter(this, vehicles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Vehicle vehiculoSeleccionado = vehicles.get(position);

            Intent intent = new Intent(VehiculosDisponibles.this, DetallesVehiculoActivity.class);
            intent.putExtra("nombre", vehiculoSeleccionado.name);
            intent.putExtra("precio", vehiculoSeleccionado.price);
            intent.putExtra("imagen", vehiculoSeleccionado.imageResource);
            startActivity(intent);
        });


    }
}
