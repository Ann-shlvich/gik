package by.bank.client.ui.view;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.client.ui.dlg.ExaminationDlg;
import by.bank.client.ui.dlg.OpinionDlg;
import by.bank.client.ui.dlg.SolutionDlg;
import by.bank.client.ui.table.ExaminationTable;
import by.bank.common.entity.Examination;
import by.bank.common.entity.Opinion;
import by.bank.common.entity.Solution;


import javax.swing.*;
import java.awt.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ExaminationView extends BasicView {

    private final Service service;

    private final Frame window;
    private ExaminationTable tblExaminations;

    public ExaminationView(Frame window, Service service) {
        this.window = window;
        this.service = service;

        initialize();
        update();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        tblExaminations = new ExaminationTable();
        addMainContent(new JScrollPane(tblExaminations));

        JButton btnUpdate = new JButton("Обновить");
        JButton btnCreate = new JButton("Добавить");
        JButton btnEdit = new JButton("Изменить");
        JButton btnDelete = new JButton("Удалить");
        JButton btnOpine = new JButton("Оценить");
        JButton btnCalculate = new JButton("Рассчитать");
       // JButton btnGraph = new JButton("График");

        switch (service.getRole()) {
            case ADMIN:
                addButtonPanel(btnUpdate, btnCreate, btnEdit, btnDelete, btnCalculate);
                break;
            case EXPERT:
                addButtonPanel(btnUpdate, btnOpine, btnCalculate);
                break;
            case USER:
                addButtonPanel(btnUpdate, btnCalculate);
                break;
            default:
                addButtonPanel(btnUpdate);
        }

        btnUpdate.addActionListener(e -> update());
        btnCreate.addActionListener(e -> create());
        btnEdit.addActionListener(e -> edit());
        btnDelete.addActionListener(e -> delete());
        btnOpine.addActionListener(e -> opine());
        btnCalculate.addActionListener(e -> calculate());
        //btnGraph.addActionListener(e -> paint());
    }

    private void update() {
        try {
            List<Examination> examinations = service.getExaminations();
            tblExaminations.getTableModel().setExaminations(examinations);
        } catch (ServiceException e) {
            showError(e.getMessage());
        }

    }

    private void create() {
        Examination examination = new Examination();
        ExaminationDlg dlg = new ExaminationDlg(window, examination, service);
        if (dlg.isConfirmed()) {
            update();
        }
    }

    private void edit() {
        int index = tblExaminations.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        Examination examination = tblExaminations.getTableModel().getRow(index);
        if (examination.hasOpinions()) {
            showError("Нельзя редактировать экспертизу с оценками");
            return;
        }
        try {
            examination = service.getExamination(examination);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        ExaminationDlg dlg = new ExaminationDlg(window, examination, service);
        if (dlg.isConfirmed()) {
            update();
        }
    }

    private void delete() {
        int index = tblExaminations.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        Examination examination = tblExaminations.getTableModel().getRow(index);
        if (confirmRemoval()) {
            try {
                service.deleteExamination(examination);
            } catch (ServiceException ex) {
                showError(ex.getMessage());
                return;
            }
            update();
        }
    }

    private void opine() {
        int index = tblExaminations.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        Examination examination = tblExaminations.getTableModel().getRow(index);
        LocalDate today = LocalDate.now();
        if (today.isBefore(examination.getStartDate())) {
            showError("Экспертиза еще не доступна для оценивания!");
            return;
        } else if (today.isAfter(examination.getEndDate())) {
            showError("Прием оценок для экспертизы уже завершен!");
            return;
        }
        try {
            examination = service.getExamination(examination);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        Opinion opinion = new Opinion();
        opinion.setExaminationId(examination.getId());
        opinion.setUserId(service.getCurrentUser().getId());
        try {
            opinion = service.getOpinion(opinion);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        OpinionDlg dlg = new OpinionDlg(window, examination, opinion, service);
        if (dlg.isConfirmed()) {
            update();
        }
    }

    private void calculate() {
        int index = tblExaminations.getSelectedRow();
        if (index < 0) {
            showError("Запись не выбрана!");
            return;
        }
        Examination examination = tblExaminations.getTableModel().getRow(index);
        Solution solution;
        try {
            solution = service.calculate(examination);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        new SolutionDlg(window, solution);
    }

}


