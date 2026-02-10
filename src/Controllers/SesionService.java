/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;
import model.Usuario;
/**
 *
 * @author Jorge
 */
public class SesionService {
    // Variable estática que mantiene al usuario en memoria durante la ejecución
    private static Usuario usuarioActual;

    // Guarda el usuario que acaba de loguearse
    public static void login(Usuario usuario) {
        usuarioActual = usuario;
    }

    // Obtiene los datos del usuario logueado (ID, nombre, etc.)
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Limpia la sesión
    public static void logout() {
        usuarioActual = null;
    }
    
    
}
