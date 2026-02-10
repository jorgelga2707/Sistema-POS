/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Database.Conexion;
import model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jorge
 */
public class ProductoDAO {
    
    /**
     * Registra un nuevo producto en la base de datos.
     */
    public boolean registrar(Producto p) {
        String sql = "INSERT INTO productos(codigo,nombre,descripcion,categoria,precio,stock) VALUES (?,?,?,?,?,?)";
        
        // Uso de try-with-resources para el cierre automático de recursos (cn, ps)
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getCategoria());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getStock());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error SQL al registrar producto: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error general al registrar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un producto existente.
     */
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, categoria=?, precio=?, stock=? WHERE codigo=?";
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getCodigo());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar producto: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error general al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un producto de la base de datos usando su código.
     */
    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo=?";
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar producto: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error general al eliminar producto: " + e.getMessage());
            return false;
        }
    }
        public int contarProductosPorCategoria(String categoria) {
        String sql = "SELECT COUNT(*) FROM productos WHERE categoria = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = Conexion.getConexion(); // Tu conexión a la BD
            ps = con.prepareStatement(sql);
            ps.setString(1, categoria);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Retorna el número de filas encontradas
            }
                } catch (SQLException e) {
                    System.out.println("Error al contar: " + e.toString());
                }
                return 0;
            }

    
    
    /**
     * Busca un producto por su código y devuelve el objeto Producto.
     */
    public Producto buscarPorCodigo(String codigo) {
        Producto p = null;
        // Basado en tu SQL: Tabla 'productos' y columna 'codigo'
        String sql = "SELECT * FROM productos WHERE codigo = ?"; 
    
        try (java.sql.Connection con = new Database.Conexion().getConexion(); 
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Producto();
                // Nombres de columna exactos de tu script SQL
                p.setId(rs.getInt("id"));           // En tu SQL es 'id', no 'id_producto'
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setCategoria(rs.getString("categoria"));
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error en DAO al buscar por código: " + e.getMessage());
        }
        return p;
    }
    
    /**
     * Busca un producto por su clave primaria (ID) y devuelve el objeto Producto.
     * **CLAVE PARA MostrarTicket.java**
     */
    public Producto buscarPorId(int idProducto) {
        // La consulta usa el campo 'id' que es la clave primaria
        String sql = "SELECT id, codigo, nombre, descripcion, categoria, precio, stock FROM productos WHERE id=?";
        Producto p = null;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto); // Ahora asignamos un entero
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al buscar producto por ID: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al buscar producto por ID: " + e.getMessage());
        }
        return p;
    }
    
    // --- MÉTODOS DE BÚSQUEDA Y LISTADO ---
    
    /**
     * Obtiene una lista de productos filtrados por el nombre de la categoría.
     * **CLAVE PARA RegistroVentas.java**
     * @param nombreCategoria El nombre de la categoría (String) almacenado en la tabla productos.
     * @return Lista de objetos Producto que coinciden con la categoría.
     */
    public List<Producto> listarPorCategoria(String nombreCategoria) {
        List<Producto> lista = new ArrayList<>();
        // Filtramos directamente por la columna 'categoria'
        String SQL = "SELECT id, codigo, nombre, stock, precio, descripcion, categoria FROM productos WHERE categoria = ?"; 
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL)) {
            
            ps.setString(1, nombreCategoria); // Asigna el nombre de la categoría
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    // Importante: También obtenemos la ID para usarla en el DetalleVenta
                    p.setId(rs.getInt("id")); 
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setStock(rs.getInt("stock"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setCategoria(rs.getString("categoria")); 
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos por categoría: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al listar productos por categoría: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene solo los nombres distintos de todas las categorías para llenar el JComboBox.
     * **CLAVE PARA RegistroVentas.java**
     */
    public List<String> listarNombresCategorias() {
        List<String> listaNombres = new ArrayList<>();
        // Usamos DISTINCT para asegurar que no haya duplicados
        String SQL = "SELECT DISTINCT categoria FROM productos ORDER BY categoria"; 
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Obtiene el String de la columna 'categoria'
                listaNombres.add(rs.getString("categoria")); 
            }
        } catch (SQLException e) {
            System.err.println("Error al listar nombres de categorías: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al listar nombres de categorías: " + e.getMessage());
        }
        return listaNombres;
    }

    public List<Producto> buscarProductos(String filtro) {
    List<Producto> lista = new ArrayList<>();
    // Ajustamos el WHERE para que busque en 'nombre' O en 'codigo'
    String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR codigo LIKE ?";

    try (Connection cn = Conexion.getConexion();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        // El primer '?' es para el nombre
        ps.setString(1, "%" + filtro + "%"); 
        // El segundo '?' es para el código
        ps.setString(2, "%" + filtro + "%");
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al filtrar productos: " + e.getMessage());
    }
    return lista;
}
    /**
     * Devuelve una lista de todos los productos en la base de datos.
     */
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, descripcion, categoria, precio, stock FROM productos";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al listar productos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al listar productos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza el stock de un producto después de una venta.
     */
    public void actualizarStock(int idProducto, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id=?"; 
        
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar stock: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al actualizar stock: " + e.getMessage());
            }
    }
    
    public List<Producto> listarStockBajo(int limite) {
    List<Producto> lista = new ArrayList<>();
    // CORRECCIÓN: tabla 'productos' y columnas id, codigo, nombre, categoria, stock
    String sql = "SELECT id, codigo, nombre, categoria, stock FROM productos WHERE stock <= ?";
    
    try (Connection con = Conexion.getConexion(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, limite);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Producto p = new Producto();
            p.setId(rs.getInt("id"));
            p.setCodigo(rs.getString("codigo"));
            p.setNombre(rs.getString("nombre"));
            p.setCategoria(rs.getString("categoria"));
            p.setStock(rs.getInt("stock"));
            lista.add(p);
        }
    } catch (SQLException e) {
        System.err.println("Error en stock bajo: " + e.getMessage());
    }
    return lista;
}
    
}

