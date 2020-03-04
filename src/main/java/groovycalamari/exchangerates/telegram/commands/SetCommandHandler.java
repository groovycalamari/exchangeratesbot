package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.telegram.core.AnswerCallbackQuery;
import io.micronaut.bots.telegram.core.Chat;
import io.micronaut.bots.telegram.core.Send;
import io.micronaut.bots.telegram.core.SendMessage;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class SetCommandHandler implements CommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SetCommandHandler.class);

    private final UpdateParser updateParser;

    public SetCommandHandler(UpdateParser updateParser) {
        this.updateParser = updateParser;
    }

    protected abstract String successMessage(@NonNull Currency currency);

    protected abstract String getCommandName();

    protected abstract void updateAttribute(@NonNull Integer userId, @NonNull Currency currency);

    @Override
    public <T extends Send> Optional<T> handle(@NonNull TelegramBot telegramBot, @NonNull Update update) {

        Optional<String> textOptional = updateParser.parseTextWithoutBotName(telegramBot, update);

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
                Optional<Integer> userIdOptional = updateParser.parseUserId(update);
                if (userIdOptional.isPresent()) {
                    Integer userId = userIdOptional.get();
                    updateAttribute(userId, currency);

                    Optional<Chat> chatOptional = updateParser.parseChat(update);
                    if (chatOptional.isPresent()) {
                        Chat chat = chatOptional.get();
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chat.getId());
                        sendMessage.setText(successMessage(currency));
                        return (Optional) Optional.of(sendMessage);
                    }

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

        return (Optional) messageWithText(update, "🤷🏻‍ Try " + getCommandName() + " currency e.g. " + getCommandName() + " USD");
    }

    protected Optional<SendMessage> messageWithCurrency(@NonNull Update update, @NonNull Currency currency) {
        return messageWithText(update, successMessage(currency));
    }

    protected Optional<SendMessage> messageWithText(@NonNull Update update, @NonNull String text) {
        return updateParser.parseChat(update).map(Chat::getId).map(chatId -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            return sendMessage;
        });
    }
}
