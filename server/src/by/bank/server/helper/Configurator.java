package by.bank.server.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurator {

    private static final String CONFIG_FILE_NAME = "server.properties";
    private static final String SERVER_PORT_PARAM = "tcp.port";
    private static final String DATABASE_URL_PARAM = "db.url";
    private static final String DATABASE_USER_PARAM = "db.user";
    private static final String DATABASE_PASSWORD_PARAM = "db.password";
    private static final String DATABASE_POOL_PARAM = "db.pool";

    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(new FileInputStream(CONFIG_FILE_NAME));
        } catch (IOException ignored) {
        }
    }

    public static boolean validate() {
        if (props.getProperty(DATABASE_URL_PARAM, "").isEmpty() ||
                props.getProperty(DATABASE_USER_PARAM, "").isEmpty()) {
            return false;
        }
        return true;
    }

    public static int getPort() {
        try {
            return Integer.parseInt(props.getProperty(SERVER_PORT_PARAM, "7007"));
        } catch (NumberFormatException ignored) {
            return 7007;
        }
    }

    public static String getDatabaseUrl() {
        return props.getProperty(DATABASE_URL_PARAM);
    }

    public static String getDatabaseUser() {
        return props.getProperty(DATABASE_USER_PARAM);
    }

    public static String getDatabasePassword() {
        return props.getProperty(DATABASE_PASSWORD_PARAM);
    }

    public static int getPoolSize() {
        try {
            return Integer.parseInt(props.getProperty(DATABASE_POOL_PARAM, "10"));
        } catch (NumberFormatException ignored) {
            return 10;
        }
    }

}
