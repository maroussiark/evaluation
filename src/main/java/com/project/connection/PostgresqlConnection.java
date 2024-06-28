package com.project.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
public class PostgresqlConnection {
    protected static PostgresqlConnection base;
    protected static Connection connection;

    private PostgresqlConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        try {
            String driver = "org.postgresql.Driver";
            String inGetConnection = "jdbc:postgresql://localhost:5432/" + database;
            Class.forName(driver);

            connection = DriverManager.getConnection(inGetConnection, user, password);
            // System.out.println("Postgres");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String database, String user, String password)
            throws SQLException, ClassNotFoundException {
        if (connection == null) {
            base = new PostgresqlConnection(database, user, password);
        }
        connection.setAutoCommit(false);
        return connection;
    }
}
