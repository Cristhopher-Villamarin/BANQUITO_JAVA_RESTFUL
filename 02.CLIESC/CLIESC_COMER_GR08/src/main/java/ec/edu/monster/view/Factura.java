package ec.edu.monster.view;

import ec.edu.monster.service.FacturaService;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Factura extends JFrame {

    private static final Logger logger = Logger.getLogger(Factura.class.getName());

    // Campos de búsqueda
    private JTextField txtCedula;
    private JButton btnBuscar;
    private JButton btnVerDetalles;

    // Tablas
    private JTable tableFacturas;
    private JTable tableDetalles;
    private DefaultTableModel tableModel;
    private DefaultTableModel detalleTableModel;

    // Servicio
    private FacturaService facturaService;
    private List<ec.edu.monster.model.Factura> facturasActuales;

    // Root panels
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;

    public Factura() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("Consulta de facturas");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        facturaService = new FacturaService();

        // ---------------- ROOT ----------------
        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initContentPanel();
    }

    // ---------------------------------------------------------------
    // HEADER (similar estilo a las demás pantallas)
    // ---------------------------------------------------------------
    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 30, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setLayout(new BorderLayout());
        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(20));

        // IZQUIERDA: títulos + búsqueda
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(10, 30, 60));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("CONSULTA DE FACTURAS");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Consulta de facturas");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel lblSub = new JLabel("Ingrese la cédula del cliente para listar sus facturas.");
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        leftPanel.add(lblCategoria);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(lblTitulo);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(lblSub);
        leftPanel.add(Box.createVerticalStrut(15));

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBackground(new Color(10, 30, 60));

        JLabel lblCedula = new JLabel("Cédula del cliente");
        lblCedula.setForeground(new Color(170, 180, 195));
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Estilo de textfield
        txtCedula = new JTextField(18);
        txtCedula.setBackground(new Color(15, 20, 35));
        txtCedula.setForeground(Color.WHITE);
        txtCedula.setCaretColor(Color.WHITE);
        Border inner = BorderFactory.createEmptyBorder(6, 10, 6, 10);
        Border line = BorderFactory.createLineBorder(new Color(70, 90, 130), 1);
        txtCedula.setBorder(BorderFactory.createCompoundBorder(line, inner));

        // Botón BUSCAR
        btnBuscar = new JButton("BUSCAR");
        btnBuscar.setFocusPainted(false);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(0, 160, 255));
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Botón VER DETALLES
        btnVerDetalles = new JButton("VER DETALLES");
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.setForeground(Color.WHITE);
        btnVerDetalles.setBackground(new Color(18, 32, 60));
        btnVerDetalles.setBorder(BorderFactory.createLineBorder(new Color(80, 100, 180)));
        btnVerDetalles.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        searchPanel.add(lblCedula);
        searchPanel.add(Box.createHorizontalStrut(12));
        searchPanel.add(txtCedula);
        searchPanel.add(Box.createHorizontalStrut(12));
        searchPanel.add(btnBuscar);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(btnVerDetalles);

        leftPanel.add(searchPanel);

        // DERECHA: botón VOLVER AL MENÚ
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(10, 30, 60));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton btnVolverMenu = new JButton("VOLVER AL MENÚ");
        btnVolverMenu.setFocusPainted(false);
        btnVolverMenu.setForeground(Color.WHITE);
        btnVolverMenu.setBackground(new Color(18, 32, 60));
        btnVolverMenu.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        btnVolverMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolverMenu.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(btnVolverMenu);
        rightPanel.add(Box.createVerticalGlue());

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Acciones
        btnBuscar.addActionListener(e -> buscarFacturas());
        btnVerDetalles.addActionListener(e -> verDetallesFactura());
        btnVolverMenu.addActionListener(e -> {
            new userMenu().setVisible(true);
            dispose();
        });
    }

    // ---------------------------------------------------------------
    // CONTENIDO (tablas) – estilo oscuro como las otras vistas
    // ---------------------------------------------------------------
    private void initContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(10, 18, 34));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        rootPanel.add(contentPanel);

        // --------- Tabla facturas ----------
        JLabel lblFacturasTitulo = new JLabel("Facturas del cliente");
        lblFacturasTitulo.setForeground(Color.WHITE);
        lblFacturasTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPanel.add(lblFacturasTitulo);
        contentPanel.add(Box.createVerticalStrut(8));

        String[] columnNames = {"Fecha", "Forma de pago", "Subtotal", "Descuento", "Total", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableFacturas = new JTable(tableModel);
        tableFacturas.setBackground(new Color(10, 18, 34));
        tableFacturas.setForeground(Color.WHITE);
        tableFacturas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableFacturas.setGridColor(new Color(30, 40, 60));
        tableFacturas.setRowHeight(38);
        tableFacturas.setSelectionBackground(new Color(20, 35, 60));
        tableFacturas.setSelectionForeground(Color.WHITE);

        JTableHeader header1 = tableFacturas.getTableHeader();
        header1.setBackground(new Color(15, 25, 45));
        header1.setForeground(new Color(180, 190, 205));
        header1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header1.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

        JScrollPane scrollFacturas = new JScrollPane(tableFacturas);
        scrollFacturas.getViewport().setBackground(new Color(10, 18, 34));
        scrollFacturas.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        contentPanel.add(scrollFacturas);

        // --------- Tabla detalle ----------
        JLabel lblDetalleTitulo = new JLabel("Detalle de la factura seleccionada");
        lblDetalleTitulo.setForeground(Color.WHITE);
        lblDetalleTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPanel.add(lblDetalleTitulo);
        contentPanel.add(Box.createVerticalStrut(8));

        String[] detalleColumns = {"Producto", "Cantidad", "Precio", "Subtotal"};
        detalleTableModel = new DefaultTableModel(detalleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDetalles = new JTable(detalleTableModel);
        tableDetalles.setBackground(new Color(10, 18, 34));
        tableDetalles.setForeground(Color.WHITE);
        tableDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableDetalles.setGridColor(new Color(30, 40, 60));
        tableDetalles.setRowHeight(36);
        tableDetalles.setSelectionBackground(new Color(20, 35, 60));
        tableDetalles.setSelectionForeground(Color.WHITE);

        JTableHeader header2 = tableDetalles.getTableHeader();
        header2.setBackground(new Color(15, 25, 45));
        header2.setForeground(new Color(180, 190, 205));
        header2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

        JScrollPane scrollDetalles = new JScrollPane(tableDetalles);
        scrollDetalles.getViewport().setBackground(new Color(10, 18, 34));
        scrollDetalles.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        contentPanel.add(scrollDetalles);
    }

    // ---------------------------------------------------------------
    // LÓGICA
    // ---------------------------------------------------------------
    private void buscarFacturas() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del cliente",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            facturasActuales = facturaService.listarFacturasPorCedula(cedula);

            tableModel.setRowCount(0);
            detalleTableModel.setRowCount(0);

            if (facturasActuales == null || facturasActuales.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron facturas para la cédula proporcionada",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (ec.edu.monster.model.Factura f : facturasActuales) {
                String fecha = f.getFecha();
                String formaPago = f.getFormaPago();
                String subtotal = f.getSubtotal() != null ? f.getSubtotal().toPlainString() : "0.00";
                String descuento = f.getDescuento() != null ? f.getDescuento().toPlainString() : "0.00";
                String total = f.getTotal() != null ? f.getTotal().toPlainString() : "0.00";
                String estado = f.getEstado();

                tableModel.addRow(new Object[]{fecha, formaPago, subtotal, descuento, total, estado});
            }

        } catch (IOException | ParseException ex) {
            logger.log(Level.SEVERE, "Error al listar facturas", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar facturas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetallesFactura() {
        int selectedRow = tableFacturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una factura de la tabla",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (facturasActuales == null || facturasActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay facturas cargadas",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ec.edu.monster.model.Factura facturaSeleccionada = facturasActuales.get(selectedRow);
        Integer idFactura = facturaSeleccionada.getId();
        if (idFactura == null) {
            JOptionPane.showMessageDialog(this,
                    "La factura seleccionada no tiene un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<ec.edu.monster.model.DetalleFactura> detalles =
                    facturaService.listarDetallesPorFactura(idFactura);

            detalleTableModel.setRowCount(0);

            if (detalles == null || detalles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La factura seleccionada no tiene detalles",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (ec.edu.monster.model.DetalleFactura d : detalles) {
                String producto = d.getNombreElectrodomestico();
                String cantidad = d.getCantidad() != null ? d.getCantidad().toString() : "0";
                String precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario().toPlainString() : "0.00";
                String subtotalLinea = d.getSubtotal() != null ? d.getSubtotal().toPlainString() : "0.00";

                detalleTableModel.addRow(new Object[]{producto, cantidad, precio, subtotalLinea});
            }

        } catch (IOException | ParseException ex) {
            logger.log(Level.SEVERE, "Error al obtener detalle de factura", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al obtener detalle de la factura: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------------
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Factura().setVisible(true));
    }
}
