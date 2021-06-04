package by.bank.client.ui.dlg;

import by.bank.common.entity.Solution;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

public class SolutionDlg extends BasicDlg {

    private static final Color [] COLORS = { Color.BLUE, Color.MAGENTA };

    private Solution solution;

    public SolutionDlg(Frame owner, Solution solution) {
        super(owner, "Решение: " + solution.getTitle());
        this.solution = solution;

        setSize(500, 500);
        setLocationRelativeTo(owner);
        setResizable(true);

        initialize();
        setVisible(true);
    }

    @Override
    protected void initialize() {
        JTextArea txtDescription = new JTextArea();
        txtDescription.setEditable(false);
        txtDescription.setText(solution.getDescription());
        JScrollPane scrDescription = new JScrollPane(txtDescription);
        scrDescription.setBorder(BorderFactory.createTitledBorder("Описание"));
        addFiller(0, scrDescription);

        Set<Solution.Item> items = solution.getItems();
        JPanel pnlList = new JPanel();
        HistogramPanel pnlHist = new HistogramPanel();
        pnlList.setBorder(BorderFactory.createTitledBorder("Рейтинг альтернатив"));
        pnlList.setLayout(new GridLayout(items.size(), 1));
        int row = 0;
        for (Solution.Item it : items) {
            JLabel text = new JLabel(String.format("%.3f A%d: %s", it.getRate(), it.getOrdinal(), it.getDescription()));
            if (row == 0) {
                text.setForeground(COLORS[0]);
            }
            pnlList.add(text);
            pnlHist.addHistogramColumn(String.format("A%d", it.getOrdinal()), (int)(100.0 * it.getRate()), (row == 0 ? COLORS[0] : COLORS[1]));
            row++;
        }
        pnlHist.layoutHistogram();
        addField(1, pnlList);
        addFiller(2, pnlHist);

        JButton btnReport = new JButton("Отчет");
        JButton btnClose = new JButton("Закрыть");
        addButtonPanel(3, btnReport, btnClose);

        btnReport.addActionListener(e -> report());
        btnClose.addActionListener(e -> close());
    }

    private void report() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text files";
            }
        });
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                PrintStream prn = new PrintStream(chooser.getSelectedFile(), "windows-1251");
                prn.println(solution.getTitle());
                prn.println(solution.getDescription());
                prn.println("\nРезультаты расчета:");
                for (Solution.Item it : solution.getItems()) {
                    prn.printf("%.3f\t%s%n", it.getRate(), it.getFullDescription());
                }
                prn.close();
            } catch (IOException ignored) {
                showError("Ошибка создания отчета!");
            }
        }
    }

    private void close() {
        dispose();
    }
}
