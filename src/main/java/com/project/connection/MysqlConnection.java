package com.project.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class MysqlConnection {
    protected static MysqlConnection base;
    protected static Connection connection;

    private MysqlConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        try {

            String driver = "com.mysql.cj.jdbc.Driver";
            String inGetConnection = "jdbc:mysql://localhost:3306/" + database;
            Class.forName(driver);
            connection = DriverManager.getConnection(inGetConnection, user, password);
            System.out.println("Mysql");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        if (connection == null) {
            base = new MysqlConnection(database, user, password);
        }
        connection.setAutoCommit(false);
        return connection;
    }

}
