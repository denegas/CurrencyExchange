package com.github.denegas.utils;

import com.github.denegas.exception.DbException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
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

    public static Connection connect(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            throw new DbException("driver is not found", e);
        } catch (SQLException e) {
            throw new DbException("error while connecting", e);
        }

    }
}
