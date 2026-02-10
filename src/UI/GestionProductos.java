/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;

import dao.ProductoDAO;
import java.awt.Color;
import javax.swing.*;
import model.Producto;
/*
/**
 *
 * @author Jorge
 */
public class GestionProductos extends javax.swing.JInternalFrame {
    private final String url = "jdbc:mysql://localhost:3306/tu_base";
    private final String user = "tu_usuario";
    private final String pass = "tu_contraseña";
    
    private void limpiarCampos() {
    // Campos de texto (JTextField/JTextArea)
    txtCodigo.setText("");
    txtNombre.setText("");
    txtDescripcion.setText("");
    txtPrecio.setText("");
    txtStock.setText("");
    txtCodigo.requestFocus(); 
    jComboBox1.setSelectedIndex(0);
    
    txtCodigo.setEditable(false);
    txtCodigo.setFocusable(false);
    }
    
    
    /**
     * Creates new form GestionProductos
     */
    public GestionProductos() {
        initComponents();
       
        //COLOR DE FONDO
        this.getContentPane().setBackground(new Color(119,119,211)); 
        
        // ASIGNAR VALIDACIONES (Esto evitará que escriban cualquier cosa)
        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            validarSoloNumeros(evt, txtStock, 5);
        }
    });

        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            validarSoloDecimales(evt, txtPrecio, 8);
        }
    });

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            // Solo letras y espacios, máximo 50 caracteres
            char c = evt.getKeyChar();
            if (!Character.isLetter(c) && !Character.isWhitespace(c) || txtNombre.getText().length() >= 50) {
                evt.consume();
            }
        }
    });
    }
    
    private void ejecutarRegistroProducto() {
 // 1. Limpiar espacios en blanco invisibles con trim()
    String nombre = txtNombre.getText().trim();
    String precioStr = txtPrecio.getText().trim();
    String stockStr = txtStock.getText().trim();

    if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || jComboBox1.getSelectedIndex() <= 0) {
        JOptionPane.showMessageDialog(this, "Error: No puede haber campos vacíos.");
        return;
    }

    try {
        double precio = Double.parseDouble(precioStr);
        int stock = Integer.parseInt(stockStr);

        // Validaciones de lógica real
        if (precio <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser mayor a cero.");
            return;
        }

        // Si pasa todo, recién creamos el objeto (Encapsulamiento)
        Producto pro = new Producto();
        pro.setCodigo(txtCodigo.getText());
        pro.setNombre(nombre);
        pro.setCategoria(jComboBox1.getSelectedItem().toString());
        pro.setDescripcion(txtDescripcion.getText().trim());
        pro.setPrecio(precio);
        pro.setStock(stock);

        if (new ProductoDAO().registrar(pro)) {
            JOptionPane.showMessageDialog(this, "Guardado con éxito.");
            limpiarCampos();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Solo se permiten números en Precio y Stock.");
    }
}
    
    // Método público para que ListadoProductos pueda "enviarle" el producto
    public void cargarDatosProducto(Producto p) {
        if (p != null) {
            // Llenamos los campos de texto con el objeto recibido
            txtCodigo.setText(p.getCodigo());
            txtNombre.setText(p.getNombre());
            txtDescripcion.setText(p.getDescripcion());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtStock.setText(String.valueOf(p.getStock()));

            // Seleccionamos la categoría en el combo
            jComboBox1.setSelectedItem(p.getCategoria());

            // Bloqueamos el código para que no se cambie (es la llave primaria)
            txtCodigo.setEditable(false);
            txtCodigo.setFocusable(false);
        }
    }
    
    // Método para validar que solo se ingresen números enteros (para Stock)
    private void validarSoloNumeros(java.awt.event.KeyEvent evt, JTextField campo, int limite) {
    char c = evt.getKeyChar();
    // Permite solo dígitos y la tecla de borrar
    if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
        evt.consume(); 
    }
    // Control de longitud máxima
    if (campo.getText().length() >= limite) {
        evt.consume();
    }
}

    // Método para validar decimales (para Precio)
    private void validarSoloDecimales(java.awt.event.KeyEvent evt, JTextField campo, int limite) {
    char c = evt.getKeyChar();
    // Permite números, un punto y la tecla de borrar
    if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
        evt.consume();
    }
    // Evita ingresar más de un punto decimal
    if (c == '.' && campo.getText().contains(".")) {
        evt.consume();
    }
    // Control de longitud máxima
    if (campo.getText().length() >= limite) {
        evt.consume();
        }
    }
    
    private void categoriasC(){
   if (jComboBox1.getSelectedIndex() <= 0) {
        txtCodigo.setText("");
        return;
    }

    String categoria = jComboBox1.getSelectedItem().toString();
    String prefijo = "";

    // Aplicamos tu nomenclatura específica
    switch (categoria) {
        case "Abarrotes":        prefijo = "A"; break;
        case "Bebidas":          prefijo = "B"; break;
        case "Panadería":        prefijo = "P"; break;
        case "Snacks":           prefijo = "S"; break;
        case "Cuidado Personal": prefijo = "C"; break;
        case "Limpieza":         prefijo = "LIM"; break; 
        default:                 prefijo = "PROD"; break;
    }

    // Llamamos al DAO para obtener el número exacto de la BD
    ProductoDAO dao = new ProductoDAO();
    int cantidadActual = dao.contarProductosPorCategoria(categoria);
    int siguiente = cantidadActual + 1;

    // %03d convierte el 6 en "006", el 10 en "010", etc.
    String nuevoCodigo = prefijo + String.format("%03d", siguiente);
    txtCodigo.setText(nuevoCodigo);
    
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        lblDescripcion = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblCategoria = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        txtNombre = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        lblStock = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo.setText("Gestion de Nuevos Productos");

        txtStock.setBackground(new java.awt.Color(204, 204, 255));
        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        lblDescripcion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDescripcion.setText("DESCRIPCION:");

        lblCodigo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCodigo.setText("CODIGO:");

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNombre.setText("NOMBRE:");

        lblCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCategoria.setText("CATEGORIA:");

        txtDescripcion.setBackground(new java.awt.Color(204, 204, 255));

        txtCodigo.setBackground(new java.awt.Color(204, 204, 255));

        txtNombre.setBackground(new java.awt.Color(204, 204, 255));

        lblPrecio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrecio.setText("PRECIO:");

        txtPrecio.setBackground(new java.awt.Color(204, 204, 255));
        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });

        lblStock.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStock.setText("STOCK:");

        jComboBox1.setBackground(new java.awt.Color(204, 204, 255));
        jComboBox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---- SELECCIONE  ----", "Nueva Categoria", "Bebidas", "Abarrotes", "Snacks", "Panadería", "Cuidado Personal", "Limpieza" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(153, 153, 255));
        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(153, 153, 255));
        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardar.setText("GUARDAR ");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGap(44, 44, 44)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblCodigo)
                        .addGap(48, 48, 48)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblCategoria)
                        .addGap(25, 25, 25)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblPrecio)
                                .addGap(50, 50, 50)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                                .addGap(28, 28, 28)
                                .addComponent(lblStock)
                                .addGap(18, 18, 18)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)))))
                .addGap(72, 72, 72))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addGap(122, 122, 122))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblTitulo)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCategoria)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescripcion)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrecio)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStock)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    categoriasC();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    ejecutarRegistroProducto();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
    limpiarCampos();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtStockActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
