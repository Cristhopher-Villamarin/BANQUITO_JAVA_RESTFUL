package ec.edu.monster.view;

import ec.edu.monster.controller.VentaController;
import ec.edu.monster.model.DetalleFactura;
import ec.edu.monster.model.DetalleVentaRequest;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;
import ec.edu.monster.service.FacturaService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Venta extends JFrame {

    private static final Logger logger = Logger.getLogger(Venta.class.getName());
    private static final String RUTA_IMAGENES = "imagenes" + File.separator + "electrodomesticos";

    private final VentaController controller = new VentaController();

    // raíz
    private JPanel rootPanel;

    // header
    private JLabel lblReferencias;
    private JLabel lblDescuento;

    // datos cliente
    private JTextField txtCedula;
    private JTextField txtNombre;

    // forma de pago
    private JRadioButton rbEfectivo;
    private JRadioButton rbCredito;
    private JTextField txtPlazoMeses;

    // catálogo en memoria
    private List<Electrodomestico> catalogo;

    // panel de tarjetas
    private JPanel cardsGrid;

    // tabla productos seleccionados
    private DefaultTableModel modelo;
    private JTable tabla;

    public Venta() {
           initComponents();
    cargarCatalogo();  
    }

    private void initComponents() {
        setTitle("Registrar nueva venta");
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Agregar scroll al rootPanel
        JScrollPane scrollPane = new JScrollPane(rootPanel);
        scrollPane.setBackground(new Color(8, 16, 32));
        scrollPane.getViewport().setBackground(new Color(8, 16, 32));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        setContentPane(scrollPane);

        initHeader();
        initContenido();
    }

    // HEADER ------------------------------------------------------------------
    private void initHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(10, 30, 60));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        header.setLayout(new BorderLayout());

        // Izquierda: textos y tarjetas
        JPanel left = new JPanel();
        left.setBackground(new Color(10, 30, 60));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("FLUJO COMERCIAL");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Registrar nueva venta");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblSub = new JLabel("Captura datos del cliente, define la forma de pago y agrega productos en una sola carrera.");
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        left.add(lblCategoria);
        left.add(Box.createVerticalStrut(8));
        left.add(lblTitulo);
        left.add(Box.createVerticalStrut(8));
        left.add(lblSub);
        left.add(Box.createVerticalStrut(18));

        // tarjetas
        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        cards.setBackground(new Color(10, 30, 60));

        JPanel cardRef = crearCard();
        lblReferencias = new JLabel("0");
        lblReferencias.setForeground(Color.WHITE);
        lblReferencias.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel lblRefSub = new JLabel("Referencias disponibles");
        lblRefSub.setForeground(new Color(180, 190, 205));
        lblRefSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cardRef.add(lblReferencias);
        cardRef.add(Box.createVerticalStrut(5));
        cardRef.add(lblRefSub);

        JPanel cardDesc = crearCard();
        lblDescuento = new JLabel("33%");
        lblDescuento.setForeground(Color.WHITE);
        lblDescuento.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel lblDesSub = new JLabel("Descuento en pago en efectivo");
        lblDesSub.setForeground(new Color(180, 190, 205));
        lblDesSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cardDesc.add(lblDescuento);
        cardDesc.add(Box.createVerticalStrut(5));
        cardDesc.add(lblDesSub);

        cards.add(cardRef);
        cards.add(cardDesc);

        left.add(cards);

        // Derecha: VOLVER AL MENÚ
        JPanel right = new JPanel();
        right.setBackground(new Color(10, 30, 60));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JButton btnVolver = new JButton("VOLVER AL MENÚ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnVolver.setFocusPainted(false);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(80, 40, 120));
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnVolver.setOpaque(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.addActionListener(e -> {
            new userMenu().setVisible(true);
            dispose();
        });

        right.add(btnVolver);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        rootPanel.add(header);
        rootPanel.add(Box.createVerticalStrut(25));
    }

    private JPanel crearCard() {
        JPanel p = new JPanel();
        p.setBackground(new Color(15, 25, 50));
        p.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(230, 80));
        return p;
    }

    // CONTENIDO ---------------------------------------------------------------
    private void initContenido() {
        // Panel grande para todo el formulario
        JPanel contenido = new JPanel();
        contenido.setBackground(new Color(8, 16, 32));
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        rootPanel.add(contenido);

        // fila superior: datos cliente + forma de pago
        JPanel filaSuperior = new JPanel(new GridLayout(1, 2, 20, 0));
        filaSuperior.setBackground(new Color(8, 16, 32));

        filaSuperior.add(crearPanelDatosCliente());
        filaSuperior.add(crearPanelFormaPago());

        contenido.add(filaSuperior);
        contenido.add(Box.createVerticalStrut(20));

        // productos
        contenido.add(crearPanelProductos());
    }

    // DATOS CLIENTE -----------------------------------------------------------
    private JPanel crearPanelDatosCliente() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(10, 18, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblSeccion = new JLabel("DATOS DEL CLIENTE");
        lblSeccion.setForeground(new Color(160, 175, 190));
        lblSeccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Identificación");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        panel.add(lblSeccion);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));

        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border line = BorderFactory.createLineBorder(new Color(60, 80, 120), 1);
        Border textBorder = BorderFactory.createCompoundBorder(line, inner);

        JLabel lblCedula = new JLabel("CÉDULA");
        lblCedula.setForeground(new Color(170, 180, 195));
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtCedula = new JTextField();
        txtCedula.setBackground(new Color(15, 20, 35));
        txtCedula.setForeground(Color.WHITE);
        txtCedula.setCaretColor(Color.WHITE);
        txtCedula.setBorder(textBorder);
        txtCedula.setPreferredSize(new Dimension(200, 35));
        txtCedula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblNombre = new JLabel("NOMBRE COMPLETO");
        lblNombre.setForeground(new Color(170, 180, 195));
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtNombre = new JTextField();
        txtNombre.setBackground(new Color(15, 20, 35));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBorder(textBorder);
        txtNombre.setPreferredSize(new Dimension(200, 35));
        txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panel.add(lblCedula);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtCedula);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtNombre);

        return panel;
    }

    // FORMA DE PAGO -----------------------------------------------------------
    private JPanel crearPanelFormaPago() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(10, 18, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblSeccion = new JLabel("CONDICIONES");
        lblSeccion.setForeground(new Color(160, 175, 190));
        lblSeccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Forma de pago");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        panel.add(lblSeccion);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));

        // tarjeta efectivo
        JPanel cardEfectivo = new JPanel();
        cardEfectivo.setBackground(new Color(15, 25, 50));
        cardEfectivo.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        cardEfectivo.setLayout(new BoxLayout(cardEfectivo, BoxLayout.Y_AXIS));

        rbEfectivo = new JRadioButton("Efectivo");
        rbEfectivo.setBackground(new Color(15, 25, 50));
        rbEfectivo.setForeground(Color.WHITE);
        rbEfectivo.setFocusPainted(false);

        JLabel lblEfectivoDesc = new JLabel("Aplica descuento automático del 33%.");
        lblEfectivoDesc.setForeground(new Color(180, 190, 205));
        lblEfectivoDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardEfectivo.add(rbEfectivo);
        cardEfectivo.add(Box.createVerticalStrut(5));
        cardEfectivo.add(lblEfectivoDesc);

        // tarjeta crédito
        JPanel cardCredito = new JPanel();
        cardCredito.setBackground(new Color(15, 25, 50));
        cardCredito.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        cardCredito.setLayout(new BoxLayout(cardCredito, BoxLayout.Y_AXIS));

        rbCredito = new JRadioButton("Crédito directo");
        rbCredito.setBackground(new Color(15, 25, 50));
        rbCredito.setForeground(Color.WHITE);
        rbCredito.setFocusPainted(false);

        JLabel lblCredDesc = new JLabel("Valida aprobación con Banquito y define plazo.");
        lblCredDesc.setForeground(new Color(180, 190, 205));
        lblCredDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardCredito.add(rbCredito);
        cardCredito.add(Box.createVerticalStrut(5));
        cardCredito.add(lblCredDesc);

        ButtonGroup group = new ButtonGroup();
        group.add(rbEfectivo);
        group.add(rbCredito);
        rbEfectivo.setSelected(true);

        panel.add(cardEfectivo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(cardCredito);
        panel.add(Box.createVerticalStrut(15));

        // plazo meses
        JLabel lblPlazo = new JLabel("PLAZO (MESES, SOLO CRÉDITO)");
        lblPlazo.setForeground(new Color(170, 180, 195));
        lblPlazo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border line = BorderFactory.createLineBorder(new Color(60, 80, 120), 1);
        Border textBorder = BorderFactory.createCompoundBorder(line, inner);

        txtPlazoMeses = new JTextField();
        txtPlazoMeses.setBackground(new Color(15, 20, 35));
        txtPlazoMeses.setForeground(Color.WHITE);
        txtPlazoMeses.setCaretColor(Color.WHITE);
        txtPlazoMeses.setBorder(textBorder);
        txtPlazoMeses.setPreferredSize(new Dimension(200, 35));
        txtPlazoMeses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPlazoMeses.setEnabled(false);

        rbEfectivo.addActionListener(e -> txtPlazoMeses.setEnabled(false));
        rbCredito.addActionListener(e -> txtPlazoMeses.setEnabled(true));

        panel.add(lblPlazo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtPlazoMeses);

        return panel;
    }

    // PRODUCTOS ---------------------------------------------------------------
    private JPanel crearPanelProductos() {
    JPanel panel = new JPanel();
    panel.setBackground(new Color(10, 18, 34));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel lblSeccion = new JLabel("PRODUCTOS");
    lblSeccion.setForeground(new Color(160, 175, 190));
    lblSeccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JLabel lblTitulo = new JLabel("Buscar y agregar referencias");
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

    JLabel lblDesc = new JLabel("Usa el selector para añadir productos con su cantidad.");
    lblDesc.setForeground(new Color(180, 190, 205));
    lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    panel.add(lblSeccion);
    panel.add(Box.createVerticalStrut(8));
    panel.add(lblTitulo);
    panel.add(Box.createVerticalStrut(4));
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(18));

    // ====== GRID DE CARDS (4 columnas + scroll interno) ======
    cardsGrid = new JPanel(new GridLayout(0, 4, 16, 16));
    cardsGrid.setBackground(new Color(10, 18, 34));

    JScrollPane scrollCards = new JScrollPane(cardsGrid);
    scrollCards.setPreferredSize(new Dimension(0, 260));
    scrollCards.getViewport().setBackground(new Color(10, 18, 34));
    scrollCards.setBorder(BorderFactory.createEmptyBorder());
    scrollCards.getHorizontalScrollBar().setUnitIncrement(16);
    scrollCards.getVerticalScrollBar().setUnitIncrement(16);

    panel.add(scrollCards);
    panel.add(Box.createVerticalStrut(20));

    // ====== TABLA DE PRODUCTOS SELECCIONADOS ======
    JLabel lblSubTabla = new JLabel("Productos seleccionados");
    lblSubTabla.setForeground(Color.WHITE);
    lblSubTabla.setFont(new Font("Segoe UI", Font.BOLD, 16));

    JLabel lblSubDesc = new JLabel("Ajusta cantidades o elimina referencias antes de procesar.");
    lblSubDesc.setForeground(new Color(180, 190, 205));
    lblSubDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    panel.add(lblSubTabla);
    panel.add(Box.createVerticalStrut(4));
    panel.add(lblSubDesc);
    panel.add(Box.createVerticalStrut(10));

    modelo = new DefaultTableModel(
            new String[]{"ID", "PRODUCTO", "PRECIO UNITARIO", "CANTIDAD", "TOTAL", "ACCIONES"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            // solo la columna de ACCIONES (botón eliminar)
            return column == 5;
        }
    };

    tabla = new JTable(modelo);
    tabla.setBackground(new Color(10, 18, 34));
    tabla.setForeground(Color.WHITE);
    tabla.setGridColor(new Color(30, 40, 60));
    tabla.setRowHeight(38);
    tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tabla.setSelectionBackground(new Color(20, 35, 60));
    tabla.setSelectionForeground(Color.WHITE);

    // ID + imagen pequeña en un círculo
    tabla.getColumnModel().getColumn(0).setCellRenderer(new IdConImagenRenderer());
    // Texto del producto
    tabla.getColumnModel().getColumn(1).setCellRenderer(new ElectrodomesticoTableRenderer());

    // Botón ELIMINAR
    tabla.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
    tabla.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

    JTableHeader header = tabla.getTableHeader();
    header.setBackground(new Color(15, 25, 45));
    header.setForeground(new Color(180, 190, 205));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

    JScrollPane scroll = new JScrollPane(tabla);
    scroll.getViewport().setBackground(new Color(10, 18, 34));
    scroll.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    panel.add(scroll);
    panel.add(Box.createVerticalStrut(15));

    // Botones inferiores (igual que antes)
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    panelBotones.setBackground(new Color(10, 18, 34));

    JButton btnCancelar = new JButton("CANCELAR") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    btnCancelar.setFocusPainted(false);
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.setBackground(new Color(60, 70, 90));
    btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btnCancelar.setPreferredSize(new Dimension(140, 40));
    btnCancelar.setOpaque(false);
    btnCancelar.setContentAreaFilled(false);
    btnCancelar.addActionListener(e -> {
        new userMenu().setVisible(true);
        dispose();
    });

    JButton btnProcesar = new JButton("PROCESAR VENTA") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    btnProcesar.setFocusPainted(false);
    btnProcesar.setForeground(Color.WHITE);
    btnProcesar.setBackground(new Color(0, 180, 255));
    btnProcesar.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
    btnProcesar.setPreferredSize(new Dimension(180, 40));
    btnProcesar.setOpaque(false);
    btnProcesar.setContentAreaFilled(false);
    btnProcesar.addActionListener(e -> procesarVenta());

    panelBotones.add(btnCancelar);
    panelBotones.add(btnProcesar);

    panel.add(panelBotones);

    return panel;
}

    // CARGAR CATÁLOGO ---------------------------------------------------------
    private void cargarCatalogo() {
        if (cardsGrid == null) {
            return;
        }

        cardsGrid.removeAll();

        try {
            catalogo = controller.obtenerCatalogo();

            if (catalogo != null) {
                for (Electrodomestico e : catalogo) {
                    // Las imágenes vienen desde fotoUrl (como en CatalogoActivo)
                    cardsGrid.add(crearCardProducto(e));
                }

                if (lblReferencias != null) {
                    lblReferencias.setText(String.valueOf(catalogo.size()));
                }
            } else if (lblReferencias != null) {
                lblReferencias.setText("0");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error cargando catálogo", ex);
            JOptionPane.showMessageDialog(this,
                    "Error cargando catálogo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private JPanel crearCardProducto(Electrodomestico e) {
    JPanel card = new JPanel();
    card.setBackground(new Color(15, 25, 50));
    card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setPreferredSize(new Dimension(200, 220));

    // ID
    JLabel lblId = new JLabel("ID #" + e.getIdElectrodomestico());
    lblId.setForeground(new Color(160, 175, 190));
    lblId.setFont(new Font("Segoe UI", Font.PLAIN, 11));

    // Imagen
    JLabel lblFoto = new JLabel();
    lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblFoto.setPreferredSize(new Dimension(120, 80));
    lblFoto.setMaximumSize(new Dimension(120, 80));
    lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
    lblFoto.setOpaque(true);
    lblFoto.setBackground(new Color(8, 16, 32));

    ImageIcon icon = null;
    try {
        String fotoUrl = e.getFotoUrl();
        if (fotoUrl != null && !fotoUrl.isBlank()) {
            URL url = new URL(fotoUrl);
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = img.getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);
            }
        }
    } catch (Exception ex) {
        logger.log(Level.WARNING, "No se pudo cargar imagen para id "
                + e.getIdElectrodomestico(), ex);
    }

    if (icon != null) {
        lblFoto.setIcon(icon);
    } else {
        lblFoto.setText("SIN IMAGEN");
        lblFoto.setForeground(new Color(140, 150, 170));
    }

    // nombre
    JLabel lblNombreProd = new JLabel("<html>" + e.getNombre() + "</html>");
    lblNombreProd.setForeground(Color.WHITE);
    lblNombreProd.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    // precio
    JLabel lblPrecioProd = new JLabel(
            e.getPrecioVenta() != null ? "$" + e.getPrecioVenta().toString() : "$0.00"
    );
    lblPrecioProd.setForeground(new Color(0, 200, 255));
    lblPrecioProd.setFont(new Font("Segoe UI", Font.BOLD, 13));

    // cantidad + botón
    JPanel filaCant = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    filaCant.setBackground(new Color(15, 25, 50));

    JSpinner sp = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    sp.setPreferredSize(new Dimension(60, 28));

    JButton btnAdd = new JButton("AÑADIR") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    btnAdd.setFocusPainted(false);
    btnAdd.setForeground(Color.WHITE);
    btnAdd.setBackground(new Color(0, 180, 255));
    btnAdd.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
    btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btnAdd.setOpaque(false);
    btnAdd.setContentAreaFilled(false);

    btnAdd.addActionListener(ev -> {
        int cantidad = (Integer) sp.getValue();
        agregarProductoDesdeCard(e, cantidad);
    });

    filaCant.add(sp);
    filaCant.add(btnAdd);

    // armar card
    card.add(lblId);
    card.add(Box.createVerticalStrut(6));
    card.add(lblFoto);
    card.add(Box.createVerticalStrut(8));
    card.add(lblNombreProd);
    card.add(Box.createVerticalStrut(4));
    card.add(lblPrecioProd);
    card.add(Box.createVerticalStrut(10));
    card.add(filaCant);

    return card;
}


   

    // AGREGAR PRODUCTO --------------------------------------------------------
    private void agregarProductoDesdeCard(Electrodomestico seleccionado, int cantidad) {
    if (seleccionado == null || cantidad <= 0) {
        return;
    }

    int id = seleccionado.getIdElectrodomestico();
    java.math.BigDecimal precio = seleccionado.getPrecioVenta() != null
            ? seleccionado.getPrecioVenta()
            : java.math.BigDecimal.ZERO;
    java.math.BigDecimal total = precio.multiply(java.math.BigDecimal.valueOf(cantidad));

    // si ya existe en la tabla, suma cantidad y total
    for (int i = 0; i < modelo.getRowCount(); i++) {
        int idRow = (int) modelo.getValueAt(i, 0);
        if (idRow == id) {
            int cantActual = (int) modelo.getValueAt(i, 3);
            int nuevaCant = cantActual + cantidad;
            modelo.setValueAt(nuevaCant, i, 3);
            java.math.BigDecimal totalAct = (java.math.BigDecimal) modelo.getValueAt(i, 4);
            modelo.setValueAt(totalAct.add(total), i, 4);
            return;
        }
    }

    modelo.addRow(new Object[]{
            id,
            seleccionado,
            precio,
            cantidad,
            total,
            "Eliminar"
    });
}


    // PROCESAR VENTA ---------------------------------------------------------
    private void procesarVenta() {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar cédula y nombre del cliente.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos un producto.",
                    "Productos faltantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String formaPago;
        if (rbEfectivo.isSelected()) {
            formaPago = "EFECTIVO";
        } else if (rbCredito.isSelected()) {
            formaPago = "CREDITO_DIRECTO";
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una forma de pago.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer plazoMeses = null;
        if ("CREDITO_DIRECTO".equals(formaPago)) {
            String plazoStr = txtPlazoMeses.getText().trim();
            if (plazoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el plazo en meses para crédito directo.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                plazoMeses = Integer.parseInt(plazoStr);
                if (plazoMeses <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El plazo debe ser un número entero mayor que cero.",
                        "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // construir request
        VentaRequest request = new VentaRequest();
        request.setCedulaCliente(cedula);
        request.setNombreCliente(nombre);
        request.setFormaPago(formaPago);
        request.setPlazoMeses(plazoMeses);

        List<DetalleVentaRequest> detalles = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int id = (int) modelo.getValueAt(i, 0);
            int cantidad = (int) modelo.getValueAt(i, 3);
            detalles.add(new DetalleVentaRequest(id, cantidad));
        }
        request.setDetalles(detalles);

        try {
            VentaResponse resp = controller.procesarVenta(request);
            if (resp != null && resp.isVentaExitosa()) {

                // 1) Mensaje de éxito
                JOptionPane.showMessageDialog(this,
                        "Venta procesada correctamente.\n" +
                                (resp.getMensaje() != null ? resp.getMensaje() : ""),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // 2) Construir datos para la factura visual
                FacturaService facturaService = new FacturaService();
                Factura factura = null;
                Integer idFactura = resp.getIdFactura();
                try {
                    if (idFactura != null) {
                        factura = facturaService.obtenerFacturaPorId(idFactura);
                    }
                } catch (Exception exSrv) {
                    logger.log(Level.WARNING, "No se pudo obtener factura por id " + idFactura, exSrv);
                }

                // Detalles: se toman directamente del VentaResponse
                List<DetalleFactura> detallesFactura = new ArrayList<>();
                if (resp.getDetalles() != null) {
                    for (ec.edu.monster.model.DetalleVentaResponse dvr : resp.getDetalles()) {
                        DetalleFactura df = new DetalleFactura();
                        df.setNombreElectrodomestico(dvr.getNombreElectrodomestico());
                        df.setCantidad(dvr.getCantidad());
                        df.setPrecioUnitario(dvr.getPrecioUnitario());
                        df.setSubtotal(dvr.getSubtotalLinea());
                        detallesFactura.add(df);
                    }
                }

                // Campos: prioridad al response, fallback a factura si existe
                String formaPagoFinal = resp.getFormaPago() != null
                        ? resp.getFormaPago()
                        : (factura != null ? factura.getFormaPago() : null);

                String estadoFinal = resp.getEstadoFactura() != null
                        ? resp.getEstadoFactura()
                        : (factura != null ? factura.getEstado() : null);

                String fechaFinal = (factura != null) ? factura.getFecha() : null;

                java.math.BigDecimal subtotalFinal = resp.getSubtotal() != null
                        ? resp.getSubtotal()
                        : (factura != null ? factura.getSubtotal() : null);

                java.math.BigDecimal descuentoFinal = resp.getDescuento() != null
                        ? resp.getDescuento()
                        : (factura != null ? factura.getDescuento() : null);

                java.math.BigDecimal totalFinal = resp.getTotal() != null
                        ? resp.getTotal()
                        : (factura != null ? factura.getTotal() : null);

                // 3) Abrir pantalla bonita de factura
                FacturaVenta pantalla = new FacturaVenta(
                        nombre,
                        cedula,
                        formaPagoFinal,
                        fechaFinal,
                        estadoFinal,
                        subtotalFinal,
                        descuentoFinal,
                        totalFinal,
                        detallesFactura
                );
                pantalla.setVisible(true);

                // 4) Limpiar la tabla
                modelo.setRowCount(0);

            } else {
                String msg = (resp != null && resp.getMensaje() != null)
                        ? resp.getMensaje()
                        : "La venta no pudo procesarse.";
                JOptionPane.showMessageDialog(this,
                        msg,
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al procesar venta", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la venta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RENDERERS --------------------------------------------------------------

    private class ElectrodomesticoComboRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Electrodomestico) {
                Electrodomestico e = (Electrodomestico) value;
                String texto = String.format("ID: %d | %s | $%s",
                        e.getIdElectrodomestico(),
                        e.getNombre(),
                        e.getPrecioVenta() != null ? e.getPrecioVenta().toString() : "-");
                label.setText(texto);

                label.setIcon(null);
                String ruta = e.getFotoRuta();
                if (ruta != null && !ruta.isEmpty()) {
                    File f = new File(ruta);
                    if (f.exists()) {
                        ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                        label.setHorizontalTextPosition(SwingConstants.RIGHT);
                    }
                }
            }

            return label;
        }
    }

    private class ElectrodomesticoTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Electrodomestico) {
                Electrodomestico e = (Electrodomestico) value;
                String texto = String.format("%s | $%s",
                        e.getNombre(),
                        e.getPrecioVenta() != null ? e.getPrecioVenta().toString() : "-");
                label.setText(texto);

                label.setIcon(null);
                String ruta = e.getFotoRuta();
                if (ruta != null && !ruta.isEmpty()) {
                    File f = new File(ruta);
                    if (f.exists()) {
                        ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                        label.setHorizontalTextPosition(SwingConstants.RIGHT);
                    }
                }
            }

            return label;
        }
    }

    // ID + icono circular pequeño
    private class IdConImagenRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            label.setText("ID #" + value);

            Object obj = table.getValueAt(row, 1); // la columna PRODUCTO tiene el Electrodomestico
            if (obj instanceof Electrodomestico) {
                Electrodomestico e = (Electrodomestico) obj;
                String ruta = e.getFotoRuta();
                label.setIcon(null);
                if (ruta != null && !ruta.isEmpty()) {
                    File f = new File(ruta);
                    if (f.exists()) {
                        ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                        label.setHorizontalTextPosition(SwingConstants.RIGHT);
                    }
                }
            }
            label.setHorizontalAlignment(SwingConstants.LEFT);
            return label;
        }
    }

    // Botón ELIMINAR
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("ELIMINAR");
            setForeground(Color.WHITE);
            setBackground(new Color(140, 40, 60));
            setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("ELIMINAR");
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(140, 40, 60));
            button.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                modelo.removeRow(row);
            }
            clicked = false;
            return "ELIMINAR";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    // MAIN --------------------------------------------------------------------
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

        java.awt.EventQueue.invokeLater(() -> new Venta().setVisible(true));
    }
}
