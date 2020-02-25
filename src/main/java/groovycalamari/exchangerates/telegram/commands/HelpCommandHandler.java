package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.dispatcher.TextCommandHandler;
import io.micronaut.bots.telegram.core.Update;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named("/help")
@Singleton
public class HelpCommandHandler extends TextCommandHandler {

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull Update update) {
        StringBuffer sb = new StringBuffer();
        sb.append("You can obtain foreign exchange rates published by the European Central Bank.\n");
        sb.append("\n\n");
        sb.append("Latest exchange rate between two currencies:\n");
        sb.append("USD EUR\n");
        sb.append("\n\n");
        sb.append("Exchange rate at a particular date between two currencies:\n");
        sb.append("2020-01-31 USD EUR\n");
        sb.append("\n\n");
        sb.append("To obtain a list of supported currency codes:\n");
        sb.append("/currencies\n");
        sb.append("\n\n");
        sb.append("About this bot:\n");
        sb.append("/about");
        return Optional.of(sb.toString());
    }
}
