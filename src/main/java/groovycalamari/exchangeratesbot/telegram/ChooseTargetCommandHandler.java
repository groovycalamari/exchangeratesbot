package groovycalamari.exchangeratesbot.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import groovycalamari.exchangeratesbot.SetTargetCommandHandler;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.context.annotation.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Requires(classes = Update.class)
@Named(ChooseTargetCommandHandler.COMMAND_CHOOSETARGET)
@Singleton
public class ChooseTargetCommandHandler extends ChooseCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseTargetCommandHandler.class);

    public static final String COMMAND_CHOOSETARGET = "choosetarget";

    protected ChooseTargetCommandHandler(ObjectMapper objectMapper,
                                         MessageComposer messageComposer) {
        super(objectMapper, messageComposer);
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
