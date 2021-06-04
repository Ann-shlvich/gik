package by.bank.client;

import by.bank.client.ui.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String [] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
