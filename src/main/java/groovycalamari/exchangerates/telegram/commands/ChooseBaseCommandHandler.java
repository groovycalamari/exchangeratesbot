package groovycalamari.exchangerates.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.DefaultUpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Named(ChooseBaseCommandHandler.COMMAND_CHOOSEBASE)
@Singleton
public class ChooseBaseCommandHandler extends ChooseCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseBaseCommandHandler.class);

    public static final String COMMAND_CHOOSEBASE = "choosebase";

    protected ChooseBaseCommandHandler(ObjectMapper objectMapper, UpdateParser updateParser) {
        super(objectMapper, updateParser);
    }

    @NonNull
    @Override
    protected String messageText(@NonNull TelegramBot telegramBot, @NonNull Update update) {
        return "Choose your default base currency";
    }

    @Override
    protected String nextCommand() {
        return CommandHandler.COMMAND_PREFIX + SetBaseCommandHandler.COMMAND_SETBASE;
    }
}
