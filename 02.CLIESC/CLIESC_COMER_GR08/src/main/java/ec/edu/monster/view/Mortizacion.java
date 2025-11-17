package ec.edu.monster.view;

import ec.edu.monster.controller.CreditoAmortizacionController;
import ec.edu.monster.model.Credito;
import ec.edu.monster.model.CuotaAmortizacion;
import ec.edu.monster.model.TablaAmortizacion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;

public class Mortizacion extends JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Mortizacion.class.getName());

    // Controller
    private final CreditoAmortizacionController controller = new CreditoAmortizacionController();

    // Root
    private JPanel rootPanel;

    // Header
    private JPanel headerPanel;
    private JButton btnVolver;

    // Búsqueda
    private JTextField txtCedula;
    private JButton btnBuscar;
    private JLabel lblMensaje;

    // Tabla créditos activos
    private JTable tblCreditos;
    private DefaultTableModel modeloCreditos;

    // Detalle crédito / tabla amortización
    private JLabel lblIdFactura;
    private JLabel lblCliente;
    private JLabel lblMonto;
    private JLabel lblCuota;
    private JTable tblCuotas;
    private DefaultTableModel modeloCuotas;

    public Mortizacion() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Tabla de amortización");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(5, 10, 25));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeader();
        initBusquedaPanel();
        initCreditosPanel();
        initDetallePanel();
    }

    private void initHeader() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(5, 25, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Tabla de amortización");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitulo = new JLabel("Créditos activos del cliente");
        subtitulo.setForeground(new Color(170, 185, 205));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel left = new JPanel();
        left.setBackground(new Color(5, 25, 60));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(titulo);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitulo);

        btnVolver = new JButton("VOLVER AL MENÚ");
        btnVolver.setFocusPainted(false);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(10, 35, 70));
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            new userMenu().setVisible(true);
            dispose();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setBackground(new Color(5, 25, 60));
        right.add(btnVolver);

        headerPanel.add(left, BorderLayout.WEST);
        headerPanel.add(right, BorderLayout.EAST);

        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    private void initBusquedaPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(5, 10, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));

        JLabel lblVolverText = new JLabel("Créditos activos del cliente");
        lblVolverText.setForeground(new Color(170, 185, 205));
        lblVolverText.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblCedula = new JLabel("Cédula del cliente");
        lblCedula.setForeground(new Color(180, 190, 210));
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtCedula = new JTextField();
        txtCedula.setBackground(new Color(5, 10, 25));
        txtCedula.setForeground(Color.WHITE);
        txtCedula.setCaretColor(Color.WHITE);
        Border inner = BorderFactory.createEmptyBorder(8, 10, 8, 10);
        Border line = BorderFactory.createLineBorder(new Color(0, 150, 255), 1);
        txtCedula.setBorder(BorderFactory.createCompoundBorder(line, inner));

        btnBuscar = new JButton("Buscar créditos activos");
        btnBuscar.setFocusPainted(false);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(0, 190, 120));
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarCreditos());

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(5, 10, 25));
        fila.add(txtCedula, BorderLayout.CENTER);
        fila.add(btnBuscar, BorderLayout.EAST);

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(220, 220, 220));
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(lblVolverText);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblCedula);
        panel.add(Box.createVerticalStrut(5));
        panel.add(fila);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblMensaje);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(15));
    }

    private void initCreditosPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(5, 15, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setLayout(new BorderLayout(0, 10));

        JLabel lblTitulo = new JLabel("Créditos activos");
        lblTitulo.setForeground(new Color(220, 230, 245));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        panel.add(lblTitulo, BorderLayout.NORTH);

        modeloCreditos = new DefaultTableModel(
                new String[]{"ID", "MONTO APROBADO", "PLAZO", "CUOTA", "Ver tabla"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tblCreditos = new JTable(modeloCreditos);
        tblCreditos.setBackground(new Color(10, 18, 34));
        tblCreditos.setForeground(Color.WHITE);
        tblCreditos.setGridColor(new Color(30, 40, 60));
        tblCreditos.setRowHeight(32);
        tblCreditos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCreditos.setSelectionBackground(new Color(20, 35, 60));
        tblCreditos.setSelectionForeground(Color.WHITE);

        JTableHeader header = tblCreditos.getTableHeader();
        header.setBackground(new Color(15, 25, 45));
        header.setForeground(new Color(190, 200, 215));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Columna botón "Ver tabla"
        TableColumnModel columnModel = tblCreditos.getColumnModel();
        columnModel.getColumn(4).setCellRenderer(new AmortButtonRenderer());
        columnModel.getColumn(4).setCellEditor(new AmortButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(tblCreditos);
        scroll.getViewport().setBackground(new Color(10, 18, 34));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(25, 35, 60)));

        panel.add(scroll, BorderLayout.CENTER);

        rootPanel.add(panel);
        rootPanel.add(Box.createVerticalStrut(15));
    }

    private void initDetallePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(5, 15, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Detalle del crédito");
        lblTitulo.setForeground(new Color(220, 230, 245));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblIdFactura = new JLabel("ID Factura: --");
        lblCliente = new JLabel("Cliente: --");
        lblMonto = new JLabel("Monto financiado: --");
        lblCuota = new JLabel("Cuota mensual: --");

        for (JLabel l : new JLabel[]{lblIdFactura, lblCliente, lblMonto, lblCuota}) {
            l.setForeground(new Color(190, 200, 215));
            l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblIdFactura);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblCliente);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblMonto);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblCuota);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblTabla = new JLabel("Tabla de amortización");
        lblTabla.setForeground(new Color(220, 230, 245));
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(lblTabla);
        panel.add(Box.createVerticalStrut(8));

        modeloCuotas = new DefaultTableModel(
                new String[]{"CUOTA", "CAPITAL", "INTERÉS", "VALOR CUOTA", "SALDO"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCuotas = new JTable(modeloCuotas);
        tblCuotas.setBackground(new Color(10, 18, 34));
        tblCuotas.setForeground(Color.WHITE);
        tblCuotas.setGridColor(new Color(30, 40, 60));
        tblCuotas.setRowHeight(30);
        tblCuotas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCuotas.setSelectionBackground(new Color(20, 35, 60));
        tblCuotas.setSelectionForeground(Color.WHITE);

        JTableHeader header = tblCuotas.getTableHeader();
        header.setBackground(new Color(15, 25, 45));
        header.setForeground(new Color(190, 200, 215));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tblCuotas);
        scroll.getViewport().setBackground(new Color(10, 18, 34));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(25, 35, 60)));

        panel.add(scroll);

        rootPanel.add(panel);
    }

    /* --------- LÓGICA --------- */

    private void buscarCreditos() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese la cédula del cliente.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloCreditos.setRowCount(0);
        modeloCuotas.setRowCount(0);
        limpiarDetalle();

        try {
            List<Credito> creditos = controller.obtenerCreditosActivos(cedula);

            if (creditos == null || creditos.isEmpty()) {
                lblMensaje.setText("No se encontraron créditos activos para el cliente.");
                return;
            }

            lblMensaje.setText("Se encontraron " + creditos.size() + " crédito(s) activo(s).");

            for (Credito c : creditos) {
                modeloCreditos.addRow(new Object[]{
                        c.getIdCredito(),
                        c.getMontoAprobado(),
                        c.getPlazoMeses() + " meses",
                        c.getCuotaFija(),
                        "Ver tabla"
                });
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al buscar créditos", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al buscar créditos activos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaAmortizacion(int idCredito) {
        try {
            TablaAmortizacion tabla = controller.obtenerTablaAmortizacion(idCredito);
            if (tabla == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la tabla de amortización.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            lblIdFactura.setText("ID Factura: " + tabla.getIdFactura());
            lblCliente.setText("Cliente: " + tabla.getNombreCliente()
                    + " (" + tabla.getCedulaCliente() + ")");
            lblMonto.setText("Monto financiado: $" + tabla.getMontoTotal());
            lblCuota.setText("Cuota mensual: $" + tabla.getCuotaMensual());

            modeloCuotas.setRowCount(0);
            if (tabla.getCuotas() != null) {
                for (CuotaAmortizacion c : tabla.getCuotas()) {
                    modeloCuotas.addRow(new Object[]{
                            c.getNumeroCuota(),
                            c.getCapital(),
                            c.getInteres(),
                            c.getCuota(),
                            c.getSaldoFinal()
                    });
                }
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al cargar tabla de amortización", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al obtener tabla de amortización: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarDetalle() {
        lblIdFactura.setText("ID Factura: --");
        lblCliente.setText("Cliente: --");
        lblMonto.setText("Monto financiado: --");
        lblCuota.setText("Cuota mensual: --");
    }

    /* --------- RENDERER / EDITOR PARA BOTÓN "VER TABLA" --------- */

    private class AmortButtonRenderer extends JButton implements TableCellRenderer {

        public AmortButtonRenderer() {
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(0, 150, 255));
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setFocusPainted(false);
            setText("Ver tabla");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(new Color(0, 120, 220));
            } else {
                setBackground(new Color(0, 150, 255));
            }
            return this;
        }
    }

    private class AmortButtonEditor extends DefaultCellEditor {

        private JButton button;
        private int currentRow;

        public AmortButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver tabla");
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0, 150, 255));
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int idCredito = (Integer) tblCreditos.getValueAt(currentRow, 0);
                    cargarTablaAmortizacion(idCredito);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver tabla";
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info :
                    UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> new Mortizacion().setVisible(true));
    }
}
