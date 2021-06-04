package by.bank.client.ui.view;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.client.ui.dlg.UserDlg;
import by.bank.client.ui.table.UserTable;
import by.bank.common.entity.User;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class UserView extends BasicView {

    private final Service service;

    private final Frame window;
    private UserTable tblUsers;

    public UserView(Frame window, Service service) {
        this.window = window;
        this.service = service;


        initialize();
        update();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        tblUsers = new UserTable();
        addMainContent(new JScrollPane(tblUsers));

        JButton btnUpdate = new JButton("Обновить");
        JButton btnCreate = new JButton("Добавить");
        JButton btnEdit = new JButton("Изменить");
        JButton btnDelete = new JButton("Удалить");
        addButtonPanel(btnUpdate, btnCreate, btnEdit, btnDelete);

        btnUpdate.addActionListener(e -> update());
        btnCreate.addActionListener(e -> create());
        btnEdit.addActionListener(e -> edit());
        btnDelete.addActionListener(e -> delete());
    }

    private void update() {
        try {
            List<User> users = service.getUsers();
            tblUsers.getTableModel().setUsers(users);
        } catch (ServiceException e) {
            showError(e.getMessage());
        }
    }

    private void create() {
        User user = new User();
        UserDlg dlg = new UserDlg(window, user, service);
        if (dlg.isConfirmed()) {
            update();
        }
    }

    private void edit() {
        int index = tblUsers.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        User expert = tblUsers.getTableModel().getRow(index);
        UserDlg dlg = new UserDlg(window, expert, service);
        if (dlg.isConfirmed()) {
            update();
        }
    }

    private void delete() {
        int index = tblUsers.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        User user = tblUsers.getTableModel().getRow(index);
        if (confirmRemoval()) {
            try {
                service.deleteUser(user);
                update();
            } catch (ServiceException ex) {
                showError(ex.getMessage());
            }
        }
    }
}
