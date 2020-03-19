package groovycalamari.exchangeratesbot.googlechat;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.googlechat.core.GoogleChatButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ChooseCommandHandler extends InlineKeyboardHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseCommandHandler.class);

    List<Currency> topCurrencies = Arrays.asList(Currency.USD, Currency.JPY, Currency.CHF, Currency.AUD, Currency.EUR, Currency.GBP, Currency.CAD, Currency.ZAR);

    @Override
    protected List<GoogleChatButton> buttons(@NonNull ChatBot bot) {
        List<GoogleChatButton> buttons = new ArrayList<>();
        for (Currency currency : topCurrencies) {
            buttons.add(currencyButton(bot, currency));
        }
        return buttons;
    }


    protected GoogleChatButton currencyButton(@NonNull ChatBot chatBot,
                                              @NonNull Currency currency) {
        return button(currency.getCode() + " - " + currency.getCountryName(),
                chatBot.getAtUsername() + " " + nextCommand() + " " + currency.getCode());
    }

    protected abstract String nextCommand();
}
