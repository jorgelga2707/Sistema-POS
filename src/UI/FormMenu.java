/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import model.Usuario;
/**
 *
 * @author Jorge
 */
public class FormMenu extends javax.swing.JFrame {
    private Usuario usuarioLogueado;
    public Usuario usuario;
    private JDesktopPane jDesktopPane_menu;
     /*
     * Creates new form FormMenu
     */
    public FormMenu(Usuario u) {
         initComponents();
         this.usuario = u;
         this.usuarioLogueado = u;
         aplicarPermisos();
        
        Controllers.SesionService.login(u);
        
        
        // Configuración general
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // DesktopPane (zona donde se abren los InternalFrame)
        jDesktopPane_menu = new JDesktopPane();
        add(jDesktopPane_menu, BorderLayout.CENTER);

        // Configuraciones
        //configurarMenuSegunRol();    
    }
    
        private void aplicarPermisos() {
            if (this.usuario != null) {
                if (this.usuario.getRol().equalsIgnoreCase("Vendedor")) {

                    // 1. BLOQUEAR SOLO USUARIOS (jMenu4)
                    jMenu4.setEnabled(false); 
                    jMenu4.setVisible(false); // El vendedor no necesita ver la gestión de usuarios
                    System.out.println("Vendedor: Puede vender, ver reportes y cerrar sesión.");

                } else {
                    // Admin: Acceso total a todo
                    jMenu3.setEnabled(true);
                    jMenu4.setEnabled(true);
                    jMenu5.setEnabled(true);
                    jMenu4.setVisible(true);
                }
            }
        }
           

    private void abrirInternalCentrado(JInternalFrame internal) {
       
        for (JInternalFrame frame : jDesktopPane_menu.getAllFrames()) {
            frame.dispose();
        }

        jDesktopPane_menu.add(internal);

        Dimension desktopSize = jDesktopPane_menu.getSize();
        Dimension frameSize = internal.getSize();

        internal.setLocation(
            (desktopSize.width - frameSize.width) / 2,
            (desktopSize.height - frameSize.height) / 2
        );

        internal.setVisible(true);
    
   }
    
    private void configurarMenuSegunRol() {     
        // Ejemplo: si es vendedor, no puede ver el menú Usuarios
       String rol = usuario.getRol().toLowerCase();

        switch(rol) {
        case "vendedor":
            jMenu4.setEnabled(false); // Usuarios
            jReporteBajo.setEnabled(false); // Opcional: bloquear reportes
            break;
        case "administrador":
            // Todo habilitado
            break;
        default:
            // Caso genérico
            jMenu4.setEnabled(false);
            jReporteBajo.setEnabled(false);
        }
    }
    
