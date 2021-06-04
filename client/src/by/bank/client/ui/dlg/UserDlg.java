package by.bank.client.ui.dlg;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.common.entity.Role;
import by.bank.common.entity.User;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UserDlg extends BasicDlg {

    private Service service;
    private User user;

    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JComboBox<Role> cmbRole;
    private JCheckBox chkReset;

    public UserDlg(Frame owner, User user, Service service) {
        super(owner, "Редактирование пользователя");
        this.user = user;
        this.service = service;

        setSize(300, 270);
        setLocationRelativeTo(owner);

        initialize();
        setVisible(true);
    }

    protected void initialize() {
        txtFullName = new JTextField();
        txtEmail = new JTextField();
        txtLogin = new JTextField();
        cmbRole = new JComboBox<>();
        Arrays.stream(Role.values()).filter(r -> r != Role.NONE).forEach(cmbRole::addItem);
        chkReset = new JCheckBox("Сброс пароля нв логин пользователя");

        addField(0, "ФИО пользователя:", txtFullName);
        addField(1, "Телефон:", txtEmail);
        addField(2, "Логин:", txtLogin);
        addField(3, "Режим доступа:", cmbRole);
        addField(4, chkReset);
        addFiller(5);

        JButton btnSave = new JButton("Сохранить");
        JButton btnCancel = new JButton("Отменить");
        addButtonPanel(6, btnSave, btnCancel);
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());

        if (user.isNew()) {
            chkReset.setSelected(true);
            chkReset.setEnabled(false);
        } else {
            txtFullName.setText(user.getFullName());
            txtEmail.setText(user.getPhone());
            txtLogin.setText(user.getLogin());
            cmbRole.setSelectedItem(user.getRole());
            txtLogin.setEditable(false);
        }
    }

    private void cancel() {
        dispose();
    }

    private void save() {
        String fullName = txtFullName.getText().trim();
        if (fullName.isEmpty() || fullName.length() > 40) {
            showError("Имя пользователя указано неверно!");
            txtFullName.requestFocus();
            return;
        }
        String phone = txtEmail.getText().trim();
        if (phone.isEmpty() || phone.length() > 20) {
            showError("Телефон пользователя указан неверно!");
            txtEmail.requestFocus();
            return;
        }
        String login = txtLogin.getText().trim();
        if (login.isEmpty() || login.length() > 20) {
            showError("Логин пользователя указан неверно!");
            txtLogin.requestFocus();
            return;
        }
        Role role = (Role) cmbRole.getSelectedItem();
        if (role == null) {
            showError("Логин пользователя указан неверно!");
            cmbRole.requestFocus();
            return;
        }
        User value = new User();
        value.setId(user.getId());
        value.setFullName(fullName);
        value.setPhone(phone);
        value.setLogin(login);
        value.setRole(role);
        if (value.isNew() || chkReset.isSelected()) {
            value.setPassword(login);
        } else {
            value.setPassword("");
        }
        try {
            if (value.isNew()) {
                service.createUser(value);
            } else {
                service.updateUser(value);
            }
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        confirmed = true;
        dispose();
    }
}
