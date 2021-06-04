package by.bank.client.ui;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.client.ui.dlg.LoginDlg;
import by.bank.client.ui.dlg.PasswordDlg;
import by.bank.client.ui.view.ExaminationView;
import by.bank.client.ui.view.UserView;
import by.bank.common.entity.Role;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final Dimension MIN_DIMENSION = new Dimension(1024, 640);

    private Service service;

    private JMenuItem miLogin;
    private JMenuItem miLogout;
    private JMenuItem miPassword;
    private JLabel lblStatus;
    private JTabbedPane tabs;

    public MainWindow() {
        super("Экспертная система банка ");
       // super.setBackground(Color.CYAN);

        this.getContentPane().setBackground(Color.pink);

        this.service = new Service();

        setSize(MIN_DIMENSION);
        setMinimumSize(MIN_DIMENSION);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        initialize();
        setVisible(true);
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        tabs = new JTabbedPane();
        lblStatus = new JLabel("");


        lblStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        add(tabs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
        add(lblStatus, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        initializeMenu();
        updateMenu();
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.CYAN);
        JMenu menuFile = new JMenu("Меню");
        miLogin = new JMenuItem("Войти");
        miLogin.addActionListener(e -> login());
        menuFile.add(miLogin);

        miLogout = new JMenuItem("Выйти");
        miLogout.addActionListener(e -> logout());
        menuFile.add(miLogout);

        miPassword = new JMenuItem("Смена пароля");
        miPassword.addActionListener(e -> changePassword());
        menuFile.add(miPassword);

        menuFile.addSeparator();
        JMenuItem it = new JMenuItem("Закрыть");
        it.addActionListener(e -> exit());
        menuFile.add(it);

        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    }

    private void updateMenu() {
        boolean isLogged = service.isConnected() && service.isLogged();
        miLogin.setEnabled(!isLogged);
        miLogout.setEnabled(isLogged);
        miPassword.setEnabled(isLogged);
        if (isLogged) {
            lblStatus.setText(String.format("%s (%s)", service.getUserFullName(),
                    service.getRole().toString()));
        } else {
            lblStatus.setText(Role.NONE.toString());
        }
    }

    private void updateContent() {
        tabs.removeAll();
        if (service.isLogged()) {
            if (service.getRole() == Role.ADMIN) {
                tabs.addTab("Пользователи", new UserView(this, service));
            }
            tabs.addTab("Экспертизы", new ExaminationView(this, service));
        }
        revalidate();
        repaint();
    }

    private void login() {
        new LoginDlg(this, service);
        updateMenu();
        updateContent();
    }

    private void logout() {
        if (service.isLogged()) {
            try {
                service.logout();
            } catch (ServiceException ignored) {
            }
        }
        updateMenu();
        updateContent();
    }

    private void changePassword() {
        new PasswordDlg(this, service);
    }

    private void exit() {
        if (service.isLogged()) {
            try {
                service.logout();
            } catch (ServiceException ignored) {
            }
        }
        System.exit(0);
    }
}
