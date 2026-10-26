package com.github.denegas.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DbWork {
    private DbWork() {
    }

    public static void setPreparedStatementParameters(PreparedStatement ps, String... params) {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setString(i + 1, params[i]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void setPreparedStatementParameters(PreparedStatement ps, int... params) {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            try {
                ps.setInt(i + 1, params[i]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
