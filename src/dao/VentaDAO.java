/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Database.Conexion;
import model.Venta;
import model.DetalleVenta;
import java.sql.*;
import java.util.ArrayList; 
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; 

public class VentaDAO {
    
    // Su método procesarTransaccionVenta... (lo mantengo truncado por espacio, asumiendo que funciona)
    public boolean procesarTransaccionVenta(Venta v, List<DetalleVenta> detalles) {
        // [CÓDIGO procesarTransaccionVenta... ]
        Connection cn = null;
        boolean transaccionExitosa = false;
        int idVentaGenerada = 0;
        
        String sqlVenta = "INSERT INTO ventas (fecha, total, id_Usuario) VALUES (?,?,?)";
        String sqlDetalle = "INSERT INTO detalle_venta(venta_id, producto_id, cantidad, subtotal) VALUES (?,?,?,?)";
        String sqlActualizarStock = "UPDATE productos SET stock = stock - ? WHERE id = ?"; 
        
        try {
            cn = Conexion.getConexion();
            cn.setAutoCommit(false); 
            
            try (PreparedStatement psVenta = cn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                
                psVenta.setTimestamp(1, new Timestamp(v.getFecha().getTime()));
                psVenta.setDouble(2, v.getTotal());
                psVenta.setInt(3, v.getIdVendedor());
                psVenta.executeUpdate();
                
                ResultSet rs = psVenta.getGeneratedKeys();
                if (rs.next()) {
                    idVentaGenerada = rs.getInt(1);
                    v.setId(idVentaGenerada); 
                } else {
                    throw new SQLException("Error al obtener la ID generada para la venta."); 
                }
            }
            
            try (PreparedStatement psDetalle = cn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = cn.prepareStatement(sqlActualizarStock)) {
                
                for (DetalleVenta d : detalles) {
                    
                    psDetalle.setInt(1, idVentaGenerada);
                    psDetalle.setInt(2, d.getIdProducto());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setDouble(4, d.getSubtotal()); // Asumiendo que getSubtotal() ya tiene el cálculo
                    psDetalle.executeUpdate();
                    
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto()); 
                    psStock.executeUpdate();
                }
            }
            
            cn.commit();
            transaccionExitosa = true;
            
        } catch (SQLException e) {
            System.err.println("Error en la transacción de venta. Haciendo ROLLBACK: " + e.getMessage());
            Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, "Transacción fallida", e);
            
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error al realizar rollback: " + ex.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error fatal en el proceso: " + e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.setAutoCommit(true); 
                    cn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        
        return transaccionExitosa;
    }
    
    // -------------------------------------------------------------------------
    // MÉTODOS CORREGIDOS / NUEVOS

    /**
     * Obtiene el ID más alto de la tabla ventas.
     */
    public int obtenerUltimaIdVenta() throws SQLException {
        // 💡 CORRECCIÓN: Usar 'id' y 'ventas'
        String sql = "SELECT MAX(id) FROM ventas"; 
        int maxId = 0;
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                maxId = rs.getInt(1); 
            }
        }
        return maxId;
    }

    /**
     * Obtiene ventas filtradas por ID, Fecha, y/o Vendedor.
     */
  public List<Venta> obtenerVentasFiltradas(Integer id, String fecha, String vendedor, String hora) throws SQLException {
    List<Venta> lista = new ArrayList<>();
    
    // 💡 CAMBIO: Se cambió id_vendedor por id_Usuario
    StringBuilder sql = new StringBuilder(
        "SELECT v.id, v.fecha, v.total, u.username AS nombre_vendedor, v.id_Usuario "
        + "FROM ventas v JOIN usuarios u ON v.id_Usuario = u.id WHERE 1=1"
    );
    
    List<Object> parametros = new ArrayList<>();
    
    if (id != null) {
        sql.append(" AND v.id = ?"); 
        parametros.add(id);
    }
    if (fecha != null && !fecha.isEmpty()) {
        sql.append(" AND DATE(v.fecha) = ?"); 
        parametros.add(fecha);
    }
    if (vendedor != null && !vendedor.isEmpty()) {
        sql.append(" AND u.username LIKE ?"); 
        parametros.add("%" + vendedor + "%");
    }
    
    sql.append(" ORDER BY v.fecha DESC");
    
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
        
        for (int i = 0; i < parametros.size(); i++) {
            stmt.setObject(i + 1, parametros.get(i));
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id")); 
                v.setFecha(rs.getTimestamp("fecha"));
                v.setTotal(rs.getDouble("total"));
                // 💡 CAMBIO: Usar id_Usuario aquí también
                v.setIdVendedor(rs.getInt("id_Usuario"));
                v.setNombreVendedor(rs.getString("nombre_vendedor"));
                lista.add(v);
            }
        }
    } catch (SQLException e) {
        throw e; 
    }
    return lista;
}

   public List<Venta> listarTodasLasVentas() throws Exception {
    List<Venta> lista = new ArrayList<>();
    
    // 💡 CAMBIO: id_vendedor -> id_Usuario
    String sql = "SELECT v.id, v.fecha, v.total, v.id_Usuario, u.username AS nombre_vendedor "
               + "FROM ventas v "
               + "INNER JOIN usuarios u ON v.id_Usuario = u.id "
               + "ORDER BY v.fecha DESC"; 
                 
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
         
        while (rs.next()) {
            Venta venta = new Venta();
            venta.setId(rs.getInt("id"));
            venta.setFecha(rs.getTimestamp("fecha"));
            venta.setTotal(rs.getDouble("total"));
            // 💡 CAMBIO: id_Usuario
            venta.setIdVendedor(rs.getInt("id_Usuario"));
            venta.setNombreVendedor(rs.getString("nombre_vendedor")); 
            
            lista.add(venta);
        }
    }
    return lista;
}
    
    public String obtenerIdVentaFormateado() {
    try {
        int ultimoId = obtenerUltimaIdVenta(); // Llama a tu método actual
        int siguienteId = ultimoId + 1;
        
        // El formato %03d significa: un número de al menos 3 dígitos, rellenado con ceros a la izquierda
        return String.format("V%03d", siguienteId); 
        
    } catch (SQLException e) {
        System.err.println("Error al obtener ID formateado: " + e.getMessage());
        return "V001"; // Valor por defecto si falla
    }
}
    public List<Venta> listarVentasHoy() {
    List<Venta> lista = new ArrayList<>();
    // Usamos id_usuario y JOIN con la tabla usuarios
    String sql = "SELECT v.id, v.fecha, v.total, u.username " +
                 "FROM ventas v " +
                 "INNER JOIN usuarios u ON v.id_usuario = u.id " +
                 "WHERE DATE(v.fecha) = CURDATE()";

    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Venta v = new Venta();
            v.setId(rs.getInt("id"));
            v.setFecha(rs.getTimestamp("fecha"));
            v.setTotal(rs.getDouble("total"));
            v.setNombreVendedor(rs.getString("username")); // <--- Ahora sí traerá el nombre
            lista.add(v);
        }
    } catch (SQLException e) {
        System.err.println("Error en ventas diario: " + e.getMessage());
    }
    return lista;
}
}
  
