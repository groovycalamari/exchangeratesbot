package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.SendMessage;
import io.micronaut.bots.telegram.core.Send;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.DefaultUpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.UpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Replaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Replaces(DefaultUpdateDispatcher.class)
@Singleton
public class ExchangeRatesUpdateDispatcher extends DefaultUpdateDispatcher implements UpdateDispatcher {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesUpdateDispatcher.class);

    private final AmountAtDateHandler amountAtDateHandler;
    private final AtDateCommandHandler atDateCommandHandler;
    private final AmountHandler amountHandler;
    private final LatestCommandHandler latestCommandHandler;

    public ExchangeRatesUpdateDispatcher(ApplicationContext applicationContext,
                                         UpdateParser updateParser,
                                         AmountAtDateHandler amountAtDateHandler,
                                         AtDateCommandHandler atDateCommandHandler,
                                         AmountHandler amountHandler,
                                         LatestCommandHandler latestCommandHandler) {
        super(applicationContext, updateParser);
        this.amountAtDateHandler = amountAtDateHandler;
        this.atDateCommandHandler = atDateCommandHandler;
        this.amountHandler = amountHandler;
        this.latestCommandHandler = latestCommandHandler;
    }


    protected Optional<Send> handleUpdateNotProcessedByCommands(@NonNull TelegramBot telegramBot,
                                                                Update update) {
        Optional<Send> messageOptional = defaultHandleUpdateNotProcessedByCommands(telegramBot, update);

        if (shouldHandleMessage(telegramBot, update)) {
            String text = updateParser.parseTextWithoutBotName(telegramBot, update).orElse(null);
            if (LOG.isInfoEnabled()) {
                LOG.info("text parsed: {}", text);
            }

            if (AmountAtDateHandler.matches(text)) {
                return amountAtDateHandler.handle(telegramBot, update);
            }
            if (AtDateCommandHandler.matches(text)) {
                return atDateCommandHandler.handle(telegramBot, update);
            }
            if (AmountHandler.matches(text)) {
                return amountHandler.handle(telegramBot, update);
            }
            if (LatestCommandHandler.matches(text)) {
                return latestCommandHandler.handle(telegramBot, update);
            }
        }
        return messageOptional;
    }

}
