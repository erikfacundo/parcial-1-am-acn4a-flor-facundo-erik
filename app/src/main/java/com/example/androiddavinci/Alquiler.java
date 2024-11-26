package com.example.androiddavinci;

public class Alquiler {

    private String fechaInicio;
    private String fechaFin;
    private String precioFinal;
    private int diasSeleccionados;
    private String userId;
    private String idAlquiler;

    public Alquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados, String userId) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioFinal = precioFinal;
        this.diasSeleccionados = diasSeleccionados;
        this.userId = userId;
    }

    public Alquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados) {
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getPrecioFinal() {
        return precioFinal;
    }

    public int getDiasSeleccionados() {
        return diasSeleccionados;
    }

    public String getUserId() {
        return userId;
    }

    public String getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(String idAlquiler) {
        this.idAlquiler = idAlquiler;
    }
}
