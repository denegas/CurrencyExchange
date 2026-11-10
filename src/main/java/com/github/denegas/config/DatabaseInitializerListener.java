package com.github.denegas.config;

import com.github.denegas.exception.DbException;
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
    public static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/Database.db?foreign_keys=true";
    private static final String CREATE_CURRENCIES_QUERY = """
            INSERT OR IGNORE INTO Currencies(code, full_name, sign)
            VALUES
                ("EUR", "Euro", "€"),
                ("USD", "US Dollar", "$"),
                ("GBP", "Pound Sterling", "£"),
                ("JPY", "Yen", "¥"),
                ("RUB", "Russian Ruble", "₽"),
                ("CNY", "Yuan Renminbi", "¥"),
                ("CHF", "Swiss Franc", "₣"),
                ("CAD", "Canadian Dollar", "$"),
                ("AUD", "Australian Dollar", "$"),
                ("NZD", "New Zealand Dollar", "$"),
                ("BYN", "Belarusian Ruble", "Br"),
                ("UAH", "Hryvnia", "₴"),
                ("KZT", "Tenge", "₸");
            """;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        loadSqliteDriver();

        String initSql = getLinesFromResourceFile("schema.sql")
                + getLinesFromResourceFile("indexes.sql")
                + CREATE_CURRENCIES_QUERY;

        String[] queries = initSql.split(";");

        executeQueries(queries);
    }

    private String getLinesFromResourceFile(String fileName) {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new NoSuchFileException("couldn't find: " + fileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DbException("error while reading resource file", e);
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
        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            for (String query : queries) {
                try (Statement statement = connection.createStatement()) {
                    if (!isValid(query)) {
                        continue;
                    }
                    statement.execute(query);
                }

            }
        } catch (SQLException e) {
            throw new DbException("error while executeQueries", e);
        }
    }
}

