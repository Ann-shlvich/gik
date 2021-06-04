package by.bank.client.ui.dlg;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoginDlg extends BasicDlg {

    private static final String HISTORY_FILE = "login.properties";
    private static final String PARAM_ADDRESS = "server.address";
    private static final String PARAM_LOGIN = "user.login";

    private Service service;

    private JTextField txtAddress;
    private JTextField txtLogin;
    private JPasswordField txtPassword;

    public LoginDlg(Frame owner, Service service) {
        super(owner, "Вход в систему");
        this.service = service;

        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(owner);

        initialize();
        getLastLogin();
        setVisible(true);
    }

    protected void initialize() {
        setLayout(new GridBagLayout());

        txtAddress = new JTextField();
        txtLogin = new JTextField();
        txtPassword = new JPasswordField();

        addField(0, "Сервер:", txtAddress);
        addField(1, "Логин:", txtLogin);
        addField(2, "Пароль:", txtPassword);
        addFiller(3);

        JButton btnCancel = new JButton("Отмена");
        JButton btnLogin = new JButton("Войти");
        addButtonPanel(4, btnLogin, btnCancel);

        btnCancel.addActionListener(e -> cancel());
        btnLogin.addActionListener(e -> login());
    }

    private void getLastLogin() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(HISTORY_FILE));
            txtAddress.setText(props.getProperty(PARAM_ADDRESS, ""));
            txtLogin.setText(props.getProperty(PARAM_LOGIN, ""));
        } catch (IOException ignored) {
        }
    }

    private void setLastLogin() {
        Properties props = new Properties();
        props.setProperty(PARAM_ADDRESS, txtAddress.getText().trim());
        props.setProperty(PARAM_LOGIN, txtLogin.getText().trim());
        try {
            props.store(new FileOutputStream(HISTORY_FILE), "Last successful login");
        } catch (IOException ignored) {
        }
    }

    private void cancel() {
        this.dispose();
    }

    private void login() {
        String address = txtAddress.getText().trim();
        String [] parts = address.split(":");
        if (parts.length != 2) {
            showError("Неверный ввод адреса!");
            return;
        }
        int port = 0;
        try {
            port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException ignored) {
            showError("Неверный ввод номера порта сервера!");
            return;
        }
        if (service.isConnected()) {
            service.disconnect();
        }
        if (!service.connect(parts[0], port)) {
            showError("Ошибка подключения к серверу!");
            return;
        }
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        try {
            service.login(login, password);
        } catch (ServiceException e) {
            showError(e.getMessage());
            service.disconnect();
            return;
        }
        setLastLogin();
        this.dispose();
    }
}
