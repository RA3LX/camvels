package com.camvels.application.dto;

public class ReportRequest {

    private String tipo;
    private Integer proveedorId;
    private boolean incluirStockBajo;
    private boolean incluirMalEstado;

    public ReportRequest() {
    }

    public ReportRequest(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public boolean isIncluirStockBajo() {
        return incluirStockBajo;
    }

    public void setIncluirStockBajo(boolean incluirStockBajo) {
        this.incluirStockBajo = incluirStockBajo;
    }

    public boolean isIncluirMalEstado() {
        return incluirMalEstado;
    }

    public void setIncluirMalEstado(boolean incluirMalEstado) {
        this.incluirMalEstado = incluirMalEstado;
    }

    public boolean requiereFiltroProductos() {
        return "proveedores_productos".equals(tipo);
    }

    public boolean tieneFiltroProductosValido() {
        return incluirStockBajo || incluirMalEstado;
    }
}
