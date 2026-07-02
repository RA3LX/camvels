package com.camvels.domain.model;

import com.camvels.domain.exception.CantidadInvalidaException;
import com.camvels.domain.exception.StockInsuficienteException;

public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private int stock;
    private int stockBuenEstado;
    private int stockMalEstado;
    private int minimo;
    private double precio;
    private String estado;
    private Integer proveedorId;

    public Producto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getStockBuenEstado() { return stockBuenEstado; }
    public void setStockBuenEstado(int stockBuenEstado) { this.stockBuenEstado = stockBuenEstado; }
    public int getStockMalEstado() { return stockMalEstado; }
    public void setStockMalEstado(int stockMalEstado) { this.stockMalEstado = stockMalEstado; }
    public int getMinimo() { return minimo; }
    public void setMinimo(int minimo) { this.minimo = minimo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }

    public void actualizarStockTotal() {
        this.stock = this.stockBuenEstado + this.stockMalEstado;
    }

    public void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a cero");
        }
    }

    public void validarSalida(int cantidad) {
        validarCantidad(cantidad);
        if (stock < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Stock disponible: " + stock
                            + " (Stock bueno: " + stockBuenEstado
                            + ", Stock malo: " + stockMalEstado + ")");
        }
    }

    public void aplicarMovimiento(TipoMovimiento tipo, int cantidad) {
        validarCantidad(cantidad);
        switch (tipo) {
            case ENTRADA -> stockBuenEstado += cantidad;
            case SALIDA -> {
                validarSalida(cantidad);
                descontarSalida(cantidad);
            }
            case AJUSTE -> convertirMalEstadoABuenEstado(cantidad);
        }
        actualizarStockTotal();
        actualizarEstadoSegunStock();
    }

    private void descontarSalida(int cantidad) {
        if (stockBuenEstado >= cantidad) {
            stockBuenEstado -= cantidad;
        } else {
            int restante = cantidad - stockBuenEstado;
            stockBuenEstado = 0;
            stockMalEstado = Math.max(0, stockMalEstado - restante);
        }
    }

    private void convertirMalEstadoABuenEstado(int cantidad) {
        if (stockMalEstado >= cantidad) {
            stockMalEstado -= cantidad;
            stockBuenEstado += cantidad;
        } else {
            int excedente = cantidad - stockMalEstado;
            stockBuenEstado += stockMalEstado;
            stockMalEstado = 0;
            stockBuenEstado += excedente;
        }
    }

    private void actualizarEstadoSegunStock() {
        if (stockMalEstado > 0) {
            estado = "mal_estado";
        } else if (stockBuenEstado > 0) {
            estado = "buen_estado";
        }
    }
}
