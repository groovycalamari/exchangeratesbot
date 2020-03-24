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
