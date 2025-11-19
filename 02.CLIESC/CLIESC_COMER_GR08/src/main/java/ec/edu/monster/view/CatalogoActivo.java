package ec.edu.monster.view;

import ec.edu.monster.controller.ElectrodomesticoController;
import ec.edu.monster.model.Electrodomestico;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CatalogoActivo extends JFrame {

    private static final Logger logger = Logger.getLogger(CatalogoActivo.class.getName());

    private final ElectrodomesticoController controller = new ElectrodomesticoController();

    // UI
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel tablaPanel;
    private JLabel lblTotalProductos;

    private JTable tabla;
    private DefaultTableModel modelo;

    public CatalogoActivo() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Catálogo activo de electrodomésticos");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initTablaPanel();
    }

    // -------------------------------------------------------------------------
    // HEADER
    // -------------------------------------------------------------------------
    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 30, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setLayout(new BorderLayout());

        // Izquierda: textos y tarjetas
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(10, 30, 60));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblMarca = new JLabel("COMERCIALIZADORA MONSTER");
        lblMarca.setForeground(new Color(160, 175, 190));
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Catálogo activo de electrodomésticos");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblSub = new JLabel(
                "Actualiza tu cotización con precios reales y disponibilidad inmediata."
        );
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        leftPanel.add(lblMarca);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(lblTitulo);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(lblSub);
        leftPanel.add(Box.createVerticalStrut(18));

        // Tarjetas de info (productos / USD)
        JPanel cardsPanel = new JPanel();
        cardsPanel.setBackground(new Color(10, 30, 60));
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));

        // Card productos
        JPanel cardProductos = new JPanel();
        cardProductos.setBackground(new Color(15, 25, 50));
        cardProductos.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        cardProductos.setLayout(new BoxLayout(cardProductos, BoxLayout.Y_AXIS));
        cardProductos.setPreferredSize(new Dimension(220, 80));

        lblTotalProductos = new JLabel("0");
        lblTotalProductos.setForeground(Color.WHITE);
        lblTotalProductos.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblProdSub = new JLabel("Productos vigentes");
        lblProdSub.setForeground(new Color(180, 190, 205));
        lblProdSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardProductos.add(lblTotalProductos);
        cardProductos.add(Box.createVerticalStrut(5));
        cardProductos.add(lblProdSub);

        // Card USD
        JPanel cardUsd = new JPanel();
        cardUsd.setBackground(new Color(15, 25, 50));
        cardUsd.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        cardUsd.setLayout(new BoxLayout(cardUsd, BoxLayout.Y_AXIS));
        cardUsd.setPreferredSize(new Dimension(260, 80));

        JLabel lblUsdTitulo = new JLabel("USD");
        lblUsdTitulo.setForeground(Color.WHITE);
        lblUsdTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel lblUsdSub = new JLabel("<html>Precios expresados en dólares<br/>americanos</html>");
        lblUsdSub.setForeground(new Color(180, 190, 205));
        lblUsdSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardUsd.add(lblUsdTitulo);
        cardUsd.add(Box.createVerticalStrut(5));
        cardUsd.add(lblUsdSub);

        cardsPanel.add(cardProductos);
        cardsPanel.add(cardUsd);

        leftPanel.add(cardsPanel);

        // Derecha: botón REGRESAR AL MENÚ + texto
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(10, 30, 60));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton btnRegresar = new JButton("REGRESAR AL MENÚ");
        btnRegresar.setFocusPainted(false);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBackground(new Color(18, 32, 60));
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        btnRegresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegresar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnRegresar.addActionListener(e -> {
            new userMenu().setVisible(true);
            dispose();
        });

        JLabel lblSync = new JLabel("Datos sincronizados con inventario central.");
        lblSync.setForeground(new Color(180, 190, 205));
        lblSync.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSync.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(btnRegresar);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(lblSync);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(25));
    }

    // -------------------------------------------------------------------------
    // TABLA
    // -------------------------------------------------------------------------
    private void initTablaPanel() {
        tablaPanel = new JPanel();
        tablaPanel.setBackground(new Color(10, 18, 34));
        tablaPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        tablaPanel.setLayout(new BorderLayout());

        // Encabezados de la sección
        JPanel top = new JPanel();
        top.setBackground(new Color(10, 18, 34));
        top.setLayout(new BorderLayout());

        JLabel lblTablaTitulo = new JLabel("Electrodomésticos disponibles");
        lblTablaTitulo.setForeground(Color.WHITE);
        lblTablaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblFiltro = new JLabel("Filtra por ID al momento de registrar una venta");
        lblFiltro.setForeground(new Color(180, 190, 205));
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFiltro.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel lblTablaMaestra = new JLabel("TABLA MAESTRA");
        lblTablaMaestra.setForeground(new Color(160, 175, 190));
        lblTablaMaestra.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        top.add(lblTablaMaestra, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout());
        mid.setBackground(new Color(10, 18, 34));
        mid.add(lblTablaTitulo, BorderLayout.WEST);
        mid.add(lblFiltro, BorderLayout.EAST);

        top.add(mid, BorderLayout.CENTER);

        tablaPanel.add(top, BorderLayout.NORTH);

        // Modelo con columna FOTO
        modelo = new DefaultTableModel(new String[]{"ID", "NOMBRE", "PRECIO", "FOTO"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo lectura
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(new Color(10, 18, 34));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(30, 40, 60));
        tabla.setRowHeight(110); // más alto para la imagen
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(20, 35, 60));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(15, 25, 45));
        header.setForeground(new Color(180, 190, 205));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

        // Renderer para la columna FOTO
        tabla.getColumnModel().getColumn(3).setCellRenderer(new FotoRenderer());
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(10, 18, 34));
        scroll.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        tablaPanel.add(scroll, BorderLayout.CENTER);

        rootPanel.add(tablaPanel);
    }

    // -------------------------------------------------------------------------
    // RENDERER PARA FOTO
    // -------------------------------------------------------------------------
    private static class FotoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, "", isSelected, hasFocus, row, column);

            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(null);
            label.setOpaque(true);
            label.setBackground(isSelected ? new Color(20, 35, 60) : new Color(10, 18, 34));

            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
                label.setText("");
            } else {
                label.setIcon(null);
                label.setText("SIN IMAGEN");
                label.setForeground(new Color(160, 175, 190));
            }

            return label;
        }
    }

    // -------------------------------------------------------------------------
    // CARGAR DATOS
    // -------------------------------------------------------------------------
    private void cargarDatos() {
        modelo.setRowCount(0);
        try {
            List<Electrodomestico> lista = controller.listar();

            for (Electrodomestico e : lista) {
                BigDecimal precio = e.getPrecioVenta();
                String precioStr = "";
                if (precio != null) {
                    precioStr = "$" + precio.toString();
                }

                // Cargar icono desde fotoUrl (si existe)
                ImageIcon icon = null;
                try {
                    String fotoUrl = e.getFotoUrl(); // <--- asegúrate de tener este getter
                    if (fotoUrl != null && !fotoUrl.isBlank()) {
                        URL url = new URL(fotoUrl);
                        BufferedImage img = ImageIO.read(url);
                        if (img != null) {
                            Image scaled = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaled);
                        }
                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "No se pudo cargar imagen para id "
                            + e.getIdElectrodomestico(), ex);
                }

                modelo.addRow(new Object[]{
                        e.getIdElectrodomestico(),
                        e.getNombre(),
                        precioStr,
                        (icon != null) ? icon : "SIN_IMAGEN"
                });
            }

            lblTotalProductos.setText(String.valueOf(lista.size()));

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al cargar catálogo", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar catálogo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------------------------------------------------------------
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

        java.awt.EventQueue.invokeLater(() -> new CatalogoActivo().setVisible(true));
    }
}
