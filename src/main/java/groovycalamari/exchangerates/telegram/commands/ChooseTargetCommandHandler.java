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

@Named(ChooseTargetCommandHandler.COMMAND_CHOOSETARGET)
@Singleton
public class ChooseTargetCommandHandler extends ChooseCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseTargetCommandHandler.class);

    public static final String COMMAND_CHOOSETARGET = "choosetarget";

    protected ChooseTargetCommandHandler(ObjectMapper objectMapper, UpdateParser updateParser) {
        super(objectMapper, updateParser);
    }

    @NonNull
    @Override
    protected String messageText(@NonNull TelegramBot telegramBot, @NonNull Update update) {
        return "Choose your default target currency";
    }

    @Override
    protected String nextCommand() {
        return CommandHandler.COMMAND_PREFIX + SetTargetCommandHandler.COMMAND_SETTARGET;
    }
}
