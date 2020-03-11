package groovycalamari.exchangeratesbot;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeRatesConfigurationProperties;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.core.TextCommandHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public abstract class ExchangeRatesApiCommandHandler extends TextCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesApiCommandHandler.class);

    protected final ChatBotMessageParser messageParser;
    protected final ObjectMapper objectMapper;
    private static final HttpClient httpClient;

    static {
        httpClient = HttpClientBuilder.create().build();
    }

    public ExchangeRatesApiCommandHandler(MessageComposer messageComposer,
                                             ChatBotMessageParser messageParser,
                                             ObjectMapper objectMapper) {
        super((messageComposer));
        this.messageParser = messageParser;
        this.objectMapper = objectMapper;
    }

    protected abstract Optional<String> parseUri(@NonNull ChatBotMessageReceive update, @NonNull String text);

    @Override
    protected Optional<String> replyUpdate(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive update) {
        String text = messageParser.parseTextWithoutBotName(bot, update).orElse(null);
        if (LOG.isInfoEnabled()) {
            LOG.info("parsed text without bot name is: {}", text);
        }
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
            return Optional.of(textForExchangeRates(bot, update, exchangeRates));
        }
        return Optional.empty();
    }

    protected String atDateUri(String dateStr, Currency base, Currency symbol) {
        return ExchangeRatesConfigurationProperties.HOST_LIVE + "/" + dateStr + "?base=" + base.toString() + "&symbols=" + symbol.toString();
    }

    protected String textForExchangeRates(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive update, @NonNull ExchangeRates rates) {
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
