package com.github.denegas.exception;

import java.sql.SQLException;

public final class SqliteExceptionUtils {
    private SqliteExceptionUtils() {
    }

    public static boolean isUniqueConstraintViolated(SQLException e) {
        return e.getErrorCode() == 19 && e.getMessage() != null && e.getMessage().contains("UNIQUE");
    }
}
