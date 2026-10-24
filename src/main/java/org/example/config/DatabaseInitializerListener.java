package org.example.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DatabaseInitializerListener implements ServletContextListener {
    private final static String URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/Database.db?foreign_keys=true";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        loadSqliteDriver();

        String sql = getLinesFromResourceFile("schema.sql")
                + getLinesFromResourceFile("indexes.sql");

        String[] queries = sql.split(";");

        executeQueries(queries);
    }

    private String getLinesFromResourceFile(String resourceFileName) {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                throw new NoSuchFileException("couldn't find: " + resourceFileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO: think what the best exception for this case
        }

    }

    private void loadSqliteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("SQLite JDBC driver not found on classpath", e);
        }
    }

    private boolean isValid(String query) {
        return !query.isBlank() && query.length() >= 2;
    }

    private void executeQueries(String[] queries) {
        try (Connection connection = DriverManager.getConnection(URL)) {

            for (String query : queries) {
                try (Statement statement = connection.createStatement()) {
                    if (!isValid(query)) {
                        continue;
                    }
                    statement.execute(query);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO: think what the best exception for this case
        }
    }
}

