package ec.edu.monster.view;

import ec.edu.monster.controller.VentaController;
import ec.edu.monster.model.DetalleVentaRequest;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Venta extends JFrame {

    private static final Logger logger = Logger.getLogger(Venta.class.getName());

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

    // productos
    private JComboBox<Electrodomestico> comboProductos;
    private JSpinner spCantidad;

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

        // fila con listado + cantidad + botón agregar
        JPanel filaAgregar = new JPanel(new GridBagLayout());
        filaAgregar.setBackground(new Color(10, 18, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border line = BorderFactory.createLineBorder(new Color(60, 80, 120), 1);
        Border textBorder = BorderFactory.createCompoundBorder(line, inner);

        // LISTADO
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        JPanel panelListado = new JPanel();
        panelListado.setLayout(new BoxLayout(panelListado, BoxLayout.Y_AXIS));
        panelListado.setBackground(new Color(10, 18, 34));

        JLabel lblListado = new JLabel("LISTADO");
        lblListado.setForeground(new Color(170, 180, 195));
        lblListado.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        comboProductos = new JComboBox<>();
        comboProductos.setBackground(new Color(15, 20, 35));
        comboProductos.setForeground(Color.WHITE);
        comboProductos.setBorder(textBorder);

        panelListado.add(lblListado);
        panelListado.add(Box.createVerticalStrut(5));
        panelListado.add(comboProductos);

        filaAgregar.add(panelListado, gbc);

        // CANTIDAD
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        JPanel panelCant = new JPanel();
        panelCant.setLayout(new BoxLayout(panelCant, BoxLayout.Y_AXIS));
        panelCant.setBackground(new Color(10, 18, 34));

        JLabel lblCant = new JLabel("CANTIDAD");
        lblCant.setForeground(new Color(170, 180, 195));
        lblCant.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spCantidad.setBorder(textBorder);
        spCantidad.setBackground(new Color(15, 20, 35));
        spCantidad.setForeground(Color.WHITE);

        panelCant.add(lblCant);
        panelCant.add(Box.createVerticalStrut(5));
        panelCant.add(spCantidad);

        filaAgregar.add(panelCant, gbc);

        // BOTÓN AGREGAR
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnAgregar = new JButton("AGREGAR") {
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
        btnAgregar.setFocusPainted(false);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(new Color(0, 180, 255));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setOpaque(false);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.addActionListener(e -> agregarProducto());

        filaAgregar.add(btnAgregar, gbc);

        panel.add(filaAgregar);
        panel.add(Box.createVerticalStrut(20));

        // Productos seleccionados
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

        modelo = new DefaultTableModel(new String[]{"ID", "PRODUCTO", "CANTIDAD"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(new Color(10, 18, 34));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(30, 40, 60));
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(20, 35, 60));
        tabla.setSelectionForeground(Color.WHITE);

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

        // botones inferiores
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
        try {
            List<Electrodomestico> catalogo = controller.obtenerCatalogo();
            comboProductos.removeAllItems();
            for (Electrodomestico e : catalogo) {
                comboProductos.addItem(e); // usa toString() para mostrar
            }
            lblReferencias.setText(String.valueOf(catalogo.size()));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error cargando catálogo", ex);
            JOptionPane.showMessageDialog(this,
                    "Error cargando catálogo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // AGREGAR PRODUCTO --------------------------------------------------------
    private void agregarProducto() {
        Electrodomestico seleccionado = (Electrodomestico) comboProductos.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad = (Integer) spCantidad.getValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor que cero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = seleccionado.getIdElectrodomestico();
        String nombre = seleccionado.getNombre();

        // si ya existe en la tabla, suma cantidad
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int idRow = (int) modelo.getValueAt(i, 0);
            if (idRow == id) {
                int cantActual = (int) modelo.getValueAt(i, 2);
                modelo.setValueAt(cantActual + cantidad, i, 2);
                return;
            }
        }

        modelo.addRow(new Object[]{id, nombre, cantidad});
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
            int cantidad = (int) modelo.getValueAt(i, 2);
            detalles.add(new DetalleVentaRequest(id, cantidad));
        }
        request.setDetalles(detalles);

        try {
            VentaResponse resp = controller.procesarVenta(request);
            if (resp != null && resp.isVentaExitosa()) {
                JOptionPane.showMessageDialog(this,
                        "Venta procesada correctamente.\n" +
                                (resp.getMensaje() != null ? resp.getMensaje() : ""),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // podrías limpiar formulario si quieres
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
