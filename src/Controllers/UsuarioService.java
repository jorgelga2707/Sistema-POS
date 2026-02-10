/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge
 */
package Controllers;

import dao.UsuarioDAO;
import model.Usuario;


public class UsuarioService {

    private UsuarioDAO dao = new UsuarioDAO();

    public Usuario login(String usuario, String password) {
        return dao.login(usuario, password);
    }

    public boolean registrar(Usuario u) {
        return dao.registrar(u);
    }
}