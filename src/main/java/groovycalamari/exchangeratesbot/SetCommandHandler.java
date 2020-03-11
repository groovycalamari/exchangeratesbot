package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.ChatBotMessageSend;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.core.MessageComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class SetCommandHandler implements CommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SetCommandHandler.class);

    private final ChatBotMessageParser messageParser;
    private final MessageComposer messageComposer;

    public SetCommandHandler(ChatBotMessageParser messageParser,
                             MessageComposer messageComposer) {
        this.messageParser = messageParser;
        this.messageComposer = messageComposer;
    }

    protected abstract String successMessage(@NonNull Currency currency);

    protected abstract String getCommandName();

    protected abstract void updateAttribute(@NonNull Serializable userId, @NonNull Currency currency);

    @Override
    public <T extends ChatBotMessageSend> Optional<T> handle(@NonNull ChatBot chatBot,
                                                             @NonNull ChatBotMessageReceive update) {

        Optional<String> textOptional = messageParser.parseTextWithoutBotName(chatBot, update);

        if (textOptional.isPresent()) {
            String text = textOptional.get();
            if (LOG.isInfoEnabled()) {
                LOG.info("text is: {}", text);
            }
            final String currencyText = text.replaceAll(getCommandName() + " ", "");
            if (LOG.isInfoEnabled()) {
                LOG.info("currencyText is: {}", text);
            }
            Optional<Currency> currencyOptional = Stream.of(Currency.values())
                    .filter(c -> c.getCode().equals(currencyText.toUpperCase())).findFirst();
            if (currencyOptional.isPresent()) {
                Currency currency = currencyOptional.get();
                if (LOG.isInfoEnabled()) {
                    LOG.info("currency is: {}", currency.getCode());
                }
                Optional<Serializable> userIdOptional = messageParser.parseUserId(update);
                if (userIdOptional.isPresent()) {
                    Serializable userId = userIdOptional.get();
                    updateAttribute(userId, currency);
                    return messageComposer.compose(successMessage(currency), update);


                } else {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("could not parse userid");
                    }
                }
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("could not parse currency");
                }
            }
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("could not parse text");
            }
        }
        return (Optional) messageComposer.compose("🤷🏻‍ Try " + getCommandName() + " currency e.g. " + getCommandName() + " USD", update);
    }
}
