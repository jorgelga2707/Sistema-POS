/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package UI;
    
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.VentaDAO;
import dao.ProductoDAO;
import model.Venta;
import model.DetalleVenta;
import model.Producto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.swing.JDesktopPane;



 /*
 * @author Jorge
 */
public class RegistroVentas extends javax.swing.JInternalFrame {
    // --- VARIABLES DE CLASE Y OBJETOS DAO ---
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private DefaultTableModel modeloTabla;
    private DefaultTableModel modeloTablaBusqueda; // Modelo para tblBuscar
    private final DecimalFormat df = new DecimalFormat("0.00");
    
        

    // Datos de la Venta en curso
    private List<DetalleVenta> detallesVenta = new ArrayList<>();
    private double totalVenta = 0.0;

    private int idVendedor;
    private final String nombreVendedor;
    private final JDesktopPane escritorio; 
    
   
    public RegistroVentas(int idVendedor, String nombreVendedor, JDesktopPane escritorio) {
        // Inicializa el modelo de la tabla ANTES de initComponents para que se use el modeloTablaBusqueda
        modeloTablaBusqueda = new DefaultTableModel() {
            @Override
            
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        initComponents();
        //COLOR DE FONDO Internal
        this.getContentPane().setBackground(new Color(255,204,255));
        
        // Color de fondo para tblBuscar 
        tblBuscar.setBackground(new java.awt.Color(204,204,255));
        tblBuscar.getParent().setBackground(new java.awt.Color(204, 204, 255));
        
        // Color de fondo para tblDetalle
        tblDetalle.getTableHeader().setBackground(new java.awt.Color(204,204,255));
        tblDetalle.getParent().setBackground(new java.awt.Color(204,204,255));
        
      
        // 1. ASIGNACIÓN DE VARIABLES
        this.idVendedor = idVendedor; 
        this.nombreVendedor = nombreVendedor;
        this.escritorio = escritorio;
          
        // 2. INICIALIZACIÓN DE TABLAS
        inicializarTablaDetalle();
        inicializarTablaBusqueda(); 
        
        // 3. CARGA INICIAL (Para ver productos al abrir)     
        cargarDatosIniciales();
    }
        
       private void inicializarTablaDetalle() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Cambiamos "ID Prod" por "CÓDIGO"
        modeloTabla.setColumnIdentifiers(new Object[]{"Código", "Nombre", "Cantidad", "Precio Unit.", "Subtotal"});
        tblDetalle.setModel(modeloTabla); 
    }
    
    private void inicializarTablaBusqueda() {
        modeloTablaBusqueda.setColumnIdentifiers(new Object[]{"Código", "Nombre", "Precio", "Stock", "Categoría"});
        tblBuscar.setModel(modeloTablaBusqueda);
    }
    
    private void cargarDatosIniciales() {
    // 1. Carga el nombre del vendedor
    txtVendedor.setText(this.nombreVendedor);
    
    // 2. Crear formatos y obtener fecha actual
    SimpleDateFormat dfFecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
    Date ahora = new Date();
    
    // 3. ASIGNAR LOS VALORES A LOS CAMPOS (Esto es lo que te falta)
    txtFecha.setText(dfFecha.format(ahora)); // <--- Agrega esta línea
    txtHora.setText(dfHora.format(ahora));   // <--- Agrega esta línea
    
    // 3. ID TIPO BOLETA (Aquí usamos el nuevo método)
    String idBoleta = ventaDAO.obtenerIdVentaFormateado();
    txtID2.setText(idBoleta);
    txtTotal.setText(df.format(0.0));
    
    // 5. Cargar tabla
    filtrarProductosBusqueda();
    
    //6. iniciar en el comboxd
    jComboBox1.requestFocusInWindow();
    
    txtID2.setFocusable(false);
    txtVendedor.setFocusable(false);
    txtFecha.setFocusable(false);
    txtHora.setFocusable(false);
    txtTotal.setFocusable(false);
    
    // Centrar el texto en los campos superiores
    txtID2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtVendedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtHora.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    }  
    
