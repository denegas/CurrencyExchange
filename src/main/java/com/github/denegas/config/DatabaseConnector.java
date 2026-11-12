package com.github.denegas.config;

import com.github.denegas.exception.DbException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnector {
    private DatabaseConnector() {
    }

    public static Connection connect(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DbException("couldn't connect to database",e);
        }
    }
}
