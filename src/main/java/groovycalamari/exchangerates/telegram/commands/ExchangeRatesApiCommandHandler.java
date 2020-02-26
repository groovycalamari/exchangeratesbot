package groovycalamari.exchangerates.telegram.commands;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.telegram.core.ChatType;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.TextCommandHandler;
import io.micronaut.bots.telegram.core.Update;
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

    protected ExchangeRatesApiCommandHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected abstract Optional<String> parseUri(String text);

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull TelegramBot telegramBot,
                                           @NonNull Update update) {
        String text = CommandHandler.parseText(update);
        String type = CommandHandler.parseType(update);
        boolean isPrivateMessage = (type != null && type.equals(ChatType.PRIVATE.toString()));

        boolean isMessageTargetToTheBot = text != null && text.contains(telegramBot.getAtUsername());

        if (text != null && (isPrivateMessage || isMessageTargetToTheBot)) {
            Optional<String> uriOpt = parseUri(text);
            if (!uriOpt.isPresent()) {
                return Optional.empty();
            }
            String uri = uriOpt.get();
            Optional<ExchangeRates> exchangeRatesOpt = exchangeRates(uri);
            if (!exchangeRatesOpt.isPresent()) {
                return Optional.empty();
            }
            ExchangeRates exchangeRates = exchangeRatesOpt.get();
            return Optional.of(textForExchangeRates(exchangeRates));
        }
        return Optional.empty();

    }


    private String textForExchangeRates(ExchangeRates rates) {
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
}
