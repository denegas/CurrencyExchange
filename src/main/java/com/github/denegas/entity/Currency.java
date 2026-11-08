package com.github.denegas.entity;

import java.util.Objects;

public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;

    public Currency() {

    }

    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(String code, String fullName, String sign) {
        this(null, code, fullName, sign);
    }

    public Currency(String code) {
        this(null, code, null, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()){
            return false;
        }
        Currency otherCurrency = (Currency) other;
        return Objects.equals(this.id, otherCurrency.getId())
                && Objects.equals(this.code, otherCurrency.getCode())
                && Objects.equals(this.fullName, otherCurrency.getFullName())
                && Objects.equals(this.sign, otherCurrency.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, fullName, sign);
    }
}