   private void filtrarProductosBusqueda() {
    String categoriaSeleccionada = (String) jComboBox1.getSelectedItem();
    modeloTablaBusqueda.setRowCount(0); // Inicia vacía

    if (categoriaSeleccionada == null || categoriaSeleccionada.equals("---- SELECCIONE  ----")) {
        return; 
    }

    try {
        List<Producto> productos = productoDAO.listarPorCategoria(categoriaSeleccionada);
        for (Producto p : productos) {
            // ORDEN: Código | Nombre | Precio | Stock | Categoría
            modeloTablaBusqueda.addRow(new Object[]{
                p.getCodigo(),   // Columna 0
                p.getNombre(),   // Columna 1
                p.getPrecio(),   // Columna 2
                p.getStock(),    // Columna 3
                p.getCategoria() // Columna 4
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
    
   private void agregarProductoATabla(int idProducto, int cantidad) {
        try {
        Producto p = productoDAO.buscarPorId(idProducto);

        if (p == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (p.getStock() < cantidad) {
             JOptionPane.showMessageDialog(this, "Stock insuficiente. Stock actual: " + p.getStock(), "Advertencia", JOptionPane.WARNING_MESSAGE);
             return;
        }

        double subtotal = p.getPrecio() * cantidad;

        DetalleVenta d = new DetalleVenta();
        d.setIdProducto(idProducto);
        d.setCantidad(cantidad);
        d.setPrecio(p.getPrecio()); 
        d.setSubtotal(subtotal);
        d.setNombreProducto(p.getNombre()); 

        detallesVenta.add(d);

        // --- MEJORA: MAYÚSCULAS EN LA SEGUNDA TABLA ---
        modeloTabla.addRow(new Object[]{
            p.getCodigo().toUpperCase(),      // Código en MAYÚSCULAS
            p.getNombre().toUpperCase(),      // Nombre en MAYÚSCULAS
            d.getCantidad(),
            df.format(d.getPrecio()),
            df.format(d.getSubtotal())
        });

        actualizarTotalVenta(subtotal);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
    }
}
   
   
   private void actualizarTotalVenta(double monto) {
        totalVenta += monto;
        txtTotal.setText(df.format(totalVenta)); // AHORA FUNCIONARÁ
    }
   
    private void registrarVenta() {
       if (detallesVenta.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay productos para registrar la venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 1. CAPTURAR EL ID FORMATEADO: Obtenemos el texto (ej. "V009") del campo de la interfaz
    String idVentaFormateado = txtID2.getText();

    int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Desea registrar esta venta por un total de S/ " + df.format(totalVenta) + "?", 
            "Confirmar Venta", JOptionPane.YES_NO_OPTION);

    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }

    Venta venta = new Venta();
    venta.setFecha(new Date());
    venta.setTotal(totalVenta);
    venta.setIdVendedor(this.idVendedor); 

    // --- 1. PROCESAR TRANSACCIÓN ---
    if (ventaDAO.procesarTransaccionVenta(venta, detallesVenta)) {

        // --- 2. GENERAR TICKET ---
        if (venta.getId() > 0) { 
             generarTicketTxt(venta);
        } else {
             JOptionPane.showMessageDialog(this, "Venta registrada, pero ID no obtenida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        // 2. MOSTRAR EL ID FORMATEADO: Aquí usamos la variable que capturamos arriba
        JOptionPane.showMessageDialog(this, "Venta registrada exitosamente. ID: " + idVentaFormateado, "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // --- 3. ACTUALIZACIÓN INMEDIATA ---
        limpiarRegistro();      
        cargarDatosIniciales(); // Actualiza el txtID2 con el siguiente número (V010)

    } else {
        JOptionPane.showMessageDialog(this, "ERROR: La venta no pudo ser registrada.", "Error de Transacción", JOptionPane.ERROR_MESSAGE);
    }
   }
   
   private void generarTicketTxt(Venta venta) {
       
        String idFormateado = String.format("VT%03d", venta.getId());
        String nombreArchivo = "Ticket_" + idFormateado + ".txt";
        File directorioTickets = new File("Tickets");

        if (!directorioTickets.exists()) {
            directorioTickets.mkdirs();
        }

        File archivo = new File(directorioTickets, nombreArchivo);

        try (FileWriter fw = new FileWriter(archivo);
             PrintWriter pw = new PrintWriter(fw)) {

            // --- CÁLCULOS DE IMPUESTOS ---
            // totalVenta = subtotal + igv -> total / 1.18 nos da el neto
            double subtotalNeto = venta.getTotal() / 1.18;
            double igv = venta.getTotal() - subtotalNeto;

            SimpleDateFormat dfFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");

            // --- Encabezado Profesional ---
            pw.println("========================================");
            pw.println("           NOMBRE DE TU EMPRESA         ");
            pw.println("       RUC: 20123456789 - LIMA, PERU    ");
            pw.println("========================================");
            pw.println("BOLETA ELECTRÓNICA: " + idFormateado);
            pw.println("Fecha: " + dfFecha.format(venta.getFecha()) + "   Hora: " + dfHora.format(venta.getFecha()));
            pw.println("Vendedor: " + this.nombreVendedor);
            pw.println("----------------------------------------");

            // --- Detalle de Productos (Alineación Mejorada) ---
            // %-4s (Cant), %-18s (Prod), %-8s (P.Unit), %8s (Subt)
            pw.printf("%-4s %-18s %-8s %8s\n", "CNT", "PRODUCTO", "P.UNIT", "TOTAL");
            pw.println("----------------------------------------");

            for (DetalleVenta d : detallesVenta) {
                // Precio unitario calculado: subtotal / cantidad
                double precioUnitario = d.getSubtotal() / d.getCantidad();

                // Cortar nombre si es muy largo para no romper la tabla
                String nombreProd = d.getNombreProducto();
                if (nombreProd.length() > 18) nombreProd = nombreProd.substring(0, 15) + "...";

                pw.printf("%-4d %-18s %-8.2f %8.2f\n", 
                        d.getCantidad(), 
                        nombreProd, 
                        precioUnitario, 
                        d.getSubtotal());
            }

            // --- Totales y Pie ---
            pw.println("----------------------------------------");
            pw.printf("%-25s S/ %10.2f\n", "OP. GRAVADA (Subtotal):", subtotalNeto);
            pw.printf("%-25s S/ %10.2f\n", "IGV (18%):", igv);
            pw.printf("%-25s S/ %10.2f\n", "TOTAL A PAGAR:", venta.getTotal());
            pw.println("========================================");
            pw.println("      ¡GRACIAS POR PREFERIRNOS!         ");
            pw.println("     Conserve su ticket para reclamos   ");
            pw.println("========================================");



        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al generar boleta: " + e.getMessage());
        }
    }
   
   
    private void quitarProductoSeleccionado(){
    int fila = tblDetalle.getSelectedRow();
    
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione el producto que desea retirar de la venta.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. Confirmación del usuario
    int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro de retirar este producto de la lista?", "Confirmar", JOptionPane.YES_NO_OPTION);
    
    if (respuesta == JOptionPane.YES_OPTION) {
        try {
            // 3. OBTENER EL SUBTOTAL DE LA FILA (Columna 4: Subtotal)
            // Usamos replace y parse para convertir el "S/ 10.00" a número
            String subtotalStr = modeloTabla.getValueAt(fila, 4).toString()
                                 .replace(",", ".").replace("S/", "").trim();
            double subtotalARestar = Double.parseDouble(subtotalStr);

            // 4. ACTUALIZAR TOTAL LÓGICO Y VISUAL
            totalVenta -= subtotalARestar;
            txtTotal.setText(df.format(totalVenta));

            // 5. ELIMINAR DE LA LISTA LÓGICA (detallesVenta)
            detallesVenta.remove(fila);

            // 6. ELIMINAR DE LA TABLA VISUAL
            modeloTabla.removeRow(fila);

            JOptionPane.showMessageDialog(this, "Producto retirado.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al recalcular el total: " + e.getMessage());
            }
        }
    }
    
    private void cancelarVentaTotal() {
     // 1. Verificar si hay algo que cancelar
    if (detallesVenta.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay una venta activa para cancelar.");
        return;
    }

    // 2. Pedir confirmación al usuario
    int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de que desea cancelar la venta actual?\nSe borrarán todos los productos añadidos.", 
            "Confirmar Cancelación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
    );

    // 3. Si responde que SÍ, reiniciamos todo
    if (confirmacion == JOptionPane.YES_OPTION) {
        limpiarRegistro(); // Borra la tabla y la lista de productos
        cargarDatosIniciales(); // Refresca ID, fecha y hora
        
        JOptionPane.showMessageDialog(this, "Venta cancelada correctamente.");
        }       
    }
    
    private void agregarProductoSeleccionado(){
    int fila = tblBuscar.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // 1. Extraer el código de la tabla celeste
        String codigoProducto = modeloTablaBusqueda.getValueAt(fila, 0).toString();
        
        // 2. PEDIR CANTIDAD (El segundo parámetro es null para que el cuadro esté VACÍO)
        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad a vender:", "Entrada de Cantidad", JOptionPane.QUESTION_MESSAGE);
        
        // VALIDACIÓN: Si el usuario cancela o deja vacío, NO añadir nada
        if (cantStr == null || cantStr.trim().isEmpty()) {
            return; 
        }
        
        int cantidad = Integer.parseInt(cantStr.trim());
        
        // Validación de cantidad positiva
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
            return;
        }
        
        // 3. Buscar el producto para obtener el ID y el Precio
        Producto p = productoDAO.buscarPorCodigo(codigoProducto); 
        
        if (p != null) {
            // Llamamos a tu método que ya tienes programado
            agregarProductoATabla(p.getId(), cantidad);
        }
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error: Debe ingresar un número entero válido para la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
        }
    }
    
  
    private void limpiarRegistro() {
       // 1. Limpiar lista lógica y tabla visual de ventas
    detallesVenta.clear();
    modeloTabla.setRowCount(0);
    
    // 2. Reiniciar el acumulador del total y el campo de texto
    totalVenta = 0.0;       
    txtTotal.setText(df.format(0.0));
    
    // 3. REGRESAR EL COMBO AL ESTADO INICIAL
    jComboBox1.setSelectedIndex(0); 
    
    // 4. Limpiar la tabla de búsqueda (esto ya lo hace filtrarProductosBusqueda al ver que el combo es index 0)
    filtrarProductosBusqueda(); 
    
    // 5. Darle el foco nuevamente al combo para la siguiente venta
    jComboBox1.requestFocusInWindow();
    }
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        btnProcesar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtVendedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtID2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        btnProducto = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscar = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("TABLA DE DETALLE DE VENTAS:");

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblDetalle);

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotal.setText("TOTAL:  S/.");

        btnProcesar.setBackground(new java.awt.Color(255, 153, 255));
        btnProcesar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProcesar.setText("PROCESAR VENTA");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo.setText("Modulo de Ventas");

        txtTotal.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("ID VENTA :");

        txtVendedor.setEditable(false);
        txtVendedor.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("FECHA :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("VENDEDOR :");

        txtID2.setEditable(false);
        txtID2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("HORA :");

        txtHora.setEditable(false);
        txtHora.setBackground(new java.awt.Color(204, 204, 255));

        txtFecha.setEditable(false);
        txtFecha.setBackground(new java.awt.Color(204, 204, 255));

        btnProducto.setBackground(new java.awt.Color(255, 153, 255));
        btnProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProducto.setText("AÑADIR PRODUCTO");
        btnProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoActionPerformed(evt);
            }
        });

        btnBorrar.setBackground(new java.awt.Color(255, 153, 255));
        btnBorrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBorrar.setText("BORRAR PRODUCTO");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---- SELECCIONE  ----", "Abarrotes", "Bebidas", "Snacks", "Panaderia", "Cuidado Personal", "Limpieza" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("CATEGORIA :");

        tblBuscar.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblBuscar);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("TABLA AÑADIR PRODUCTOS:");

        btnCancelar.setBackground(new java.awt.Color(255, 153, 255));
        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelar.setText("CANCELAR VENTA");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtVendedor)
                                    .addComponent(txtID2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(61, 61, 61)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 25, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(txtHora)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane2)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitulo)
                                .addGap(253, 253, 253))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(121, 121, 121)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnProcesar)
                                        .addGap(175, 175, 175)))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(59, 59, 59))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitulo)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        registrarVenta();
    }//GEN-LAST:event_btnProcesarActionPerformed

    private void btnProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoActionPerformed
        agregarProductoSeleccionado();
    }//GEN-LAST:event_btnProductoActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
     // 1. Verificar si hay una fila seleccionada en la tabla de detalle
        quitarProductoSeleccionado(); 
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        filtrarProductosBusqueda();    
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelarVentaTotal(); 
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JButton btnProducto;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblBuscar;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtID2;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables
}
