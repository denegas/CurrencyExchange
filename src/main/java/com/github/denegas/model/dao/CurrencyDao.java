package com.github.denegas.model.dao;


import com.github.denegas.config.DatabaseConnector;
import com.github.denegas.exception.SqliteExceptionUtils;
import com.github.denegas.model.entity.Currency;
import com.github.denegas.exception.AlreadyExistsException;
import com.github.denegas.exception.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<Currency> {
    private static final String GET_BY_CODE_QUERY = """
             SELECT id, code, full_name, sign
             FROM Currencies
             WHERE code = ?;
            """;
    private static final String INSERT_QUERY = """
            INSERT INTO Currencies(code, full_name, sign)
            VALUES (?, ?, ?);
            """;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM Currencies;
            """;
    private final String url;

    public CurrencyDao(String url) {
        this.url = url;
    }

    @Override
    public List<Currency> getAll() {
        try (Connection connection = DatabaseConnector.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            return getCurrenciesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new DbException("error while getAll()", e);
        }
    }

    public Optional<Currency> findByCode(String code) {

        try (Connection connection = DatabaseConnector.connect(url);
             PreparedStatement ps = connection.prepareStatement(GET_BY_CODE_QUERY)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            return getCurrencyFromResultSet(rs);

        } catch (SQLException e) {
            throw new DbException("error while finding currency", e);
        }
    }

    @Override
    public Currency insert(Currency currency) {
        try (Connection connection = DatabaseConnector.connect(url);
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.executeUpdate();
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                long id = getCreatedCurrencyId(resultSet);

                return new Currency(id, currency.getCode(), currency.getFullName(), currency.getSign());
            }

        } catch (SQLException e) {
            if (SqliteExceptionUtils.isUniqueConstraintViolated(e)) {
                throw new AlreadyExistsException("creating currency already exists");
            }
            throw new DbException("error while inserting currency", e);
        }
    }


    private long getCreatedCurrencyId(ResultSet generatedKeys) {
        try {
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            throw new DbException("error while getting currency's id", e);
        }
        throw new DbException("couldn't create currency");
    }

    private Optional<Currency> getCurrencyFromResultSet(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                Currency currency = mapRowToCurrency(resultSet);

                return Optional.of(currency);
            }
        } catch (SQLException e) {
            throw new DbException("error while getting currency", e);
        }
        return Optional.empty();
    }

    private List<Currency> getCurrenciesFromResultSet(ResultSet resultSet) {
        List<Currency> currencies = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Currency currency = mapRowToCurrency(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new DbException("error while getting currencies", e);
        }
    }

    private Currency mapRowToCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
