package ec.edu.monster.view;

import ec.edu.monster.controller.CreditoEstadoController;
import ec.edu.monster.model.CreditoEvaluacionResponse;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstadoCredito extends JFrame {

    private static final Logger logger = Logger.getLogger(EstadoCredito.class.getName());

    private final CreditoEstadoController controller = new CreditoEstadoController();

    // raíz
    private JPanel rootPanel;

    // input cédula
    private JTextField txtCedula;
    private JButton btnConsultar;

    // tarjetas resultado
    private JLabel lblResultadoTitulo;
    private JLabel lblResultadoCedula;

    private JLabel lblMontoMaximo;
    private JLabel lblMoneda;

    private JLabel lblEstadoCredito;
    private JLabel lblIdCredito;

    // mensaje
    private JTextArea txtMensaje;

    public EstadoCredito() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Estado de crédito del cliente");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeader();
        initCedulaPanel();
        initTarjetasPanel();
        initMensajePanel();
    }

    // HEADER ------------------------------------------------------------------
    private void initHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(10, 30, 60));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        header.setLayout(new BorderLayout());

        // izquierda: textos
        JPanel left = new JPanel();
        left.setBackground(new Color(10, 30, 60));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("CONEXIÓN BANQUITO");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Estado de crédito del cliente");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblSub = new JLabel("Consulta si el cliente puede acceder a crédito directo y visualiza el detalle en tarjetas.");
        lblSub.setForeground(new Color(180, 190, 205));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        left.add(lblCategoria);
        left.add(Box.createVerticalStrut(8));
        left.add(lblTitulo);
        left.add(Box.createVerticalStrut(8));
        left.add(lblSub);

        // derecha: volver al menú
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
        rootPanel.add(Box.createVerticalStrut(20));
    }

    // PANEL CÉDULA ------------------------------------------------------------
    private void initCedulaPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(10, 20, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("CÉDULA DEL CLIENTE");
        lblTitulo.setForeground(new Color(170, 180, 195));
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(10, 20, 40));

        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border line = BorderFactory.createLineBorder(new Color(60, 80, 120), 1);
        Border textBorder = BorderFactory.createCompoundBorder(line, inner);

        txtCedula = new JTextField();
        txtCedula.setBackground(new Color(15, 20, 35));
        txtCedula.setForeground(Color.WHITE);
        txtCedula.setCaretColor(Color.WHITE);
        txtCedula.setBorder(textBorder);

        btnConsultar = new JButton("CONSULTAR") {
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
        btnConsultar.setFocusPainted(false);
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setBackground(new Color(0, 180, 255));
        btnConsultar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnConsultar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConsultar.setOpaque(false);
        btnConsultar.setContentAreaFilled(false);
        btnConsultar.addActionListener(e -> consultarCredito());

        fila.add(txtCedula, BorderLayout.CENTER);
        fila.add(btnConsultar, BorderLayout.EAST);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(fila);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    // TARJETAS ----------------------------------------------------------------
    private void initTarjetasPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(8, 16, 32));
        panel.setLayout(new GridLayout(1, 3, 18, 0));

        // tarjeta resultado
        JPanel cardResultado = crearCard();
        JLabel lblResTitulo = new JLabel("RESULTADO");
        lblResTitulo.setForeground(new Color(170, 180, 195));
        lblResTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblResultadoTitulo = new JLabel("Sin consultar");
        lblResultadoTitulo.setForeground(Color.WHITE);
        lblResultadoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblResultadoCedula = new JLabel("Cédula: --");
        lblResultadoCedula.setForeground(new Color(180, 190, 205));
        lblResultadoCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardResultado.add(lblResTitulo);
        cardResultado.add(Box.createVerticalStrut(10));
        cardResultado.add(lblResultadoTitulo);
        cardResultado.add(Box.createVerticalStrut(5));
        cardResultado.add(lblResultadoCedula);

        // tarjeta monto máximo
        JPanel cardMonto = crearCard();
        JLabel lblMontoTitulo = new JLabel("MONTO MÁXIMO");
        lblMontoTitulo.setForeground(new Color(170, 180, 195));
        lblMontoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblMontoMaximo = new JLabel("$0.00");
        lblMontoMaximo.setForeground(Color.WHITE);
        lblMontoMaximo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblMoneda = new JLabel("Cuota estimada: --");
        lblMoneda.setForeground(new Color(180, 190, 205));
        lblMoneda.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardMonto.add(lblMontoTitulo);
        cardMonto.add(Box.createVerticalStrut(10));
        cardMonto.add(lblMontoMaximo);
        cardMonto.add(Box.createVerticalStrut(5));
        cardMonto.add(lblMoneda);

        // tarjeta estado crédito
        JPanel cardEstado = crearCard();
        JLabel lblEstadoTitulo = new JLabel("ESTADO DEL CRÉDITO");
        lblEstadoTitulo.setForeground(new Color(170, 180, 195));
        lblEstadoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblEstadoCredito = new JLabel("Sin consultar");
        lblEstadoCredito.setForeground(Color.WHITE);
        lblEstadoCredito.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblIdCredito = new JLabel("ID crédito: --");
        lblIdCredito.setForeground(new Color(180, 190, 205));
        lblIdCredito.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cardEstado.add(lblEstadoTitulo);
        cardEstado.add(Box.createVerticalStrut(10));
        cardEstado.add(lblEstadoCredito);
        cardEstado.add(Box.createVerticalStrut(5));
        cardEstado.add(lblIdCredito);

        panel.add(cardResultado);
        panel.add(cardMonto);
        panel.add(cardEstado);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    private JPanel crearCard() {
        JPanel p = new JPanel();
        p.setBackground(new Color(10, 20, 40));
        p.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    // MENSAJE -----------------------------------------------------------------
    private void initMensajePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(10, 18, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("MENSAJE");
        lblTitulo.setForeground(new Color(170, 180, 195));
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtMensaje = new JTextArea(4, 20);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setLineWrap(true);
        txtMensaje.setEditable(false);
        txtMensaje.setBackground(new Color(10, 18, 34));
        txtMensaje.setForeground(new Color(220, 225, 235));
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(txtMensaje);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(10, 18, 34));

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(scroll);

        rootPanel.add(panel);
    }

    // LÓGICA: CONSULTAR CRÉDITO ----------------------------------------------
    private void consultarCredito() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el número de cédula del cliente.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CreditoEvaluacionResponse resp = controller.verificarSujetoCredito(cedula);
            if (resp == null) {
                JOptionPane.showMessageDialog(this,
                        "No se obtuvo respuesta de la evaluación de crédito.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // suponemos que el modelo tiene getSujetoCredito(), getMontoMaximo() y getMensaje()
            boolean sujeto = Boolean.TRUE.equals(resp.getSujetoCredito());

            lblResultadoTitulo.setText(sujeto ? "Sujeto de crédito" : "No sujeto de crédito");
            lblResultadoCedula.setText("Cédula: " + cedula);

            if (resp.getMontoMaximoCredito() != null) {
                lblMontoMaximo.setText("$" + resp.getMontoMaximoCredito().toString());
            } else {
                lblMontoMaximo.setText("$0.00");
            }

            // Mostrar cuota mensual si está disponible
            if (resp.getCuotaMensual() != null) {
                lblMoneda.setText("Cuota estimada: $" + resp.getCuotaMensual().toString());
            } else {
                lblMoneda.setText("Cuota estimada: --");
            }

            lblEstadoCredito.setText(sujeto ? "Aprobado" : "Rechazado");
            
            // Mostrar ID de crédito si está disponible
            if (resp.getIdCredito() != null) {
                lblIdCredito.setText("ID crédito: " + resp.getIdCredito());
            } else {
                lblIdCredito.setText("ID crédito: --");
            }

            String mensaje = resp.getMensaje() != null ? resp.getMensaje() :
                    (sujeto ? "Cliente cumple requisitos para ser sujeto de crédito."
                            : "Cliente no cumple requisitos para crédito directo.");
            txtMensaje.setText(mensaje);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al consultar estado de crédito", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar estado de crédito: " + ex.getMessage(),
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

        java.awt.EventQueue.invokeLater(() -> new EstadoCredito().setVisible(true));
    }
}
