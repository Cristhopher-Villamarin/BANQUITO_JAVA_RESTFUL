package ec.edu.monster.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setBackground(new Color(30, 30, 45));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(150, 80, 80), 1));
        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText(value.toString());
        return this;
    }
}