    public void abrirTicketPorID(int idVenta) {
        // 1. Generar el nombre EXACTO del archivo (Formato Unificado)
        // El nombre es ahora Ticket_VT001.txt, sin guiones bajos extra ni timestamp.
        String nombreExacto = String.format("Ticket_VT%03d.txt", idVenta);

        // 2. Definir la carpeta de tickets (Ruta Unificada)
        File directorioTickets = new File("Tickets");

        // --- Mensajes de Depuración ---
        System.out.println("DEBUG: Buscando archivo: " + nombreExacto);
        System.out.println("DEBUG: En el directorio: " + directorioTickets.getAbsolutePath());

        // 3. Verificar la existencia de la carpeta
        if (!directorioTickets.exists() || !directorioTickets.isDirectory()) {
            JOptionPane.showMessageDialog(this, 
                "La carpeta 'Tickets' no existe. Buscado en: " + directorioTickets.getAbsolutePath(), 
                "Error de Directorio", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File[] todosLosArchivos = directorioTickets.listFiles();
        File archivoEncontrado = null;

        if (todosLosArchivos != null) {
            // 4. Buscar la coincidencia exacta de nombre
            for (File archivo : todosLosArchivos) {
                String nombre = archivo.getName();

                // Usamos equalsIgnoreCase para encontrar el archivo de forma robusta
                if (nombre.equalsIgnoreCase(nombreExacto)) { 
                    archivoEncontrado = archivo;
                    System.out.println("DEBUG: Archivo encontrado: " + archivoEncontrado.getName());
                    break; 
                }
            }
        }

        if (archivoEncontrado != null) {
            // 5. Abrir el archivo encontrado
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivoEncontrado);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se pudo abrir automáticamente. Búsquelo en: " + archivoEncontrado.getAbsolutePath(), 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error I/O al intentar abrir el ticket: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // 6. Mostrar error si no se encuentra
            JOptionPane.showMessageDialog(this, 
                "El archivo del ticket (Venta N° VT" + String.format("%03d", idVenta) + ") no se pudo encontrar en la carpeta Tickets.", 
                "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jGestion = new javax.swing.JMenuItem();
        jBuscar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jRegistrar = new javax.swing.JMenuItem();
        jMostrar = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jReporteBajo = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jUsuarios = new javax.swing.JMenuItem();
        jGestionU = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jCerrar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        setUndecorated(true);

        jMenuBar1.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/carrito.png"))); // NOI18N
        jMenu1.setText("Productos");
        jMenu1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenu1.setPreferredSize(new java.awt.Dimension(150, 50));

        jGestion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo-producto.png"))); // NOI18N
        jGestion.setText("Gestion de Productos Nuevos");
        jGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGestionActionPerformed(evt);
            }
        });
        jMenu1.add(jGestion);

        jBuscar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/producto.png"))); // NOI18N
        jBuscar.setText("Busqueda y Edición ");
        jBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarActionPerformed(evt);
            }
        });
        jMenu1.add(jBuscar);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/historial1.png"))); // NOI18N
        jMenu2.setText("Ventas");
        jMenu2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenu2.setPreferredSize(new java.awt.Dimension(150, 50));

        jRegistrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo.png"))); // NOI18N
        jRegistrar.setText("Registrar Venta");
        jRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRegistrarActionPerformed(evt);
            }
        });
        jMenu2.add(jRegistrar);

        jMostrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jMostrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reportes.png"))); // NOI18N
        jMostrar.setText("Mostrar Ticket");
        jMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMostrarActionPerformed(evt);
            }
        });
        jMenu2.add(jMostrar);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reporte1.png"))); // NOI18N
        jMenu3.setText("Reportes ");
        jMenu3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenu3.setPreferredSize(new java.awt.Dimension(150, 50));

        jReporteBajo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jReporteBajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/categorias.png"))); // NOI18N
        jReporteBajo.setText("Reporte General");
        jReporteBajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jReporteBajoActionPerformed(evt);
            }
        });
        jMenu3.add(jReporteBajo);

        jMenuBar1.add(jMenu3);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cliente.png"))); // NOI18N
        jMenu4.setText("Usuarios");
        jMenu4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenu4.setPreferredSize(new java.awt.Dimension(150, 50));

        jUsuarios.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N
        jUsuarios.setText("Registrar Usuarios");
        jUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUsuariosActionPerformed(evt);
            }
        });
        jMenu4.add(jUsuarios);

        jGestionU.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jGestionU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo-cliente.png"))); // NOI18N
        jGestionU.setText("Gestion de USuarios");
        jGestionU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGestionUActionPerformed(evt);
            }
        });
        jMenu4.add(jGestionU);

        jMenuBar1.add(jMenu4);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/configuraciones.png"))); // NOI18N
        jMenu5.setText("Configuraciòn");
        jMenu5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenu5.setPreferredSize(new java.awt.Dimension(150, 50));

        jCerrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrar-sesion.png"))); // NOI18N
        jCerrar.setText("Cerrar Sesión");
        jCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCerrarActionPerformed(evt);
            }
        });
        jMenu5.add(jCerrar);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGestionActionPerformed
        // TODO add your handling code here:
        abrirInternalCentrado(new GestionProductos());
    }//GEN-LAST:event_jGestionActionPerformed

    private void jBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBuscarActionPerformed
        // TODO add your handling code here:
        abrirInternalCentrado(new ListadoProductos());
    }//GEN-LAST:event_jBuscarActionPerformed

    private void jCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCerrarActionPerformed
    // 1. Limpiar los datos del usuario en memoria
    Controllers.SesionService.logout();
    
    // 2. Abrir la ventana de Login
    FrmLogin login = new FrmLogin(); // Asegúrate de que este sea el nombre de tu clase de Login
    login.setVisible(true);
    
    // 3. Cerrar solo el menú actual
    this.dispose();
    }//GEN-LAST:event_jCerrarActionPerformed

    private void jMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMostrarActionPerformed
        abrirInternalCentrado(new MostrarTicket());
    }//GEN-LAST:event_jMostrarActionPerformed

    private void jUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsuariosActionPerformed
        // TODO add your handling code here:
        abrirInternalCentrado(new RegistrarU());
    }//GEN-LAST:event_jUsuariosActionPerformed

    private void jRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRegistrarActionPerformed
                                     
    // Cambiamos getUsuario() por getUsername()
    abrirInternalCentrado(new RegistroVentas(usuario.getId(), usuario.getUsername(), this.jDesktopPane_menu));

    }//GEN-LAST:event_jRegistrarActionPerformed

    private void jReporteBajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jReporteBajoActionPerformed
        // TODO add your handling code here:
        abrirInternalCentrado(new ReporteGeneral());
    }//GEN-LAST:event_jReporteBajoActionPerformed

    private void jGestionUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGestionUActionPerformed
     // Accedemos al ID del objeto 'usuario' que cargaste en el constructor
    int idParaElFrame = this.usuario.getId(); 
    
    // Llamamos a tu método de centrado pasando la nueva instancia con el ID
    abrirInternalCentrado(new GestionUsuarios(idParaElFrame));
    }//GEN-LAST:event_jGestionUActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       /* Configurar Look & Feel opcional */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    /* Abrir el login */
    java.awt.EventQueue.invokeLater(() -> {
        new FrmLogin().setVisible(true);
    });
      
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jBuscar;
    private javax.swing.JMenuItem jCerrar;
    private javax.swing.JMenuItem jGestion;
    private javax.swing.JMenuItem jGestionU;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMostrar;
    private javax.swing.JMenuItem jRegistrar;
    private javax.swing.JMenuItem jReporteBajo;
    private javax.swing.JMenuItem jUsuarios;
    // End of variables declaration//GEN-END:variables
}
