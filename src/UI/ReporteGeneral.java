/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;

import dao.ProductoDAO;
import dao.VentaDAO;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Producto;
import model.Venta;

/**
 *
 * @author Jorge
 */
public class ReporteGeneral extends javax.swing.JInternalFrame {

    /**
     * Creates new form ReporteGeneral
     */
    public ReporteGeneral() {
        initComponents();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        this.getContentPane().setBackground(new Color(255,204,204));
        tabReportes.getParent().setBackground(new java.awt.Color(255,102,102));
        tabReportes.setBackground(new java.awt.Color(255,102,102));
        
        txtDia.setFocusable(false);
        txtDia.setEditable(false);
        txtDia.setHorizontalAlignment(javax.swing.JTextField.CENTER);       
        }
    
        private void configurarTablaStockBajo() {
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            modelo.addColumn("CÓDIGO"); 
            modelo.addColumn("NOMBRE");
            modelo.addColumn("STOCK");
            tabReportes.setModel(modelo);
        }

        private void configurarTablaVentasHoy() {
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            modelo.addColumn("ID VENTA");
            modelo.addColumn("FECHA / HORA");
            modelo.addColumn("TOTAL S/");
            modelo.addColumn("VENDEDOR");
            tabReportes.setModel(modelo);
        }
        
