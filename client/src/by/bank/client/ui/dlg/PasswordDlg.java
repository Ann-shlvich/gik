package by.bank.client.ui.dlg;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;

import javax.swing.*;
import java.awt.*;

public class PasswordDlg extends BasicDlg {
    private Service service;

    private JPasswordField pwdOld;
    private JPasswordField pwdNew;
    private JPasswordField pwdConfirm;

    public PasswordDlg(Frame owner, Service service) {
        super(owner, "Изменение пароля");
        this.service = service;

        setSize(300, 150);
        setLocationRelativeTo(owner);

        initialize();
        setVisible(true);
    }

    protected void initialize() {
        setLayout(new GridBagLayout());

        pwdOld = new JPasswordField();
        pwdNew = new JPasswordField();
        pwdConfirm = new JPasswordField();

        addField(0, "Старый пароль:", pwdOld);
        addField(1, "Новый пароль:", pwdNew);
        addField(2, "Подтверждение:", pwdConfirm);

        JButton btnChange = new JButton("Изменить");
        JButton btnCancel = new JButton("Отмена");
        addFiller(3);
        addButtonPanel(4, btnChange, btnCancel);

        btnChange.addActionListener(e -> change());
        btnCancel.addActionListener(e -> cancel());
    }

    private void change() {
        final String oldPassword = new String(pwdOld.getPassword());
        final String newPassword = new String(pwdNew.getPassword());
        final String confirm = new String(pwdConfirm.getPassword());

        if (newPassword.isEmpty()) {
            showError("Новый пароль не может быть пустым!");
            return;
        }
        if (!newPassword.equals(confirm)) {
            showError("Новый пароль не совпадает с подтверждением!");
            return;
        }
        try {
            service.changePassword(oldPassword, newPassword);
        } catch (ServiceException e) {
            showInfo(e.getMessage());
        }
        dispose();
    }

    private void cancel() {
        dispose();
    }
}
