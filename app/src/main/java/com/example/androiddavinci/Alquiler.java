package com.example.androiddavinci;

public class Alquiler {
    private String fechaInicio;
    private String fechaFin;
    private String precioFinal;
    private int diasSeleccionados;

    // Constructor vacío requerido para Firebase
    public Alquiler() {
    }

    // Constructor con parámetros
    public Alquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioFinal = precioFinal;
        this.diasSeleccionados = diasSeleccionados;
    }

    // Getters y setters
    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(String precioFinal) {
        this.precioFinal = precioFinal;
    }

    public int getDiasSeleccionados() {
        return diasSeleccionados;
    }

    public void setDiasSeleccionados(int diasSeleccionados) {
        this.diasSeleccionados = diasSeleccionados;
    }
}
