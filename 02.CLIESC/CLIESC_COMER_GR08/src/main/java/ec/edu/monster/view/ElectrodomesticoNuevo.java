package ec.edu.monster.view;

import ec.edu.monster.controller.ElectrodomesticoController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectrodomesticoNuevo extends JFrame {

    private static final Logger logger =
            Logger.getLogger(ElectrodomesticoNuevo.class.getName());

    private final ElectrodomesticoController controller = new ElectrodomesticoController();

    // Componentes (incluye imagen)
    private JPanel rootPanel, headerPanel, formPanel;
    private JLabel lblTitulo, lblSubtitulo, lblNombre, lblPrecio, lblFoto;
    private JTextField txtNombre, txtPrecio;
    private JButton btnVolver, btnCancelar, btnGuardar, btnElegirFoto;
    private JLabel lblNombreArchivo;

    // Bytes para la foto
    private byte[] fotoBytes = null;
    private String nombreArchivo = null;

    public ElectrodomesticoNuevo() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Nuevo electrodoméstico");
        setSize(900, 580);
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

        Border inner = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border whiteLine = BorderFactory.createLineBorder(new Color(70, 90, 130), 1);

        // Nombre
        lblNombre = new JLabel("NOMBRE");
        lblNombre.setForeground(new Color(170, 180, 195));
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtNombre = new JTextField();
        txtNombre.setBackground(new Color(15, 20, 35));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
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

        // FOTO DEL PRODUCTO
        lblFoto = new JLabel("FOTO DEL PRODUCTO");
        lblFoto.setForeground(new Color(170, 180, 195));
        lblFoto.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel panelFoto = new JPanel(new BorderLayout(10, 0));
        panelFoto.setBackground(new Color(10, 18, 34));

        btnElegirFoto = new JButton("Choose File");
        btnElegirFoto.setFocusPainted(false);
        btnElegirFoto.setForeground(Color.WHITE);
        btnElegirFoto.setBackground(new Color(20, 25, 45));
        btnElegirFoto.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 150)));
        btnElegirFoto.setPreferredSize(new Dimension(130, 34));
        btnElegirFoto.addActionListener(e -> seleccionarFoto());

        lblNombreArchivo = new JLabel("No file chosen");
        lblNombreArchivo.setForeground(new Color(170, 180, 195));
        lblNombreArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel archivoPanel = new JPanel(new BorderLayout());
        archivoPanel.setBackground(new Color(15, 20, 35));
        archivoPanel.setBorder(BorderFactory.createCompoundBorder(whiteLine, inner));
        archivoPanel.add(lblNombreArchivo, BorderLayout.CENTER);

        panelFoto.add(btnElegirFoto, BorderLayout.WEST);
        panelFoto.add(archivoPanel, BorderLayout.CENTER);

        // Botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botonesPanel.setBackground(new Color(10, 18, 34));

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(20, 25, 45));
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 150)));
        btnCancelar.setPreferredSize(new Dimension(130, 36));
        btnCancelar.addActionListener(e -> volverAlListado());

        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setFocusPainted(false);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(0, 160, 255));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btnGuardar.setPreferredSize(new Dimension(130, 36));
        btnGuardar.addActionListener(e -> guardarNuevo());

        botonesPanel.add(btnCancelar);
        botonesPanel.add(btnGuardar);

        // Agregar todo al form
        formPanel.add(lblNombre);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtNombre);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(lblPrecio);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPrecio);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(lblFoto);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(panelFoto);
        formPanel.add(Box.createVerticalStrut(30));

        formPanel.add(botonesPanel);

        rootPanel.add(formPanel);
    }

    // -------------------------------
    // Subir imagen
    // -------------------------------
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto del producto");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            nombreArchivo = archivo.getName();
            lblNombreArchivo.setText(nombreArchivo);

            try (FileInputStream fis = new FileInputStream(archivo)) {
                fotoBytes = controller.readAllBytes(fis);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error leyendo imagen: " + ex.getMessage());
            }
        }
    }

    // -------------------------------
    // Guardar nuevo producto
    // -------------------------------
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número válido.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controller.crear(nombre, precio, fotoBytes, nombreArchivo);

            JOptionPane.showMessageDialog(this,
                    "Electrodoméstico creado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlListado();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al crear: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlListado() {
        new adminInicio().setVisible(true);
        dispose();
    }
}
