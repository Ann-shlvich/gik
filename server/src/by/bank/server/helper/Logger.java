package by.bank.server.helper;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static Logger INSTANCE;

    private final PrintStream prn;

    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger(System.out);
                }
            }
        }
        return INSTANCE;
    }

    private Logger(PrintStream prn) {
        this.prn = prn;
    }

    public synchronized void log(String message) {
        prn.println(formatter.format(LocalDateTime.now()) + ":" + message);
    }

}
