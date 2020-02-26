package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.ChatType;
import io.micronaut.bots.telegram.dispatcher.DefaultUpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.core.Send;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;
import java.util.Optional;

@Replaces(DefaultUpdateDispatcher.class)
@Singleton
public class ExchangeRatesUpdateDispatcher extends DefaultUpdateDispatcher {
    private final AtDateCommandHandler atDateCommandHandler;
    private final LatestCommandHandler latestCommandHandler;

    public ExchangeRatesUpdateDispatcher(ApplicationContext applicationContext,
                                         AtDateCommandHandler atDateCommandHandler,
                                         LatestCommandHandler latestCommandHandler) {
        super(applicationContext);
        this.atDateCommandHandler = atDateCommandHandler;
        this.latestCommandHandler = latestCommandHandler;
    }

    @Override
    protected Optional<Send> handleUpdateNotProcessedByCommands(@NonNull TelegramBot telegramBot,
                                                                Update update) {
        Optional<Send> messageOptional = super.handleUpdateNotProcessedByCommands(telegramBot, update);
        String text = CommandHandler.parseText(update);
        String type = CommandHandler.parseType(update);

        boolean isPrivateMessage = (type != null && type.equals(ChatType.PRIVATE.toString()));

        boolean isMessageTargetToTheBot = text != null && text.contains(telegramBot.getAtUsername());

        if (text != null && (isPrivateMessage || isMessageTargetToTheBot)) {
            if (AtDateCommandHandler.matches(text)) {
                return atDateCommandHandler.handle(telegramBot, update);
            }
            if (LatestCommandHandler.matches(text)) {
                return latestCommandHandler.handle(telegramBot, update);
            }
        }
        return messageOptional;
    }
}
