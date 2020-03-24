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
import io.exchangeratesapi.ExchangeRates;
import io.exchangeratesapi.ExchangeRatesApi;
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MatcherCommandHandler;
import io.micronaut.bots.core.MessageComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AmountHandler extends ExchangeRatesApiCommandHandler implements MatcherCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AmountHandler.class);

    protected final UserRepository userRepository;

    public AmountHandler(MessageComposer messageComposer,
                         ChatBotMessageParser messageParser,
                         ExchangeRatesApi exchangeRatesApi,
                         UserRepository userRepository) {
        super(messageComposer, messageParser, exchangeRatesApi);
        this.userRepository = userRepository;
    }

    @Override
    public boolean matches(@NonNull @NotBlank String text) {
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
    protected String textForExchangeRates(@NonNull ChatBot bot, @NonNull ChatBotMessageReceive update, @NonNull ExchangeRates rates) {

        String text = super.textForExchangeRates(bot, update, rates);

        Optional<String> updateTextOptional = messageParser.parseTextWithoutBotName(bot, update);
        if (updateTextOptional.isPresent()) {
            String updateText = updateTextOptional.get();

            Optional<BigDecimal> amountOptional = parseAmount(updateText);
            if (amountOptional.isPresent()) {
                BigDecimal amount = amountOptional.get();
                return "" + amount + " " + rates.getBase().getCode() + " = " + String.join(" ", rates.getRates().keySet().stream().map(curr -> round(rates.getRates().get(curr).multiply(amount), 2, BigDecimal.ROUND_HALF_UP) + " " + curr.getCode()).collect(Collectors.toList())) + ".\n" + text;
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("could parsed text without bot name ");
            }
        }
        return text;
    }

    static double round(BigDecimal bd, int precision, int roundingMode) {
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    @Override
    protected Optional<ExchangeRates> exchangeRates(@NonNull ChatBotMessageReceive update, @NonNull String text) {
        Optional<Serializable> userIdOptional = messageParser.parseUserId(update);
        if (userIdOptional.isPresent()) {
            Serializable userId = userIdOptional.get();
            Optional<Settings> settingsOptional = userRepository.findByUserId(userId);
            if (settingsOptional.isPresent()) {
                Settings settings = settingsOptional.get();
                if (settings.getBase() != null && settings.getTarget() != null) {
                    return exchangeRatesWithSettings(settings, text);

                } else {
                    if (settings.getBase() == null) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("settings base cannot be null");
                        }
                    }
                    if (settings.getTarget() != null) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("settings target cannot be null");
                        }
                    }
                }
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Could not retrieve settings for user {}", userId);
                }
            }
        }
        return Optional.empty();
    }

    protected Optional<ExchangeRates> exchangeRatesWithSettings(Settings settings, String text) {
        return latest(settings.getBase(), settings.getTarget());
    }
}
