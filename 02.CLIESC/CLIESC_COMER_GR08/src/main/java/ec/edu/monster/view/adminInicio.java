package ec.edu.monster.view;

import ec.edu.monster.controller.ElectrodomesticoController;
import ec.edu.monster.model.Electrodomestico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class adminInicio extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private ElectrodomesticoController controller = new ElectrodomesticoController();
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JLabel lblContador;
    private JLabel lblNumero;

    public adminInicio() {
        setTitle("Gestión de Electrodomésticos - Admin");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initTablePanel();
        cargarDatos();
    }

    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 30, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setLayout(new BorderLayout());

        // Panel izquierdo con título y descripción
        JPanel textoPanel = new JPanel();
        textoPanel.setBackground(new Color(10, 30, 60));
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));

        JLabel lblCategoria = new JLabel("PANEL ADMINISTRATIVO");
        lblCategoria.setForeground(new Color(160, 175, 190));
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitulo = new JLabel("Gestión de electrodomésticos");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSubtitulo = new JLabel("Administra el catálogo corporativo, asegura precios vigentes y ordena el inventario disponible.");
        lblSubtitulo.setForeground(new Color(180, 190, 205));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        textoPanel.add(lblCategoria);
        textoPanel.add(Box.createVerticalStrut(10));
        textoPanel.add(lblTitulo);
        textoPanel.add(Box.createVerticalStrut(8));
        textoPanel.add(lblSubtitulo);
        textoPanel.add(Box.createVerticalStrut(15));

        // Panel con contador de registros
        JPanel contadorPanel = new JPanel();
        contadorPanel.setBackground(new Color(15, 25, 50));
        contadorPanel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        contadorPanel.setLayout(new BoxLayout(contadorPanel, BoxLayout.Y_AXIS));
        contadorPanel.setMaximumSize(new Dimension(200, 80));

        lblNumero = new JLabel("0");
        lblNumero.setForeground(Color.WHITE);
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblNumero.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblContador = new JLabel("0 Registros activos");
        lblContador.setForeground(new Color(160, 175, 190));
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblContador.setAlignmentX(Component.LEFT_ALIGNMENT);

        contadorPanel.add(lblNumero);
        contadorPanel.add(Box.createVerticalStrut(5));
        contadorPanel.add(lblContador);

        textoPanel.add(contadorPanel);

        // Panel derecho con botones (vertical)
        JPanel botonesPanel = new JPanel();
        botonesPanel.setBackground(new Color(10, 30, 60));
        botonesPanel.setLayout(new BoxLayout(botonesPanel, BoxLayout.Y_AXIS));

        JButton btnCerrar = new JButton("CERRAR SESIÓN");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(80, 40, 120));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setMaximumSize(new Dimension(240, 40));
        btnCerrar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnCerrar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        JButton btnNuevo = new JButton("NUEVO ELECTRODOMÉSTICO");
        btnNuevo.setFocusPainted(false);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setBackground(new Color(0, 160, 255));
        btnNuevo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNuevo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNuevo.setMaximumSize(new Dimension(240, 40));
        btnNuevo.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnNuevo.addActionListener(e -> {
            new ElectrodomesticoNuevo().setVisible(true);
            dispose();
        });

        botonesPanel.add(btnCerrar);
        botonesPanel.add(Box.createVerticalStrut(10));
        botonesPanel.add(btnNuevo);

        headerPanel.add(textoPanel, BorderLayout.WEST);
        headerPanel.add(botonesPanel, BorderLayout.EAST);

        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(20));
    }

    private void initTablePanel() {
        // Modelo de tabla
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Precio", "Editar", "Eliminar"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3; // Solo Editar y Eliminar
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(new Color(10, 18, 34));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(30, 40, 60));
        tabla.setRowHeight(40);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(20, 35, 60));
        tabla.setSelectionForeground(Color.WHITE);

        // Header de la tabla
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(15, 25, 45));
        header.setForeground(new Color(180, 190, 205));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 70, 100)));

        tabla.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Editar").setCellEditor(new ButtonEditor("EDITAR", this));

        tabla.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Eliminar").setCellEditor(new ButtonEditor("ELIMINAR", this));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(10, 18, 34));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(30, 40, 60)));
        scroll.setPreferredSize(new Dimension(1000, 400));

        rootPanel.add(scroll);
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        try {
            List<Electrodomestico> lista = controller.listarElectrodomesticos();
            for (Electrodomestico e : lista) {
                modelo.addRow(new Object[]{
                    e.getIdElectrodomestico(),
                    e.getNombre(),
                    e.getPrecioVenta(),
                    "Editar",
                    "Eliminar"
                });
            }
            
            // Actualizar contador de registros
            if (lblNumero != null) {
                lblNumero.setText(String.valueOf(lista.size()));
            }
            if (lblContador != null) {
                lblContador.setText("Registros activos");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error cargando datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar(int id) {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el electrodoméstico?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarElectrodomestico(id);
                cargarDatos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    public void editar(int id) {
        ElectrodomesticoEditar vista = new ElectrodomesticoEditar(id);
        vista.setVisible(true);
        dispose();
    }
}
