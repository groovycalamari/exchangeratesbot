package groovycalamari.exchangerates.telegram.commands;

import io.exchangeratesapi.Currency;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Introspected
public class ExchangeRates {
    private Currency base;

    private String date;

    private Map<Currency, BigDecimal> rates = new HashMap<>();

    public ExchangeRates() {
    }

    public Currency getBase() {
        return base;
    }

    public void setBase(Currency base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<Currency, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<Currency, BigDecimal> rates) {
        this.rates = rates;
    }
}

