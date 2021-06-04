package by.bank.client.ui.table;

import javax.swing.*;

public class BasicTable<T extends BasicModel> extends JTable {

    public BasicTable(T model) {
        super(model);

        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setRowSelectionAllowed(true);
        getTableHeader().setReorderingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public T getTableModel() {
        return (T) getModel();
    }
}
