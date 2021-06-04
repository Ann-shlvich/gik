package by.bank.client.ui.table;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public abstract class BasicModel implements TableModel {
    protected EventListenerList listenerList = new EventListenerList();

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public synchronized void fireTableDataChanged() {
        fireTableDataChanged(new TableModelEvent(this));
    }

    public synchronized void fireTableDataChanged(int rowIndex) {
        fireTableDataChanged(new TableModelEvent(this, rowIndex));
    }

    protected void fireTableDataChanged(TableModelEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener)listeners[i+1]).tableChanged(e);
            }
        }
    }
}
