package validator;

import com.github.denegas.model.entity.Currency;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.model.validator.currency.CurrencyValidator;
import com.github.denegas.model.validator.currency.CurrencyValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyValidatorTest {
    private CurrencyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CurrencyValidatorImpl();
    }

    @Test
    void validateAcceptsValidCurrency() {
        Currency validCurrency = new Currency("USD", "Dollar", "$");
        assertDoesNotThrow(() -> validator.validate(validCurrency));
    }

    @Test
    void validateChecksNull() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    void validateChecksEmptyCode() {
        Currency emptyCodeCurrency = new Currency(" ", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(emptyCodeCurrency));
    }

    @Test
    void validateChecksEmptyName() {
        Currency emptyNameCurrency = new Currency("USD", " ", "$");
        assertThrows(ValidationException.class, () -> validator.validate(emptyNameCurrency));
    }

    @Test
    void validateChecksEmptySign() {
        Currency emptySignCurrency = new Currency("USD", "Dollar", " ");
        assertThrows(ValidationException.class, () -> validator.validate(emptySignCurrency));
    }

    @Test
    void validateChecksNullCode() {
        Currency nullCodeCurrency = new Currency(null, "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(nullCodeCurrency));
    }

    @Test
    void validateChecksNullName() {
        Currency nullNameCurrency = new Currency("USD", null, "$");
        assertThrows(ValidationException.class, () -> validator.validate(nullNameCurrency));
    }

    @Test
    void validateChecksNullSign() {
        Currency nullSignCurrency = new Currency("USD", "Dollar", null);
        assertThrows(ValidationException.class, () -> validator.validate(nullSignCurrency));
    }

    @Test
    void validateChecksCodeUpperCase() {
        Currency lowerCaseCode = new Currency("usd", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(lowerCaseCode));
    }

    @Test
    void validateChecksShortCodeLength() {
        Currency tooShortCode = new Currency("US", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooShortCode));
    }

    @Test
    void validateChecksLongCodeLength() {
        Currency tooLongCode = new Currency("USDD", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooLongCode));
    }

    @Test
    void validateChecksLongNameLength() {
        String longName = "A".repeat(65);
        Currency tooLongName = new Currency("USD", longName, "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooLongName));
    }

    @Test
    void validateChecksLongSignLength() {
        String longSign = "&".repeat(7);
        Currency tooLongSign = new Currency("USD", "Dollar", longSign);
        assertThrows(ValidationException.class, () -> validator.validate(tooLongSign));
    }

    @Test
    void validateDoesNotThrowOnValidValue() {
        Currency validCurrency = new Currency("USD", "Dollar", "$");
        assertDoesNotThrow(() -> validator.validate(validCurrency));
    }

}
