package groovycalamari.exchangerates.telegram.commands;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeRatesConfigurationProperties;
import io.micronaut.bots.telegram.core.Chat;
import io.micronaut.bots.telegram.core.ChatType;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.TextCommandHandler;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public abstract class ExchangeRatesApiCommandHandler extends TextCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesApiCommandHandler.class);

    protected final ObjectMapper objectMapper;
    private static final HttpClient httpClient;

    static {
        httpClient = HttpClientBuilder.create().build();
    }

    protected ExchangeRatesApiCommandHandler(UpdateParser updateParser,
                                             ObjectMapper objectMapper) {
        super((updateParser));
        this.objectMapper = objectMapper;
    }

    protected abstract Optional<String> parseUri(@NonNull Update update,  @NonNull String text);

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull TelegramBot telegramBot,
                                           @NonNull Update update) {
        String text = updateParser.parseTextWithoutBotName(telegramBot, update).orElse(null);
        if (text != null) {
            Optional<String> uriOpt = parseUri(update, text);
            if (!uriOpt.isPresent()) {
                return Optional.empty();
            }
            String uri = uriOpt.get();
            Optional<ExchangeRates> exchangeRatesOpt = exchangeRates(uri);
            if (!exchangeRatesOpt.isPresent()) {
                return Optional.empty();
            }
            ExchangeRates exchangeRates = exchangeRatesOpt.get();
            return Optional.of(textForExchangeRates(telegramBot, update, exchangeRates));
        }
        return Optional.empty();
    }

    protected String atDateUri(String dateStr, Currency base, Currency symbol) {
        return ExchangeRatesConfigurationProperties.HOST_LIVE + "/" + dateStr + "?base=" + base.toString() + "&symbols=" + symbol.toString();
    }

    protected String textForExchangeRates(@NonNull TelegramBot telegramBot, @NonNull Update update, @NonNull ExchangeRates rates) {
        String text = "At " + rates.getDate().toString() + " 1 " + rates.getBase().getCode() + " was ";
        for (Currency currency : rates.getRates().keySet()) {
            text +=   rates.getRates().get(currency) + " " + currency.getCode();
        }
        return text;
    }

    protected Optional<ExchangeRates> exchangeRates(String uri) {
        HttpGet request = new HttpGet(uri);
        try {
            LOG.info("executing request: "+ uri);
            HttpResponse response = httpClient.execute(request);
            LOG.info("response status code: "+ response.getStatusLine().getStatusCode());
            return Optional.of(objectMapper.readValue(response.getEntity().getContent(), ExchangeRates.class));
        } catch (IOException ioe) {
            LOG.error("IOOE xception fechting response "+ ioe.getMessage());
        }
        return Optional.empty();
    }

    protected String latestUri(Currency base, Currency symbol) {
        return ExchangeRatesConfigurationProperties.HOST_LIVE + "/latest?base=" + base.toString() + "&symbols=" + symbol.toString();
    }
}
