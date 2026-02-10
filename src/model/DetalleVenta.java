/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

/**
 *
 * @author Jorge
 */
public class DetalleVenta {
    
    private int id;
    
    // El atributo es idVenta
    private int idVenta; 
    
    private int idProducto;
    private int cantidad;
    private double precio;
    
    // Atributos Añadidos: Necesarios para el Ticket y Consistencia en UI/DAO
    private double subtotal; 
    private String nombreProducto; // <-- ESTE ERA EL CAMPO FALTANTE PARA EL TICKET

    public DetalleVenta() {}

    // Constructor actualizado (opcional, pero útil)
    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precio, double subtotal, String nombreProducto) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        this.nombreProducto = nombreProducto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Getter y Setter para idVenta
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    // Métodos para Subtotal

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    // Métodos para Nombre del Producto (CRUCIAL PARA EL TICKET)

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
