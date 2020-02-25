package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.TextCommandHandler;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named("/currencies")
@Singleton
public class CurrenciesCommandHandler extends TextCommandHandler {

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull Update update) {
        StringBuilder sb = new StringBuilder();
        for (Currency currency : Currency.values()) {
            sb.append(currency.getCode())
                    .append(" - ")
                    .append(currency.getCountryName())
                    .append("\n");
        }
        return Optional.of(sb.toString());
    }
}
