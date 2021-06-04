package by.bank.server;

import by.bank.server.core.SessionProcessor;
import by.bank.server.helper.Configurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String [] args) {
        if (!Configurator.validate()) {
            System.out.println("Отсутствует конфигурация сервера!");
            return;
        }
        try {
            ServerSocket serverSocket = new ServerSocket(Configurator.getPort(), 5);
            System.out.println("Сервер запущен на порте " + Configurator.getPort());
            while (true) {
                Socket socket = serverSocket.accept();
                new SessionProcessor(socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка сервера: " + e.getMessage());
        }

    }
}
