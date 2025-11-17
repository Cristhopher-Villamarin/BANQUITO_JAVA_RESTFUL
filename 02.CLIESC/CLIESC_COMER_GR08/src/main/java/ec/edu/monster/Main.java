package ec.edu.monster;

import ec.edu.monster.view.Login;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal para iniciar la aplicación de escritorio
 * Comercializadora Monster
 */
public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.WARNING, "No se pudo configurar Nimbus Look and Feel", ex);
        }

        // Iniciar la aplicación con la pantalla de Login
        java.awt.EventQueue.invokeLater(() -> {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
        });
    }
}
