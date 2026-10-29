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
    public static void setPreparedStatementParameters(PreparedStatement ps, String code) throws SQLException{
        ps.setString(1, code);
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

    public static void setPreparedStatementParameters(PreparedStatement ps,
                                                      long baseCurrencyId, long targetCurrencyId, BigDecimal rate) throws SQLException {
        ps.setLong(1, baseCurrencyId);
        ps.setLong(2, targetCurrencyId);
        ps.setBigDecimal(3, rate);
    }
}
