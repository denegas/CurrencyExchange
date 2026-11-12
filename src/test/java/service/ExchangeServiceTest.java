package service;

import com.github.denegas.exception.NotFoundException;
import com.github.denegas.model.dao.CurrencyDao;
import com.github.denegas.model.dao.ExchangeRateDao;
import com.github.denegas.model.dto.ExchangeRequestDto;
import com.github.denegas.model.service.ExchangeService;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.model.validator.exchange.ExchangeValidator;
import com.github.denegas.model.validator.exchange.ExchangeValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeServiceTest {
    private ExchangeService service;
    private ExchangeRateDao exchangeRateDao;
    private ExchangeValidator exchangeValidator;
    private CurrencyDao currencyDao;
    private CurrencyValidator currencyValidator;

    @BeforeEach
    void setUp() {
        this.currencyDao = mock(CurrencyDao.class);
        this.exchangeRateDao = mock(ExchangeRateDao.class);

        this.currencyValidator = new CurrencyValidatorImpl();
        this.exchangeValidator = new ExchangeValidatorImpl(currencyValidator);

        this.service = new ExchangeService(exchangeRateDao, currencyDao, exchangeValidator);
    }

    @Test
    void exchange_ThrowsNotFound_WhenDaoReturnsEmpty() {
        when(currencyDao.findByCode(any())).thenReturn(Optional.empty());
        when(exchangeRateDao.findByCodes(any(), any())).thenReturn(Optional.empty());
        ExchangeRequestDto validRequest = new ExchangeRequestDto("USD", "RUB", BigDecimal.ONE);
        Assertions.assertThrows(NotFoundException.class, () -> service.exchange(validRequest));
    }
}
