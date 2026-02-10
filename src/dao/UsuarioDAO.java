/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Database.Conexion;
import static Database.Conexion.getConexion;
import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jorge
 */

public class UsuarioDAO {

    public Usuario login(String username, String password) {
        Usuario u = null;
        String sql = "SELECT * FROM usuarios WHERE username=? AND password=?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username")); // CAMBIADO
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
            }
        } catch (Exception e) {
            System.out.println("Error login: " + e.getMessage());
        }
        return u;
    }

    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios(username, password, rol) VALUES (?,?,?)";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername()); // CAMBIADO
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error registrar: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username")); // CAMBIADO
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error listar: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET username=?, password=?, rol=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsername()); // CAMBIADO
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        }
    }
}

