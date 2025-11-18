package ec.edu.monster.view;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class userMenu extends JFrame {

    private static final Logger logger = Logger.getLogger(userMenu.class.getName());

    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel menuPanel;

    public userMenu() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Comercializadora Monster - Menú usuario");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rootPanel = new JPanel();
        rootPanel.setBackground(new Color(8, 16, 32));
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setContentPane(rootPanel);

        initHeaderPanel();
        initMenuPanel();
    }

    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(40, 45, 50));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 70, 80), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        headerPanel.setLayout(new BorderLayout());

        // Panel izquierdo: título + texto + "USUARIO MONSTER"
        JPanel left = new JPanel();
        left.setBackground(new Color(40, 45, 50));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Comercializadora Monster");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblTexto = new JLabel(
                "Consulta catálogo, registra ventas y controla el ciclo del crédito desde un solo lugar."
        );
        lblTexto.setForeground(new Color(180, 190, 205));
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnUsuario = new JButton("USUARIO MONSTER");
        btnUsuario.setFocusPainted(false);
        btnUsuario.setForeground(new Color(200, 200, 200));
        btnUsuario.setBackground(new Color(50, 55, 60));
        btnUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 75, 80), 1),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btnUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btnUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(lblTitulo);
        left.add(Box.createVerticalStrut(8));
        left.add(lblTexto);
        left.add(Box.createVerticalStrut(18));
        left.add(btnUsuario);

        // Panel derecho: botón Cerrar sesión
        JPanel right = new JPanel();
        right.setBackground(new Color(40, 45, 50));
        right.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setForeground(new Color(200, 200, 200));
        btnCerrar.setBackground(new Color(50, 55, 60));
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 75, 80), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> {
            // volver al login
            new Login().setVisible(true);
            dispose();
        });

        right.add(btnCerrar);

        headerPanel.add(left, BorderLayout.WEST);
        headerPanel.add(right, BorderLayout.EAST);

        rootPanel.add(headerPanel);
        rootPanel.add(Box.createVerticalStrut(25));
    }

    private void initMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(20, 25, 35));
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 50, 65), 1),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel lblMenuTitulo = new JLabel("Menú de opciones");
        lblMenuTitulo.setForeground(Color.WHITE);
        lblMenuTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel lblMenuSub = new JLabel("Selecciona una opción.");
        lblMenuSub.setForeground(new Color(180, 190, 205));
        lblMenuSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        menuPanel.add(lblMenuTitulo);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(lblMenuSub);
        menuPanel.add(Box.createVerticalStrut(20));

        // Contenedor de tarjetas
        JPanel cardsContainer = new JPanel();
        cardsContainer.setBackground(new Color(20, 25, 35));
        cardsContainer.setLayout(new GridLayout(1, 5, 18, 0));

        // Tarjeta 1: Catálogo
        JPanel cardCatalogo = createCard(
                "Catálogo de electrodomésticos",
                "<html>Explora precios actualizados y<br/>disponibilidad antes de cotizar.</html>"
        );
        cardCatalogo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CatalogoActivo().setVisible(true);
                dispose();
            }
        });

        // Tarjeta 2: Nueva venta
        JPanel cardVenta = createCard(
                "Registrar nueva venta",
                "<html>Captura datos del cliente,<br/>forma de pago y productos en<br/>una sola pantalla.</html>"
        );
        cardVenta.addMouseListener(new java.awt.event.MouseAdapter() {
          @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Venta().setVisible(true);
                dispose();
            }
        });

        // Tarjeta 3: Estado de crédito
        JPanel cardEstado = createCard(
                "Estado de crédito",
                "<html>Verifica si el cliente es sujeto<br/>de crédito directo con Banquito.</html>"
        );
        cardEstado.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new EstadoCredito().setVisible(true);
                dispose();
            }
        });

        // Tarjeta 4: Tabla de amortización
        JPanel cardAmort = createCard(
                "Tabla de amortización",
                "<html>Consulta cuotas pendientes y<br/>montos del financiamiento<br/>aprobado.</html>"
        );
        cardAmort.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Mortizacion().setVisible(true);
                dispose();
            }
        });

        // Tarjeta 5: Facturas
        JPanel cardFacturas = createCard(
                "Facturas",
                "<html>Consulta las facturas emitidas<br/>a un cliente y sus estados.</html>"
        );
        cardFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Factura().setVisible(true);
                dispose();
            }
        });

        cardsContainer.add(cardCatalogo);
        cardsContainer.add(cardVenta);
        cardsContainer.add(cardEstado);
        cardsContainer.add(cardAmort);
        cardsContainer.add(cardFacturas);

        menuPanel.add(cardsContainer);

        rootPanel.add(menuPanel);
    }

    private JPanel createCard(String titulo, String descripcionHtml) {
        JPanel card = new JPanel();
        card.setBackground(new Color(30, 35, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 60, 75), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(40, 45, 55));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 80, 95), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(30, 35, 45));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 60, 75), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        JLabel lblTitulo = new JLabel("<html>" + titulo + "</html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblDesc = new JLabel(descripcionHtml);
        lblDesc.setForeground(new Color(180, 190, 205));
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDesc);

        return card;
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new userMenu().setVisible(true));
    }
}
