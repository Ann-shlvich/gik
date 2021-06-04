package by.bank.client.ui.table;

import by.bank.common.entity.Examination;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExaminationModel extends BasicModel {

    private static final String [] COLUMNS = {"Заголовок экспертизы", "Начало оценивания", "Завершение оценивания", "Количество оценок"};

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final List<Examination> examinations = new ArrayList<>();

    @Override
    public int getRowCount() {
        return examinations.size();
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
        Examination examination = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return examination.getTitle();
            case 1:
                return formatter.format(examination.getStartDate());
            case 2:
                return formatter.format(examination.getEndDate());
            case 3:
                return Integer.toString(examination.getOpinionCount());
        }
        return null;
    }

    public void setExaminations(List<Examination> experts) {
        this.examinations.clear();
        this.examinations.addAll(experts);
        fireTableDataChanged();
    }

    public Examination getRow(int rowIndex) {
        return rowIndex < examinations.size() ? examinations.get(rowIndex) : null;
    }
}
