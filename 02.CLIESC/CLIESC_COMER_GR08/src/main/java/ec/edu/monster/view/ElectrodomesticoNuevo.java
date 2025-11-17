package ec.edu.monster.view;

import ec.edu.monster.controller.ElectrodomesticoController;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectrodomesticoNuevo extends JFrame {

    private static final Logger logger =
            Logger.getLogger(ElectrodomesticoNuevo.class.getName());

    private final ElectrodomesticoController controller = new ElectrodomesticoController();

    // Componentes
    private JPanel rootPanel, headerPanel, formPanel;
    private JLabel lblTitulo, lblSubtitulo, lblNombre, lblPrecio;
    private JTextField txtNombre, txtPrecio;
    private JButton btnVolver, btnCancelar, btnGuardar;

    public ElectrodomesticoNuevo() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Nuevo electrodoméstico");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initFormPanel();
    }

    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 30, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setLayout(new BorderLayout());

        JPanel textoPanel = new JPanel();
        textoPanel.setBackground(new Color(10, 30, 60));
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("CATÁLOGO MAESTRO");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblTitulo = new JLabel("Nuevo electrodoméstico");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        lblSubtitulo = new JLabel("Mantén actualizada la lista de referencias con precios oficiales.");
        lblSubtitulo.setForeground(new Color(180, 190, 205));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        textoPanel.add(lblCategoria);
        textoPanel.add(Box.createVerticalStrut(10));
        textoPanel.add(lblTitulo);
        textoPanel.add(Box.createVerticalStrut(8));
        textoPanel.add(lblSubtitulo);

        btnVolver = new JButton("VOLVER AL LISTADO");
        btnVolver.setFocusPainted(false);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(18, 32, 60));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(80, 100, 180)));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(190, 36));
        btnVolver.addActionListener(e -> volverAlListado());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setBackground(new Color(10, 30, 60));
        btnPanel.add(btnVolver);

        headerPanel.add(textoPanel, BorderLayout.WEST);
        headerPanel.add(btnPanel, BorderLayout.EAST);

        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    private void initFormPanel() {
        formPanel = new JPanel();
        formPanel.setBackground(new Color(10, 18, 34));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Nombre
        lblNombre = new JLabel("NOMBRE");
        lblNombre.setForeground(new Color(170, 180, 195));
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtNombre = new JTextField();
        txtNombre.setBackground(new Color(15, 20, 35));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border whiteLine = BorderFactory.createLineBorder(new Color(70, 90, 130), 1);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(whiteLine, inner));

        // Precio
        lblPrecio = new JLabel("PRECIO DE VENTA (USD)");
        lblPrecio.setForeground(new Color(170, 180, 195));
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtPrecio = new JTextField();
        txtPrecio.setBackground(new Color(15, 20, 35));
        txtPrecio.setForeground(Color.WHITE);
        txtPrecio.setCaretColor(Color.WHITE);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(whiteLine, inner));

        // Panel de botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setBackground(new Color(10, 18, 34));
        botonesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(20, 25, 45));
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 150)));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(130, 36));
        btnCancelar.addActionListener(e -> volverAlListado());

        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setFocusPainted(false);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(0, 160, 255));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(130, 36));
        btnGuardar.addActionListener(e -> guardarNuevo());

        botonesPanel.add(btnCancelar);
        botonesPanel.add(btnGuardar);

        // Add al form
        formPanel.add(lblNombre);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtNombre);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblPrecio);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPrecio);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(botonesPanel);

        rootPanel.add(formPanel);
    }

    // -------------------------------------------------------------------------
    // FUNCIONALIDAD
    // -------------------------------------------------------------------------

    private void guardarNuevo() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar nombre y precio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser numérico.",
                    "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controller.crearElectrodomestico(nombre, precio);
            JOptionPane.showMessageDialog(this,
                    "Electrodoméstico creado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlListado();
        } catch (IOException | ParseException ex) {
            logger.log(Level.SEVERE, "Error al crear electrodoméstico", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlListado() {
        new adminInicio().setVisible(true);
        dispose();
    }

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

        java.awt.EventQueue.invokeLater(() -> new ElectrodomesticoNuevo().setVisible(true));
    }
}
