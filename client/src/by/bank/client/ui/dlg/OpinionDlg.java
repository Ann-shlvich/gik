package by.bank.client.ui.dlg;

import by.bank.client.service.Service;
import by.bank.client.service.ServiceException;
import by.bank.common.entity.Alternative;
import by.bank.common.entity.Examination;
import by.bank.common.entity.Opinion;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class OpinionDlg extends BasicDlg {

    private Examination examination;
    private Opinion opinion;
    private Service service;

    private Frame owner;
    private JTextArea txtDescription;
    private DefaultListModel<Alternative> mdlAlternatives;
    private JList<Alternative> lstAlternatives;

    public OpinionDlg(Frame owner, Examination examination, Opinion opinion, Service service) {
        super(owner, examination.getTitle());
        this.service = service;
        this.examination = examination;
        this.opinion = opinion;

        setSize(500, 500);
        setLocationRelativeTo(owner);

        initialize();
        setVisible(true);
    }

    @Override
    protected void initialize() {
        txtDescription = new JTextArea(5, 30);
        txtDescription.setEditable(false);
        txtDescription.setText(examination.getDescription());
        JScrollPane scrDescription = new JScrollPane(txtDescription);
        scrDescription.setBorder(BorderFactory.createTitledBorder("Описание"));
        addFiller(0, new JScrollPane(txtDescription));
        addField(1, new JLabel("Расположите альтернативы в порядке убывания их приоритета"));

        mdlAlternatives = new DefaultListModel<>();
        lstAlternatives = new JList<>(mdlAlternatives);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Альтернативы"));
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(lstAlternatives), BorderLayout.CENTER);

        JButton btnUp = new JButton("Поднять");
        JButton btnDwn = new JButton("Опустить");
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnl.add(btnUp);
        pnl.add(btnDwn);
        panel.add(pnl, BorderLayout.SOUTH);
        addFiller(2, panel);

        btnUp.addActionListener(e -> moveUp());
        btnDwn.addActionListener(e -> moveDown());

        JButton btnSave = new JButton("Сохранить");
        JButton btnDelete = new JButton("Удалить");
        JButton btnCancel = new JButton("Отмена");
        if (opinion.isNew()) {
            addButtonPanel(3, btnSave, btnCancel);
        } else {
            addButtonPanel(3, btnSave, btnDelete, btnCancel);
        }

        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnCancel.addActionListener(e -> cancel());

        final Map<Integer, Alternative> alts = new HashMap<>();
        examination.getAlternatives().forEach(a -> alts.put(a.getOrdinal(), a));
        int altCount = examination.getAlternatives().size();
        if (opinion.isNew() || opinion.getData() == null) {
            loadDefaultOrder(alts, altCount);
        } else {
            try {
                DataInputStream ins = new DataInputStream(new ByteArrayInputStream(opinion.getData()));
                for (int i = 0; i < altCount; i++) {
                    int num = ins.readInt();
                    mdlAlternatives.addElement(alts.get(num));
                }
                ins.close();
            } catch (IOException ignored) {
                loadDefaultOrder(alts, altCount);
            }
        }
    }

    private void loadDefaultOrder(Map<Integer, Alternative> alts, int altCount) {
        mdlAlternatives.removeAllElements();
        for (int i = 0; i < altCount; i++) {
            mdlAlternatives.addElement(alts.get(i+1));
        }
    }

    private void moveUp() {
        int index = lstAlternatives.getSelectedIndex();
        if (index == 0) {
            return;
        }
        exchangeRows(index, index - 1);
    }

    private void moveDown() {
        int index = lstAlternatives.getSelectedIndex();
        if (index == mdlAlternatives.getSize() - 1) {
            return;
        }
        exchangeRows(index, index + 1);
    }

    private void exchangeRows(int ind1, int ind2) {
        Alternative alt1 = mdlAlternatives.getElementAt(ind1);
        Alternative alt2 = mdlAlternatives.getElementAt(ind2);
        mdlAlternatives.setElementAt(alt1, ind2);
        mdlAlternatives.setElementAt(alt2, ind1);
        lstAlternatives.setSelectedIndex(ind2);
    }

    private void cancel() {
        dispose();
    }

    private void save() {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream outs = new DataOutputStream(bytes);
            for (int i = 0; i < mdlAlternatives.getSize(); i++) {
                Alternative alt = mdlAlternatives.getElementAt(i);
                outs.writeInt(alt.getOrdinal());
            }
            opinion.setData(bytes.toByteArray());
        } catch (IOException ignored) {
            showError("Ошибка сохранения данных!");
            return;
        }
        try {
            service.saveOpinion(opinion);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        confirmed = true;
        dispose();
    }

    private void delete() {
        if (JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить оценку?", "Вопрос",
                JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        try {
            service.deleteOpinion(opinion);
            confirmed = true;
        } catch (ServiceException ex) {
            showError(ex.getMessage());
            return;
        }
        confirmed = true;
        dispose();
    }
}
