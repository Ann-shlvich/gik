package by.bank.client.ui.table;

import by.bank.common.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserModel extends BasicModel {

    private static final String [] COLUMNS = {"ФИО пользователя", "Телефон", "Аккаунт", "Роль" };

    private final List<User> users = new ArrayList<>();

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getFullName();
            case 1:
                return user.getPhone();
            case 2:
                return user.getLogin();
            case 3:
                return user.getRole().toString();
        }
        return null;
    }

    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        fireTableDataChanged();
    }

    public User getRow(int rowIndex) {
        return rowIndex < users.size() ? users.get(rowIndex) : null;
    }
}
