package by.bank.client.ui.dlg;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.common.entity.Alternative;
import by.bank.common.entity.Examination;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExaminationDlg extends BasicDlg {

    private Service service;
    private Examination examination;

    private Frame owner;
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JSpinner spnStartDate;
    private JSpinner spnEndDate;
    private DefaultListModel<Alternative> mdlAlternatives;
    private JList<Alternative> lstAlternatives;

    public ExaminationDlg(Frame owner, Examination examination, Service service) {
        super(owner, "Редактирование экспертизы");
        this.service = service;
        this.examination = examination;

        this.setSize(500, 500);
        this.setLocationRelativeTo(owner);

        initialize();
        setVisible(true);
    }


    @Override
    protected void initialize() {
        txtTitle = new JTextField();
        txtDescription = new JTextArea(5, 30);
        SpinnerDateModel m1 = new SpinnerDateModel();
        SpinnerDateModel m2 = new SpinnerDateModel();
        spnStartDate = new JSpinner(m1);
        spnEndDate = new JSpinner(m2);
        spnStartDate.setEditor(new JSpinner.DateEditor(spnStartDate, "dd.MM.yyyy"));
        spnEndDate.setEditor(new JSpinner.DateEditor(spnEndDate, "dd.MM.yyyy"));
        JScrollPane scrDescription = new JScrollPane(txtDescription);
        scrDescription.setBorder(BorderFactory.createTitledBorder("Описание"));
        mdlAlternatives = new DefaultListModel<>();
        lstAlternatives = new JList<>(mdlAlternatives);

        addField(1, "Название:", txtTitle);
        addFiller(2, scrDescription);
        addField(3, "Начало опроса:", spnStartDate);
        addField(4, "Конец опроса:", spnEndDate);
        addFiller(5, createAlternativePanel());

        JButton btnSave = new JButton("Сохранить");
        JButton btnCancel = new JButton("Отменить");
        addButtonPanel(6, btnSave, btnCancel);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());

        if (!examination.isNew()) {
            txtTitle.setText(examination.getTitle());
            txtDescription.setText(examination.getDescription());
            spnStartDate.getModel().setValue(java.sql.Date.valueOf(examination.getStartDate()));
            spnEndDate.getModel().setValue(java.sql.Date.valueOf(examination.getEndDate()));
            for (Alternative alt : examination.getAlternatives()) {
                mdlAlternatives.addElement(alt);
            }
        }
    }

    private JPanel createAlternativePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Альтернативы"));
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(lstAlternatives), BorderLayout.CENTER);

        JButton btnUp = new JButton("Поднять");
        JButton btnDwn = new JButton("Опустить");
        JButton btnAdd = new JButton("Добавить");
        JButton btnEdt = new JButton("Изменить");
        JButton btnDel = new JButton("Удалить");
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridLayout(1, 4));
        pnl.add(btnUp);
        pnl.add(btnDwn);
        pnl.add(btnAdd);
        pnl.add(btnEdt);
        pnl.add(btnDel);
        panel.add(pnl, BorderLayout.SOUTH);

        btnUp.addActionListener(e -> moveUp());
        btnDwn.addActionListener(e -> moveDown());
        btnAdd.addActionListener(e -> addAlt());
        btnEdt.addActionListener(e -> edtAlt());
        btnDel.addActionListener(e -> delAlt());

        return panel;
    }

    private void cancel() {
        dispose();
    }

    private void save() {
        String title = txtTitle.getText().trim();
        if (title.isEmpty() || title.length() > 50) {
            showError("Заголовок экспертизы указан неверно!");
            txtTitle.requestFocus();
            return;
        }
        String description = txtDescription.getText();
        if (description.length() > 500) {
            showError("Превышена максимальная длина описания экспертизы!");
            txtDescription.requestFocus();
            return;
        }
        LocalDate startDate = ((Date) spnStartDate.getModel().getValue())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = ((Date) spnEndDate.getModel().getValue())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (startDate.isAfter(endDate)) {
            showError("Период приема оценок экспертов указан неверно!");
            spnStartDate.requestFocus();
            return;
        }
        int altCount = mdlAlternatives.getSize();
        if (altCount < 2) {
            showError("Указано недостаточное количество альтернатив!");
            return;
        }
        List<Alternative> list = new ArrayList<>();
        for (int i = 0; i < altCount; i++) {
            Alternative alt = mdlAlternatives.getElementAt(i);
            alt.setOrdinal(i + 1);
            list.add(alt);
        }
        Examination value = new Examination();
        value.setId(examination.getId());
        value.setTitle(title);
        value.setDescription(description);
        value.setStartDate(startDate);
        value.setEndDate(endDate);
        value.setAlternatives(list);
        try {
            if (value.isNew()) {
                service.createExamination(value);
            } else {
                service.updateExamination(value);
            }
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        confirmed = true;
        dispose();
    }

    private void moveUp() {
        int index = lstAlternatives.getSelectedIndex();
        if (index == 0) {
            return;
        }
        Alternative alt1 = mdlAlternatives.getElementAt(index);
        Alternative alt2 = mdlAlternatives.getElementAt(index - 1);
        mdlAlternatives.setElementAt(alt1, index - 1);
        mdlAlternatives.setElementAt(alt2, index);
        lstAlternatives.setSelectedIndex(index - 1);
    }

    private void moveDown() {
        int index = lstAlternatives.getSelectedIndex();
        if (index == mdlAlternatives.getSize() - 1) {
            return;
        }
        Alternative alt1 = mdlAlternatives.getElementAt(index);
        Alternative alt2 = mdlAlternatives.getElementAt(index + 1);
        mdlAlternatives.setElementAt(alt1, index + 1);
        mdlAlternatives.setElementAt(alt2, index);
        lstAlternatives.setSelectedIndex(index + 1);
    }

    private void addAlt() {
        String text = "";
        do {
            text = JOptionPane.showInputDialog("Введите описание альтернативы", text);
            if (text == null || text.isEmpty()) {
                return;
            }
            if (text.length() < 100) {
                break;
            }
            showError("Максмальная длина описания 100 символов!");
        } while (true);
        Alternative alt = new Alternative();
        alt.setDescription(text);
        mdlAlternatives.addElement(alt);
    }

    private void edtAlt() {
        int index = lstAlternatives.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String text = mdlAlternatives.getElementAt(index).getDescription();
        do {
            text = JOptionPane.showInputDialog("Введите описание альтернативы", text);
            if (text == null || text.isEmpty()) {
                return;
            }
            if (text.length() < 100) {
                break;
            }
            showError("Максмальная длина описания 100 символов!");
        } while (true);
        mdlAlternatives.getElementAt(index).setDescription(text);
    }

    private void delAlt() {
        int index = lstAlternatives.getSelectedIndex();
        if (index < 0) {
            return;
        }
        mdlAlternatives.removeElementAt(index);
    }
}
