/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.Main to edit this template
 */
package Main;
import UI.FrmLogin;
/**
 *
 * @author Jorge
 */
public class Main {
     public static void main(String[] args) {

        /* Aplicar Look and Feel tipo Windows (opcional pero recomendado) */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al aplicar LookAndFeel: " + e.getMessage());
        }

        /* Iniciar la aplicación mostrando el Login */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmLogin().setVisible(true);
            }
        });
    }
}
