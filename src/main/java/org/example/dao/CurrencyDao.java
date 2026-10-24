package org.example.dao;

import org.example.dto.CurrencyDto;
import org.example.utils.DbWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<CurrencyDto> {
    private static final String GET_BY_CODE_QUERY = """
             SELECT code, full_name, sign
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
    public List<CurrencyDto> getAll() {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            return getCurrenciesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CurrencyDto> getByCode(String code) {

        try (Connection connection = connect()) {
            PreparedStatement ps = connection.prepareStatement(GET_BY_CODE_QUERY);
            DbWork.setPreparedStatementParameters(ps, code);
            ResultSet rs = ps.executeQuery();

            return getCurrencyFromResultSet(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insert(CurrencyDto currencyDto) {
        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            DbWork.setPreparedStatementParameters(ps, currencyDto.code(), currencyDto.fullName(), currencyDto.sign());
            // isChanged
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    

    private Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("driver is not found", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Optional<CurrencyDto> getCurrencyFromResultSet(ResultSet resultSet) {

        try {
            if (resultSet.next()) {
                CurrencyDto currency = new CurrencyDto(resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        resultSet.getString("sign"));
                return Optional.of(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private List<CurrencyDto> getCurrenciesFromResultSet(ResultSet resultSet) {
        List<CurrencyDto> currencies = new ArrayList<>();

        try {
            while (resultSet.next()) {
                CurrencyDto currency = new CurrencyDto(resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        resultSet.getString("sign"));
                currencies.add(currency);

            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
