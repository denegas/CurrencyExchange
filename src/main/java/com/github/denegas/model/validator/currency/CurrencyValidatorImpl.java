package com.github.denegas.model.validator.currency;

import com.github.denegas.model.entity.Currency;
import com.github.denegas.exception.ValidationException;
import com.github.denegas.model.utils.ValidatorUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyValidatorImpl implements CurrencyValidator {
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final String CODE_REGEX = "^[A-Z]{3}$";
    private static final int MAX_SIGN_LENGTH = 6;
    private static final int MAX_FULL_NAME_LENGTH = 64;
    private static final Pattern codePattern = Pattern.compile(CODE_REGEX);

    @Override
    public void validate(Currency currency) {
        if (currency == null) {
            throw new ValidationException("request body is required");
        }
        String code = currency.getCode();
        String fullName = currency.getFullName();
        String sign = currency.getSign();

        if (ValidatorUtils.isEmptyParameter(code)
                || ValidatorUtils.isEmptyParameter(fullName)
                || ValidatorUtils.isEmptyParameter(sign)) {
            throw new ValidationException("currency's parameters should not be empty");
        }
        if (fullName.length() > MAX_FULL_NAME_LENGTH) {
            throw new ValidationException("max currency's name length is: " + MAX_FULL_NAME_LENGTH);
        }
        if (sign.length() > MAX_SIGN_LENGTH) {
            throw new ValidationException("max currency's sign length is: " + MAX_SIGN_LENGTH);
        }
        if (!isValidCurrencyCode(code)) {
            throw new ValidationException("currency's code must consist of 3 uppercase letters");
        }
    }

    @Override
    public boolean isValidCurrencyCode(String code) {
        if (code == null) {
            return false;
        }
        Matcher matcher = codePattern.matcher(code);
        return code.length() == CURRENCY_CODE_LENGTH && code.equals(code.toUpperCase()) && matcher.matches();
    }
}
