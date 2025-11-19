package ec.edu.monster.view;

import ec.edu.monster.controller.ElectrodomesticoController;
import ec.edu.monster.model.Electrodomestico;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class adminInicio extends JFrame {

    private ElectrodomesticoController controller = new ElectrodomesticoController();
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JLabel lblContador;
    private JLabel lblNumero;

    // panel que contendrá las tarjetas
    private JPanel cardsPanel;
    private JScrollPane scrollPane;

    public adminInicio() {
        setTitle("Gestión de Electrodomésticos - Admin");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initCardsPanel();
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
        contadorPanel.setMaximumSize(new Dimension(220, 80));

        lblNumero = new JLabel("0");
        lblNumero.setForeground(Color.WHITE);
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblNumero.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblContador = new JLabel("Registros activos");
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
        btnCerrar.setMaximumSize(new Dimension(220, 40));
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
        btnNuevo.setMaximumSize(new Dimension(220, 40));
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

    private void initCardsPanel() {
        // Panel que contendrá las tarjetas en una grilla 4xN
        cardsPanel = new JPanel();
        cardsPanel.setBackground(new Color(8, 16, 32));
        cardsPanel.setLayout(new GridLayout(0, 4, 20, 20)); // 0 filas, 4 columnas, gaps

        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getViewport().setBackground(new Color(8, 16, 32));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rootPanel.add(scrollPane);
    }

    /** Carga los datos desde el controller y construye las tarjetas */
    private void cargarDatos() {
        cardsPanel.removeAll();

        try {
            List<Electrodomestico> lista = controller.listar();

            for (Electrodomestico e : lista) {
                cardsPanel.add(crearTarjeta(e));
            }

            // Rellena la última fila con paneles vacíos si no son múltiplos de 4 (para que no se vea tan chueco)
            int resto = lista.size() % 4;
            if (resto != 0) {
                int vacios = 4 - resto;
                for (int i = 0; i < vacios; i++) {
                    JPanel filler = new JPanel();
                    filler.setBackground(new Color(8, 16, 32));
                    cardsPanel.add(filler);
                }
            }

            // Actualizar contador
            if (lblNumero != null) {
                lblNumero.setText(String.valueOf(lista.size()));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    /** Crea una tarjeta individual de electrodoméstico */
    private JPanel crearTarjeta(Electrodomestico e) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(10, 18, 34));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(25, 35, 60)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(240, 330));

        // Panel imagen
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(220, 150));
        imagePanel.setBackground(new Color(14, 22, 40));
        imagePanel.setBorder(BorderFactory.createDashedBorder(new Color(60, 75, 110)));

        JLabel lblImagen = new JLabel("SIN IMAGEN", SwingConstants.CENTER);
        lblImagen.setForeground(new Color(150, 160, 175));
        lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Si hay URL de imagen, intentamos cargarla
        String fotoUrl = e.getFotoUrl();
        if (fotoUrl != null && !fotoUrl.isBlank()) {
            try {
                URL url = new URL(fotoUrl);
                Image img = ImageIO.read(url);
                if (img != null) {
                    Image scaled = img.getScaledInstance(220, 150, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(scaled));
                    lblImagen.setText("");
                }
            } catch (Exception ex) {
                // Si falla, dejamos "SIN IMAGEN"
            }
        }

        imagePanel.add(lblImagen, BorderLayout.CENTER);

        // Panel de texto (ID, nombre, precio)
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(10, 18, 34));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblId = new JLabel("ID #" + e.getIdElectrodomestico());
        lblId.setForeground(new Color(140, 150, 170));
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblNombre = new JLabel("<html><b>" + e.getNombre() + "</b></html>");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNombre.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel lblPrecio = new JLabel("$" + e.getPrecioVenta());
        lblPrecio.setForeground(new Color(0, 200, 255));
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));

        infoPanel.add(lblId);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblPrecio);

        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botonesPanel.setBackground(new Color(10, 18, 34));

        JButton btnEditar = new JButton("EDITAR");
        btnEditar.setFocusPainted(false);
        btnEditar.setForeground(new Color(0, 180, 255));
        btnEditar.setBackground(new Color(20, 25, 40));
        btnEditar.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
        btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditar.setPreferredSize(new Dimension(90, 32));

        btnEditar.addActionListener(ev -> editar(e.getIdElectrodomestico()));

        JButton btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setFocusPainted(false);
        btnEliminar.setForeground(new Color(255, 230, 230));
        btnEliminar.setBackground(new Color(90, 40, 60));
        btnEliminar.setBorder(BorderFactory.createLineBorder(new Color(200, 90, 110)));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(90, 32));

        btnEliminar.addActionListener(ev -> eliminar(e.getIdElectrodomestico()));

        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(botonesPanel, BorderLayout.SOUTH);

        return card;
    }

    /** Eliminar electrodoméstico con confirmación */
    public void eliminar(int id) {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el electrodoméstico?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminar(id);
                cargarDatos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    /** Abrir pantalla de edición */
    public void editar(int id) {
        ElectrodomesticoEditar vista = new ElectrodomesticoEditar(id);
        vista.setVisible(true);
        dispose();
    }
}
