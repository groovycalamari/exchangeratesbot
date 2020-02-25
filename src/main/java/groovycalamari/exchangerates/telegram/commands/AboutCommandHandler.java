package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.dispatcher.TextCommandHandler;
import io.micronaut.bots.telegram.core.Update;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named("/about")
@Singleton
public class AboutCommandHandler extends TextCommandHandler {

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull Update update) {
        StringBuffer sb = new StringBuffer();
        sb.append("This bot uses https://exchangeratesapi.io\n");
        sb.append("Made with \uD83D\uDC99 by Sergio del Amo (https://twitter.com/sdelamo)");
        return Optional.of(sb.toString());
    }
}
