/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Jorge
 */
public class CorrelativoDAO {
  public int obtenerNumeroActual(String nombre) {
        int numero = 0;
        String sql = "SELECT numero_actual FROM correlativo WHERE nombre = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    numero = rs.getInt("numero_actual");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el número de correlativo: " + e.getMessage());
        }
        return numero;
    }

    /**
     * Incrementa el número de correlativo DENTRO de una transacción ya iniciada.
     * @param cn La conexión activa de la transacción de venta.
     * @param nombre El nombre del correlativo (ej: "VENTA").
     * @return true si el incremento fue exitoso.
     */
    public boolean incrementarNumero(Connection cn, String nombre) throws SQLException {
        // NOTA: Esta operación se ejecutará en la misma transacción que la venta.
        String sql = "UPDATE correlativo SET numero_actual = numero_actual + 1 WHERE nombre = ?";
        
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                 throw new SQLException("Error al incrementar correlativo. El registro '" + nombre + "' no existe.");
            }
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            // Se propaga la excepción para que VentaDAO sepa que debe hacer ROLLBACK
            throw new SQLException("Fallo al actualizar el número de boleta: " + e.getMessage());
        }
    }
}  

