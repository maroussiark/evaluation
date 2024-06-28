package com.project.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class OracleConnection {
    protected static OracleConnection base;
    protected static Connection connection;

    private OracleConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        try {
            String driver = "oracle.jdbc.driver.OracleDriver";
            String inGetConnection = "jdbc:oracle:thin:@localhost:1521:" + database; /// orcl = database
            Class.forName(driver);
            connection = DriverManager.getConnection(inGetConnection, user, password);
            // System.out.println("Oracle");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        if (connection == null) {
            base = new OracleConnection(database, user, password);
        }
        connection.setAutoCommit(false);
        return connection;
    }

    public static Connection getConnection(String user, String password)
            throws SQLException, ClassNotFoundException {
        return getConnection("orcl", user, password);
    }
}
