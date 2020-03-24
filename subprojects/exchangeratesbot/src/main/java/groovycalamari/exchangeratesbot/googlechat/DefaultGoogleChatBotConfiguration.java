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

import io.micronaut.bots.googlechat.core.GoogleChatBotConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

@Named(DefaultGoogleChatBotConfiguration.GOOGLE_CHAT_BOT_EXCHANGERATES)
@Singleton
public class DefaultGoogleChatBotConfiguration implements GoogleChatBotConfiguration {
    public static final String GOOGLE_CHAT_BOT_EXCHANGERATES = "exchangerates";
    @Nonnull
    @Override
    public String getProjectId() {
        return System.getenv("PROJECT_ID");
    }

    @Nonnull
    @Override
    public String getAtUsername() {
        return "@ExchangeRates";
    }

    @Nonnull
    @Override
    public String getName() {
        return GOOGLE_CHAT_BOT_EXCHANGERATES;
    }
}
