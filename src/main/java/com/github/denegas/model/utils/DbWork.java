package com.github.denegas.model.utils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DbWork {
    private DbWork() {
    }

    public static void setPreparedStatementParameters(PreparedStatement ps, String code, String fullName, String sign) throws SQLException {
        ps.setString(1, code);
        ps.setString(2, fullName);
        ps.setString(3, sign);
    }

    public static void setPreparedStatementParameters(PreparedStatement ps, String code) throws SQLException {
        ps.setString(1, code);
    }

    public static void setPreparedStatementParameters(PreparedStatement ps, BigDecimal rate,
                                                      String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        ps.setBigDecimal(1, rate);
        ps.setString(2, baseCurrencyCode);
        ps.setString(3, targetCurrencyCode);
    }

    public static void setPreparedStatementParameters(PreparedStatement ps, String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        ps.setString(1, baseCurrencyCode);
        ps.setString(2, targetCurrencyCode);
    }


    public static boolean isUniqueConstraintViolated(SQLException e){
        return e.getErrorCode() == 19 && e.getMessage() != null && e.getMessage().contains("UNIQUE");
    }
}
