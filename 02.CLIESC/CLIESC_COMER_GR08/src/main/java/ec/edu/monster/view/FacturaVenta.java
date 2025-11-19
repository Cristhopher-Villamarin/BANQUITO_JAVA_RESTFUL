package ec.edu.monster.view;

import ec.edu.monster.model.DetalleFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class FacturaVenta extends JFrame {

    private static final Logger logger =
            Logger.getLogger(FacturaVenta.class.getName());

    // UI
    private JPanel rootPanel;
    private JLabel lblClienteValor;
    private JLabel lblCedulaValor;
    private JLabel lblFechaValor;
    private JLabel lblFormaPagoValor;
    private JLabel lblEstadoValor;
    private JLabel lblSubtotalValor;
    private JLabel lblDescuentoValor;
    private JLabel lblTotalValor;
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;

    // Constructor para uso real
    public FacturaVenta(String nombreCliente,
                        String cedula,
                        String formaPago,
                        String fecha,
                        String estado,
                        BigDecimal subtotal,
                        BigDecimal descuento,
                        BigDecimal total,
                        List<DetalleFactura> detalles) {
        initComponents();
        setDatosCabecera(nombreCliente, cedula, formaPago, fecha, estado);
        setTotales(subtotal, descuento, total);
        cargarDetalles(detalles);
    }

    // Constructor sin datos (p.ej. para probar rápido)
    public FacturaVenta() {
        this("Cliente demo",
             "0000000000",
             "EFECTIVO",
             "2025-01-01",
             "PAGADA",
             BigDecimal.ZERO,
             BigDecimal.ZERO,
             BigDecimal.ZERO,
             java.util.Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("Factura de venta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initInfoPanel();
        initDetallesPanel();
        initTotalesPanel();
        initBotonera();
    }

    // HEADER ------------------------------------------------------------------
    private void initHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(new Color(10, 30, 60));
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        header.setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setBackground(new Color(10, 30, 60));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("COMPROBANTE DE VENTA");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Factura");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblSub = new JLabel("Detalle de la operación registrada.");
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        left.add(lblCategoria);
        left.add(Box.createVerticalStrut(6));
        left.add(lblTitulo);
        left.add(Box.createVerticalStrut(6));
        left.add(lblSub);

        JPanel right = new JPanel();
        right.setBackground(new Color(10, 30, 60));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel lblBadge = new JLabel("VENTA EXITOSA");
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(0, 170, 120));
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        lblBadge.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(lblBadge);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        rootPanel.add(header);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    // INFO CABECERA -----------------------------------------------------------
    private void initInfoPanel() {
        JPanel info = new JPanel();
        info.setBackground(new Color(10, 18, 34));
        info.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        info.setLayout(new GridLayout(2, 3, 24, 10));

        lblClienteValor   = crearItemInfo(info, "CLIENTE");
        lblCedulaValor    = crearItemInfo(info, "CÉDULA");
        lblFechaValor     = crearItemInfo(info, "FECHA");
        lblFormaPagoValor = crearItemInfo(info, "FORMA DE PAGO");
        lblEstadoValor    = crearItemInfo(info, "ESTADO");
        // 6ta celda vacía para respirar
        info.add(new JLabel());

        rootPanel.add(info);
        rootPanel.add(Box.createVerticalStrut(18));
    }

    private JLabel crearItemInfo(JPanel parent, String titulo) {
        JPanel p = new JPanel();
        p.setBackground(new Color(10, 18, 34));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(160, 175, 190));
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblValor = new JLabel("-");
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 14));

        p.add(lblTitulo);
        p.add(Box.createVerticalStrut(4));
        p.add(lblValor);

        parent.add(p);
        return lblValor;
    }

    // DETALLES ----------------------------------------------------------------
    private void initDetallesPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(10, 18, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panel.setLayout(new BorderLayout(0, 10));

        JLabel lbl = new JLabel("Productos comprados");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel("Incluye cantidades, precios unitarios y subtotales.");
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel top = new JPanel();
        top.setBackground(new Color(10, 18, 34));
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(lbl);
        top.add(Box.createVerticalStrut(3));
        top.add(lblSub);

        panel.add(top, BorderLayout.NORTH);

        modeloDetalles = new DefaultTableModel(
                new String[]{"Producto", "Cantidad", "P. Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setBackground(new Color(10, 18, 34));
        tablaDetalles.setForeground(Color.WHITE);
        tablaDetalles.setGridColor(new Color(30, 40, 60));
        tablaDetalles.setRowHeight(34);
        tablaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalles.setSelectionBackground(new Color(20, 35, 60));
        tablaDetalles.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaDetalles.getTableHeader();
        header.setBackground(new Color(15, 25, 45));
        header.setForeground(new Color(180, 190, 205));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

        JScrollPane scroll = new JScrollPane(tablaDetalles);
        scroll.getViewport().setBackground(new Color(10, 18, 34));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(10));
    }

    // TOTALES -----------------------------------------------------------------
    private void initTotalesPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(8, 16, 32));
        panel.setLayout(new BorderLayout());

        JPanel inner = new JPanel();
        inner.setBackground(new Color(10, 18, 34));
        inner.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        inner.setLayout(new GridLayout(3, 2, 10, 6));

        lblSubtotalValor  = crearItemTotal(inner, "SUBTOTAL");
        lblDescuentoValor = crearItemTotal(inner, "DESCUENTO");
        lblTotalValor     = crearItemTotal(inner, "TOTAL A PAGAR");

        panel.add(inner, BorderLayout.EAST);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(15));
    }

    private JLabel crearItemTotal(JPanel parent, String titulo) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(160, 175, 190));
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblValor = new JLabel("$0.00", SwingConstants.RIGHT);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));

        parent.add(lblTitulo);
        parent.add(lblValor);
        return lblValor;
    }

    // BOTONES -----------------------------------------------------------------
    private void initBotonera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(new Color(8, 16, 32));

        JButton btnCerrar = new JButton("CERRAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrar.setFocusPainted(false);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(60, 70, 90));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btnCerrar.setOpaque(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnCerrar);
        rootPanel.add(panel);
    }

    // -------------------------------------------------------------------------
    // Métodos para cargar datos
    // -------------------------------------------------------------------------
    private void setDatosCabecera(String nombreCliente,
                                  String cedula,
                                  String formaPago,
                                  String fecha,
                                  String estado) {
        lblClienteValor.setText(nombreCliente != null ? nombreCliente : "-");
        lblCedulaValor.setText(cedula != null ? cedula : "-");
        lblFormaPagoValor.setText(formaPago != null ? formaPago : "-");
        lblFechaValor.setText(fecha != null ? fecha : "-");
        lblEstadoValor.setText(estado != null ? estado : "-");
    }

    private void setTotales(BigDecimal subtotal, BigDecimal descuento, BigDecimal total) {
        lblSubtotalValor.setText(formatearMonto(subtotal));
        lblDescuentoValor.setText(formatearMonto(descuento));
        lblTotalValor.setText(formatearMonto(total));
    }

    private String formatearMonto(BigDecimal valor) {
        if (valor == null) return "$0.00";
        return "$" + valor.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private void cargarDetalles(List<DetalleFactura> detalles) {
        modeloDetalles.setRowCount(0);
        if (detalles == null) return;

        for (DetalleFactura d : detalles) {
            String producto = d.getNombreElectrodomestico();
            String cantidad = d.getCantidad() != null ? d.getCantidad().toString() : "0";
            BigDecimal precio = d.getPrecioUnitario();
            BigDecimal subtotalLinea = d.getSubtotal();

            modeloDetalles.addRow(new Object[]{
                    producto,
                    cantidad,
                    formatearMonto(precio),
                    formatearMonto(subtotalLinea)
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaVenta().setVisible(true));
    }
}
