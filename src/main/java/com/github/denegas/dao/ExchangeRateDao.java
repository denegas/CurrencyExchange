package com.github.denegas.dao;

import com.github.denegas.entity.Currency;
import com.github.denegas.entity.ExchangeRate;
import com.github.denegas.exception.DbException;
import com.github.denegas.utils.DbWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements Dao<ExchangeRate> {
    private static final String BASE_CURRENCY_NAME = "base";
    private static final String TARGET_CURRENCY_NAME = "target";
    private static final String UPDATE_QUERY = """
            UPDATE ExchangeRates
            
            SET rate = ?
            WHERE base_currency_id = (SELECT id FROM Currencies WHERE code = ?)
            AND target_currency_id = (SELECT id FROM Currencies WHERE code = ?);
            """;
    private static final String GET_BY_BASE_TARGET_CODES = """
            SELECT ExchangeRates.id AS exhange_rates_id,
            
                   base.id AS base_currency_id,
                   base.full_name AS base_currency_full_name,
                   base.code AS base_currency_code,
                   base.sign AS base_currency_sign,
            
                   target.id AS target_currency_id,
                   target.full_name AS target_currency_full_name,
                   target.code AS target_currency_code,
                   target.sign AS target_currency_sign,
            
                   ExchangeRates.rate
            FROM ExchangeRates
            JOIN Currencies base ON ExchangeRates.base_currency_id = base.id
            JOIN Currencies target ON ExchangeRates.target_currency_id = target.id
            WHERE base.code = ?
            AND target.code = ?;
            
            """;
    private static final String GET_CURRENCIES_AFTER_CREATING = """
            SELECT base.id AS base_currency_id,
                   base.full_name AS base_currency_full_name,
                   base.code AS base_currency_code,
                   base.sign AS base_currency_sign,
            
                   target.id AS target_currency_id,
                   target.full_name AS target_currency_full_name,
                   target.code AS target_currency_code,
                   target.sign AS target_currency_sign
            
            FROM ExchangeRates
            INNER JOIN Currencies AS base
            ON base.id  = ExchangeRates.base_currency_id
            INNER JOIN Currencies AS target
            ON target.id = ExchangeRates.target_currency_id
            WHERE ExchangeRates.id = ?;
            """;
    private static final String GET_ALL_QUERY = """
            SELECT ExchangeRates.id AS exhange_rates_id,
            
                   base.id AS base_currency_id,
                   base.full_name AS base_currency_full_name,
                   base.code AS base_currency_code,
                   base.sign AS base_currency_sign,
            
                   target.id AS target_currency_id,
                   target.full_name AS target_currency_full_name,
                   target.code AS target_currency_code,
                   target.sign AS target_currency_sign,
            
                   ExchangeRates.rate
            FROM ExchangeRates
            INNER JOIN Currencies AS base
            ON base.id  = ExchangeRates.base_currency_id
            INNER JOIN Currencies AS target
            ON target.id = ExchangeRates.target_currency_id;
            """;
    private static final String INSERT_QUERY = """
            INSERT INTO ExchangeRates(base_currency_id, target_currency_id, rate)
            SELECT base.id,
                   target.id,
                   ?
            FROM Currencies target
            CROSS JOIN Currencies base
            WHERE base.code = ? AND target.code = ?;
            """;

    private final String url;

    public ExchangeRateDao(String url) {
        this.url = url;
    }

    @Override
    public List<ExchangeRate> getAll() {
        try (Connection connection = DbWork.connect(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY)) {

            return getAllExchangeRatesFromResultSet(resultSet);

        } catch (SQLException e) {
            throw new DbException("error while getting exchange rates", e);
        }
    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_BASE_TARGET_CODES)) {
            DbWork.setPreparedStatementParameters(preparedStatement, baseCurrencyCode, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getExchangeRateFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new DbException("error while getting exchangeRate", e);
        }
    }

    @Override
    public ExchangeRate insert(ExchangeRate exchangeRate) {
        long exchangeRateId;
        Currency baseCurrency;
        Currency targetCurrency;

        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            String baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
            String targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();

            DbWork.setPreparedStatementParameters(preparedStatement, exchangeRate.getRate(),
                    baseCurrencyCode, targetCurrencyCode);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exchangeRateId = generatedKeys.getLong(1);
                } else {
                    throw new DbException("insert failed, have no id to return");
                }

            }

        } catch (SQLException e) {
            throw new DbException("error while inserting", e);
        }
        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CURRENCIES_AFTER_CREATING)) {
            preparedStatement.setLong(1, exchangeRateId);

            try (ResultSet currencies = preparedStatement.executeQuery()) {
                if (currencies.next()) {
                    baseCurrency = mapRowToCurrency(currencies,BASE_CURRENCY_NAME);
                    targetCurrency = mapRowToCurrency(currencies,TARGET_CURRENCY_NAME);
                    return new ExchangeRate(exchangeRateId,
                            baseCurrency, targetCurrency,
                            exchangeRate.getRate());
                } else {
                    throw new DbException("Insertion failed, couldn't find currencies");
                }

            }


        } catch (SQLException e) {
            throw new DbException("error while getting currencies(base, target)");
        }

    }

    //TODO: update method
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            long baseCurrencyId = exchangeRate.getBaseCurrency().getId();
            long targetCurrencyId = exchangeRate.getTargetCurrency().getId();

            // DbWork.setPreparedStatementParameters(preparedStatement, exchangeRate.getRate(), baseCurrencyId, targetCurrencyId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(exchangeRate);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DbException("error while updating", e);
        }
    }

    public boolean existsRate(String baseCurrencyCode, String targetCurrencyCode) {
        return findByCodes(baseCurrencyCode, targetCurrencyCode).isPresent();
    }


    private Optional<ExchangeRate> getExchangeRateFromResultSet(ResultSet resultSet) throws SQLException {

        if (resultSet.next()) {
            ExchangeRate exchangeRate = mapRowToExchangeRate(resultSet);
            return Optional.of(exchangeRate);
        }
        return Optional.empty();
    }

    private List<ExchangeRate> getAllExchangeRatesFromResultSet(ResultSet resultSet) throws SQLException {
        List<ExchangeRate> rates = new ArrayList<>();
        while (resultSet.next()) {
            ExchangeRate exchangeRate = mapRowToExchangeRate(resultSet);
            rates.add(exchangeRate);
        }
        return rates;
    }

    private ExchangeRate mapRowToExchangeRate(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = mapRowToCurrency(resultSet, BASE_CURRENCY_NAME);
        Currency targetCurrency = mapRowToCurrency(resultSet, TARGET_CURRENCY_NAME);

        return new ExchangeRate(
                resultSet.getLong("exhange_rates_id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate")
        );
    }

    private Currency mapRowToCurrency(ResultSet resultSet, String currencyName) throws SQLException {
        return new Currency(
                resultSet.getLong(currencyName + "_currency_id"),
                resultSet.getString(currencyName + "_currency_code"),
                resultSet.getString(currencyName + "_currency_full_name"),
                resultSet.getString(currencyName + "_currency_sign")
        );
    }
}


