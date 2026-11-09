package com.github.denegas.model.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate {
    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(Long id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate() {

    }

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        ExchangeRate otherRate = (ExchangeRate) other;
        return Objects.equals(this.id, otherRate.getId())
                && Objects.equals(this.baseCurrency, otherRate.getBaseCurrency())
                && Objects.equals(this.targetCurrency, otherRate.getTargetCurrency())
                && (this.rate == null) ? (otherRate.getRate() == null) : this.rate.compareTo(otherRate.getRate()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.baseCurrency, this.targetCurrency, this.rate);
    }
}
