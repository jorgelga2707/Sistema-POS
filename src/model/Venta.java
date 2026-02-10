/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Date;

/**
 *
 * @author Jorge
 */

public class Venta {

    private int id;
    private Date fecha;
    private double total;
    private int idVendedor;
    
    // Atributo Añadido: Necesario para MostrarTicket
    private String nombreVendedor; 

    public Venta() {}

    public Venta(Date fecha, double total) {
        this.fecha = fecha;
        this.total = total;
    }
    
    // Métodos Añadidos para el Nombre del Vendedor (Usado por VentaDAO)
    
    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}