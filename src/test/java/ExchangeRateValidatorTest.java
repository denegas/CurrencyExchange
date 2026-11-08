import com.github.denegas.dto.ExchangeRateRequestDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.validator.exchangerate.ExchangeRateValidator;
import com.github.denegas.validator.exchangerate.ExchangeRateValidatorImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class ExchangeRateValidatorTest {
    private CurrencyValidator currencyValidator;
    private ExchangeRateValidator validator;

    @BeforeEach
    void setUp() {
        currencyValidator = new CurrencyValidatorImpl();
        validator = new ExchangeRateValidatorImpl(currencyValidator);
    }

    @Test
    void validateThrowsOnNull() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    static Stream<ExchangeRateRequestDto> emptyParametersExchangeRateProvider() {
        return Stream.of(
                new ExchangeRateRequestDto("USD", "RUB", null),
                new ExchangeRateRequestDto("USD", null, BigDecimal.ONE),
                new ExchangeRateRequestDto(null, "RUB", BigDecimal.ONE),
                new ExchangeRateRequestDto(" ", "USD", BigDecimal.ONE),
                new ExchangeRateRequestDto("USD", " ", BigDecimal.ONE)
        );
    }

    @ParameterizedTest
    @MethodSource("emptyParametersExchangeRateProvider")
    void validateThrowsOnEmptyParameters(ExchangeRateRequestDto requestDto) {
        assertThrows(ValidationException.class, () -> validator.validate(requestDto));
    }

    @Test
    void validateThrowsOnInvalidRate() {
        ExchangeRateRequestDto zeroRate = new ExchangeRateRequestDto("USD", "RUB", BigDecimal.ZERO);
        ExchangeRateRequestDto lowerZeroRate = new ExchangeRateRequestDto("USD", "RUB", new BigDecimal(-1));
        assertThrows(ValidationException.class, () -> validator.validate(zeroRate));
        assertThrows(ValidationException.class, () -> validator.validate(lowerZeroRate));
    }

    @Test
    void validateThrowsOnSameCurrencies() {
        ExchangeRateRequestDto sameCurrenciesRate = new ExchangeRateRequestDto("USD", "USD", BigDecimal.ONE);
        assertThrows(ValidationException.class, () -> validator.validate(sameCurrenciesRate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"usd", "USDDD", "US1", "US", "", "   ", "!!!"})
    void validateThrowsOnInvalidCurrencyCodes(String invalidCode) {
        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(invalidCode, "XXX", BigDecimal.ONE);
        assertThrows(ValidationException.class, () -> validator.validate(requestDto));
    }
}
