package groovycalamari.exchangerates.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AmountHandler extends ExchangeRatesApiCommandHandler {
    private final UserRepository userRepository;

    public AmountHandler(UpdateParser updateParser,
                         ObjectMapper objectMapper,
                         UserRepository userRepository) {
        super(updateParser, objectMapper);
        this.userRepository = userRepository;
    }

    public static boolean matches(String text) {
        try {
            new BigDecimal(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    protected Optional<BigDecimal> parseAmount(String text) {
        try {
            return Optional.of(new BigDecimal(text));
        } catch(NumberFormatException e) {

        }
        return Optional.empty();

    }

    @Override
    protected String textForExchangeRates(@NonNull TelegramBot telegramBot,
                                          @NonNull Update update,
                                          @NonNull ExchangeRates rates) {
        String text = super.textForExchangeRates(telegramBot, update, rates);

        Optional<String> updateTextOptional = updateParser.parseTextWithoutBotName(telegramBot, update);
        if (updateTextOptional.isPresent()) {
            String updateText = updateTextOptional.get();

                Optional<BigDecimal> amountOptional = parseAmount(updateText);
                if (amountOptional.isPresent()) {
                    BigDecimal amount = amountOptional.get();
                    return "" + amount + " " + rates.getBase().getCode() + " = " + String.join(" ", rates.getRates().keySet().stream().map(curr -> round(rates.getRates().get(curr).multiply(amount), 2, BigDecimal.ROUND_HALF_UP) + " " + curr.getCode()).collect(Collectors.toList())) + ".\n" + text;
                }
        }
        return text;
    }

    static double round(BigDecimal bd, int precision, int roundingMode) {
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    @Override
    protected Optional<String> parseUri(@NonNull Update update, @NonNull String text) {
        Optional<Integer> userIdOptional = updateParser.parseUserId(update);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            Optional<Settings> settingsOptional = userRepository.findByUserId(userId);
            if (settingsOptional.isPresent()) {
                Settings settings = settingsOptional.get();
                if (settings.getBase() != null && settings.getTarget() != null) {
                    return parseUriAtSettings(settings, text);
                }

            }
        }
        return Optional.empty();
    }

    protected Optional<String> parseUriAtSettings(Settings settings, String text) {
            return Optional.of(latestUri(settings.getBase(), settings.getTarget()));
    }
}
