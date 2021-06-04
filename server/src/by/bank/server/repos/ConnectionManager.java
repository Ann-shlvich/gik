package by.bank.server.repos;

import by.bank.server.helper.Configurator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    private static final int MAX_TIMEOUT = 1000;
    private static ConnectionManager INSTANCE;

    private String url;
    private String user;
    private String password;
    private int maxSize;
    private List<Connection> connections;

    private ConnectionManager() {
        maxSize = Configurator.getPoolSize();
        url = Configurator.getDatabaseUrl();
        user = Configurator.getDatabaseUser();
        password = Configurator.getDatabasePassword();
        connections = new ArrayList<>();
    }

    public static  ConnectionManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ConnectionManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConnectionManager();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized Connection getConnection() throws SQLException {
        Connection con = null;
        if (connections.isEmpty()) {
            con = createConnection();
        } else {
            con = connections.remove(connections.size() - 1);
        }
        if (!con.isValid(MAX_TIMEOUT)) {
            con = createConnection();
        }
        return con;
    }

    public synchronized void releaseConnection(Connection con) {
        try {
            if (con != null && connections.size() < maxSize && con.isValid(MAX_TIMEOUT)) {
                connections.add(con);
            } else {
                closeConnection(con);
            }
        } catch (SQLException ignored) {
        }
    }

    public synchronized void shutdown() {
        for (Connection con : connections) {
            closeConnection(con);
        }
        connections.clear();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
