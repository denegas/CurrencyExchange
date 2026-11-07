import com.github.denegas.entity.Currency;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.validator.currency.CurrencyValidator;
import com.github.denegas.validator.currency.CurrencyValidatorImpl;
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
    public void validateAcceptsValidCurrency() {
        Currency validCurrency = new Currency("USD", "Dollar", "$");
        assertDoesNotThrow(() -> validator.validate(validCurrency));
    }

    @Test
    public void validateChecksNull() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void validateChecksEmptyCode() {
        Currency emptyCodeCurrency = new Currency(" ", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(emptyCodeCurrency));
    }

    @Test
    public void validateChecksEmptyName() {
        Currency emptyNameCurrency = new Currency("USD", " ", "$");
        assertThrows(ValidationException.class, () -> validator.validate(emptyNameCurrency));
    }

    @Test
    public void validateChecksEmptySign() {
        Currency emptySignCurrency = new Currency("USD", "Dollar", " ");
        assertThrows(ValidationException.class, () -> validator.validate(emptySignCurrency));
    }

    @Test
    public void validateChecksNullCode() {
        Currency nullCodeCurrency = new Currency(null, "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(nullCodeCurrency));
    }

    @Test
    public void validateChecksNullName() {
        Currency nullNameCurrency = new Currency("USD", null, "$");
        assertThrows(ValidationException.class, () -> validator.validate(nullNameCurrency));
    }

    @Test
    public void validateChecksNullSign() {
        Currency nullSignCurrency = new Currency("USD", "Dollar", null);
        assertThrows(ValidationException.class, () -> validator.validate(nullSignCurrency));
    }

    @Test
    public void validateChecksCodeUpperCase() {
        Currency lowerCaseCode = new Currency("usd", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(lowerCaseCode));
    }

    @Test
    public void validateChecksShortCodeLength() {
        Currency tooShortCode = new Currency("US", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooShortCode));
    }

    @Test
    public void validateChecksLongCodeLength() {
        Currency tooLongCode = new Currency("USDD", "Dollar", "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooLongCode));
    }

    @Test
    public void validateChecksLongNameLength() {
        String longName = "A".repeat(65);
        Currency tooLongName = new Currency("USD", longName, "$");
        assertThrows(ValidationException.class, () -> validator.validate(tooLongName));
    }

    @Test
    public void validateChecksLongSignLength() {
        String longSign = "&".repeat(7);
        Currency tooLongSign = new Currency("USD", "Dollar", longSign);
        assertThrows(ValidationException.class, () -> validator.validate(tooLongSign));
    }

}
