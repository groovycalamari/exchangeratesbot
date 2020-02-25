package groovycalamari.exchangerates.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeRatesConfigurationProperties;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class AtDateCommandHandler extends ExchangeRatesApiCommandHandler {

    private static final Pattern pattern;

    static {
        String patternString = "^(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])) ("+ String.join("|",Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+") ("+ String.join("|",Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+")$";
        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    protected AtDateCommandHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public static boolean matches(String text) {
        return pattern.matcher(text).matches();
    }

    @Override
    protected Optional<String> parseUri(String text) {
        Matcher matcher = pattern.matcher(text.toUpperCase());
        matcher.find();
        if (matcher.groupCount() >= 5) {
            String dateStr = matcher.group(1);
            Currency base = Currency.valueOf(matcher.group(4));
            Currency symbol = Currency.valueOf(matcher.group(5));
            return Optional.of(ExchangeRatesConfigurationProperties.HOST_LIVE + "/" + dateStr + "?base=" + base.toString() + "&symbols=" + symbol.toString());
        }
        return Optional.empty();
    }
}
