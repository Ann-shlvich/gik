package by.bank.client.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BasicView extends JPanel {

    public BasicView() {
        setLayout(new GridBagLayout());
    }

    protected void addMainContent(JComponent cmp) {
        add(cmp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    }

    protected void addButtonPanel(JButton ... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        Arrays.stream(buttons).forEach(panel::add);
        add(panel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmRemoval() {
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить запись?",
                "Вопрос", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return false;
        }
        return true;
    }
}
