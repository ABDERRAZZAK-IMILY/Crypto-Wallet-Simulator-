package com.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ui.Main;
import com.util.AppLogger;

public class JDBCpostgresconnect {

    private static final Logger logger = AppLogger.getLogger(Main.class.getName());

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static JDBCpostgresconnect instance;
    private Connection connection;

    private JDBCpostgresconnect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Connection successfully");
        } catch (ClassNotFoundException e) {
            logger.severe("PostgreSQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static JDBCpostgresconnect getInstance() {
        if (instance == null) {
            instance = new JDBCpostgresconnect();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connection closed successfully");
            } catch (SQLException e) {
                logger.severe("Error closing connection: " + e.getMessage());
            }
        }
    }
}
