package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.core.TextCommandHandler;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named(CurrenciesCommandHandler.COMMAND_CURRENCIES)
@Singleton
public class CurrenciesCommandHandler extends TextCommandHandler {
    public static final String COMMAND_CURRENCIES = "currencies";

    public CurrenciesCommandHandler(MessageComposer messageComposer) {
        super(messageComposer);
    }

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull ChatBot bot,
                                           @NonNull ChatBotMessageReceive update) {
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