        private void cargarProductosStockBajo() {
        // LLAMADA CLAVE: Asegura que la tabla tenga títulos correctos
        configurarTablaStockBajo();
        DefaultTableModel modelo = (DefaultTableModel) tabReportes.getModel();
        modelo.setRowCount(0);

        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listarStockBajo(10);

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos con stock crítico (menor a 10 unidades).");
        } else {
            for (Producto p : lista) {
                modelo.addRow(new Object[]{ p.getCodigo(), p.getNombre(), p.getStock() });
            }
        }
        // Limpiamos el total del día ya que esto es stock, no ventas
        txtDia.setText("---");
        }
        
        
        private void ejecutarReporteVentasHoy() {
        // Obtenemos el modelo y limpiamos
        configurarTablaVentasHoy();
        
        DefaultTableModel modelo = (DefaultTableModel) tabReportes.getModel();
        modelo.setRowCount(0); 

        VentaDAO vDao = new VentaDAO();
        List<Venta> lista = vDao.listarVentasHoy();
        double sumaTotal = 0;

        for (Venta v : lista) {
            String idFormateado = String.format("V%03d", v.getId());
            modelo.addRow(new Object[]{
                idFormateado, 
                v.getFecha(), 
                String.format("%.2f", v.getTotal()), 
                v.getNombreVendedor()
            });
            sumaTotal += v.getTotal();
        }

        // Encapsulamos la actualización del total en la UI
        txtDia.setText(String.format("%.2f", sumaTotal));

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se registraron ventas hoy.");
            }
        }
        
        
        private void exportarReporteStockBajo() {
        // Cambiamos la validación para que busque "Código" en lugar de "ID"
        if (tabReportes.getRowCount() == 0 || !tabReportes.getColumnName(0).equalsIgnoreCase("Código")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor visualizar el 'Inventario Stock Bajo' antes de guardar el reporte.", 
                "Acción Requerida", 
                JOptionPane.WARNING_MESSAGE);
            return; 
        }

        // 2. Configurar Carpeta y Archivo
        File directorio = new File("Reporte Stock Bajo");
        if (!directorio.exists()) directorio.mkdirs();

        String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        File archivo = new File(directorio, "Stock_Bajo_" + fecha + ".txt");

        // 3. ESCRITURA CON ALINEACIÓN
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(archivo, false))) {
        pw.println("      REPORTE DE PRODUCTOS CON STOCK BAJO");
        pw.println("==================================================");
        
        String fechaHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        pw.println(" Ultima Actualización: " + fechaHora);
        pw.println("--------------------------------------------------");
        
        // Cabecera profesional
        pw.printf(" %-12s | %-22s | %-8s %n", "CÓDIGO", "PRODUCTO", "STOCK");
        pw.println("--------------------------------------------------");

        for (int i = 0; i < tabReportes.getRowCount(); i++) {
            // AJUSTE DE ÍNDICES: 
            // 0 = Código, 1 = Nombre, 2 = Stock (Ya no hay ID)
            Object vCod = tabReportes.getValueAt(i, 0); 
            Object vNom = tabReportes.getValueAt(i, 1); 
            Object vSto = tabReportes.getValueAt(i, 2); 
            
            if (vCod != null && vNom != null && vSto != null) {
                // %-22.22s trunca nombres largos para mantener la estética
                pw.printf(" %-12s | %-22.22s | %-8s %n", 
                    vCod.toString(), 
                    vNom.toString(), 
                    vSto.toString());
            }
        }
        
        pw.println("--------------------------------------------------");
        pw.printf(" TOTAL PRODUCTOS EN CRITICIDAD: %d %n", tabReportes.getRowCount());
        pw.println("==================================================");
        
        
        JOptionPane.showMessageDialog(this, "El reporte de Stock Criticos ha sido actualizado correctamente.");

        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void generarArchivoReporteDiario() {
        // 1. VALIDACIÓN: Evitar guardar si la tabla está vacía o no es la correcta
        if (tabReportes.getRowCount() == 0 || !tabReportes.getColumnName(0).equalsIgnoreCase("ID VENTA")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor visualizar las 'Ventas Diarias' antes de guardar el reporte.", 
                "Acción Requerida", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Configurar Carpeta y Archivo (Crea un archivo nuevo por cada día)
        File directorio = new File("Reporte Ventas Diarias");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        File archivo = new File(directorio, "Venta_Dia_" + fecha + ".txt");

        // 3. LÓGICA DE ESCRITURA
        // Usamos false para sobrescribir y tener siempre la versión más reciente del día
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(archivo, false))) {

            // Encabezados profesionales
            pw.println("            REPORTE OFICIAL DE VENTAS ");
            pw.println("==============================================");
            pw.println("Fecha de Emisión: " + fecha);
            pw.println("Ultima Actualización: " + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
            pw.println("----------------------------------------------");
            pw.printf("%-10s | %-12s | %-15s%n", "TICKET", "TOTAL S/", "VENDEDOR");
            pw.println("----------------------------------------------");

            for (int i = 0; i < tabReportes.getRowCount(); i++) {
                try {
                    // Obtenemos datos de las celdas
                    String idTicket = tabReportes.getValueAt(i, 0).toString();
                    String vendedor = tabReportes.getValueAt(i, 3).toString();

                    // Limpieza y conversión segura del monto (Aquí fallaba antes)
                    String totalRaw = tabReportes.getValueAt(i, 2).toString()
                                        .replace("S/", "")
                                        .replace(",", ".")
                                        .trim();

                    double totalNum = Double.parseDouble(totalRaw);
                    String totalFormateado = String.format("S/ %.2f", totalNum);

                    // Escribir fila en el archivo
                    pw.printf("%-10s | %-12s | %-15s%n", idTicket, totalFormateado, vendedor);

                } catch (Exception e) {
                    // Si una fila está mal, se anota el error en el TXT pero el programa SIGUE
                    pw.println(">>> ERROR EN FILA " + (i + 1) + ": Datos incompletos o formato inválido.");
                }
            }

            pw.println("----------------------------------------------");
            // Tomamos el total general del campo de texto
            pw.printf("%-20s TOTAL GEN: S/ %s%n", "", txtDia.getText());
            pw.println("==============================================");

            JOptionPane.showMessageDialog(this, "El reporte de ventas del día ha sido actualizado correctamente.");

            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, "Error crítico al escribir el archivo: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        
        private void abrirCarpetaProyecto() {
         // TODO add your handling code here:
        try {
        // Obtenemos la ruta donde se ejecuta el programa
        File ruta = new File("."); 
        String path = ruta.getAbsolutePath();
        
        // Verificamos si el escritorio es soportado por el sistema
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Abre el explorador de archivos en la carpeta del proyecto
            desktop.open(new File(path));
        } else {
            JOptionPane.showMessageDialog(this, "Operación no soportada en este sistema operativo.");
        }
        } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error al abrir la carpeta: " + ex.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tabReportes = new javax.swing.JTable();
        btnMostrar = new javax.swing.JButton();
        btnDia = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtDia = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        BtnCarpeta = new javax.swing.JButton();
        btnGuardarbajo = new javax.swing.JButton();
        btnGuardardia = new javax.swing.JButton();

        tabReportes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabReportes);

        btnMostrar.setBackground(new java.awt.Color(255, 102, 102));
        btnMostrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMostrar.setText("MOSTRAR INVENTARIO STOCK BAJO");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        btnDia.setBackground(new java.awt.Color(255, 102, 102));
        btnDia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDia.setText("MOSTRAR VENTA DIARIAS");
        btnDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("TOTAL DEL DIA :  S/");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("REPORTE GENERAL ");

        BtnCarpeta.setBackground(new java.awt.Color(255, 102, 102));
        BtnCarpeta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BtnCarpeta.setText("MOSTRAR CARPETAS");
        BtnCarpeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCarpetaActionPerformed(evt);
            }
        });

        btnGuardarbajo.setBackground(new java.awt.Color(255, 102, 102));
        btnGuardarbajo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardarbajo.setText("GUARDAR REPORTE STOCK BAJO");
        btnGuardarbajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarbajoActionPerformed(evt);
            }
        });

        btnGuardardia.setBackground(new java.awt.Color(255, 102, 102));
        btnGuardardia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardardia.setText("GUARDAR REPORTE DE VENTAS DEL DIA");
        btnGuardardia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardardiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(224, 224, 224)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnCarpeta, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDia, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMostrar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnGuardarbajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuardardia, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDia, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardarbajo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardardia, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCarpeta, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
    cargarProductosStockBajo();                            
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiaActionPerformed
     ejecutarReporteVentasHoy();                                                                           
    }//GEN-LAST:event_btnDiaActionPerformed

    private void BtnCarpetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCarpetaActionPerformed
    abrirCarpetaProyecto();
    }//GEN-LAST:event_BtnCarpetaActionPerformed

    private void btnGuardarbajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarbajoActionPerformed
    exportarReporteStockBajo();

    }//GEN-LAST:event_btnGuardarbajoActionPerformed

    private void btnGuardardiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardardiaActionPerformed
    generarArchivoReporteDiario();     
    }//GEN-LAST:event_btnGuardardiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCarpeta;
    private javax.swing.JButton btnDia;
    private javax.swing.JButton btnGuardarbajo;
    private javax.swing.JButton btnGuardardia;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabReportes;
    private javax.swing.JTextField txtDia;
    // End of variables declaration//GEN-END:variables
}
