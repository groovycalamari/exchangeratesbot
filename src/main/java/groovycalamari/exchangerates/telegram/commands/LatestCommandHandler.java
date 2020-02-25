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
public class LatestCommandHandler extends ExchangeRatesApiCommandHandler {

    private static final Pattern pattern;

    static {
        String patternString = "^("+ String.join("|", Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+") ("+ String.join("|",Stream.of(Currency.values()).map(Currency::toString).collect(Collectors.toList()))+")$";
        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    protected LatestCommandHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public static boolean matches(String text) {
        return pattern.matcher(text).matches();
    }

    @Override
    protected Optional<String> parseUri(String text) {
        Matcher matcher = pattern.matcher(text.toUpperCase());
        matcher.find();
        if (matcher.groupCount() >= 2) {
            Currency base = Currency.valueOf(matcher.group(1));
            Currency symbol = Currency.valueOf(matcher.group(2));
            return Optional.of(ExchangeRatesConfigurationProperties.HOST_LIVE + "/latest?base=" + base.toString() + "&symbols=" + symbol.toString());
        }
        return Optional.empty();
    }
}
