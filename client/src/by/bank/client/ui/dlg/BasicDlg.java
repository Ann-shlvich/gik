package by.bank.client.ui.dlg;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class BasicDlg extends JDialog {

    protected boolean confirmed;

    public BasicDlg(Frame owner, String title) {
        super(owner, title, true);

        setLayout(new GridBagLayout());
        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    protected abstract void initialize();

    protected void addField(int row, String caption, JComponent cmp) {
        add(new JLabel(caption), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        add(cmp, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
    }

    protected void addField(int row, JComponent cmp) {
        add(cmp, new GridBagConstraints(0, row, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    }

    protected void addButtonPanel(int row, JButton ... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        Arrays.stream(buttons).forEach(panel::add);
        add(panel, new GridBagConstraints(0, row, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    protected void addFiller(int row, JComponent cmp) {
        add(cmp, new GridBagConstraints(0, row, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    protected void addFiller(int row) {
        addFiller(row, new JLabel(""));
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }
}
