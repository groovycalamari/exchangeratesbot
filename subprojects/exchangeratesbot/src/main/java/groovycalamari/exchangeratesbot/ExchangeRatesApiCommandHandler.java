/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.exchangeratesapi.ExchangeRates;
import io.exchangeratesapi.ExchangeRatesApi;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.core.TextCommandHandler;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

public abstract class ExchangeRatesApiCommandHandler extends TextCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesApiCommandHandler.class);
    public static final String RATES_FETCH_FAILED_MESSAGE = "I could not retrieve the exchange rates. Please try again later";

    protected final ChatBotMessageParser messageParser;
    protected final ExchangeRatesApi exchangeRatesApi;
    protected final SimpleDateFormat dateFormat;
    protected final DateTimeFormatter formatter;

    public ExchangeRatesApiCommandHandler(MessageComposer messageComposer,
                                          ChatBotMessageParser messageParser,
                                          ExchangeRatesApi exchangeRatesApi) {
        super((messageComposer));
        this.messageParser = messageParser;
        this.exchangeRatesApi = exchangeRatesApi;
        dateFormat = new SimpleDateFormat(ExchangeRatesApi.DATE_FORMAT);
        formatter = DateTimeFormatter.ofPattern(ExchangeRatesApi.DATE_FORMAT);

    }

    @Override
    protected Optional<String> replyUpdate(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive update) {
        String text = messageParser.parseTextWithoutBotName(bot, update).orElse(null);
        if (LOG.isInfoEnabled()) {
            LOG.info("parsed text without bot name is: {}", text);
        }
        if (text != null) {
            Optional<ExchangeRates> exchangeRatesOpt = exchangeRates(update, text);
            if (!exchangeRatesOpt.isPresent()) {
                return (Optional) messageComposer.compose(RATES_FETCH_FAILED_MESSAGE, update);
            }
            ExchangeRates exchangeRates = exchangeRatesOpt.get();
            return Optional.of(textForExchangeRates(bot, update, exchangeRates));
        }
        return Optional.empty();
    }

    protected abstract Optional<ExchangeRates> exchangeRates(@NonNull ChatBotMessageReceive update, @NonNull String text);


    protected String textForExchangeRates(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive update,
                                          @NonNull ExchangeRates rates) {
        String text = "At " + rates.getDate().toString() + " 1 " + rates.getBase().getCode() + " was ";
        for (Currency currency : rates.getRates().keySet()) {
            text += rates.getRates().get(currency) + " " + currency.getCode();
        }
        return text;
    }

    protected Optional<ExchangeRates> latest(Currency base, Currency symbol) {
        try {
            return Optional.of(exchangeRatesApi.latest(base, Collections.singletonList(symbol)).blockingGet());
        } catch (HttpClientResponseException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("could not fetch latest exchange for {} => {} msg {}", base.getCode(), symbol.getCode(), e.getMessage());
            }
            return Optional.empty();
        }
    }

    protected Optional<ExchangeRates> atDate(String dateStr, Currency base, Currency symbol) {
        LocalDate ld = LocalDate.parse(dateStr, formatter);
        return atLocalDate(ld, base, symbol);
    }

    protected Optional<ExchangeRates> atLocalDate(LocalDate ld, Currency base, Currency symbol) {
        try {
            return Optional.of(exchangeRatesApi.atDate(ld, base, Collections.singletonList(symbol)).blockingGet());
        } catch (HttpClientResponseException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("could not fetch latest exchange for {} => {} msg {}", base.getCode(), symbol.getCode(), e.getMessage());
            }
            return Optional.empty();
        }
    }
}