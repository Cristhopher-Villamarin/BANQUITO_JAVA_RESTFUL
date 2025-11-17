package ec.edu.monster.view;

import ec.edu.monster.controller.LoginController;
import ec.edu.monster.model.LoginResponse;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;

public class Login extends JFrame {

    private static final Logger logger = Logger.getLogger(Login.class.getName());

    private final LoginController loginController = new LoginController();

    // Componentes
    private JPanel rootPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblError;

    public Login() {
        initComponents();
        setLocationRelativeTo(null); // centrar
    }

    private void initComponents() {
        // Frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Comercializadora MONSTER - Login");
        setSize(1100, 600);
        setMinimumSize(new Dimension(1000, 550));

        rootPanel = new JPanel(new GridLayout(1, 2));
        rootPanel.setBackground(new Color(4, 16, 32)); // fondo oscuro
        getContentPane().add(rootPanel);

        initLeftPanel();
        initRightPanel();
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(5, 22, 46));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblBadge = new JLabel("PLATAFORMA COMERCIAL & FINANCIERA");
        lblBadge.setForeground(new Color(180, 195, 210));
        lblBadge.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblTitulo1 = new JLabel("COMERCIALIZADORA");
        lblTitulo1.setForeground(Color.WHITE);
        lblTitulo1.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel lblTitulo2 = new JLabel("MONSTER");
        lblTitulo2.setForeground(Color.WHITE);
        lblTitulo2.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel lblSub = new JLabel("Ventas, crédito directo y monitoreo de créditos.");
        lblSub.setForeground(new Color(190, 200, 210));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        leftPanel.add(lblBadge);
        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(lblTitulo1);
        leftPanel.add(lblTitulo2);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(lblSub);
        leftPanel.add(Box.createVerticalGlue());

        rootPanel.add(leftPanel);
    }

    private void initRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setBackground(new Color(9, 12, 24));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GroupLayout layout = new GroupLayout(rightPanel);
        rightPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblBienvenido = new JLabel("BIENVENIDO");
        lblBienvenido.setForeground(new Color(160, 170, 185));
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblInicia = new JLabel("Inicia sesión");
        lblInicia.setForeground(Color.WHITE);
        lblInicia.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel lblDesc = new JLabel("Autoriza tu ingreso para continuar con los procesos comerciales.");
        lblDesc.setForeground(new Color(170, 180, 195));
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblUsuario = new JLabel("USUARIO");
        lblUsuario.setForeground(new Color(170, 180, 195));
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtUsuario = new JTextField();
        txtUsuario.setBackground(new Color(15, 20, 35));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        // Panel para USUARIO
        JPanel userFieldPanel = new JPanel(new BorderLayout());
        userFieldPanel.setOpaque(false);

        JLabel userIcon = new JLabel("👤");
        userIcon.setForeground(new Color(170, 180, 195));
        userIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8)); // separación

        userFieldPanel.add(userIcon, BorderLayout.WEST);
        userFieldPanel.add(txtUsuario, BorderLayout.CENTER);


        JLabel lblPassword = new JLabel("CONTRASEÑA");
        lblPassword.setForeground(new Color(170, 180, 195));
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(15, 20, 35));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        // borde: línea + padding
        Border innerPadding = BorderFactory.createEmptyBorder(8, 10, 8, 10);
        Border whiteLine   = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border fieldBorder = BorderFactory.createCompoundBorder(whiteLine, innerPadding);

        txtUsuario.setBorder(fieldBorder);
        txtPassword.setBorder(fieldBorder);
// Panel para PASSWORD
        JPanel passFieldPanel = new JPanel(new BorderLayout());
        passFieldPanel.setOpaque(false);

        JLabel lockIcon = new JLabel("🔒");
        lockIcon.setForeground(new Color(170, 180, 195));
        lockIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));

        passFieldPanel.add(lockIcon, BorderLayout.WEST);
        passFieldPanel.add(txtPassword, BorderLayout.CENTER);
        btnIngresar = new JButton("INGRESAR") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Color de fondo según estado
            if (getModel().isPressed()) {
                g2.setColor(new Color(0, 133, 225));
            } else {
                g2.setColor(new Color(0, 153, 255));
            }

            // Rectángulo redondeado (cambia 30 para más o menos redondeado)
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();

            super.paintComponent(g);
        }
    };
            btnIngresar.setContentAreaFilled(false);
            btnIngresar.setBorderPainted(false);
            btnIngresar.setFocusPainted(false);
            btnIngresar.setOpaque(false);
            btnIngresar.setForeground(Color.WHITE);
            btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnIngresar.setPreferredSize(new Dimension(420, 42));
              // más largo y un pelín más alto


        btnIngresar.addActionListener(e -> hacerLogin());

        lblError = new JLabel(" ");
        lblError.setForeground(new Color(255, 90, 90));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblBienvenido)
                .addComponent(lblInicia)
                .addComponent(lblDesc)
                .addGap(20)
                .addComponent(lblUsuario)
                .addComponent(txtUsuario)
                .addGap(10)
                .addComponent(lblPassword)
                .addComponent(txtPassword)
                .addGap(10)
                .addComponent(lblError)
                .addComponent(btnIngresar, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(lblBienvenido)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInicia)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDesc)
                .addGap(30)
                .addComponent(lblUsuario)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuario, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(lblPassword)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(lblError)
                .addGap(20)
                .addComponent(btnIngresar, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(40)
        );

        rootPanel.add(rightPanel);
    }

    private void hacerLogin() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        lblError.setText(" ");

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Debe ingresar usuario y contraseña.");
            return;
        }

        try {
            LoginResponse resp = loginController.login(username, password);

            if (resp != null && resp.isAutenticado()) {
                String rol = resp.getRol() != null ? resp.getRol() : "";

                if ("USER".equalsIgnoreCase(rol)) {
                    new userMenu().setVisible(true);
                    this.dispose();
                } else if ("ADMIN".equalsIgnoreCase(rol)) {
                    new adminInicio().setVisible(true);
                    this.dispose();
                } else {
                    lblError.setText("Rol no reconocido: " + rol);
                }
            } else {
                String msg = (resp != null && resp.getMensaje() != null)
                        ? resp.getMensaje()
                        : "Credenciales inválidas";
                lblError.setText(msg);
            }

        } catch (IOException | ParseException ex) {
            logger.log(Level.SEVERE, "Error al iniciar sesión", ex);
            lblError.setText("Error de conexión: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        /* Nimbus look & feel */
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

        EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}
