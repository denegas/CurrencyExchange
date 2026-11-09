package com.github.denegas.model.utils;

import java.sql.SQLException;

public final class DbWork {
    private DbWork() {
    }



    public static boolean isUniqueConstraintViolated(SQLException e){
        return e.getErrorCode() == 19 && e.getMessage() != null && e.getMessage().contains("UNIQUE");
    }
}
