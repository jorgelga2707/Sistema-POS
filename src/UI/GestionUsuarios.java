/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;

import dao.UsuarioDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Usuario;

/**
 *
 * @author Jorge
 */
public class GestionUsuarios extends javax.swing.JInternalFrame {
    private int idAdmin;    
    /**
     * Creates new form GestionUsuarios
     */
    public GestionUsuarios(int idLogueado) {
        initComponents();
        inicializarTablaUsuarios();

        // 1. Asignamos el ID a la variable global
        this.idAdmin = idLogueado;

        // 2. Establecemos el texto con el formato deseado (U001)
        // NOTA: No agregues otra línea de txtUsuario.setText después de esta.
        txtUsuario.setText(String.format("U%03d", idAdmin));

        // 3. Bloqueamos el campo para que sea informativo y no cambie
        txtUsuario.setEditable(false);
        txtUsuario.setFocusable(false);
        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        
        // 4. Estética de la tabla
        tabMostrar.setBackground(new java.awt.Color(0,255,204)); 
        tabMostrar.setFillsViewportHeight(true);
        this.getContentPane().setBackground(new java.awt.Color(0,153,153));
        
  
    }
    
    private void inicializarTablaUsuarios() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"ID USUARIO", "USUARIO", "CONTRASEÑA", "ROL"});
        tabMostrar.setModel(modelo);
    }
    
            private void listarUsuarios() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tabMostrar.getModel();
            modelo.setRowCount(0);
            UsuarioDAO dao = new UsuarioDAO();
            List<Usuario> lista = dao.listar();

            for (Usuario u : lista) {
                // Formateamos el ID para la tabla
                String idFormateado = String.format("U%03d", u.getId());
                modelo.addRow(new Object[]{
                    idFormateado, 
                    u.getUsername(),
                    "********",
                    u.getRol()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar: " + e.getMessage());
        }
    }

   private void ejecutarEdicion() {
   
    // 1. PRIMER PASO: Validar si hay una fila seleccionada
    int filaSeleccionada = tabMostrar.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        // Si no hay selección, mostramos advertencia y salimos del método con return
        JOptionPane.showMessageDialog(this, 
        "Debe seleccionar un Usuario en la tabla.", 
        "Aviso", // Aquí va el título de la ventanita
        JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. Si pasó la validación, procedemos con la lógica
    try {
        // Extraemos el ID del campo txtUsuario que se llenó al hacer clic en la tabla
        String idTexto = txtUsuario.getText().replace("U", "");
        int id = Integer.parseInt(idTexto);

        // Obtenemos el nombre actual de la tabla para mostrarlo en el cuadro de edición
        String nombreActual = tabMostrar.getValueAt(filaSeleccionada, 1).toString();

        // Ventanitas para capturar datos nuevos (con el nombre actual precargado)
        String nombre = (String) JOptionPane.showInputDialog(this, 
                "Nuevo nombre de usuario:", "Edición de Usuario", 
                JOptionPane.QUESTION_MESSAGE, null, null, nombreActual);
        
        if (nombre == null || nombre.trim().isEmpty()) return;

        String pass = JOptionPane.showInputDialog(this, "Nueva contraseña:");
        if (pass == null) return;

        // Selector de Rol
        Object[] opcionesRol = {"Administrador", "Vendedor"};
        String rolActual = tabMostrar.getValueAt(filaSeleccionada, 3).toString();
        
        Object seleccion = JOptionPane.showInputDialog(this, "Seleccione el rol:", "Rol", 
                            JOptionPane.QUESTION_MESSAGE, null, opcionesRol, rolActual);
        
        if (seleccion == null) return;
        String rol = seleccion.toString();

        // 3. Ejecutar actualización
        Usuario userEdit = new Usuario(id, nombre, pass, rol);
        UsuarioDAO dao = new UsuarioDAO();

        if (dao.actualizar(userEdit)) {
            JOptionPane.showMessageDialog(this, "¡Usuario U" + id + " actualizado con éxito!");
            listarUsuarios(); 
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al procesar la edición: " + e.getMessage());
    }
}
    
    

    private void ejecutarEliminacion() {
        int fila = tabMostrar.getSelectedRow();
        if (fila >= 0) {
            String idTexto = tabMostrar.getValueAt(fila, 0).toString();
            int id = Integer.parseInt(idTexto.replace("U", ""));

            if (id == this.idAdmin) {
                JOptionPane.showMessageDialog(this, "No puedes eliminar tu propia cuenta.");
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario " + idTexto + "?");
            if (confirmar == JOptionPane.YES_OPTION) {
                UsuarioDAO dao = new UsuarioDAO();
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                    listarUsuarios();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabMostrar = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnMostrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("GESTION DE USUARIOS");

        tabMostrar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabMostrar);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("ID USUARIO :");

        txtUsuario.setBackground(new java.awt.Color(0, 255, 204));

        btnMostrar.setBackground(new java.awt.Color(0, 255, 204));
        btnMostrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMostrar.setText("MOSTRAR USUARIOS");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(0, 255, 204));
        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEditar.setText("EDITAR USUARIOS");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(0, 255, 204));
        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR USUARIOS");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(30, 30, 30)
                                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(86, 86, 86)
                                    .addComponent(btnMostrar)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(btnEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
    listarUsuarios();
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    ejecutarEliminacion();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
    ejecutarEdicion();
    }//GEN-LAST:event_btnEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabMostrar;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
