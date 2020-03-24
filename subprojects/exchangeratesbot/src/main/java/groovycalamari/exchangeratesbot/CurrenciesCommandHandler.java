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
import io.micronaut.bots.core.ChatBot;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.core.TextCommandHandler;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named(CurrenciesCommandHandler.COMMAND_CURRENCIES)
@Singleton
public class CurrenciesCommandHandler extends TextCommandHandler {
    public static final String COMMAND_CURRENCIES = "currencies";

    public CurrenciesCommandHandler(MessageComposer messageComposer) {
        super(messageComposer);
    }

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull ChatBot bot,
                                           @NonNull ChatBotMessageReceive update) {
        StringBuilder sb = new StringBuilder();
        for (Currency currency : Currency.values()) {
            sb.append(currency.getCode())
                    .append(" - ")
                    .append(currency.getCountryName())
                    .append("\n");
        }
        return Optional.of(sb.toString());
    }
}
