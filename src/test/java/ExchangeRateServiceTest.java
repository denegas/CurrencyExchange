import com.github.denegas.exception.NotFoundException;
import com.github.denegas.model.dao.ExchangeRateDao;
import com.github.denegas.model.dto.ExchangeRateRequestDto;
import com.github.denegas.model.entity.ExchangeRate;
import com.github.denegas.model.mapper.exchangerate.ExchangeRateMapper;
import com.github.denegas.model.service.ExchangeRateService;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.model.validator.exchangerate.ExchangeRateValidator;
import com.github.denegas.model.validator.exchangerate.ExchangeRateValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceTest {
    private ExchangeRateService service;
    private ExchangeRateDao dao;
    private ExchangeRateValidator validator;
    private ExchangeRateMapper mapper;
    private CurrencyValidator currencyValidator;


    @BeforeEach
    void setUp() {
        this.dao = Mockito.mock(ExchangeRateDao.class);
        this.mapper = ExchangeRateMapper.INSTANCE;
        this.validator = new ExchangeRateValidatorImpl(currencyValidator);
        this.currencyValidator = new CurrencyValidatorImpl();
        this.service = new ExchangeRateService(dao, validator, mapper, currencyValidator);
    }

    @Test
    void findByCode_ThrowsNotFound_WhenDaoReturnsEmpty() {
        when(dao.findByCodes(any(), any())).thenReturn(Optional.empty());
        ExchangeRateRequestDto codesPair = new ExchangeRateRequestDto("USD", "RUB", null);
        Assertions.assertThrows(NotFoundException.class, () -> service.findByCodes(codesPair));
    }

    @Test
    void update_ThrowsNotFound_WhenDaoReturnsFalse() {
        when(dao.update(any())).thenReturn(false);
        ExchangeRateRequestDto someRequestDto = new ExchangeRateRequestDto("USD", "RUB", BigDecimal.ONE);
        Assertions.assertThrows(NotFoundException.class, () -> service.update(someRequestDto));
    }
}
