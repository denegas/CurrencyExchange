package service;

import com.github.denegas.model.dao.CurrencyDao;
import com.github.denegas.exception.NotFoundException;
import com.github.denegas.model.mapper.currency.CurrencyMapper;
import com.github.denegas.model.service.CurrencyService;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {
    private CurrencyService service;
    private CurrencyDao dao;
    private CurrencyValidator validator;
    private CurrencyMapper mapper = CurrencyMapper.INSTANCE;
    @BeforeEach
    void setUp() {
        dao = mock(CurrencyDao.class);
        validator = new CurrencyValidatorImpl();
        service = new CurrencyService(dao, validator, mapper);
    }

    @Test
    void findByCode_ThrowsNotFound_WhenDaoReturnsEmpty() {
        when(dao.findByCode(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.findByCode("XXX"));
    }
}
