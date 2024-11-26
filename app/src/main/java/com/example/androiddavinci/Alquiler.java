package com.example.androiddavinci;

public class Alquiler {

    private String fechaInicio;
    private String fechaFin;
    private String precioFinal;
    private int diasSeleccionados;
    private String userId;
    private String idAlquiler;

    public Alquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioFinal = precioFinal;
        this.diasSeleccionados = diasSeleccionados;
    }

    public Alquiler(String fechaInicio, String fechaFin, String precioFinal, int diasSeleccionados, String userId) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioFinal = precioFinal;
        this.diasSeleccionados = diasSeleccionados;
        this.userId = userId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(String idAlquiler) {
        this.idAlquiler = idAlquiler;
    }
}
