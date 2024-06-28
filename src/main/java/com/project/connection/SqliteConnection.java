package com.project.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class SqliteConnection {
    protected static SqliteConnection base;
    protected static Connection connection;

    private SqliteConnection(String database)
            throws SQLException, ClassNotFoundException {
        try {

            String driver = "org.sqlite.JDBC";
            String inGetConnection = "jdbc:sqlite:/home/dm/NetBeansProjects/Katsaka/" + database + ".db";
            Class.forName(driver);
            connection = DriverManager.getConnection(inGetConnection);
            System.out.println("Sqlite");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String database)
            throws SQLException, ClassNotFoundException {
        if (connection == null) {
            base = new SqliteConnection(database);
        }
        connection.setAutoCommit(false);
        return connection;
    }

}
