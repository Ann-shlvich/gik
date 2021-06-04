package by.bank.server.core;

import by.bank.common.service.*;
import by.bank.server.helper.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SessionProcessor extends Thread {

    private static int SESSION_COUNT = 0;

    private Socket socket;
    private Logger logger;
    private String client;

    public SessionProcessor(Socket socket) {
        this.socket = socket;
        this.logger = Logger.getInstance();
        this.client = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

        start();
    }

    public void run() {
        logger.log(client + ": подключение");
        logger.log("Количество подключений: " + ++SESSION_COUNT);

        boolean flag = true;
        ObjectOutputStream outs;
        ObjectInputStream ins;
        ProcessorManager manager = new ProcessorManager();
        try {
            outs = new ObjectOutputStream(socket.getOutputStream());
            ins = new ObjectInputStream(socket.getInputStream());

            do {
                Request request = null;
                Response response = null;
                try {
                    request = (Request) ins.readObject();
                    flag = request.getRequestType() != RequestType.LOGOUT;
                    Processor processor = manager.getRequestProcessor(request.getRequestType());
                    if (processor == null) {
                        throw new ClassNotFoundException("Ошибка обработки запросов типа " + request.getRequestType());
                    }
                    response = processor.process(request);
                } catch (ClassNotFoundException | ClassCastException ex) {
                    logger.log(client + ": неверный запрос: " + ex.getMessage());
                    response = new SimpleResponse(ResponseType.ERROR, "Неверный запрос");
                }
                outs.writeObject(response);
            } while (flag);
            socket.shutdownInput();
            socket.shutdownOutput();
            manager.shutdown();
        } catch (IOException ignored) {
            if (flag) {
                logger.log(client + ": ошибка соединения");
            }
        } finally {
            logger.log(client + ": отключение клиента");
            logger.log("Количество подключений: " + --SESSION_COUNT);
        }
    }
}
