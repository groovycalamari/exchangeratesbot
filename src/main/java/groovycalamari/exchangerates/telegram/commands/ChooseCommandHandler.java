package groovycalamari.exchangerates.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.telegram.core.InlineKeyboardButton;
import io.micronaut.bots.telegram.core.InlineKeyboardMarkup;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.InlineKeyboardHandler;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ChooseCommandHandler extends InlineKeyboardHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseBaseCommandHandler.class);

    List<Currency> topCurrencies = Arrays.asList(Currency.USD, Currency.JPY, Currency.CHF, Currency.AUD, Currency.EUR, Currency.GBP, Currency.CAD, Currency.ZAR);

    protected ChooseCommandHandler(ObjectMapper objectMapper, UpdateParser updateParser)  {
        super(objectMapper, updateParser);
    }

    @NonNull
    @Override
    protected InlineKeyboardMarkup createInlineKeyboardMarkup(@NonNull TelegramBot telegramBot, @NonNull Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int counter = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Currency currency : topCurrencies) {
            row.add(currencyButton(telegramBot, currency));
            counter++;
            if (counter == 2) {
                keyboard.add(row);
                row = new ArrayList<>();
                counter = 0;
            }
        }
        keyboard.add(row);

        inlineKeyboardMarkup.setInlineKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    protected InlineKeyboardButton currencyButton(@NonNull TelegramBot telegramBot,
                                                  @NonNull Currency currency) {
        InlineKeyboardButton baseBtn = new InlineKeyboardButton();
        baseBtn.setCallbackData(telegramBot.getAtUsername() + " " + nextCommand() + " " + currency.getCode());
        baseBtn.setText(currency.getCode() + " - " + currency.getCountryName());
        return baseBtn;
    }

    protected abstract String nextCommand();
}
