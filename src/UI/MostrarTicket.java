/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.VentaDAO;
import java.awt.Color;
import model.Venta;
import java.util.List;
import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;


/*
 * @author Jorge
 */
public class MostrarTicket extends javax.swing.JInternalFrame {
    
    private FormMenu menuPrincipal;
    // Inicialización de DAOs
    private final VentaDAO ventaDAO = new VentaDAO();
    private DefaultTableModel modeloTabla;
    private final DecimalFormat df = new DecimalFormat("0.00");
    
    // Formatos de Fecha y Hora (Variables de clase para consistencia)
    private final SimpleDateFormat dfFecha = new SimpleDateFormat("yyyy/MM/dd");
    private final SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
    /**
     * Creates new form MostrarTicket
     */
    public MostrarTicket() {
        initComponents();
        inicializarTabla();
        //COLOR DE FONDO
        this.getContentPane().setBackground(new Color(0,153,204));
        tabMostrar.getParent().setBackground(new java.awt.Color(102,204,255));
        tabMostrar.setBackground(new java.awt.Color(102,204,255));
        
       
        txtVendedor.setHorizontalAlignment(SwingConstants.CENTER);
        txtHora.setHorizontalAlignment(SwingConstants.CENTER);
        txtFecha.setHorizontalAlignment(SwingConstants.CENTER);
        txtID2.setHorizontalAlignment(SwingConstants.CENTER);
        
        modeloTabla.setRowCount(0); // Aseguramos que la tabla inicie limpia
        cargarUltimoTicketEnCampos();
        
        // 2.  Evitar que el cursor parpadee en el ID al iniciar
        txtID2.setFocusable(false);
        txtFecha.setFocusable(false);
        txtVendedor.setFocusable(false);
        txtHora.setFocusable(false);
        
        tabMostrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tabMostrarMouseClicked(evt);
            }
        });        
            btnListar.addActionListener(e -> buscarVentasFiltradas()); // Nuevo método
            btnArchivo.addActionListener(this::btnArchivoActionPerformed);
        }
    
        private void inicializarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.setColumnIdentifiers(new Object[]{"ID VENTA", "FECHA", "HORA", "VENDEDOR", "TOTAL"});
        tabMostrar.setModel(modeloTabla); 
        }
        
        private void limpiarCamposSuperiores() {
        txtID2.setText("");
        txtFecha.setText("");
        txtVendedor.setText("");
        txtHora.setText("");
        }
        
        // AÑADIR: Lógica para llenar campos desde una fila seleccionada
        private void llenarCamposDesdeFila(int fila) {
            if (fila >= 0 && fila < modeloTabla.getRowCount()) {
                String idVenta = modeloTabla.getValueAt(fila, 0).toString();
                String fecha = modeloTabla.getValueAt(fila, 1).toString();
                String hora = modeloTabla.getValueAt(fila, 2).toString();
                String vendedor = modeloTabla.getValueAt(fila, 3).toString();

                txtID2.setText(idVenta); 
                txtFecha.setText(fecha);
                txtHora.setText(hora);
                txtVendedor.setText(vendedor);
            } else {
                limpiarCamposSuperiores();
            }
        }

        private void cargarTodasLasVentas() {
        modeloTabla.setRowCount(0); 
        try {
        List<Venta> listaVentas = ventaDAO.listarTodasLasVentas(); 

        for (Venta v : listaVentas) {
            Object[] fila = new Object[5];
            
            // --- CAMBIO AQUÍ: Formatear ID a V001 ---
            fila[0] = String.format("V%03d", v.getId()); 
            
            fila[1] = dfFecha.format(v.getFecha());
            fila[2] = dfHora.format(v.getFecha());
            fila[3] = v.getNombreVendedor();
            fila[4] = df.format(v.getTotal());
            modeloTabla.addRow(fila);
        }
        
        if (tabMostrar.getRowCount() > 0) {
            tabMostrar.setRowSelectionInterval(0, 0); 
            llenarCamposDesdeFila(0); 
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
        }
    }
    
    // 3. El método de filtrado 
    private void buscarVentasFiltradas() {
    
    String idVentaStr = txtID2.getText().trim();
    String fechaStr = txtFecha.getText().trim();
    String vendedorStr = txtVendedor.getText().trim();
    
    Integer idVenta = null;
    
    if (!idVentaStr.isEmpty()) {
        // Quitamos cualquier letra "V" o "v" para que el Integer no falle
        String idSoloNumeros = idVentaStr.toUpperCase().replace("V", "");
        
        // Verificamos si después de quitar la V realmente quedan números
        if (idSoloNumeros.matches("\\d+")) { 
            idVenta = Integer.valueOf(idSoloNumeros);
        }
        // Si no son números, el idVenta se queda como null y busca por los otros filtros 
        // sin lanzar el mensaje de error de "Número entero".
    }

    try {
        List<Venta> listaVentas = ventaDAO.obtenerVentasFiltradas(
            idVenta,
            fechaStr.isEmpty() ? null : fechaStr,
            vendedorStr.isEmpty() ? null : vendedorStr,
            null 
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        for (Venta v : listaVentas) {
            Object[] fila = new Object[5];
            // Volvemos a poner la V solo para la vista de la tabla
            fila[0] = String.format("V%03d", v.getId()); 
            fila[1] = dateFormat.format(v.getFecha());
            fila[2] = timeFormat.format(v.getFecha());
            fila[3] = v.getNombreVendedor();
            fila[4] = df.format(v.getTotal());
            modeloTabla.addRow(fila);
        }
        
    } catch (Exception e) {
        System.err.println("Error en búsqueda: " + e.getMessage());
    }
}
    // CORRECCIÓN: Manejador de clic de la tabla
    private void tabMostrarMouseClicked(java.awt.event.MouseEvent evt) {                                        
        int fila = tabMostrar.getSelectedRow();
        if (fila != -1) {
        // Obtenemos el ID de la tabla (ejemplo: "V001")
        String idConV = tabMostrar.getValueAt(fila, 0).toString();
        
        // Seteamos los campos superiores
        txtID2.setText(idConV); // El ID se queda con la V para que se vea bien
        txtFecha.setText(tabMostrar.getValueAt(fila, 1).toString());
        txtVendedor.setText(tabMostrar.getValueAt(fila, 3).toString());
        txtHora.setText(tabMostrar.getValueAt(fila, 2).toString());
        }
    }

    private void cargarUltimoTicketEnCampos() {
            try {
                // Obtenemos el ID de la última venta desde el DAO
                int ultimoId = ventaDAO.obtenerUltimaIdVenta();

                if (ultimoId > 0) {
                    // Buscamos los detalles de esa venta específica
                    List<Venta> ventaUnica = ventaDAO.obtenerVentasFiltradas(ultimoId, null, null, null);

                    if (!ventaUnica.isEmpty()) {
                        Venta v = ventaUnica.get(0);

                        // --- CAMBIO AQUÍ PARA EL FORMATO V001 ---
                        String idFormateado = String.format("V%03d", v.getId());
                        txtID2.setText(idFormateado); 
                        // ----------------------------------------

                        txtFecha.setText(dfFecha.format(v.getFecha()));
                        txtHora.setText(dfHora.format(v.getFecha()));
                        txtVendedor.setText(v.getNombreVendedor());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al cargar datos iniciales del ticket: " + e.getMessage());
            }
        }
    private void procesarAperturaTicket(String idLimpio) {
        try {
            int idVenta = Integer.parseInt(idLimpio);
            abrirTicketPorID(idVenta);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID seleccionado no es válido.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTicketPorID(int idVenta) {
    // 1. Definimos el nombre exacto
    String nombreArchivo = String.format("Ticket_VT%03d.txt", idVenta);
    
    // 2. Buscamos la carpeta 'Tickets' relativa a donde se ejecuta el programa
    File carpeta = new File("Tickets");
    File archivo = new File(carpeta, nombreArchivo);

    // 3. Verificamos existencia antes de intentar abrir
    if (archivo.exists()) {
        try {
            Desktop.getDesktop().open(archivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el ticket: " + e.getMessage());
        }
    } else {
        // MENSAJE DE AYUDA: Si no lo encuentra, te dirá dónde lo está buscando
        JOptionPane.showMessageDialog(this, 
            "El archivo no existe en esta PC.\nRuta buscada: " + archivo.getAbsolutePath(), 
            "Archivo no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void gestionarVisualizacionTicket(){
    int fila = tabMostrar.getSelectedRow();

        if (fila != -1) {
            // Extraemos el valor de la columna ID (0) y limpiamos caracteres no numéricos
            String idBruto = tabMostrar.getValueAt(fila, 0).toString();
            String idLimpio = idBruto.replaceAll("[^0-9]", "");
            
            // Delegamos la apertura al método procesador
            procesarAperturaTicket(idLimpio);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione una venta de la tabla.", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
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

        btnListar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        txtVendedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtID2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabMostrar = new javax.swing.JTable();
        btnArchivo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        btnListar.setBackground(new java.awt.Color(102, 204, 255));
        btnListar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnListar.setText("MOSTRAR VENTAS");
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("ID VENTA :");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("VENDEDOR :");

        txtFecha.setEditable(false);
        txtFecha.setBackground(new java.awt.Color(102, 204, 255));

        txtVendedor.setEditable(false);
        txtVendedor.setBackground(new java.awt.Color(102, 204, 255));
        txtVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVendedorActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("FECHA :");

        txtID2.setEditable(false);
        txtID2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("HORA :");

        txtHora.setEditable(false);
        txtHora.setBackground(new java.awt.Color(102, 204, 255));

        tabMostrar.setBackground(new java.awt.Color(0, 255, 255));
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

        btnArchivo.setBackground(new java.awt.Color(102, 204, 255));
        btnArchivo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnArchivo.setText("MOSTRAR ARCHIVO");
        btnArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArchivoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("CONSULTA Y RE-IMPRESION  DE TICKETS DE VENTA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtVendedor)
                                    .addComponent(txtID2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(btnArchivo)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel15)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
    cargarTodasLasVentas();
          
    }//GEN-LAST:event_btnListarActionPerformed

    private void txtVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVendedorActionPerformed

    private void btnArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoActionPerformed
        gestionarVisualizacionTicket();
    }//GEN-LAST:event_btnArchivoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArchivo;
    private javax.swing.JButton btnListar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabMostrar;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtID2;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables
}
