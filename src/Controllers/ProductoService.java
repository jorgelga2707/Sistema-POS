/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import dao.ProductoDAO;
import model.Producto;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jorge
 */
public class ProductoService {
    private ProductoDAO dao = new ProductoDAO();
    private static List<String> codigos = new ArrayList<>();

    // Validar códigos duplicados con ArrayList
    public boolean registrarProducto(Producto p) {

        if (codigos.contains(p.getCodigo())) {
            return false;
        }

        boolean ok = dao.registrar(p);
        if (ok) {
            codigos.add(p.getCodigo());
        }
        return ok;
    }

    public boolean actualizarProducto(Producto p) {
        return dao.actualizar(p);
    }

    public boolean eliminarProducto(String codigo) {
        codigos.remove(codigo);
        return dao.eliminar(codigo);
    }

    public Producto buscarPorCodigo(String codigo) {
        return dao.buscarPorCodigo(codigo);
    }

    public List<Producto> listarProductos() {
        return dao.listar();
    }
}
    
