/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import dao.ProductoDAO;
import dao.VentaDAO;
import model.DetalleVenta;
import model.Producto;
import model.Venta;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Jorge
 */
public class VentaService {

    private VentaDAO ventaDAO = new VentaDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private List<DetalleVenta> detalles = new ArrayList<>();

    public void agregarProducto(Producto p, int cantidad) {
        DetalleVenta d = new DetalleVenta();
        d.setIdProducto(p.getId());
        d.setCantidad(cantidad);
        d.setPrecio(p.getPrecio());
        detalles.add(d);
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleVenta d : detalles) {
            total += d.getCantidad() * d.getPrecio();
        }
        return total;
    }

    public int registrarVenta() {
        // 1. Validar si hay productos en el carrito
        if (detalles.isEmpty()) {
            System.err.println("Error: No hay productos en el detalle para registrar la venta.");
            return -1; 
        }

        // 2. Crear el encabezado de la venta (Venta)
        // Asumiendo que el constructor de Venta toma Date y Total
        Venta v = new Venta(new Date(), calcularTotal());

        // 3. Ejecutar la transacción completa: Venta, Detalle y Stock (Manejado en el DAO)
        // El DAO debe recuperar la ID generada y asignársela al objeto 'v'.
        boolean transaccionExitosa = ventaDAO.procesarTransaccionVenta(v, detalles);

        if (transaccionExitosa) {
            // La transacción fue exitosa. La ID generada ahora está en v.getId()
            int idVentaGenerada = v.getId();

            // 4. Generar el Ticket y Limpiar el carrito
            // Nota: generarTicket utiliza la lista 'detalles' que aún no se ha limpiado.
            generarTicket(idVentaGenerada);
            detalles.clear(); // Limpiamos la lista después de usarla

            return idVentaGenerada;
        } else {
            // La transacción falló (ROLLBACK)
            System.err.println("La transacción de venta falló y fue cancelada por la DB.");
            return -1; // Retorna un código de error o lanza una excepción
        }

    }

    private void generarTicket(int idVenta) {
        try {
            FileWriter fw = new FileWriter("ticket_" + idVenta + ".txt");
            fw.write("=== TICKET DE VENTA ===\n");
            fw.write("Venta N°: " + idVenta + "\n");
            fw.write("Fecha: " + new Date() + "\n\n");

            for (DetalleVenta d : detalles) {
                fw.write("Producto ID: " + d.getIdProducto()
                        + " Cantidad: " + d.getCantidad()
                        + " Precio: " + d.getPrecio() + "\n");
            }

            fw.write("\nTOTAL: " + calcularTotal());
            fw.close();

        } catch (Exception e) {
            System.out.println("Error ticket: " + e.getMessage());
        }
    }
}