package validator;

import com.github.denegas.model.dto.ExchangeRequestDto;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import com.github.denegas.model.validator.exchange.ExchangeValidator;
import com.github.denegas.model.validator.exchange.ExchangeValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExchangeValidatorTest {
    private ExchangeValidator validator;
    private CurrencyValidator currencyValidator;

    @BeforeEach
    void setUp() {
        this.currencyValidator = new CurrencyValidatorImpl();
        this.validator = new ExchangeValidatorImpl(currencyValidator);
    }

    @Test
    void validateThrowsOnNull() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    static Stream<ExchangeRequestDto> emptyParametersExchangeProvide() {
        return Stream.of(
                new ExchangeRequestDto(null, "USD", BigDecimal.ONE),
                new ExchangeRequestDto("USD", null, BigDecimal.ONE),
                new ExchangeRequestDto("RUB", "USD", null),
                new ExchangeRequestDto("   ", "USD", BigDecimal.ONE),
                new ExchangeRequestDto("USD", "   ", BigDecimal.ONE)
        );
    }

    @ParameterizedTest
    @MethodSource("emptyParametersExchangeProvide")
    void validateThrowsOnEmptyParameters(ExchangeRequestDto requestDto) {
        assertThrows(ValidationException.class, () -> validator.validate(requestDto));
    }

    @Test
    void validateThrowsOnInvalidAmount() {
        ExchangeRequestDto zeroAmountExchange = new ExchangeRequestDto("USD", "RUB", BigDecimal.ZERO);
        assertThrows(ValidationException.class, () -> validator.validate(zeroAmountExchange));
    }

    @Test
    void validateDoesNotThrowOnValidValue() {
        ExchangeRequestDto validExchange = new ExchangeRequestDto("USD", "RUB", new BigDecimal(70));
        assertDoesNotThrow(() -> validator.validate(validExchange));
    }
}
