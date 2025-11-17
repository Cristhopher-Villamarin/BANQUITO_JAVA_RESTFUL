package ec.edu.monster.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {

    private JButton btn;
    private String action;
    private adminInicio parent;

    public ButtonEditor(String action, adminInicio parent) {
        super(new JTextField());
        this.action = action;
        this.parent = parent;

        btn = new JButton();
        btn.setOpaque(true);
        btn.setBackground(new Color(40, 35, 55));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));

        btn.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        btn.setText(action);

        btn.addActionListener(e -> {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());

            if ("ELIMINAR".equals(action)) {
                parent.eliminar(id);
            } else if ("EDITAR".equals(action)) {
                parent.editar(id);
            }
        });

        return btn;
    }
}
