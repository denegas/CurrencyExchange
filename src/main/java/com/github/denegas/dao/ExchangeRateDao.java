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
    private static final String UPDATE_QUERY = """
            UPDATE ExchangeRates
            SET rate = ?
            WHERE base_currency_id = ?
            AND target_currency_id = ?;
            """;
    private static final String GET_BY_BASE_TARGET_CODES = """
            SELECT *
            FROM ExchangeRates
            JOIN Currencies base ON ExchangeRates.base_currency_id = base.id
            JOIN Currencies target ON ExchangeRates.target_currency_id = target.id
            WHERE base.code = ?
            AND target.code = ?;
            
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
            VALUES(?, ?, ?);
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
        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            long baseCurrencyId = exchangeRate.getBaseCurrency().getId();
            long targetCurrencyId = exchangeRate.getTargetCurrency().getId();

            DbWork.setPreparedStatementParameters(preparedStatement, baseCurrencyId, targetCurrencyId, exchangeRate.getRate());

            preparedStatement.executeUpdate();
            long rateId;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                rateId = generatedKeys.getLong("id");
            }
            return new ExchangeRate(rateId,
                    exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(),
                    exchangeRate.getRate());
        } catch (SQLException e) {
            throw new DbException("error while inserting", e);
        }
    }

    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        try (Connection connection = DbWork.connect(url);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            long baseCurrencyId = exchangeRate.getBaseCurrency().getId();
            long targetCurrencyId = exchangeRate.getTargetCurrency().getId();

            DbWork.setPreparedStatementParameters(preparedStatement, exchangeRate.getRate(), baseCurrencyId, targetCurrencyId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(exchangeRate);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DbException("error while updating", e);
        }
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
        Currency baseCurrency = new Currency(
                resultSet.getLong("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_full_name"),
                resultSet.getString("base_currency_sign")
        );
        Currency targetCurrency = new Currency(
                resultSet.getLong("target_currency_id"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_full_name"),
                resultSet.getString("target_currency_sign")
        );

        return new ExchangeRate(
                resultSet.getLong("exhange_rates_id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate")
        );
    }
}


