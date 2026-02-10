/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;
import dao.ProductoDAO;
import java.awt.Color;
import model.Producto;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Jorge
 */
public class ListadoProductos extends javax.swing.JInternalFrame {
        // 1. REFERENCIA AL FORMULARIO DE GESTIÓN (Para evitar el error de variable inexistente)
    private GestionProductos frmGestion;

    // 2. MODELO DE TABLA DE SOLO LECTURA
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Bloquea edición directa. Solo se edita con el botón.
        }
    };
    /**
     * Creates new form ListadoProductos
     */
    public ListadoProductos() {
        initComponents();
        configurarVentana();
        configurarTabla();     
    }
    
    public void setGestionForm(GestionProductos gestion) {
        this.frmGestion = gestion;
    }
    
    private void configurarVentana() {
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        this.getContentPane().setBackground(new Color(153, 255, 255));       
    }
    
    private void configurarTabla() {
        if (modelo.getColumnCount() == 0) {
            modelo.addColumn("Código");
            modelo.addColumn("Nombre");
            modelo.addColumn("Descripción");
            modelo.addColumn("Categoría");
            modelo.addColumn("Precio");
            modelo.addColumn("Stock");
        }
        tblProductos.setModel(modelo);
        tblProductos.setBackground(new Color(0, 204, 204));
    }
    
    
    

   private void listarProductos(String filtro) {
        modelo.setRowCount(0);
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = (filtro == null || filtro.isEmpty()) 
                ? dao.listar() 
                : dao.buscarProductos(filtro);

        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getCodigo(), p.getNombre(), p.getDescripcion(), 
                p.getCategoria(), p.getPrecio(), p.getStock()
            });
        }
    }
    
    private void editarProductos() {
        int fila = tblProductos.getSelectedRow();

    if (fila != -1) {
        try {
            // 1. Obtener datos actuales de la fila seleccionada
            String codigo = modelo.getValueAt(fila, 0).toString();
            String nombreActual = modelo.getValueAt(fila, 1).toString();
            String descActual = modelo.getValueAt(fila, 2).toString();
            String precioActual = modelo.getValueAt(fila, 4).toString();
            String stockActual = modelo.getValueAt(fila, 5).toString();

            // 2. Pedir nuevos datos mediante JOptionPanes
            String nuevoNombre = JOptionPane.showInputDialog(this, "Editar Nombre:", nombreActual);
            if (nuevoNombre == null) return; // Si cancela, sale

            String nuevaDesc = JOptionPane.showInputDialog(this, "Editar Descripción:", descActual);
            if (nuevaDesc == null) return;

            String nuevoPrecio = JOptionPane.showInputDialog(this, "Editar Precio:", precioActual);
            if (nuevoPrecio == null) return;

            String nuevoStock = JOptionPane.showInputDialog(this, "Editar Stock:", stockActual);
            if (nuevoStock == null) return;

            // 3. Crear el objeto Producto con los nuevos datos
            Producto p = new Producto();
            p.setCodigo(codigo);
            p.setNombre(nuevoNombre);
            p.setDescripcion(nuevaDesc);
            p.setCategoria(modelo.getValueAt(fila, 3).toString()); // Mantiene la categoría original
            p.setPrecio(Double.parseDouble(nuevoPrecio));
            p.setStock(Integer.parseInt(nuevoStock));

            // 4. Guardar en la Base de Datos a través del DAO
            ProductoDAO dao = new ProductoDAO();
            if (dao.actualizar(p)) {
                JOptionPane.showMessageDialog(this, "¡Producto actualizado con éxito!");
                listarProductos(null); // Refresca la tabla para ver los cambios
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Precio y Stock deben ser números válidos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla para editar.");
    }
}
    
    private void ejecutarEliminacion() {
        int fila = tblProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        String codigo = modelo.getValueAt(fila, 0).toString();
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar " + codigo + "?") == JOptionPane.YES_OPTION) {
            if (new ProductoDAO().eliminar(codigo)) {
                listarProductos(null);
            }
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

        txtProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblTitulo = new javax.swing.JLabel();
        btnMostrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        btnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        txtProducto.setBackground(new java.awt.Color(204, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("TABLA DE PRODUCTOS :");

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo.setText("GESTION DE PRODUCTOS");

        btnMostrar.setBackground(new java.awt.Color(0, 204, 204));
        btnMostrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMostrar.setText("MOSTRAR TOTAL DE PRODUCTOS");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        tblProductos.setBackground(new java.awt.Color(153, 255, 255));
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblProductos);

        btnBuscar.setBackground(new java.awt.Color(0, 204, 204));
        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBuscar.setText("BUSCAR PRODUCTO");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("PRODUCTO :");

        btnEditar.setBackground(new java.awt.Color(0, 204, 204));
        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(0, 204, 204));
        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
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
                        .addGap(244, 244, 244)
                        .addComponent(lblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(130, 130, 130)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lblTitulo)
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
    listarProductos(null);
        JOptionPane.showMessageDialog(this, "Total: " + modelo.getRowCount());    
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    listarProductos(txtProducto.getText().trim());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
    editarProductos();
    
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        ejecutarEliminacion();
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtProducto;
    // End of variables declaration//GEN-END:variables
}
